package com.cic.partner.manager.mybatis.model;

import java.util.Date;

/**
 * 
 * @author Mango
 */
public class NlifeProd { 
	private String prodCode; //产品代码
	private String prodName; //产品名称
	private String coreProdCode; //核心产品代码
	private String appType; //个团标识 0个 1团
	private String instant; //是否支持即时投保 Y/N
	private Long maxDelayDay; //最长延时起保天数
	private Long minDelayDay; //最短延时起保天数
	private Long maxDays; //最大保险期间
	private Long minDays; //最小保险期间
	private String pppUnit; //保险期间单位
	private Long maxNumber; //最大份数
	private Date startDate; //定义时间
	private String status; //状态
	private String prodType; //产品类型
	private String poolType; //共保类型 00不共保 10主共 20从共
	private String ageLimit; //是否限制投保年龄：Y是，N否
	private Integer minAge; //最小投保年龄
	private Integer maxAge; //最大投保年龄
	private String maxOccupLevel; //最高职业类别
	private String insuredNoLimit; //是否限制投保人数Y是，N否
	private Integer minInsuredNo; //最低投保人数
	private String agentType; //代理类型（字典数据agent_type ）
	private Date operTime; //更新时间

    public void setProdCode(String prodCode){
    	this.prodCode = prodCode;
    }
    public String getProdCode(){
    	return	this.prodCode;
    }
    public void setProdName(String prodName){
    	this.prodName = prodName;
    }
    public String getProdName(){
    	return	this.prodName;
    }
    public void setCoreProdCode(String coreProdCode){
    	this.coreProdCode = coreProdCode;
    }
    public String getCoreProdCode(){
    	return	this.coreProdCode;
    }
    public void setAppType(String appType){
    	this.appType = appType;
    }
    public String getAppType(){
    	return	this.appType;
    }
    public void setInstant(String instant){
    	this.instant = instant;
    }
    public String getInstant(){
    	return	this.instant;
    }
    public void setMaxDelayDay(Long maxDelayDay){
    	this.maxDelayDay = maxDelayDay;
    }
    public Long getMaxDelayDay(){
    	return	this.maxDelayDay;
    }
    public void setMinDelayDay(Long minDelayDay){
    	this.minDelayDay = minDelayDay;
    }
    public Long getMinDelayDay(){
    	return	this.minDelayDay;
    }
    public void setMaxDays(Long maxDays){
    	this.maxDays = maxDays;
    }
    public Long getMaxDays(){
    	return	this.maxDays;
    }
    public void setMinDays(Long minDays){
    	this.minDays = minDays;
    }
    public Long getMinDays(){
    	return	this.minDays;
    }
    public void setPppUnit(String pppUnit){
    	this.pppUnit = pppUnit;
    }
    public String getPppUnit(){
    	return	this.pppUnit;
    }
    public void setMaxNumber(Long maxNumber){
    	this.maxNumber = maxNumber;
    }
    public Long getMaxNumber(){
    	return	this.maxNumber;
    }
    public void setStartDate(Date startDate){
    	this.startDate = startDate;
    }
    public Date getStartDate(){
    	return	this.startDate;
    }
    public void setStatus(String status){
    	this.status = status;
    }
    public String getStatus(){
    	return	this.status;
    }
    public void setProdType(String prodType){
    	this.prodType = prodType;
    }
    public String getProdType(){
    	return	this.prodType;
    }
    public void setPoolType(String poolType){
    	this.poolType = poolType;
    }
    public String getPoolType(){
    	return	this.poolType;
    }
    public void setAgeLimit(String ageLimit){
    	this.ageLimit = ageLimit;
    }
    public String getAgeLimit(){
    	return	this.ageLimit;
    }
    public void setMinAge(Integer minAge){
    	this.minAge = minAge;
    }
    public Integer getMinAge(){
    	return	this.minAge;
    }
    public void setMaxAge(Integer maxAge){
    	this.maxAge = maxAge;
    }
    public Integer getMaxAge(){
    	return	this.maxAge;
    }
    public void setMaxOccupLevel(String maxOccupLevel){
    	this.maxOccupLevel = maxOccupLevel;
    }
    public String getMaxOccupLevel(){
    	return	this.maxOccupLevel;
    }
    public void setInsuredNoLimit(String insuredNoLimit){
    	this.insuredNoLimit = insuredNoLimit;
    }
    public String getInsuredNoLimit(){
    	return	this.insuredNoLimit;
    }
    public void setMinInsuredNo(Integer minInsuredNo){
    	this.minInsuredNo = minInsuredNo;
    }
    public Integer getMinInsuredNo(){
    	return	this.minInsuredNo;
    }
    public void setAgentType(String agentType){
    	this.agentType = agentType;
    }
    public String getAgentType(){
    	return	this.agentType;
    }
    public void setOperTime(Date operTime){
    	this.operTime = operTime;
    }
    public Date getOperTime(){
    	return	this.operTime;
    }

}