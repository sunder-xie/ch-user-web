<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="uedroot"
	value="${pageContext.request.contextPath}/template/default" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>设置评级规则</title>
<%@include file="/inc/inc.jsp"%>
</head>
<body>
	<div class="content-wrapper-iframe">
		<!--右侧灰色背景-->
		<!--框架标签结束-->
		<div class="row">
			<!--外围框架-->
			<div class="col-lg-12">
				<!--删格化-->
				<div class="row">
					<!--内侧框架-->
					<div class="col-lg-12">
						<!--删格化-->
						<div class="main-box clearfix">
							<!--白色背景-->
							<!--查询结束-->
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--框架标签结束-->
		<div class="row">
			<!--外围框架-->
			<div class="col-lg-12">
				<!--删格化-->
				<div class="row">
					<!--内侧框架-->
					<div class="col-lg-12">
						<!--删格化-->
						<div class="main-box clearfix">
							<!--白色背景-->
							<!--标题-->
							<header class="main-box-header clearfix">
								<h2 class="pull-left">设置评级规则</h2>
							</header>
							<!--标题结束-->
							<div class="main-box-body clearfix">
								<!--table表格-->
								<div class="table-responsive clearfix">
									<form id="rankForm" method="post">
									<div class="radio-box">
										<span>请选择评级周期:</span> <input type="radio" class="radio-2"
											name="periodType_" checked value="M"> <label for="radio-2">月</label>
										<input type="radio" class="radio-1" name="periodType_" value="Q">
										<label for="radio-1">季度</label> <input type="radio"
											class="radio-1" name="periodType_" value="Y"> <label
											for="radio-1">年</label>
									</div>
									<div>
										<span>请选择店铺级数:</span>
										<select class="select select-small" id="rankRegion">
										<option value="">请选择</option>
										<c:forEach var="i" begin="2" end="20">
										<option>${i}</option>
										</c:forEach>
										</select>
										<span>(2-20个等级之间)</span>
									</div>
									<table class="table table-border table-bordered">
										<thead>
											<tr>
												<th>店铺佣金等级</th>
												<th>等级名称</th>
												<th>等级图片</th>
											</tr>
										</thead>
										<tbody id="TBODY_RANKRULE">
										
										</tbody>
									</table>
									<div class="text-c">
									<input type="button" id="saveRule" class="biu-btn btn-blue btn-xlarge  radius" value="保存">
									</div>
									</form>
								<!--/table表格结束-->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var pager;
		(function() {
			seajs.use('app/jsp/rank/rankrule', function(RankRulePager) {
				pager = new RankRulePager({
					element : document.body
				});
				pager.render();
			});
		})();
	</script>

<script id="rankRuleImpl" type="text/x-jsrender">
	{{for id}}
		<tr>
			<td><p class="f-14" style="font-weight:400;">等级 {{:index}}:  <input type='hidden' value='{{:index}}' name='list[{{:index}}].rank'><input class="int-text int-mini" name="list[{{:index}}].minFee" type="text"> - <input class="int-text int-mini" name="list[{{:index}}].maxFee" type="text">元</p></td>
			<td><p class="f-14">等级名称 :  <input class="int-text int-small" name="list[{{:index}}].rankName" type="text"></p></td>
			<td><p class="f-14">图片名称 :  <input class="int-text int-small" name="list[{{:index}}].rankLogo" type="text">&nbsp;&nbsp;&nbsp;<span class="btn-upload">
				<input type="button" class="btn-default btn-medium" value="浏览文件"/>
				<input type="file" class="int-file"/></span></p></td>
		</tr>
	{{/for}}
</script>
</body>
</html>