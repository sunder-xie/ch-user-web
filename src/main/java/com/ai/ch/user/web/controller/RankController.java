package com.ai.ch.user.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ai.ch.user.api.custfile.interfaces.ICustFileSV;
import com.ai.ch.user.api.custfile.params.CmCustFileExtVo;
import com.ai.ch.user.api.custfile.params.InsertCustFileExtRequest;
import com.ai.ch.user.api.custfile.params.QueryCustFileExtRequest;
import com.ai.ch.user.api.custfile.params.QueryCustFileExtResponse;
import com.ai.ch.user.api.custfile.params.UpdateCustFileExtRequest;
import com.ai.ch.user.api.rank.interfaces.IRankSV;
import com.ai.ch.user.api.rank.params.InsertRankRuleRequest;
import com.ai.ch.user.api.rank.params.QueryRankRuleRequest;
import com.ai.ch.user.api.rank.params.QueryRankRuleResponse;
import com.ai.ch.user.api.rank.params.ShopRankRuleVo;
import com.ai.ch.user.api.rank.params.UpdateRankRuleRequest;
import com.ai.ch.user.web.constants.ChWebConstants;
import com.ai.ch.user.web.model.sso.client.GeneralSSOClientUser;
import com.ai.ch.user.web.vo.ShopRankParamVo;
import com.ai.opt.base.exception.BusinessException;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.components.idps.IDPSClientFactory;
import com.ai.opt.sdk.dubbo.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.UUIDUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientConstants;
import com.ai.paas.ipaas.image.IImageClient;

/**
 * 入驻商户评级controller
 * @author Zh
 *
 */
@RestController
@RequestMapping("/rank")
public class RankController {

	private static final Logger LOG = LoggerFactory.getLogger(BillingController.class);

