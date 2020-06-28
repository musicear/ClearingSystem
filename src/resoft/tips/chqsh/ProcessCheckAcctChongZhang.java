package resoft.tips.chqsh;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;
/**
 * <p>�����������̨����,TIPS���е�ʱ����һ��Ĩ��</p>
 * Author: fanchengwei
 * Date: 2007-11-10
 * Time: 09:36:06
 * 
 * Update by liwei 2008.10.27
 * Description������˶���Ĩ�˺��޸���Ӧ״̬ʱ������ݼ�������
 * 
 * �޸��˳�����ɺ��״ֵ̬��
 * adjustAcct���adjustStatus�ֶκ�realtimepayment��batchPackDetail��checkStatus�ֶε�ֵ����һֱ
 * ֵ�б�˵����
 * 			0����ʾ������Ϊ�쳣��Ϣ����resultΪ��90000������MSG_DATAΪ��9004����Ҫ��Ĩ�ʴ���
 * 			1����ʾ����Ҫ����
 * 			2�����˳ɹ�
 * 			3�����˴��������쳣
 * 
 */
public class ProcessCheckAcctChongZhang implements Action{
	
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctChongZhang.class);
	
	public int execute(Message msg) {
    
		SendMsgToBankSystem send = new SendMsgToBankSystem();
		String tipsDate = msg.getString("ChkDate");        		//��������
        String chkAcctType=msg.getString("ChkAcctType");		//��������
        String IADAC_DAT = "";
        String MSG_DATA = "9004";                               //���˽��
        String JRN_NO = "";
        String VCH_NO = "";
        String TR_CODE = "";
        String RcvMsg = "";
        String error = "";
        String updateSql="";
        
        if(chkAcctType.equals("1")){							//0:�ռ�;1:����
        	/**
        	 * ����Ĩ��  	ע��Ҫ�ӽ��׽��Ϊ9004�������
        	 */
    		String sql = "select * from adjustAcct where adjustStatus='0' and MSG_DATA='" + MSG_DATA + "'and chkDate='"+tipsDate+"' order by traNo";
    		logger.info("Ĩ�����ݼ���SQL is:"+sql);
            List adjustLists = null;
            try{
            	adjustLists= QueryUtil.queryRowSet(sql);
            }catch(Exception e){
            	e.printStackTrace();
            	logger.info("Ĩ�����ݲ�ѯ������");
            }
            synchronized(TuxedoPackager.getTuxedoPackager()){
	            
            	for (int i=0;i<adjustLists.size();i++){
	            
		    		Map row=(Map)adjustLists.get(i);  		    		
		    		String entrusDate=(String)row.get("ENTRUSTDATE");
	    			String taxOrgCode=(String)row.get("TAXORGCODE");
	    			String traNo=(String)row.get("TraNo");
	    			String packNo=(String)row.get("PackNo")==null?"":(String)row.get("PackNo");
	    			
	    			//��ʼ��Ĩ����Ϣ	    			
		    		JRN_NO = (String)row.get("JRN_NO");
		    		VCH_NO = (String)row.get("VCH_NO");
		    		TR_CODE = (String)row.get("TR_CODE");
		    		RcvMsg = send.sendOutMsg(JRN_NO, VCH_NO, TR_CODE,VCH_NO.substring(0, 4));
		    		logger.info("�����Ĩ�˽ӵ�����Ӧ��\n["+RcvMsg+"]");
		    		if(RcvMsg.length()==85){
		    			//���ж˽ɿ�δ��		    			 
//		    			id = RcvMsg.substring(37,38);							//��̨���׽��
//				    	TR_CODE = "0" + RcvMsg.substring(11, 14);				//������
				    	IADAC_DAT = RcvMsg.substring(21, 29);					//�����
				    	JRN_NO = RcvMsg.substring(54, 61);						//��־��
				    	VCH_NO = RcvMsg.substring(69, 77);						//��Ʊ��
//				    	MSG_DATA = RcvMsg.substring(81, 85);					//����������
		    			error = RcvMsg.substring(81, 85);		    					    			
		    					    			
		    			if(error.equals("9004")){		//Ĩ�˳ɹ�
		    				DBUtil.executeUpdate("update adjustAcct set adjustStatus='2',reason='��Ĩ��',IADAC_DAT='"+IADAC_DAT+"',JRN_NO='"+JRN_NO+"',VCH_NO='"+VCH_NO+"' where chkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ");		    			
				    		if ( !packNo.equals("") ) {
				    			updateSql="update batchPackDetail set checkStatus='2',chkDate='"+tipsDate+"',result='00001',addWord='��Ĩ��' where tipsWorkDate='"+tipsDate+"' and packNo='"+packNo+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";				    			
				    		}else {
				    			updateSql="update realTimePayMent set checkStatus='2',chkDate='"+tipsDate+"',result='00001',addWord='��Ĩ��' where tipsWorkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";				    			
				    		}	
				    		logger.info("Ĩ�ʳɹ�!�޸�״̬SQL is:"+updateSql);
				    		DBUtil.executeUpdate(updateSql);
				    		updateSql="";
		    			}else{							//Ĩ��ʧ��
		    				DBUtil.executeUpdate("update adjustAcct set adjustStatus='3',reason='Ĩ��ʧ����˶�',IADAC_DAT='"+IADAC_DAT+"',JRN_NO='"+JRN_NO+"',VCH_NO='"+VCH_NO+"' where chkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ");
				    		if ( !packNo.equals("") ) {
				    			updateSql="update batchPackDetail set checkStatus='3',chkDate='"+tipsDate+"',result='00002',addWord='Ĩ��ʧ����˶�' where tipsWorkDate='"+tipsDate+"' and packNo='"+packNo+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
				    		}else {
				    			updateSql="update realTimePayMent set checkStatus='3',chkDate='"+tipsDate+"',result='00002',addWord='Ĩ��ʧ����˶�' where tipsWorkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
				    		}
				    		logger.info("Ĩ��ʧ��!�޸�״̬SQL is:"+updateSql);
				    		DBUtil.executeUpdate(updateSql);
				    		updateSql="";
		    			}
		    		}else {								//Ĩ���쳣			    		
		    			DBUtil.executeUpdate("update adjustAcct set adjustStatus='3',reason='Ĩ������������˶�',IADAC_DAT='"+IADAC_DAT+"',JRN_NO='"+JRN_NO+"',VCH_NO='"+VCH_NO+"' where chkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ");
			    		if ( !packNo.equals("") ) {
			    			updateSql="update batchPackDetail set checkStatus='3',chkDate='"+tipsDate+"',result='00003',addWord='Ĩ������������˶�' where tipsWorkDate='"+tipsDate+"' and packNo='"+packNo+"'and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
			    		}else {
			    			updateSql="update realTimePayMent set checkStatus='3',chkDate='"+tipsDate+"',result='00003',addWord='Ĩ������������˶�' where tipsWorkDate='"+tipsDate+"' and taxOrgCode='"+taxOrgCode+"' and ENTRUSTDATE='"+entrusDate+"' and TraNo='"+traNo+"' ";
			    		}
			    		logger.info("Ĩ����������!�޸�״̬SQL is:"+updateSql);
			    		DBUtil.executeUpdate(updateSql);
			    		updateSql="";
			    	}
		    		JRN_NO = VCH_NO = TR_CODE = RcvMsg = error = "";
	            }   
            }
        }
        return SUCCESS;
    }
	
	public static void main(String[] args){
		Connection conn = DBUtil.getNewConnection();
        resoft.xlink.comm.helper.ThreadLocalContext.getInstance().getContext().setProperty("java.sql.Connection", conn);
		Message msg=new DefaultMessage();
		msg.set("ChkDate", DateTimeUtil.getDateString());
		msg.set("ChkAcctType", "1");
		(new ProcessCheckAcctChongZhang()).execute(msg);
	}
	
}
