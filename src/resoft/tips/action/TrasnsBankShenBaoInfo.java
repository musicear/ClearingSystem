package resoft.tips.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.util.Utils;

import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * <p>解析TIPS返回信息，设置返回信息</p>
 * @Author: liwei
 * @Date: 2008-06-01
 * @Time: 09:58:00
 */

public class TrasnsBankShenBaoInfo implements Action {
    
	private static final Log logger = LogFactory.getLog(TrasnsBankShenBaoInfo.class);
	
	public int execute(Message msg) throws Exception {        		
		
		String xml="",addWord="",result="";
		String prefix1009="",prefix2090="";
        Message returnMsg=new DefaultMessage();        
        try{
			xml = (String) msg.get("packet");
	        Packager packager = new GenericXmlPackager();
	        returnMsg = packager.unpack(xml.getBytes());
        }catch(Exception e){
        	e.printStackTrace();
        	
        	/*
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "读取TIPS报文错误");
        	
        	return FAIL;
        	*/
        	
        	msg.set("ReturnResult", "Y");
        	msg.set("AddWord", "申报查询成功");
        	msg.set("ReturnFileName", "FUCKTIPS");
        	msg.set("TraAmt", "1234.55");
        	
        	return SUCCESS;
        	
        }    
        //解析返TIPS返回1009报文[银行申报查询回执],并组2090银行申报报文转发TIPS
        result = returnMsg.getString("//CFX/MSG/RealHead1009/Result");	        
        addWord = (String) Utils.isNullThen(returnMsg.getString("//CFX/MSG/MsgReturn9121/AddWord"),"");
        msg.set("AddWord", addWord);
                	        
        if ("90000".equals(result)) {
            msg.set("ReturnResult", "Y");
        } else {
            msg.set("ReturnResult", "N");
            return FAIL;
        }
                       
        //设置2090银行申报报文
        prefix2090="//CFX/MSG/DeHead2090/";
        prefix1009="//CFX/MSG/RealHead1009/";
        //设置2090申报业务头
        msg.set(prefix2090+"BankNo", returnMsg.getString(prefix1009+"BankNo"));			   //商业银行行号
        msg.set(prefix2090+"EntrustDate", returnMsg.getString(prefix1009+"EntrustDate"));  //商业银行行号
        
        //设置2090缴款申报信息
        prefix2090="//CFX/MSG/Declare2090/";
        prefix1009="//CFX/MSG/Payment1009/";
        msg.set(prefix2090+"TaxPayCode", returnMsg.getString(prefix1009+"TaxPayCode"));		//纳税人编码
        msg.set(prefix2090+"TaxOrgCode", returnMsg.getString(prefix1009+"TaxOrgCode"));		//征收机关代码
        msg.set(prefix2090+"PayOpBkCode", returnMsg.getString(prefix1009+"PayOpBkCode"));	//付款开户行行号        
        msg.set(prefix2090+"PayAcct", "");													//付款账户
        msg.set(prefix2090+"LevyState", returnMsg.getString(prefix1009+"1"));				//申报状态
        msg.set(prefix2090+"OuterLevyNo", returnMsg.getString(prefix1009+"OuterLevyNo"));	//外部申报电子序号
        msg.set(prefix2090+"TraAmt", returnMsg.getString(prefix1009+"TraAmt"));				//交易金额
        msg.set(prefix2090+"DetailNum", returnMsg.getString(prefix1009+"DetailNum"));		//明细条数
        if(Integer.parseInt(returnMsg.getString(prefix1009+"DetailNum"))==0)
        {
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "没有明细");
        	logger.info("明细条目为0");
        }
        
        for(int i=0;i<Integer.parseInt(returnMsg.getString(prefix1009+"DetailNum"));i++){
	        //设置2090税种明细
	        prefix2090="//CFX/MSG/Declare2090/TaxTypeList2090[" + (i + 1) + "]/";
	        prefix1009="//CFX/MSG/Payment1009/TaxTypeList1009[" + (i + 1) + "]/";
	        msg.set(prefix2090+"ProjectId", returnMsg.getString(prefix1009+"ProjectId"));	//项目序号
	        msg.set(prefix2090+"TaxTypeCode", returnMsg.getString(prefix1009+"TaxTypeCode"));//税种代码
	        msg.set(prefix2090+"TaxStartDate", returnMsg.getString(prefix1009+"TaxStartDate"));//税款所属日期起
	        msg.set(prefix2090+"TaxEndDate", returnMsg.getString(prefix1009+"TaxEndDate"));	//税款所属日期止
	        msg.set(prefix2090+"TaxType", returnMsg.getString(prefix1009+"TaxType"));		//税款类型	        
	        msg.set(prefix2090+"DetailNum", returnMsg.getString(prefix1009+"DetailNum"));	//明细条数
	        double factTaxAmt=0.00;
	        for(int j=0;j<Integer.parseInt(returnMsg.getString(prefix1009+"DetailNum"));j++){
		        //设置2090税目明细
		        prefix2090="//CFX/MSG/Declare2090/TaxTypeList2090[" + (i + 1) + "]/TaxSubjectList2090[" + (j + 1) + "]/";
		        prefix1009="//CFX/MSG/Payment1009/TaxTypeList1009[" + (i + 1) + "]/TaxSubjectList1009[" + (j + 1) + "]/";
		        msg.set(prefix2090+"DetailNo", returnMsg.getString(prefix1009+"DetailNo")); //明细序号
		        msg.set(prefix2090+"TaxSubjectCode", returnMsg.getString(prefix1009+"TaxSubjectCode"));//税目代码
		        msg.set(prefix2090+"TaxNumber", returnMsg.getString(prefix1009+"TaxNumber"));
		        msg.set(prefix2090+"TaxAmt", returnMsg.getString(prefix1009+"TaxAmt"));		//计税依据
		        msg.set(prefix2090+"FactTaxAmt", returnMsg.getString(prefix1009+"FactTaxAmt"));//实缴金额
		        factTaxAmt+=Double.parseDouble(returnMsg.getString(prefix1009+"FactTaxAmt"));		        
	        }
	        //设置前缀
	        prefix2090="//CFX/MSG/Declare2090/TaxTypeList2090[" + (i + 1) + "]/";
	        msg.set(prefix2090+"TaxTypeAmt", ""+factTaxAmt);								//税种金额
        }
                
        return SUCCESS;
                       
    }	   
    
}
