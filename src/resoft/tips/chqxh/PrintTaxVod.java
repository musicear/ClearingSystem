package resoft.tips.chqxh;
import java.io.File;
import java.io.FileWriter;
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
 * <p>��ӡ˰Ʊ��Ϣ</p>
 * Author: liwei
 * Date: 2007-10-22
 * Time: 18:06:06
 */

public class PrintTaxVod implements Action{
	private static final Log logger = LogFactory.getLog(PrintTaxVod.class);
	private static Configuration conf = Configuration.getInstance();
	
	String fileName = "";
    String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	
	String bankOrgCode="",inputTeller="",payOpBkCode="",payOpBkName="",payBkCode="402653000011",payBkName="";
	String printType="",taxOrgCode="",payAcct="",taxPayCode="",startDate="",endDate="";
	String taxOrgName="";
	String sqlWhere="",tempFileData="";
	
	public int execute(Message msg) throws Exception {
		ACEPackager ace2006=(ACE2006)msg.get("ACEObj");
    	//��ʼ��ACE���ױ�����
		ace2006.makeTransBody();
		bankOrgCode=(String)ace2006.pkTHHeadList.get("BankOrgCode");	//��������
		inputTeller=(String)ace2006.pkTHHeadList.get("InputTeller");	//��Ա��
		//��ӡ��Ա
		TaxPiaoInfo.printTeller=inputTeller;
		
		printType=(String)ace2006.pkTHBodyList.get("PrintType");		//��ӡ���� 1��ʵʱ��2��������3�����ռ������ڲ�ѯ
		taxOrgCode=(String)ace2006.pkTHBodyList.get("TaxOrgCode");		//���ջ���
		payAcct=(String)ace2006.pkTHBodyList.get("PayAcct");			//�ʺ�
		taxPayCode=(String)ace2006.pkTHBodyList.get("TaxPayCode");		//��˰�˱���
		startDate=(String)ace2006.pkTHBodyList.get("StartDate");		//������
		endDate=(String)ace2006.pkTHBodyList.get("EndDate");			//ֹ����
		
		fileName="TIPS"+bankOrgCode+inputTeller+"000";
		
		//���ջ���
		if(taxOrgCode!=null && !taxOrgCode.equals("") && printType.equals("2")){
			sqlWhere+=" and a.taxOrgCode='"+taxOrgCode+"' ";
			taxOrgName=DBUtil.queryForString("select taxOrgName from TaxOrgMng where EnabledFlag='Y' and taxOrgCode='"+taxOrgCode+"' ");
			ace2006.tradeStatus="000";
		}
		if(ace2006.tradeStatus.equals("000")){
			//�������д���ת��
			payOpBkCode=DBUtil.queryForString("select DMBENO from pmdma where DMSBNO='"+bankOrgCode+"' ");
			//���ҿ���������
			payOpBkName=DBUtil.queryForString("select ORUCUN from agora where ORUCUS='"+bankOrgCode+"' ");
			//���Ҹ������к�
			payBkName=DBUtil.queryForString("select GENBANKNAME from bankMng where RECKBANKNO='"+payBkCode+"' ");
			
			//�ɹ����˻��� (��TIPS���˳ɹ�)
			sqlWhere+=" and a.result='90000' and a.checkStatus='1' ";
			//�������к�
			//sqlWhere+=" and a.payOpBkCode='"+payOpBkCode+"' ";			
			//�����ʺ�
			if(payAcct!=null && !payAcct.equals("")){
				sqlWhere+=" and a.payAcct='"+payAcct+"' ";
			}
			//��˰�˱���
			if (taxPayCode!=null && !taxPayCode.equals("")) {
				sqlWhere+=" and a.taxPayCode='"+taxPayCode+"' ";
			}			
			//��ѯ����
			sqlWhere+=" and a.bankTraDate='"+startDate+"' ";							
			
			//�����ļ�					
			ace2006.tradeStatus="000";
			try {
				ace2006.tradeStatus="000";
				msg.set("FileName", fileName);
				msg.set("PiaoCount", makeTaxPiaoDeatil());
			}catch(Exception e){
				ace2006.tradeStatus="010";
				msg.set("AddWord", "����˰Ʊ��ӡ�ļ������쳣");
				logger.info("����˰Ʊ��ӡ�ļ������쳣��");
				e.printStackTrace();				
			}
		}else {
			ace2006.tradeStatus="020";
			msg.set("AddWord", "���ջ��ش���");
		}
		
		msg.set("VCResult", ace2006.tradeStatus);
		
		if (ace2006.tradeStatus.equals("000")) {//�ɹ�
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("FileName")+"|"+msg.getString("PiaoCount"));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	    	    	
		return SUCCESS;
	}
	
