package com.ai.ch.user.web.vo;

public class BusinessListInfo {
	/**
	 * 计数
	 */
	public Integer index;
	/**
	 * 用户Id
	 */
	public String userId;

	/**
	 * 用户名称
	 */
	public String userName;

	/**
	 * 企业名称
	 */
	public String custName;
	/**
	 * 用户状态 冻结、解冻、注销
	 */
	public String userState;
	/**
	 * 审核状态
	 */
	public String auditState;
	/**
	 * 用户类型
	 */
	public String userType;

	/**
	 * 拒绝原因
	 */
	public String refusalReason;
	/**
	 * 创建时间
	 */
	public String createTime;
	/**
	 * 上传状态
	 */
	public String uploadStatus;

	/**
	 * 经营品类
	 */
	public String businessCategory;

	/**
	 * 审核时间
	 */
	public String auditTime;

	
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getBusinessCategory() {
		return businessCategory;
	}

	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}

}
