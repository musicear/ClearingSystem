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
 * <p>��ѯ�Ƿ��Ѿ�ǩԼ</p>
 * Author: liwei
 * Date: 2007-07-09
 * Time: 13:06:06
 */
public class CheckProveInfoStatus implements Action {
    
	private static final Log logger = LogFactory.getLog(CheckProveInfoStatus.class);
    
	public int execute(Message msg) throws Exception {

    	String payOpBkCode=msg.getString("PayOpBkCode");//msg.getString("PayOpBkCode");
        String payAcct = msg.getString("PayAcct");//�����ʺ�
        String taxPayCode = msg.getString("TaxPayCode");//��˰�˱���
        String taxPayName = msg.getString("TaxPayName");//��˰������
        String protocolNo = msg.getString("ProtocolNo");//Э�����
        String taxOrgCode = msg.getString("TaxOrgCode");//���ջ��ش���
        String BranceNo=msg.getString("BranchNo");
        String sql = "select count(*) from ProveInfo where EnabledFlag='Y' and taxPayName like '%"+taxPayName+"%' and taxOrgCode='" + taxOrgCode + "' and taxPayCode='" + taxPayCode + "' and payAcct='" + payAcct + "' and protocolNo='" + protocolNo + "' ";
        logger.info("ǩ��Э����֤Э���Ƿ���ǩԼSQL is:" + sql);
        int count = DBUtil.queryForInt(sql);
        if (count > 0) {//�Ѿ��ɹ�ǩԼ
            msg.set("ReturnResult", "Y");
            msg.set("AddWord", "���ʺ��Ѿ�ǩԼ");
            return FAIL;
        } else {
        	List bankInfos=QueryUtil.queryRowSet("select BankPayCode from BankOrgInfo where BankOrgCode='"+BranceNo+"'");
        	if(bankInfos.size()>0){
        		Map valueMap=(Map)bankInfos.get(0);
        		String BankPayCode=(String)valueMap.get("BankPayCode");
        		msg.set("PayBkCode", BankPayCode);//BankPayCode
            	msg.set("VCSign", "0");
            	Map params = new HashMap();
                params.put("taxOrgCode",taxOrgCode);//���ջ��ش���
                params.put("taxPayCode",taxPayCode);//��˰�˱���
                params.put("payOpBkCode",payOpBkCode);//payOpBkCode��Ҫtuxedo��ѯ���ݿ⣿������
                msg.set("PayOpBkCode", payOpBkCode);//payOpBkCode
                params.put("payBkCode",BankPayCode);//����������������������������������������
                params.put("payAcct",payAcct);      //�����ʺ�
                params.put("TaxPayName",taxPayName);
                String acctSeq = msg.getString("AcctSeq");
                if( acctSeq!=null && !acctSeq.equals("") ) {
                    params.put("AcctSeq",msg.getString("AcctSeq"));
                }
                //�ɿλ����
                params.put("handOrgName",msg.get("HandOrgName")==null?msg.get("PayAcctName"):msg.get("HandOrgName"));
                params.put("protocolNo",protocolNo);//Э�����
                params.put("sendTime", DateTimeUtil.getDateTimeString());
                params.put("verifyResult","0");
                params.put("addWord","Э��ǩ���ɹ�!");
                params.put("inputTeller",msg.getString("UserId"));//Ȫ�ݹ̶�Ϊ999999
                params.put("EnabledFlag","Y");
                try {
                	DBUtil.insert("ProveInfo",params);
                }catch(Exception e){
                    msg.set("ReturnResult", "N");
                    msg.set("AddWord", "¼������Э�����ݿ��쳣");
                    return FAIL;
                }
                return SUCCESS;
        	}else{
                msg.set("ReturnResult", "N");
                msg.set("AddWord", "�����ʺŶ�Ӧ�кŲ�����");
                return FAIL;
        	}


      }//end else
    }
    }
