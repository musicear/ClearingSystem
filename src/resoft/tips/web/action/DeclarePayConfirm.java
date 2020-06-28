package resoft.tips.web.action;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

public class DeclarePayConfirm extends AbstractAction {
	private static final Log logger = LogFactory.getLog(DeclarePayConfirm.class);
	public String execute() {
		Message msg = new Message();
		Message returnData;
		String Result="";
		msg.setValue("TaxVouNo", taxVouNo);
		msg.setValue("OriTaxOrgCode", oriTaxOrgCode);
		msg.setValue("OriEntrustDate", oriEntrustDate);	
		msg.setValue("OriTraNo", triTraNo);
		msg.setValue("PayAcct", payAcct);
		msg.setValue("HandOrgName", handOrgName);
		//msg.setValue("DeclareFlag", declareFlag);
		try{
			returnData = TransCommUtil.send("T2400", msg);
			Result=returnData.getValue("ReturnResult");
			if("Y".equals(Result)){
				setMessage("扣款成功，谢谢申报");
				return SUCCESS;
			}else{
                setMessage("查询失败，具体信息是："+returnData.getValue("AddWord"));
                return ERROR;  				
			}
		}catch (IOException e) {
            setMessage("连接后台交易系统失败");
            logger.error("连接后台交易系统失败", e);
            return ERROR;
        }
	}
	  public String getTaxVouNo() {
		    return taxVouNo;
		  }
		  public void setTaxVouNo(String taxVouNo) {
		    this.taxVouNo = taxVouNo;
		  }
		  public String getTriTraNo() {
		    return triTraNo;
		  }
		  public void setTriTraNo(String triTraNo) {
		    this.triTraNo = triTraNo;
		  }
		  public String getOriEntrustDate() {
		    return oriEntrustDate;
		  }
		  public void setOriEntrustDate(String oriEntrustDate) {
		    this.oriEntrustDate = oriEntrustDate;
		  }
		  public String getOriTaxOrgCode() {
		    return oriTaxOrgCode;
		  }
		  public void setOriTaxOrgCode(String oriTaxOrgCode) {
		    this.oriTaxOrgCode = oriTaxOrgCode;
		  }    
		  public String getHandOrgName(){
		    	return handOrgName;
		  }
		  public void setHandOrgName(String handOrgName){
		    	this.handOrgName=handOrgName;
		  }
          public String getPayAcct(){
        	  return payAcct;
          }
          public void setPayAcct(String payAcct){
        	  this.payAcct=payAcct;
          }
//		  public String getDeclareFlag() {
//		    return declareFlag;
//		  }
//		  public void setDeclareFlag(String declareFlag) {
//		    this.declareFlag = declareFlag;
//		  }
    private String taxVouNo;
    private String triTraNo;
    private String oriEntrustDate;
    private String oriTaxOrgCode;
    private String handOrgName;
    private String payAcct;
    //private String declareFlag;
}
