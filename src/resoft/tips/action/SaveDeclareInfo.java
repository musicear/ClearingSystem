package resoft.tips.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.comm.Packager;
import resoft.xlink.comm.impl.GenericXmlPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;


/**
 * <p>读取返回报文1008,保存申报数据到DeclareInfo</p>
 * @Author: liwei
 * @Date: 2008-06-01
 * @Time: 13:38:34
 */

public class SaveDeclareInfo implements Action {
	
	private static final Log logger = LogFactory.getLog(SaveDeclareInfo.class);
    
	public int execute(Message msg) throws Exception {
		String prefix="",xml="";
		
		xml = (String) msg.get("packet");
        Packager packager = new GenericXmlPackager();
        Message returnMsg = packager.unpack(xml.getBytes());
                
        Map params = new HashMap();
        msg.clear();
        //从CFX->MSG->RealHead1008取得,将其转化为//CFX/MSG/RealHead1008/
        String TraNo=returnMsg.getString("//CFX/MSG/RealHead1008/TraNo");
        String TaxOrgCode=returnMsg.getString("//CFX/MSG/RealHead1008/TaxOrgCode");
        String EntrustDate=returnMsg.getString("//CFX/MSG/RealHead1008/EntrustDate");
        //设置委托日期、交易流水号、征收机关
        msg.set("TaxOrgCode", TaxOrgCode);
        msg.set("TraNo", TraNo);
        msg.set("EntrustDate", EntrustDate);
        
        msg.set("TaxVouNo",returnMsg.getString("//CFX/MSG/Payment1008/TaxVouNo"));
        msg.set("OriTaxOrgCode",TaxOrgCode);
        msg.set("OriTraNo",TraNo);
        
        String ReturnResult=returnMsg.getString("//CFX/MSG/RealHead1008/Result").equals("90000")?"Y":"N";
        msg.set("ReturnResult", ReturnResult);
        msg.set("AddWord", returnMsg.getString("//CFX/MSG/RealHead1008/AddWord"));
         
        params.put("TraNo",TraNo );
        params.put("TaxOrgCode", TaxOrgCode);
        params.put("EntrustDate",EntrustDate );
        params.put("TipsWorkDate", returnMsg.getString("//CFX/HEAD/WorkDate"));
        params.put("OriEntrustDate", returnMsg.getString("//CFX/MSG/RealHead1008/OriEntrustDate"));
        params.put("OriLevyNo", returnMsg.getString("//CFX/MSG/RealHead1008/OriLevyNo"));
        params.put("Result", returnMsg.getString("//CFX/MSG/RealHead1008/Result"));
        params.put("AddWord", returnMsg.getString("//CFX/MSG/RealHead1008/AddWord"));
                       
        //从CFX->MSG->TurnAccount1008取得,将其转化为//CFX/MSG/TurnAccount1008/        
        params.put("TreCode", returnMsg.getString("//CFX/MSG/TurnAccount1008/TreCode"));
        params.put("PayeeBankNo", returnMsg.getString("//CFX/MSG/TurnAccount1008/PayeeBankNo"));
        params.put("PayeeAcct", returnMsg.getString("//CFX/MSG/TurnAccount1008/PayeeAcct"));
        params.put("PayeeName", returnMsg.getString("//CFX/MSG/TurnAccount1008/PayeeName"));
        params.put("PayOpBkCode", returnMsg.getString("//CFX/MSG/TurnAccount1008/PayOpBkCode"));
        
        //从CFX->MSG-> Payment1008取得,将其转化为//CFX/MSG/Payment1008/
        params.put("HandOrgName", returnMsg.getString("//CFX/MSG/Payment1008/HandOrgName"));
        params.put("TraAmt", new Double(returnMsg.getString("//CFX/MSG/Payment1008/TraAmt").toString()));
        params.put("TaxVouNo", returnMsg.getString("//CFX/MSG/Payment1008/TaxVouNo"));
        params.put("BillDate", returnMsg.getString("//CFX/MSG/Payment1008/BillDate"));
        params.put("TaxPayCode", returnMsg.getString("//CFX/MSG/Payment1008/TaxPayCode"));
        params.put("TaxPayName", returnMsg.getString("//CFX/MSG/Payment1008/TaxPayName"));
        params.put("CorpCode", returnMsg.getString("//CFX/MSG/Payment1008/CorpCode"));
        params.put("BudgetType", returnMsg.getString("//CFX/MSG/Payment1008/BudgetType"));
        params.put("TrimSign", returnMsg.getString("//CFX/MSG/Payment1008/TrimSign"));
        params.put("CorpType", returnMsg.getString("//CFX/MSG/Payment1008/CorpType"));
        params.put("PrintVouSign", returnMsg.getString("//CFX/MSG/Payment1008/PrintVouSign"));
        params.put("Remark", returnMsg.getString("//CFX/MSG/Payment1008/Remark"));
        params.put("Remark1", returnMsg.getString("//CFX/MSG/Payment1008/Remark1"));
        params.put("Remark2", returnMsg.getString("//CFX/MSG/Payment1008/Remark2"));        
        params.put("RecvTime",DateTimeUtil.getDateTimeString());
        params.put("ChkDate", returnMsg.getString("//CFX/HEAD/WorkDate"));
        logger.info(params);
        String delSql="delete from DeclareInfo where traNo='"+TraNo+"' and taxOrgCode='"+TaxOrgCode+"' and entrustDate='"+EntrustDate+"'";
        DBUtil.executeUpdate(delSql);
        DBUtil.insert("DeclareInfo",params);
                       
        //保存税票明细信息
        Map detailParams =new HashMap();
        detailParams.put("TraNo",returnMsg.getString("//CFX/MSG/RealHead1008/TraNo"));
        detailParams.put("EntrustDate",returnMsg.getString("//CFX/MSG/RealHead1008/EntrustDate"));
        detailParams.put("TaxOrgCode",returnMsg.getString("//CFX/MSG/RealHead1008/TaxOrgCode"));
        detailParams.put("PackNo","0");//实时扣税填0
        detailParams.put("BizCode", "3");
        int taxTypeNum = Integer.parseInt(returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeNum"));        
        prefix = "//CFX/MSG/Payment1008/TaxTypeList1008";
        for(int i = 1;i<=taxTypeNum;i++) {
            String projectId = returnMsg.getString(prefix + "[" + i + "]/ProjectId");
            detailParams.put("ProjectId", new Integer(projectId));
        	detailParams.put("TaxTypeName",returnMsg.getString(prefix + "[" + i + "]/TaxTypeName" ));
        	detailParams.put("TaxStartDate",returnMsg.getString(prefix + "[" + i + "]/TaxStartDate" ));
        	detailParams.put("TaxEndDate",returnMsg.getString(prefix + "[" + i + "]/TaxEndDate" ));
            String taxTypeAmt = returnMsg.getString(prefix + "[" + i + "]/TaxTypeAmt");
            detailParams.put("TaxTypeAmt",new Double(taxTypeAmt));
            
        	logger.info(detailParams);
        	String vochSql=" delete from VoucherTaxType "+"" 
        				  +" where bizCode='3' and taxOrgCode='"+TaxOrgCode+"' " 
        				  +" and entrustDate='"+EntrustDate+"' and packNo='0' " 
        				  +" and traNo='"+TraNo+"' and ProjectId="+projectId;
        	DBUtil.executeUpdate(vochSql);
        	DBUtil.insert("VoucherTaxType",detailParams);
        }
        
         //非列表 申报后显示供确认
         msg.get("TaxPayCode",returnMsg.getString("//CFX/MSG/Payment1008/TaxPayCode"));
         msg.get("TaxPayName",returnMsg.getString("//CFX/MSG/Payment1008/TaxPayName"));
         msg.set("HandOrgName", returnMsg.getString("//CFX/MSG/Payment1008/HandOrgName"));
         msg.set("TaxVouNo", returnMsg.getString("//CFX/MSG/Payment1008/TaxVouNo"));
         msg.set("TraNo", TraNo);
         msg.set("EntrustDate", EntrustDate);
         msg.set("TaxOrgCode", TaxOrgCode);
         //设置返回的税票号
         msg.set("//CFX/MSG/RealHead3001/TraNo", returnMsg.getString("//CFX/MSG/RealHead1008/TraNo"));
         //设置返回的交易金额、税种等信息
         int taxTypeIndex = 1;//税种序号

         msg.set("TraAmt", returnMsg.getString("//CFX/MSG/Payment1008/TraAmt"));
         msg.set("DetailNum", returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeNum"));
         List taxTypeNameList = (List) returnMsg.get("list", "//CFX/MSG/Payment1008/TaxTypeList1008/TaxTypeName");
         for(Iterator itrTaxTypeNameList = taxTypeNameList.iterator(); itrTaxTypeNameList.hasNext(); ){
	           //获取税种信息
	           String taxTypeName = (String) itrTaxTypeNameList.next();
	           String taxStartDate = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxStartDate");
	           String taxEndDate = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxEndDate");
	           String taxType = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxType");	 
	           String taxTypeAmt = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxTypeAmt");
	           String taxDetailNum = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/DetailNum");	
	           List taxSubjCodeList = (List) returnMsg.get("list", "//CFX/MSG/Payment1008/TaxTypeList1008 [" + taxTypeIndex + "]/TaxSubjectList1008/TaxSubjectCode");
	           
	           msg.set("TaxTypeName" + taxTypeIndex, taxTypeName);
	           msg.set("TaxStartDate" + taxTypeIndex, taxStartDate);
	           msg.set("TaxEndDate" + taxTypeIndex, taxEndDate);
	           msg.set("TaxType" + taxTypeIndex, taxType);
	           msg.set("TaxTypeAmt" + taxTypeIndex, taxTypeAmt);
	           msg.set("TaxDetailNum" + taxTypeIndex, taxDetailNum);
	           int taxSubjIndexInTips = 1;
	           for (Iterator itrTaxSubj = taxSubjCodeList.iterator(); itrTaxSubj.hasNext();) {
		             String taxSubjectCode = (String) itrTaxSubj.next();
		             String taxNumber = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxSubjectList1008[" + taxSubjIndexInTips + "]/TaxNumber");
		             String taxAmt = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxSubjectList1008[" + taxSubjIndexInTips + "]/TaxAmt");
		             String ExpTaxAmt = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxSubjectList1008[" + taxSubjIndexInTips + "]/ExpTaxAmt");
		             String DiscountTaxAmt = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxSubjectList1008[" + taxSubjIndexInTips + "]/DiscountTaxAmt");
		             String factTaxAmt = returnMsg.getString("//CFX/MSG/Payment1008/TaxTypeList1008[" + taxTypeIndex + "]/TaxSubjectList1008[" + taxSubjIndexInTips + "]/FactTaxAmt");
		             msg.set("TaxSubjectCode" +taxTypeIndex+ (taxSubjIndexInTips-1), taxSubjectCode);
		             msg.set("TaxNumber" + taxTypeIndex+ (taxSubjIndexInTips-1), taxNumber);
		             msg.set("TaxAmt" + taxTypeIndex+ (taxSubjIndexInTips-1), taxAmt);
		             msg.set("ExpTaxAmt"+taxTypeIndex+ (taxSubjIndexInTips-1), ExpTaxAmt);
		             msg.set("DiscountTaxAmt"+taxTypeIndex+ (taxSubjIndexInTips-1), DiscountTaxAmt);
		             msg.set("FactTaxAmt" + taxTypeIndex+ (taxSubjIndexInTips-1), factTaxAmt);
		             taxSubjIndexInTips++;
	           }
	           
	           taxTypeIndex++;
	           
         }

        return SUCCESS;
    }
}
