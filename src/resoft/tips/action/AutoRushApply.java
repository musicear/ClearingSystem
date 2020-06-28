package resoft.tips.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.bankImpl.qhdyh.SzsmRushApplyPackager;
import resoft.xlink.core.Message;

/**
 * <p>���³�����״̬������ʵʱ��˰�ĳ�����־</p>
 * Author: Albert Li
 * Date: 2007-6-7
 * Time: 19:11:18
 */
public class AutoRushApply extends AbstractSyncAction {

	private Socket sender=null;
	String result="",addWord="";

	private static final Log logger = LogFactory.getLog(AutoRushApply.class);
	Configuration conf = Configuration.getInstance();
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	private String taxOrgCode;

	private String STATUS;

	private String sendmsg;
	private String rcvChargeMsg;
	private String oriEntrustDate;
	private String oriTraNo;

	private String CHANNEL_SEQ_NO;

	private String REVERSAL_REASON;
	private String REF_NO;

	private String WorkDate;

    public int execute(Message msg) throws Exception {     	
    	WorkDate=msg.getString("//CFX/HEAD/WorkDate");
    	taxOrgCode=msg.getString("//CFX/MSG/RealHead3001/TaxOrgCode");
    	oriTraNo=msg.getString("//CFX/MSG/RealHead3001/TraNo");
    	oriEntrustDate=msg.getString("//CFX/MSG/RealHead3001/EntrustDate");
    	String result = msg.getString("Result");
        if (!result.equals("94999")) {
            return SUCCESS;
        }
    	logger.info("      oriEntrustDate    "+oriEntrustDate);
    	//DR_ACCT_NO=msg.getString("//CFX/MSG/Payment3001/PayAcct");
    	//CR_ACCT_NO=msg.getString("//CFX/MSG/TurnAccount3001/PayeeAcct");

    	CHANNEL_SEQ_NO=msg.getString("CHANNEL_SEQ_NO");

    	STATUS="1"; 
    	//�����ж��Ƿ�ʱ���жϸý����Ƿ������н�����
    	String bank_trano = check_bank_no(CHANNEL_SEQ_NO);
    	if("".equals(bank_trano) || null == bank_trano){
    		return FAIL;
    	}
    	SaveRushApply();
    	
		SzsmRushApplyPackager pushpack=new SzsmRushApplyPackager();
		if(STATUS.equals("1"))//��֤ͨ��
		{  
			
				pushpack.CreatePushSystemHeadData(WorkDate);
				pushpack.CreateCardBodyData(REF_NO, REVERSAL_REASON, CHANNEL_SEQ_NO);
			    sendmsg=pushpack.MakeUpCardRushApplyPackager();
			    byte[]sendbt=sendmsg.getBytes();
		        logger.info("send cardpush:["+sendmsg+"]");
		        try {
					sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
					sender.setSoTimeout(6*1000);			
					DataOutputStream out=new DataOutputStream(sender.getOutputStream());
					DataInputStream in=new DataInputStream(sender.getInputStream());
				 
					out.write(sendbt); 
				 
					out.flush();
					byte[] packageLengthbyte=new byte[10];
					in.read(packageLengthbyte,0,10);
					String lengthStr=new String(packageLengthbyte);
					int packetlength=Integer.parseInt(lengthStr);
					byte[] recPack=new byte[packetlength];	
					in.read(recPack,0,packetlength); 
					rcvChargeMsg = new String(recPack);
					pushpack.revXml=rcvChargeMsg.trim();
					logger.info("rece sm cardpushpack"+rcvChargeMsg);
					
				}
		        catch (IOException e) { 
					e.printStackTrace();
				} 
			
		}
		if(rcvChargeMsg.equals("NULL")){//�����ͨѶ����
    		result="94999";		
    		addWord="������ͨѶ�쳣";
	    }
		String sql = "update RealtimePayment set checkStatus='4',cancelFlag='Y' " +
        " where TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "' and traNo='" + oriTraNo + "'";
		logger.info("        �Զ�����SQL       "+sql);
		DBUtil.executeUpdate(sql);
	 
        return SUCCESS;
    }
	private void SaveRushApply() {
		// TODO Auto-generated method stub 
	     /*   try {
	        	cancelNo=Getcancelno();
	            String sql = "select count(*) from RushApply where cancelNo='" + cancelNo + "'" +
	                    " and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "'";
	            System.out.println("the sql is:"+sql);
	            int cnt = DBUtil.queryForInt(sql);
	            if (cnt > 0) {
	                sql = "delete from RushApply where  cancelNo='" + cancelNo + "'" +
	                        " and TaxOrgCode='" + taxOrgCode + "' and EntrustDate='" + oriEntrustDate + "'";
	                DBUtil.executeUpdate(sql);
	            }
	        } catch (SQLException e) {
	            logger.error("query data error", e); 
	        }
	        //���������Ϣ
	        Map params = new HashMap();
	        params.put("cancelNo", cancelNo);
	        params.put("entrustDate", oriEntrustDate);
	        params.put("taxOrgCode", taxOrgCode);
	        params.put("oriEntrustDate", oriEntrustDate);
	        params.put("oriTraNo", OriTraNo);
	        params.put("cancelReason", "�����Զ�����");
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        params.put("recvTime", df.format(new Date()));
	        params.put("cancelAnswer", "1");
	        params.put("addWord", "�����ɹ�");
	        DBUtil.insert("RushApply", params); */
	}

	
	private String check_bank_no(String traNo){
		String sql = "select bankTraNo from RealtimePayment where traNo='"+traNo+"'";
		try {
			traNo = DBUtil.queryForString(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return traNo;
	}
}
