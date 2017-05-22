package com.cic.partner.manager.mybatis.model;

import java.math.BigDecimal;

/**
 * 
 * @author Mango
 */
public class NlifeCov { 
	private String prodCode; //产品代码
	private String coreRiskCode; //核心条款代码
	private String coreCovCode; //核心责任代码
	private String parentCov; //上级责任
	private String covName; //责任名称
	private BigDecimal covAmt; //责任保额
	private String isInputAmount; //是否输入保额：（Y 保额；N 页面传入）
	private BigDecimal noCalimAmount; //免赔额
	private BigDecimal claimRate; //赔付比例%
	private String calId; //保费算法

    public void setProdCode(String prodCode){
    	this.prodCode = prodCode;
    }
    public String getProdCode(){
    	return	this.prodCode;
    }
    public void setCoreRiskCode(String coreRiskCode){
    	this.coreRiskCode = coreRiskCode;
    }
    public String getCoreRiskCode(){
    	return	this.coreRiskCode;
    }
    public void setCoreCovCode(String coreCovCode){
    	this.coreCovCode = coreCovCode;
    }
    public String getCoreCovCode(){
    	return	this.coreCovCode;
    }
    public void setParentCov(String parentCov){
    	this.parentCov = parentCov;
    }
    public String getParentCov(){
    	return	this.parentCov;
    }
    public void setCovName(String covName){
    	this.covName = covName;
    }
    public String getCovName(){
    	return	this.covName;
    }
    public void setCovAmt(BigDecimal covAmt){
    	this.covAmt = covAmt;
    }
    public BigDecimal getCovAmt(){
    	return	this.covAmt;
    }
    public void setIsInputAmount(String isInputAmount){
    	this.isInputAmount = isInputAmount;
    }
    public String getIsInputAmount(){
    	return	this.isInputAmount;
    }
    public void setNoCalimAmount(BigDecimal noCalimAmount){
    	this.noCalimAmount = noCalimAmount;
    }
    public BigDecimal getNoCalimAmount(){
    	return	this.noCalimAmount;
    }
    public void setClaimRate(BigDecimal claimRate){
    	this.claimRate = claimRate;
    }
    public BigDecimal getClaimRate(){
    	return	this.claimRate;
    }
    public void setCalId(String calId){
    	this.calId = calId;
    }
    public String getCalId(){
    	return	this.calId;
    }

}