	//���ɲ�ѯ�ļ�
	public String makeTaxPiaoDeatil() throws Exception {		
        File f = new File(tmpPath, fileName);
        Writer writer = new FileWriter(f);
        
        //��ѯʵʱ
        String realTimeSql="select * from realTimePayMent a where 1=1 "+sqlWhere;        
        List realTimeList=QueryUtil.queryRowSet(realTimeSql);
        
        //��ѯ����
		String batchSql="select * from BatchPackDetail a where 1=1 "+sqlWhere;
		List batchList=QueryUtil.queryRowSet(batchSql);
		
		if(realTimeList.size()>0||batchList.size()>0){
			writer.write("\033@\n");
			writer.write("\033(C1L1H10L10H\n");
		}
		
		//����ʵʱ
		for(int i=0;i<realTimeList.size();i++){
			Map row=(Map)realTimeList.get(i);
			row.put("TaxVodType", "1");
			TaxPiaoInfo piaoInfo=new TaxPiaoInfo();
			piaoInfo.packTags.put("PayOpBkName", (String)piaoInfo.packTags.get("PayOpBkName")+payOpBkName);
			piaoInfo.packTags.put("TaxOrgName", (String)piaoInfo.packTags.get("TaxOrgName")+taxOrgName);			
			piaoInfo.packTags.put("PayeeOrgName", (String)piaoInfo.packTags.get("PayeeOrgName")+payBkName);			
            writer.write(piaoInfo.initTaxPiaoInfo(row)+"\n"); 
            piaoInfo=null;            
            writer.flush();
            //��һ�δ�ӡ��ʼ��3�У�֮��Ϳ�5��
            if (TaxPiaoInfo.startNullRows==3) {
            	TaxPiaoInfo.startNullRows=4;
            }
            if (TaxPiaoInfo.startNullRows==4) {
            	TaxPiaoInfo.startNullRows=5;
            }else if(TaxPiaoInfo.startNullRows==5){
            	TaxPiaoInfo.startNullRows=4;
            }
		}		
		
		//��������	
		for(int i=0;i<batchList.size();i++){
			Map row=(Map)batchList.get(i);
			row.put("TaxVodType", "2");
			TaxPiaoInfo piaoInfo=new TaxPiaoInfo();
			piaoInfo.packTags.put("PayOpBkName", (String)piaoInfo.packTags.get("PayOpBkName")+payOpBkName);
			piaoInfo.packTags.put("TaxOrgName", (String)piaoInfo.packTags.get("TaxOrgName")+taxOrgName);			
			piaoInfo.packTags.put("PayeeOrgName", (String)piaoInfo.packTags.get("PayeeOrgName")+payBkName);			
            writer.write(piaoInfo.initTaxPiaoInfo(row)+"\n"); 
            piaoInfo=null;            
            writer.flush();
            //��һ�δ�ӡ��ʼ��3�У�֮��Ϳ�5��
            if (TaxPiaoInfo.startNullRows==3) {
            	TaxPiaoInfo.startNullRows=4;
            }
            if (TaxPiaoInfo.startNullRows==4) {
            	TaxPiaoInfo.startNullRows=5;
            }else if(TaxPiaoInfo.startNullRows==5){
            	TaxPiaoInfo.startNullRows=4;
            }
		}		
		writer.close();
		
		//�޸�ʵʱ��˰�Ĵ�ӡ����
		if (realTimeList.size()>0) {
			DBUtil.executeUpdate("update realTimePayMent a set a.printTimes=a.printTimes+1 where 1=1 "+sqlWhere);
		}
		//�޸�������˰�Ĵ�ӡ����
		if (batchList.size()>0) {
			DBUtil.executeUpdate("update BatchPackDetail a set a.printTimes=a.printTimes+1 where 1=1 "+sqlWhere);
		}
		
		logger.info("��Ϣ�����:" + f.getAbsolutePath());					
		return ""+(realTimeList.size()+batchList.size());
	}
	
}
