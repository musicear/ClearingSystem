package resoft.tips.chqxh;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
 * <p>����Э���ѯ</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 11:02:00
 */

public class SfxyCx implements Action {
	
	private static final Log logger = LogFactory.getLog(SfxyCx.class);
	private static Configuration conf = Configuration.getInstance();
	
	String sqlWhere="",tempFileData="";
    
	//��ִ��ϸ��Ϣ:��˰�˱���(20)+�Ǽ�����(8)+ί������(8)+�����ʺ�(19)+�����˻�����(62)+��˰������(62)+���ջ��ش���(12)+���ջ�������(62)+Э���(60)+��������(8)+�Ǽǹ�Ա(6)+ɾ����Ա(6)+��֤��־(1)  	
	private String []sfxyLists={"TaxPayCode","SendTime","SendTime","PayAcct","handOrgName","TaxPayName","TaxOrgCode","TaxOrgName","ProtocolNo","RemoveTime","InputTeller","RemoveTeller","VerifyResult"};
	private String []sfxyLenFormat={"20","8","8","19","62","62","12","62","60","8","6","6","1"};
	
    public int execute(Message msg) throws Exception {    	    	
    	ACEPackager ace2009=(ACE2009)msg.get("ACEObj");
    	//��ʼ��ACE���ױ�����
    	ace2009.makeTransBody();
    	msg.set("BankOrgCode", (String)ace2009.pkTHHeadList.get("BankOrgCode"));//��������
    	msg.set("InputTeller", (String)ace2009.pkTHHeadList.get("InputTeller"));//��Ա��
    	
    	String taxPayCode=(String)ace2009.pkTHBodyList.get("TaxPayCode");		//��˰�˱���
    	String payAcct=(String)ace2009.pkTHBodyList.get("PayAcct");				//�����ʺ� 
    	//ģ����ѯ
    	if (!taxPayCode.trim().equals("")) {
    		sqlWhere=" and a.taxPayCode='"+taxPayCode+"' "; 
    	}
    	if (!payAcct.trim().equals("")) {
    		sqlWhere=" and a.payAcct='"+payAcct+"' ";
    	}
    	
    	//��ѯ�Ƿ���Э����Ϣ    	
    	int count=DBUtil.queryForInt("select count(*) from ProveInfo a where 1=1 "+sqlWhere);
    	logger.info("�ź�����Э���ѯsql��"+"select count(*) from ProveInfo a where 1=1 "+sqlWhere);
        if(count>0){
        	//��ѯ����
        	msg.set("sfxyCount", ACEPackUtil.getFieldFormatDef("16", ""+count));
        	List queryList = QueryUtil.queryRowSet("select a.TaxPayCode,a.SendTime,a.SendTime,a.PayAcct,a.handOrgName,a.TaxPayName,a.TaxOrgCode,c.TaxOrgName,a.ProtocolNo,a.RemoveTime,a.InputTeller,a.RemoveTeller,a.VerifyResult,a.EnabledFlag from ProveInfo a,BankMng b,taxOrgMng c where a.payBkCode=b.ReckBankNo and a.taxOrgCode=c.taxOrgCode "+sqlWhere);
        	logger.info("�ź�����Э���ѯ��ѯ��ϸ��"+"select * from ProveInfo a,BankMng b,taxOrgMng c where a.payBkCode=b.ReckBankNo and a.taxOrgCode=c.taxOrgCode "+sqlWhere);
        	if(queryList.size()>0){
        		for(int i=0;i<queryList.size();i++){
	        		Map row=(Map)queryList.get(i);	        		
	        		String temp="";
	        		for(int j=0;j<(sfxyLists.length-1);j++){
	        			temp+=ACEPackUtil.getFieldFormatDef(sfxyLenFormat[j], (String)row.get(sfxyLists[j]));
	        			//logger.info("["+sfxyLists[i]+":"+ACEPackUtil.getFieldFormatDef(sfxyLenFormat[j], (String)row.get(sfxyLists[j]))+"]");
	        		}	        		
	        		String verifyResult=(String)row.get("VerifyResult");//0:��֤ͨ����1:��֤δͨ��
	        		String enabledFlag=(String)row.get("EnabledFlag");	//Y:��ЧЭ�飬N:��ЧЭ��
	        		logger.info("EnabledFlag val:["+enabledFlag+"],verifyResult:["+verifyResult+"]");
	        		String proveFlag="";
	        		if (enabledFlag.equals("Y")) {	//Э����Ч
	        			if (verifyResult.equals("0")) {
	        				proveFlag="1";		//Э����Ч��ͨ����֤
	        			}else {
	        				proveFlag="0";		//Э����Ч,�Ǽ�δ��֤
	        			}
	        		}else {
	        			if (verifyResult.equals("0")) {
	        				proveFlag="2";		//��֤����
	        			}else {
	        				proveFlag="3";		//δ��֤����
	        			}
	        		}
	        		temp+=proveFlag;
	        		//�����ϸ��Ϣ
	        		tempFileData=msg.getString("TempFileData")==null?"":msg.getString("TempFileData");
	        		tempFileData+=temp+"\n";
	        		msg.set("TempFileData", tempFileData);	        		
        		}
        		ace2009.tradeStatus="000";
                msg.set("VCResult", ace2009.tradeStatus);			
        	}           	        	        
        }else{        	
        	ace2009.tradeStatus="001";
        	msg.set("VCResult",ace2009.tradeStatus);
        	msg.set("AddWord", "Э�鲻����");        	
        }
    	    	
    	/**
    	 * ���׷�����Ϣ
    	 * 		�ɹ���״̬��[3]|����[16]|�ļ���[19]
    	 * 		ʧ�ܣ�״̬��[3]|������Ϣ����
    	 * */
    	if (ace2009.tradeStatus.equals("000")) {//�ɹ�
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("sfxyCount")+"|"+getSfxyFileName(msg));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	return SUCCESS;
    }
    
    //��Э���ѯ��Ϣ������ʱ�ļ�
    public String getSfxyFileName(Message msg) throws IOException{
    	//�ļ�����TIPS[4]+������[6]+��Ա��[6]+"000"[3]		len:19
    	String fileName = "TIPS"+msg.getString("BankOrgCode")+msg.getString("InputTeller")+"000";
        String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
        File f = new File(tmpPath, fileName);
        try {
            Writer writer = new FileWriter(f);
            writer.write((String)msg.getString("TempFileData"));            
            writer.close();
            logger.info("Э����ϸ��Ϣ��["+(String)msg.getString("TempFileData")+"]");            
            logger.info("��Ϣ�����:" + f.getAbsolutePath());
        } catch (IOException e) {            
            logger.error("��Ϣ����Ϊ��" + msg.getString("TempFileData"));
            e.printStackTrace();
            throw e;
        }
        return fileName;    	
    }    
}
