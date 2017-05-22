package com.cic.partner.manager.mybatis.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author Mango
 */
public class NlifeCon { 
	private String prodCode; //产品代码
	private String companyCode; //共保保险公司代码
	private String companyName; //共保保险公司名称
	private String companyType; //共保保险公司类型 1中华 其他3
	private String chiefFlag; //首席(主共)标志 1是 0否
	private BigDecimal conRate; //共保费率
	private Long operUser;
	private Date operTime;

    public void setProdCode(String prodCode){
    	this.prodCode = prodCode;
    }
    public String getProdCode(){
    	return	this.prodCode;
    }
    public void setCompanyCode(String companyCode){
    	this.companyCode = companyCode;
    }
    public String getCompanyCode(){
    	return	this.companyCode;
    }
    public void setCompanyName(String companyName){
    	this.companyName = companyName;
    }
    public String getCompanyName(){
    	return	this.companyName;
    }
    public void setCompanyType(String companyType){
    	this.companyType = companyType;
    }
    public String getCompanyType(){
    	return	this.companyType;
    }
    public void setChiefFlag(String chiefFlag){
    	this.chiefFlag = chiefFlag;
    }
    public String getChiefFlag(){
    	return	this.chiefFlag;
    }
    public void setConRate(BigDecimal conRate){
    	this.conRate = conRate;
    }
    public BigDecimal getConRate(){
    	return	this.conRate;
    }
    public void setOperUser(Long operUser){
    	this.operUser = operUser;
    }
    public Long getOperUser(){
    	return	this.operUser;
    }
    public void setOperTime(Date operTime){
    	this.operTime = operTime;
    }
    public Date getOperTime(){
    	return	this.operTime;
    }

}