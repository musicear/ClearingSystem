package resoft.tips.bankImpl.sxbank;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����Э���ѯ</p>
 * Author: fanchengwei
 * 
 * Update By yangyuanxu 2010.3.19 
 */
public class QueryProveInfo implements Action {
	 
	 private static final Log logger = LogFactory.getLog(QueryProveInfo.class);
	 
	 public int execute(Message msg) throws Exception {
		 
	        String TaxPayCode = msg.getString("TaxPayCode").trim();
	        String ProtocolNo = msg.getString("ProtocolNo").trim();
	        //String Dflag = msg.getString("Dflag");	        
	        String verifyresult = "";
	        String sql = "",sqlWhere="",enabledflag="";
	        
	        /**
	         * Dflag:��Ϊnull��ʱ���ǽ�������Э��¼�룬��Ҫ��֤ǩԼЭ���Э�����Ƿ����
	         * Dflag:����Ϊnull��ʱ���ǽ�������Э��Ĳ�ѯ
	         * */
	        	        
//	        if ( Dflag == null  ){	//Ĭ�ϲ�ѯδ��֤��Э����Ϣ
//	        	sqlWhere+=" and a.EnabledFlag='Y' ";
//	        }else {
//	        	sqlWhere+=" and a.verifyresult='"+Dflag+"' and a.EnabledFlag='Y' ";
//	        }
	        	        
	        //��˰�˱����ѯ
	        //if ( (Dflag != null) && (TaxPayCode!=null && !TaxPayCode.equals("")) ) {
	        	sqlWhere+=" and TaxPayCode='" + TaxPayCode + "' ";
	      //  }
	        
	        //Э�����
	        if ( ProtocolNo!=null && !ProtocolNo.equals("") ) {
	        	sqlWhere+=" and protocolNo='" + ProtocolNo + "' ";
	        }
	        	        
	        sql="select * from ProveInfo where 1=1"+sqlWhere;	         
//	        if ( Dflag==null ) {	//����Э��¼����֤�Ƿ��Ѿ���ǩԼЭ��
//	        	int count=DBUtil.queryForInt("select count(*) from ("+sql+")");
//	        	if (count>0) {
//	        		msg.set("ReturnResult", "Y");
//		            msg.set("AddWord", "��Э�����Ѿ�ǩԼ��");
//		            //return FAIL;
//	        	}
//	        }	        
	        logger.info("����Э���ѯSQL is:"+sql);
	        List proveinfos = QueryUtil.queryRowSet(sql);
	        if(proveinfos.size()>0){
	        	Map row = (Map) proveinfos.get(0);	   
	        	String payOpbkCode = (String)row.get("PayOpbkCode");
	        	//String payOpbkName = DBUtil.queryForString("select bankorgname from bankorginfo where Bankorgcode='"+payOpbkCode.trim()+"'");
        		//msg.set("PayOpBkName", payOpbkName.trim()); //����������  
        		String payBKCode = (String)row.get("PayBkCode");
        		msg.set("PayBkCode", payBKCode.trim());		//�����д���
                msg.set("TaxPayName", (String)row.get("taxPayName"));	//��˰������
                msg.set("PayAcctName", (String)row.get("HandOrgName"));	//�ɿλ����
                msg.set("TaxPayCode", (String)row.get("TaxPayCode"));	//��˰�˱��
                msg.set("ProtocolNo", (String)row.get("ProtocolNo"));	//Э�����
                String taxOrgCode = (String)row.get("TaxOrgCode");
                msg.set("TaxOrgCode", taxOrgCode.trim());	//���ջ��ش���
                msg.set("PayOpBkCode", payOpbkCode.trim());	//��������к�
                msg.set("PayAcct", (String)row.get("PayAcct"));			//�����ʻ�
                String sendTime = (String)row.get("sendTime");
                String newDate=sendTime.trim().substring(0, 4)+sendTime.trim().substring(5, 7)+sendTime.trim().substring(8, 10);
                msg.set("ContactDate",newDate);		//�����˻�����Э��ǩ��ʱ��      
             //   String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='"+taxOrgCode+"'");
             //   msg.set("TaxOrgName",taxOrgName);    //���ջ�������   
              //  String payBKName = DBUtil.queryForString("select bankorgname from bankorginfo where bankorgcode='"+payBKCode.trim()+"'");
              //  msg.set("PayBkName", payBKName);							//����������
                msg.set("ReturnResult", "Y");
                msg.set("AddWord", "��Э�����");
                verifyresult = (String)row.get("verifyresult");
                enabledflag = (String)row.get("EnabledFlag");
                String addWord = (String)row.get("AddWord");
                String AddWord = addWord.substring(0, 4);

                if(verifyresult.equals("0")&& enabledflag.equals("Y")){
                	msg.set("ProveStatus", "��Э����¼������֤");					//Э��״̬
                }else if(verifyresult.equals("1")&&enabledflag.equals("Y")){
                	if("��֤ʧ��".equals(AddWord)){
                		msg.set("ProveStatus", addWord);
                	}else{
                	msg.set("ProveStatus", "��Э����¼��δ��֤");
                	}
                }else if((verifyresult.equals("1")&&enabledflag.equals("N")) || (verifyresult.equals("1")&&enabledflag.equals("C"))){
                	msg.set("ProveStatus", "��Э���Ѿ�ɾ��");
                }else{
                	msg.set("ProveStatus", "��Э��״̬����");
                }
                
	        }else{
	            msg.set("ReturnResult", "N");
	            msg.set("AddWord", "��Э�鲻����");
	        }
	        return SUCCESS;
	    }
}
