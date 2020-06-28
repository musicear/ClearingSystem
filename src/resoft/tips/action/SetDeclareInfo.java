package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置申报信息</p>
 * Author: liguoyin
 * Date: 2007-8-15
 * Time: 9:51:20
 */

public class SetDeclareInfo implements Action {
    
	public int execute(Message msg) throws Exception {
        
		msg.set("//CFX/MSG/Declare2090/TaxPayCode", msg.get("TaxPayCode"));
        msg.set("//CFX/MSG/Declare2090/TaxOrgCode", msg.get("TaxOrgCode"));
        //msg.set("//CFX/MSG/Declare2090/PayOpBkCode", msg.get("PayOpBkCode"));
        msg.set("//CFX/MSG/Declare2090/PayOpBkCode", "123132");
        msg.set("//CFX/MSG/Declare2090/PayAcct", msg.get("PayAcct"));
        msg.set("//CFX/MSG/Declare2090/HandOrgName", msg.get("HandOrgName"));
        msg.set("//CFX/MSG/Declare2090/LevyState", msg.get("LevyState"));
        msg.set("//CFX/MSG/Declare2090/OuterLevyNo", msg.get("OuterLevyNo"));
        msg.set("//CFX/MSG/Declare2090/DetailNum",msg.getString("DetailNum"));
        msg.set("//CFX/MSG/Declare2090/TraAmt", msg.get("TraAmt"));
        
        //读取税种信息，把相同税种的税目合并
        int detailNum = Integer.parseInt(msg.getString("DetailNum"));
        for(int i=1;i<=detailNum;i++){
	          String taxTypeCode = msg.getString("TaxTypeCode" + i);
	          String taxStartDate = msg.getString("TaxStartDate" + i);
	          String taxEndDate = msg.getString("TaxEndDate" + i);
	          String taxType = msg.getString("TaxType" + i);
	          String TaxTypeAmt=msg.getString("TaxTypeAmt"+i);
	          String TaxDetailNum=msg.getString("TaxDetailNum"+i);
	          msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/ProjectId", Integer.toString(i));
	          msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxTypeCode", taxTypeCode);
	          msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxStartDate", taxStartDate);
	          msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxType", taxType);
	          msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxEndDate", taxEndDate);
	          msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxTypeAmt", TaxTypeAmt);
	          msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/DetailNum", TaxDetailNum);
	          int sumDetailNum=Integer.parseInt(TaxDetailNum);
	          for(int j=1;j<=sumDetailNum;j++){
		            msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxSubjectList2090[" + j + "]/DetailNo",Integer.toString(j));
		            msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxSubjectList2090[" + j + "]/TaxSubjectCode",msg.getString("TaxSubjectCode" + i+(j-1)));
		            msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxSubjectList2090[" + j + "]/TaxNumber",msg.getString("TaxNumber" + i+(j-1)));
		            msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxSubjectList2090[" + j + "]/TaxAmt",msg.getString("TaxAmt" + i+(j-1)));
		            msg.set("//CFX/MSG/Declare2090/TaxTypeList2090[" + i + "]/TaxSubjectList2090[" + j + "]/FactTaxAmt",msg.getString("FactTaxAmt" + i+(j-1)));
	          }
        }

        return SUCCESS;
    }
}
