package com.cic.partner.manager.mybatis.model;


/**
 * 
 * @author Mango
 */
public class NlifeRisk { 
	private String prodCode; //产品代码
	private String coreRiskCode; //核心条款代码
	private String riskName; //条款名称
	private String prmryFlag; //主条款标识 0主条款1附加条款

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
    public void setRiskName(String riskName){
    	this.riskName = riskName;
    }
    public String getRiskName(){
    	return	this.riskName;
    }
    public void setPrmryFlag(String prmryFlag){
    	this.prmryFlag = prmryFlag;
    }
    public String getPrmryFlag(){
    	return	this.prmryFlag;
    }

}