	/**
	 * 跳转页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/rankrule")
	public ModelAndView rankRule(HttpServletRequest request) {
		// 调dubbo服务
		IRankSV rankSV = DubboConsumerFactory.getService("iRankSV");
		LOG.info("获取服务");
		QueryRankRuleRequest queryRankRuleRequest = new QueryRankRuleRequest();
		queryRankRuleRequest.setTenantId(ChWebConstants.Tenant.TENANT_ID);
		LOG.info("查询开始");
		QueryRankRuleResponse response = rankSV.queryRankRule(queryRankRuleRequest);
		LOG.info("判断是否存在记录");
		if (response.getList().isEmpty()){
			return new ModelAndView("/jsp/crm/rankrule");
		}
		else {
			ModelAndView model = new ModelAndView("/jsp/crm/rankrule-edit");
			String periodType_ = "";
			if ("Y".equals(response.getList().get(0).getPeriodType())){
			periodType_ = "年";
			}
		if ("Q".equals(response.getList().get(0).getPeriodType())){
			periodType_ = "季度";
		}
		if ("M".equals(response.getList().get(0).getPeriodType()))
		{
			periodType_ = "月";
		}
			model.addObject("periodType", periodType_);
			model.addObject("rank", response.getList().size());
			return model;
		}

	}
	
	/**
	 * 获取评级详情json串
	 * @param request
	 * @return
	 */
	@RequestMapping("/getJsonData")
	@ResponseBody
	public ResponseData<String> getJsonData(HttpServletRequest request){
		ResponseData<String> responseData = new ResponseData<>(ChWebConstants.OperateCode.SUCCESS, "success");
		ResponseHeader responseHeader = new ResponseHeader(true, ChWebConstants.OperateCode.SUCCESS, "success");
		String count = request.getParameter("count");
		StringBuilder json = new StringBuilder();
		String data ="";
		if(!count.isEmpty()){
			json.append("{\"result\":[");
			for(int i=2;i<Integer.parseInt(count);i++){
				json.append("{\"index\":"+i+"},");
			}
			data = json.substring(0, json.length()-1);
			data+="]}";
		}
		responseData.setData(data);
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}
	/**
	 * 获取初始JSON串
	 * @param request
	 * @return
	 */
	@RequestMapping("/getInitData")
	@ResponseBody
	public ResponseData<ShopRankParamVo> getInitData(HttpServletRequest request){
		// 调dubbo服务
		ResponseData<ShopRankParamVo> responseData = new ResponseData<>(ChWebConstants.OperateCode.SUCCESS, "success");
		ResponseHeader responseHeader = new ResponseHeader(true, ChWebConstants.OperateCode.SUCCESS, "success");
		ShopRankParamVo shopRankParamVo = new ShopRankParamVo();
		IRankSV rankSV = DubboConsumerFactory.getService("iRankSV");
		QueryRankRuleRequest queryRankRuleRequest = new QueryRankRuleRequest();
		queryRankRuleRequest.setTenantId(ChWebConstants.Tenant.TENANT_ID);
		QueryRankRuleResponse response = rankSV.queryRankRule(queryRankRuleRequest);
		GeneralSSOClientUser user = (GeneralSSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		Map<String,String> urlMap=getUrlMap(user.getTenantId());
		Map<String,String> nameMap=getNameMap(user.getTenantId());
		Map<String,String> idpsMap=getIdpsMap(user.getTenantId());
		shopRankParamVo.setUrlMap(urlMap);
		shopRankParamVo.setNameMap(nameMap);
		shopRankParamVo.setIdpsMap(idpsMap);
		shopRankParamVo.setResult(response.getList());
		//截取中间数据
		List<ShopRankRuleVo> middleData = new ArrayList<>();
		if(!response.getList().isEmpty()){
		if(response.getList().size()>2){
			//废弃
			/*for (int index = 1; index <= response.getList().size()-2; index++) {
				middle.append(JSON.toJSONString(response.getList().get(index)));
				middle.append(",");
			}
			String str= middle.substring(0, middle.length()-1);
			str+="]}";*/
			middleData = response.getList().subList(1, response.getList().size()-1);
			shopRankParamVo.setMiddleData(middleData);
		}else{
			shopRankParamVo.setMiddleData(middleData);
		}
			responseData.setData(shopRankParamVo);
			responseData.setResponseHeader(responseHeader);
			//System.out.println(JSON.toJSONString(responseData));
		}
		return responseData;
	}
	
	/**
	 * 保存入驻商户评级信息
	 * @param request
	 * @param rankRuleRequest
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping("/saverule")
	public ModelAndView saveRule(HttpServletRequest request, InsertRankRuleRequest rankRuleRequest) throws IOException ,Exception{
		ModelAndView view=null;
		try {
			String idpsns = "ch-user-web-idps";
	        // 获取imageClient
	        IImageClient im = IDPSClientFactory.getImageClient(idpsns);
			Integer rank = rankRuleRequest.getList().get(rankRuleRequest.getList().size()-1).getRank();
			IRankSV rankSV = DubboConsumerFactory.getService("iRankSV");
			ICustFileSV custfileSV = DubboConsumerFactory.getService("iCustfileSV");
			MultipartHttpServletRequest file = (MultipartHttpServletRequest) request;
			List<CmCustFileExtVo> list = new ArrayList<CmCustFileExtVo>();
			InsertCustFileExtRequest custFileExtRequest = new InsertCustFileExtRequest();
			//获取登陆用户信息
			GeneralSSOClientUser user = (GeneralSSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
			for(int i=1;i<=rank;i++){
				 CmCustFileExtVo cmCustFileExtVo = new CmCustFileExtVo(); 
			     MultipartFile image = file.getFile("img"+i);
			     String idpsId = im.upLoadImage(image.getBytes(), UUIDUtil.genId32() + ".png");
			     cmCustFileExtVo.setAttrValue(idpsId);
			     cmCustFileExtVo.setTenantId(user.getTenantId());
			     cmCustFileExtVo.setAttrId(String.valueOf(i));
			     cmCustFileExtVo.setInfoName(request.getParameter("rankName"+i));
			     rankRuleRequest.getList().get(i-1).setRankLogo(idpsId);
			     list.add(cmCustFileExtVo);
			}
			custFileExtRequest.setList(list);
			// 设置最大最小值
			rankRuleRequest.getList().get(rankRuleRequest.getList().size() - 1).setMaxScore(Long.valueOf(request.getParameter("maxScore")));
			for (ShopRankRuleVo shopRankRuleVo : rankRuleRequest.getList()) {
				shopRankRuleVo.setTenantId(ChWebConstants.Tenant.TENANT_ID);
				shopRankRuleVo.setPeriodType(request.getParameter("periodType_"));
				shopRankRuleVo.setOperId(Long.valueOf(user.getUserId()));
				shopRankRuleVo.setOperName(user.getLoginName());
				shopRankRuleVo.setRankName(shopRankRuleVo.getRankName().trim());
			}
			rankRuleRequest.setTenantId(ChWebConstants.COM_TENANT_ID);
			// 调dubbo服务
			rankSV.insertRankRule(rankRuleRequest);
			custfileSV.insertCustFileExt(custFileExtRequest);
			view = new ModelAndView("/jsp/crm/success");
		} catch (BusinessException e) {
			LOG.error("保存失败");
			view = new ModelAndView("/jsp/crm/fail");
		}
		return view;
	}
	
	/**
	 * 更新入驻商户评级信息
	 * @param request
	 * @param rankRuleRequest
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping("/updaterule")
	public ModelAndView updateRule(HttpServletRequest request, UpdateRankRuleRequest rankRuleRequest) throws IOException ,Exception{
		ModelAndView view=null;
		try {
			String idpsns = "ch-user-web-idps";
	        // 获取imageClient
	        IImageClient im = IDPSClientFactory.getImageClient(idpsns);
			Integer rank = rankRuleRequest.getList().get(rankRuleRequest.getList().size()-1).getRank();
			IRankSV rankSV = DubboConsumerFactory.getService("iRankSV");
			ICustFileSV custfileSV = DubboConsumerFactory.getService("iCustfileSV");
			MultipartHttpServletRequest file = (MultipartHttpServletRequest) request;
			List<CmCustFileExtVo> list = new ArrayList<CmCustFileExtVo>();
			UpdateCustFileExtRequest custFileExtRequest = new UpdateCustFileExtRequest();
			GeneralSSOClientUser user = (GeneralSSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
			for(int i=1;i<=rank;i++){
				 CmCustFileExtVo cmCustFileExtVo = new CmCustFileExtVo(); 
			     MultipartFile image = file.getFile("img"+i);
			     if(image.getSize()!=0){
			     String idpsId= im.upLoadImage(image.getBytes(), UUIDUtil.genId32() + ".png");
			     cmCustFileExtVo.setAttrValue(idpsId);
			     cmCustFileExtVo.setTenantId(user.getTenantId());
			     cmCustFileExtVo.setInfoName(request.getParameter("rankName"+i));
			     cmCustFileExtVo.setAttrId(String.valueOf(i));
			     rankRuleRequest.getList().get(i-1).setRankLogo(idpsId);
			     list.add(cmCustFileExtVo);
			     }
			}
			custFileExtRequest.setList(list);
			//设置最大值
			rankRuleRequest.getList().get(rankRuleRequest.getList().size() - 1).setMaxScore(Long.valueOf(request.getParameter("maxScore")));
			for (ShopRankRuleVo shopRankRuleVo : rankRuleRequest.getList()) {
				shopRankRuleVo.setTenantId(ChWebConstants.Tenant.TENANT_ID);
				shopRankRuleVo.setPeriodType(request.getParameter("periodType_"));
				shopRankRuleVo.setOperId(Long.valueOf(user.getUserId()));
				shopRankRuleVo.setOperName(user.getLoginName());
				shopRankRuleVo.setRankName(shopRankRuleVo.getRankName().trim());
			}
			// 调dubbo服务
			rankRuleRequest.setTenantId(user.getTenantId());
			custFileExtRequest.setTenantId(user.getTenantId());
			rankSV.updateRankRule(rankRuleRequest);
			custfileSV.updateCustFileExt(custFileExtRequest);
			view = new ModelAndView("/jsp/crm/success");
		} catch (BusinessException e) {
			LOG.error("更新失败");
			view = new ModelAndView("/jsp/crm/fail");
		}
		return view;
	}
	
	
	/**
	 * 获取url的Map
	 * @param tenantId
	 * @return
	 */
	public Map<String,String> getUrlMap(String tenantId){
		String idpsns = "ch-user-web-idps";
	    // 获取imageClient
	    IImageClient im = IDPSClientFactory.getImageClient(idpsns);
		ICustFileSV custfileSV = DubboConsumerFactory.getService("iCustfileSV");
		QueryCustFileExtRequest custFileExtRequest = new QueryCustFileExtRequest();
		
		custFileExtRequest.setTenantId(tenantId);
		QueryCustFileExtResponse response = custfileSV.queryCustFileExt(custFileExtRequest);
		Map<String,String> urlMap = new HashMap<String,String>();
		if(response!=null&&!response.getList().isEmpty()){
			for (CmCustFileExtVo cmCustFileExtVo : response.getList()) {
				String url = im.getImageUrl(cmCustFileExtVo.getAttrValue(), ".jpg");
				urlMap.put(cmCustFileExtVo.getAttrId(), url);
			}
		}
			return urlMap;
	}
	/**
	 * 获取图片name的Map
	 * @param tenantId
	 * @return
	 */
	public Map<String,String> getNameMap(String tenantId){
		ICustFileSV custfileSV = DubboConsumerFactory.getService("iCustfileSV");
		QueryCustFileExtRequest custFileExtRequest = new QueryCustFileExtRequest();
		custFileExtRequest.setTenantId(tenantId);
		QueryCustFileExtResponse response = custfileSV.queryCustFileExt(custFileExtRequest);
		Map<String,String> nameMap = new HashMap<String,String>();
		if(response!=null&&!response.getList().isEmpty()){
			for (CmCustFileExtVo cmCustFileExtVo : response.getList()) {
				nameMap.put(cmCustFileExtVo.getAttrId(), cmCustFileExtVo.getInfoName());
			}
		}
		return nameMap;
	}
	/**
	 * 获取图片url的Map
	 * @param tenantId
	 * @return
	 */
	public Map<String,String> getIdpsMap(String tenantId){
		ICustFileSV custfileSV = DubboConsumerFactory.getService("iCustfileSV");
		QueryCustFileExtRequest custFileExtRequest = new QueryCustFileExtRequest();
		custFileExtRequest.setTenantId(tenantId);
		QueryCustFileExtResponse response = custfileSV.queryCustFileExt(custFileExtRequest);
		Map<String,String> nameMap = new HashMap<String,String>();
		if(response!=null&&!response.getList().isEmpty()){
			for (CmCustFileExtVo cmCustFileExtVo : response.getList()) {
				nameMap.put(cmCustFileExtVo.getAttrId(), cmCustFileExtVo.getAttrValue());
			}
		}
		return nameMap;
	}
	
}
