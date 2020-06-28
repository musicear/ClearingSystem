package resoft.tips.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>查询是否已经签约</p>
 * Author: liwei
 * Date: 2007-07-09
 * Time: 13:06:06
 */
public class CheckProveInfoStatus implements Action {
    
	private static final Log logger = LogFactory.getLog(CheckProveInfoStatus.class);
    
	public int execute(Message msg) throws Exception {

    	String payOpBkCode=msg.getString("PayOpBkCode");//msg.getString("PayOpBkCode");
        String payAcct = msg.getString("PayAcct");//付款帐号
        String taxPayCode = msg.getString("TaxPayCode");//纳税人编码
        String taxPayName = msg.getString("TaxPayName");//纳税人名称
        String protocolNo = msg.getString("ProtocolNo");//协议书号
        String taxOrgCode = msg.getString("TaxOrgCode");//征收机关代码
        String BranceNo=msg.getString("BranchNo");
        String sql = "select count(*) from ProveInfo where EnabledFlag='Y' and taxPayName like '%"+taxPayName+"%' and taxOrgCode='" + taxOrgCode + "' and taxPayCode='" + taxPayCode + "' and payAcct='" + payAcct + "' and protocolNo='" + protocolNo + "' ";
        logger.info("签订协议验证协议是否已签约SQL is:" + sql);
        int count = DBUtil.queryForInt(sql);
        if (count > 0) {//已经成功签约
            msg.set("ReturnResult", "Y");
            msg.set("AddWord", "该帐号已经签约");
            return FAIL;
        } else {
        	List bankInfos=QueryUtil.queryRowSet("select BankPayCode from BankOrgInfo where BankOrgCode='"+BranceNo+"'");
        	if(bankInfos.size()>0){
        		Map valueMap=(Map)bankInfos.get(0);
        		String BankPayCode=(String)valueMap.get("BankPayCode");
        		msg.set("PayBkCode", BankPayCode);//BankPayCode
            	msg.set("VCSign", "0");
            	Map params = new HashMap();
                params.put("taxOrgCode",taxOrgCode);//征收机关代码
                params.put("taxPayCode",taxPayCode);//纳税人编码
                params.put("payOpBkCode",payOpBkCode);//payOpBkCode需要tuxedo查询数据库？？？？
                msg.set("PayOpBkCode", payOpBkCode);//payOpBkCode
                params.put("payBkCode",BankPayCode);//？？？？？？？？？？？？？？？？？？？？
                params.put("payAcct",payAcct);      //付款帐号
                params.put("TaxPayName",taxPayName);
                String acctSeq = msg.getString("AcctSeq");
                if( acctSeq!=null && !acctSeq.equals("") ) {
                    params.put("AcctSeq",msg.getString("AcctSeq"));
                }
                //缴款单位名称
                params.put("handOrgName",msg.get("HandOrgName")==null?msg.get("PayAcctName"):msg.get("HandOrgName"));
                params.put("protocolNo",protocolNo);//协议书号
                params.put("sendTime", DateTimeUtil.getDateTimeString());
                params.put("verifyResult","0");
                params.put("addWord","协议签定成功!");
                params.put("inputTeller",msg.getString("UserId"));//泉州固定为999999
                params.put("EnabledFlag","Y");
                try {
                	DBUtil.insert("ProveInfo",params);
                }catch(Exception e){
                    msg.set("ReturnResult", "N");
                    msg.set("AddWord", "录入三方协议数据库异常");
                    return FAIL;
                }
                return SUCCESS;
        	}else{
                msg.set("ReturnResult", "N");
                msg.set("AddWord", "付款帐号对应行号不存在");
                return FAIL;
        	}


      }//end else
    }
    }
