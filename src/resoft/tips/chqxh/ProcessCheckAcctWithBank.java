package resoft.tips.chqxh;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>�����ź����̨���Ͷ����ļ�</p>
 * Author: liwei
 * Date: 2007-09-10
 * Time: 09:36:06
 */
public class ProcessCheckAcctWithBank implements Action {
	private static Configuration conf = Configuration.getInstance();
	private static final Log logger = LogFactory.getLog(ProcessCheckAcctWithBank.class);
	private static String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");	
	public int execute(Message msg) throws Exception {
        //������Ϣ
		String tipsDate = msg.getString("ChkDate");
        String tipsOrd= msg.getString("ChkAcctOrd");
        String fileName=("TIPSDZ"+tipsOrd);
        //ʵʱ���
        String realTimeSumAmt=DBUtil.queryForString("select sum(cast(traAmt as decimal(15,2))) from RealtimePayment where checkStatus='1' and chkAcctOrd='"+tipsOrd+"' and chkDate='"+tipsDate+"' ");
        if (realTimeSumAmt==null || realTimeSumAmt.trim().equals("")) {
        	realTimeSumAmt="0.00";
        }
        //�������
        String batchSumAmt=DBUtil.queryForString("select sum(cast(traAmt as decimal(15,2))) from BatchPackDetail where checkStatus='1' and chkAcctOrd='"+tipsOrd+"' and chkDate='"+tipsDate+"' ");
        if (batchSumAmt==null || batchSumAmt.trim().equals("")) {
        	batchSumAmt="0.00";
        }
        //�����ܽ��
        double checkSumAmt=Double.parseDouble(realTimeSumAmt) + Double.parseDouble(batchSumAmt) ;
        
        int realTimeCount=DBUtil.queryForInt("select count(*) from RealtimePayment where checkStatus='1' and chkAcctOrd='"+tipsOrd+"' and chkDate='"+tipsDate+"' ");
        int batchCount=DBUtil.queryForInt("select count(*) from BatchPackDetail where checkStatus='1' and chkAcctOrd='"+tipsOrd+"' and chkDate='"+tipsDate+"' ");
        //�����ܱ���
        int checkCount=realTimeCount+batchCount;        
                
        if (checkSumAmt>0 && checkCount>0) {        	
        	ACE2035 ace2035=new ACE2035();
    		//��ʼ�����˻�����Ϣ
    		ace2035.packTags.put("FileName", fileName);
    		ace2035.packTags.put("TipsDate",tipsDate);
    		ace2035.packTags.put("TipsOrd",tipsOrd);
    		ace2035.packTags.put("SumCount",""+checkCount);    		
    		ace2035.packTags.put("SumAmt", resoft.tips.util.CurrencyUtil.getCurrencyFormat((new BigDecimal(checkSumAmt)).toString()));
    		File f = new File(tmpPath, fileName);    		
    		Writer writer = new FileWriter(f);
    		
        	//����ʵʱ
        	if (Double.parseDouble(realTimeSumAmt) >0 && realTimeCount>0) {
        		String sql = "select * from RealtimePayment where checkStatus='1' and chkAcctOrd='"+tipsOrd+"' and chkDate='"+tipsDate+"' order by traNo";
                List realTimePayLists = QueryUtil.queryRowSet(sql);
                for (int i=0;i<realTimePayLists.size();i++){
                	//��ʼ��������ϸ��Ϣ
		    		ACE2035Deatil ace2035Deatil=new ACE2035Deatil();
		    		Map row=(Map)realTimePayLists.get(i);
		    		ace2035Deatil.packTags.put("TipsDate", tipsDate);
		    		ace2035Deatil.packTags.put("TipsOrd", tipsOrd);
		    		ace2035Deatil.packTags.put("TaxUserNo", ACEPackUtil.leftStr("32",(String)row.get("TaxPayCode")));
		    		ace2035Deatil.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("payAcct")));
		    		ace2035Deatil.packTags.put("TaxOrgCode", ACEPackUtil.leftStr("12", (String)row.get("taxOrgCode")));
		    		ace2035Deatil.packTags.put("TrsNo", ACEPackUtil.leftStrFormat("10",(String)row.get("traNo")," "));
		    		ace2035Deatil.packTags.put("TrsAmt", ((String)row.get("traAmt")).replaceAll("\\.", ""));
		    		writer.write(ace2035Deatil.initTransBody()+"\r\n");
                }
        	}
        	//��������
        	if (Double.parseDouble(batchSumAmt) >0 && batchCount>0) {
        		String sql = "select * from BatchPackDetail where checkStatus='1' and chkAcctOrd='"+tipsOrd+"' and chkDate='"+tipsDate+"' order by traNo";
                List batchLists = QueryUtil.queryRowSet(sql);
                for (int i=0;i<batchLists.size();i++){
                	//��ʼ��������ϸ��Ϣ
		    		ACE2035Deatil ace2035Deatil=new ACE2035Deatil();
		    		Map row=(Map)batchLists.get(i);
		    		ace2035Deatil.packTags.put("TipsDate", tipsDate);
		    		ace2035Deatil.packTags.put("TipsOrd", tipsOrd);
		    		ace2035Deatil.packTags.put("TaxUserNo", ACEPackUtil.leftStr("32",(String)row.get("TaxPayCode")));
		    		ace2035Deatil.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("payAcct")));
		    		ace2035Deatil.packTags.put("TaxOrgCode", ACEPackUtil.leftStr("12", (String)row.get("taxOrgCode")));
		    		ace2035Deatil.packTags.put("TrsNo", ACEPackUtil.leftStrFormat("10",(String)row.get("traNo")," "));
		    		ace2035Deatil.packTags.put("TrsAmt", ((String)row.get("traAmt")).replaceAll("\\.", ""));
		    		writer.write(ace2035Deatil.initTransBody()+"\r\n");
		    		writer.flush();
                }
        	}        	
        	writer.close();
        	logger.info("�����ļ������:" + f.getAbsolutePath());   
        	
        	//��װ����������Ϣ
            ace2035.initPack();
            logger.info("������Ϣ��"+ace2035.packTags);            
        	msg.set("ACESendObj", ace2035);
        	
        	//���ͺ���        	
        	SendMsgToBankSys sender=new SendMsgToBankSys(msg);
        	sender.sendMsg();
        	
        	//������˽��    	
	    	ace2035=(ACE2035)msg.get("ACERecObj");
	    	if (ace2035!=null) {
	    		ace2035.makeTransBody();
	    		String markId=((String)ace2035.packTags.get("MarkId")).trim();
	    		msg.set("MarkId", markId);//���ظ� web ϵͳ
	    		if (markId.equals("AAAAAAA")) {//�������ף����ҳɹ�����	    			
	    			DBUtil.executeUpdate("update PayCheck set payTreasFlag='Y' where chkAcctOrd='"+tipsOrd+"' and chkDate='"+tipsDate+"' ");
	    		}else {
	    			msg.set("FailInfo", ((String)ace2035.packTags.get("FailInfo")).trim());
		    	}
	    	}else{//�����ͨѶ��ʱ������������
	    		msg.set("MarkId", "FUCKNET");//���ظ� web ϵͳ
	    		msg.set("FailInfo", "�����ͨѶ��ʱ������������");
	    	}
        }                                      
        return SUCCESS;
    }    
}
