package resoft.tips.chqsh;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����Э���ѯ</p>

 */
public class QueryProveInfo implements Action {
	 
	 private static final Log logger = LogFactory.getLog(QueryProveInfo.class);
	 
	 public int execute(Message msg) throws Exception {
		 
	        String TaxPayCode = msg.getString("TaxPayCode");
	        String ProtocolNo = msg.getString("ProtocolNo");	        
	        String verifyresult = "";
	        String sql = "",sqlWhere="",enabledflag="";
	        
     
           if((TaxPayCode==null ||TaxPayCode.equals("")) &&(ProtocolNo==null || ProtocolNo.equals("")) ){
        	   msg.set("ReturnResult", "N");
	            msg.set("AddWord", "��˰�˱�����Э����Ų���ͬʱΪ�գ�");
	            return FAIL;
           }
	       if((TaxPayCode==null ||TaxPayCode.equals(""))&&(ProtocolNo!=null && !ProtocolNo.equals(""))) //��˰�˱���Ϊ�գ�Э����Ų�Ϊ��	        
	       {
	    	   sqlWhere+=" and protocolNo='" + ProtocolNo + "' ";
	       }
	    	   //��˰�˱����ѯ
	       if ( (TaxPayCode!=null && !TaxPayCode.equals(""))&& (ProtocolNo==null || ProtocolNo.equals(""))) {
	        	sqlWhere+=" and TaxPayCode='" + TaxPayCode + "' "; 
	        } 
	        //Э�����
	        if ( (ProtocolNo!=null && !ProtocolNo.equals(""))&&(TaxPayCode!=null && !TaxPayCode.equals("")) ) {
	        	sqlWhere+=" and TaxPayCode='" + TaxPayCode + "' "; 
	        	sqlWhere+=" and protocolNo='" + ProtocolNo + "' ";
	        }
	        	        
	        sql="select * from ProveInfo where 1=1"+sqlWhere+" order by enabledflag";	         
        
	        logger.info("����Э���ѯSQL is:"+sql);
	        List proveinfos = QueryUtil.queryRowSet(sql);
	        if(proveinfos.size()>0){
	        	
	        	Map row = (Map) proveinfos.get(proveinfos.size()-1);	   
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
                String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='" + taxOrgCode + "'");
                if(taxOrgName.equals("")||taxOrgName.equals(null))
                	
                	msg.set("TaxOrgName", "");	//���ջ�������
                else
                	msg.set("TaxOrgName", taxOrgName);
                msg.set("PayOpBkCode", payOpbkCode.trim());	//��������к�
                
                
                msg.set("PayAcct", (String)row.get("PayAcct"));			//�����ʻ�
                //String sendTime = (String)row.get("sendTime");
                //String newDate=sendTime.trim().substring(0, 4)+sendTime.trim().substring(5, 7)+sendTime.trim().substring(8, 10);
                //msg.set("ContactDate",newDate);		//�����˻�����Э��ǩ��ʱ��      
             //   String taxOrgName = DBUtil.queryForString("select taxOrgName from taxOrgMng where taxOrgCode='"+taxOrgCode+"'");
             //   msg.set("TaxOrgName",taxOrgName);    //���ջ�������   
              //  String payBKName = DBUtil.queryForString("select bankorgname from bankorginfo where bankorgcode='"+payBKCode.trim()+"'");
              //  msg.set("PayBkName", payBKName);							//����������
                msg.set("ReturnResult", "Y");
                msg.set("AddWord", "��Э�����");
                verifyresult = (String)row.get("verifyresult");
                enabledflag = (String)row.get("EnabledFlag");
                logger.info("״̬Ϊ��"+enabledflag);
                String addWord = (String)row.get("AddWord");
                String AddWord = addWord.substring(0, 4);

                if(verifyresult.equals("0")&& enabledflag.equals("Y")){
                	msg.set("AddWord", "��Э����¼������֤");					//Э��״̬
                }else if(verifyresult.equals("1")&&enabledflag.equals("Y")){
                	if("��֤ʧ��".equals(AddWord)){
                		msg.set("AddWord", addWord);
                	}else{
                	msg.set("AddWord", "��Э����¼��δ��֤");
                	}
                }else if((verifyresult.equals("1")&&enabledflag.equals("N")) || (verifyresult.equals("1")&&enabledflag.equals("C"))){
                	msg.set("AddWord", "��Э���Ѿ�ɾ��");
                }else{
                	msg.set("AddWord", "��Э��״̬����");
                }
                if(taxOrgName.equals("")||taxOrgName.equals(null))
                {
                	msg.set("AddWord", "���ջ��ز�����");
                	
                }
	        }else{
	            msg.set("ReturnResult", "N");
	            msg.set("AddWord", "��Э�鲻����");
	        }
	        return SUCCESS;
	    }
}
