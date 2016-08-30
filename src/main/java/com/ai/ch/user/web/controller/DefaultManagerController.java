package com.ai.ch.user.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ai.ch.user.api.defaultlog.interfaces.IDefaultLogSV;
import com.ai.ch.user.api.defaultlog.params.DefaultLogVo;
import com.ai.ch.user.api.defaultlog.params.InsertDefaultLogRequest;
import com.ai.ch.user.api.defaultlog.params.QueryDefaultLogRequest;
import com.ai.ch.user.api.defaultlog.params.QueryDefaultLogResponse;
import com.ai.ch.user.web.constants.ChWebConstants;
import com.ai.ch.user.web.constants.ChWebConstants.ExceptionCode;
import com.ai.ch.user.web.model.sso.client.GeneralSSOClientUser;
import com.ai.opt.base.vo.PageInfo;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.dubbo.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.BeanUtils;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientConstants;
import com.ai.platform.common.api.sysuser.interfaces.ISysUserQuerySV;
import com.ai.platform.common.api.sysuser.param.SysUserQueryRequest;
import com.ai.platform.common.api.sysuser.param.SysUserQueryResponse;
import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping("/defaultManager")
public class DefaultManagerController {
	
	@RequestMapping("/defaultPager")
    public ModelAndView billingPager() {
        return new ModelAndView("/jsp/defaultManager/defaultManagerList");
    }
	
	@RequestMapping("/defaultManagerPager")
	public ModelAndView defaultManager() {
		return new ModelAndView("/jsp/defaultManager/defaultManagerList");
	}
	
	@RequestMapping("/addDefaultInfo")
	public ModelAndView addDefaultInfo(HttpServletRequest request,String userId,String userName,String custName) {
		Map<String, Object> model = new HashMap<String, Object>();
 		model.put("userId", userId);
 		model.put("userName", userName);
 		model.put("custName", custName);
		return new ModelAndView("/jsp/defaultManager/addDefault",model);
	}
	
	@RequestMapping("/saveDefaultInfo")
	@ResponseBody
	public ResponseData<String> saveDefaultInfo(HttpServletRequest request,DefaultLogVo defaultLogInfo) {
		IDefaultLogSV defaultLog = DubboConsumerFactory.getService("iDefaultLogSV");
		ISysUserQuerySV sysUserQuery = DubboConsumerFactory.getService("iSysUserQuerySV");
		ResponseData<String> responseData = null;
        ResponseHeader responseHeader = null;
        HttpSession session = request.getSession();
        GeneralSSOClientUser user = (GeneralSSOClientUser) session.getAttribute(SSOClientConstants.USER_SESSION_KEY);
        InsertDefaultLogRequest defaultLogRequest = new InsertDefaultLogRequest();
        try{
        	SysUserQueryRequest sysUserQueryRequest = new SysUserQueryRequest();
        	sysUserQueryRequest.setTenantId(user.getTenantId());
        	sysUserQueryRequest.setLoginName(user.getLoginName());
        	SysUserQueryResponse  userQueryResponse = sysUserQuery.queryUserInfo(sysUserQueryRequest);
			BeanUtils.copyProperties(defaultLogRequest,defaultLogInfo);
			defaultLogRequest.setDeductDate(new Timestamp(new Date().getTime()));
			defaultLogRequest.setOperId(Long.parseLong(userQueryResponse.getNo()));
			defaultLogRequest.setOperName(userQueryResponse.getLoginName());
		    GeneralSSOClientUser userClient = (GeneralSSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
			defaultLogRequest.setTenantId(userClient.getTenantId());
			defaultLog.insertDefaultLog(defaultLogRequest);
			responseData = new ResponseData<String>(ExceptionCode.SUCCESS_CODE, "操作成功", null);
            responseHeader = new ResponseHeader(true,ExceptionCode.SUCCESS_CODE, "操作成功");
        }catch(Exception e){
        	responseData = new ResponseData<String>(ExceptionCode.ERROR_CODE, "操作失败", null);
            responseHeader = new ResponseHeader(false,ExceptionCode.ERROR_CODE, "操作失败");
        }
        responseData.setResponseHeader(responseHeader);
        responseData.setData(JSON.toJSONString(defaultLogRequest));
        return responseData;
	}
	
	@RequestMapping("/defaultHistoryPager")
	public ModelAndView defaultHistoryPager(HttpServletRequest request,String userId,String userName,String custName) {
		Map<String, Object> model = new HashMap<String, Object>();
 		model.put("userId", userId);
 		model.put("userName", userName);
 		model.put("custName", custName);
		return new ModelAndView("/jsp/defaultManager/defaultHistoryList",model);
	}
	
	
	//获取供货商列表
	@RequestMapping("/getDefaultHistoryList")
	@ResponseBody
	public ResponseData<PageInfo<DefaultLogVo>> getDefaultHistoryList(HttpServletRequest request,QueryDefaultLogRequest defaultLogRequest) {
		ResponseData<PageInfo<DefaultLogVo>> responseData = null;
		try {
			IDefaultLogSV defaultLog = DubboConsumerFactory.getService("iDefaultLogSV");
			QueryDefaultLogResponse defaultLogResponse = defaultLog.queryDefaultLog(defaultLogRequest);
			if (defaultLogResponse != null && defaultLogResponse.getResponseHeader().isSuccess()) {
				PageInfo<DefaultLogVo> pageInfo = defaultLogResponse.getPageInfo();
				responseData = new ResponseData<PageInfo<DefaultLogVo>>(ChWebConstants.OperateCode.SUCCESS, "查询成功", pageInfo);
			} else {
				responseData = new ResponseData<PageInfo<DefaultLogVo>>(ExceptionCode.SYSTEM_ERROR, "查询失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseData = new ResponseData<PageInfo<DefaultLogVo>>(ExceptionCode.SYSTEM_ERROR, "查询失败", null);
		}
		return responseData;
	}
	
}
