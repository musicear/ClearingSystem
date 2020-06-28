package resoft.tips.chqsh;


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
 * <p>���ع������ж˽ɿ���Ϣ</p>
 * @Author: liwei
 * @Date: 2008-06-07
 * @Time: 18:06:06
 */

public class ShowYhdjkInfo implements Action{
	
	private static final Log logger = LogFactory.getLog(ShowYhdjkInfo.class);
	private static Configuration conf = Configuration.getInstance();
	
	SendMsgToBankSystem send = new SendMsgToBankSystem();
	private String payOpBkNameSql="select brtbrnam from brt where brtbr=";
	
	String fileName = "",rcvMsg="";
    String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	
	String bankOrgCode="",inputTeller="",payOpBkCode="",payOpBkName="",payBkName="";
	String printType="",taxOrgCode="",traNo="",entrustDate="";
	String taxOrgName="";
	String sqlWhere="",tempFileData="";
	String tradeStatus="";
	
	public int execute(Message msg) throws Exception {
		
		bankOrgCode=msg.getString("BranchNo");			//��������
		inputTeller=msg.getString("UserId");			//��Ա��
		//��ӡ��Ա
		TaxPiaoInfo.printTeller=inputTeller;
				
		taxOrgCode=msg.getString("TaxOrgCode");			//���ջ���
		entrustDate=msg.getString("EntrustDate");		//ί������
		traNo=msg.getString("TraNo");					//��ˮ��
		
		fileName="TIPS"+bankOrgCode+inputTeller+"000";		
		tradeStatus="000";
						
		//���ջ���
		if(taxOrgCode!=null && !taxOrgCode.equals("")){			
			taxOrgName=DBUtil.queryForString("select taxOrgName from TaxOrgMng where EnabledFlag='Y' and taxOrgCode='"+taxOrgCode+"' ");
			tradeStatus="000";
		}
			
		
		//ȡ����������
		payOpBkNameSql+=bankOrgCode;
		rcvMsg = send.sendMsg(payOpBkNameSql, "8802");
		if(!rcvMsg.equals("") && rcvMsg != null){
			int i = rcvMsg.indexOf(10) + 1;
			payOpBkName = rcvMsg.substring(i).trim();
			logger.info("payOpBkName is:"+payOpBkName);			
		}else{
			msg.set("ReturnResult", "N");
			msg.set("AddWord", "δ��ȡ������������ƣ�");
			tradeStatus = "222";
		}
				
		if(tradeStatus.equals("000")){			
			//�����ļ�					
			tradeStatus="000";
			try{
				msg.set("FileName", fileName);
				String piaoCount=makeTaxPiaoDeatil();
				msg.set("PiaoCount", piaoCount);
				if (piaoCount.equals("0")){//û�з��������ļ�¼
					tradeStatus="040";
					msg.set("AddWord", "û�з��������ļ�¼");
				}
			}catch(Exception e){
				tradeStatus="010";
				msg.set("AddWord", "����˰Ʊ��ӡ�ļ������쳣");
				logger.info("����˰Ʊ��ӡ�ļ������쳣��");
				e.printStackTrace();				
			}
		}else if(!tradeStatus.equals("111")){
			tradeStatus="020";
			msg.set("AddWord", "δ��ȡ�����ջ��ػ򸶿�����к�");
		}
		msg.set("VCResult", tradeStatus);
		
		if (tradeStatus.equals("000")) {//�ɹ�
			msg.set("ReturnResult", "Y");	
			msg.set("ReturnFileName", fileName);
    	}else {
    		msg.set("ReturnResult", "N");
    	}    	    	
		return SUCCESS;
	}
	
	//���ɲ�ѯ�ļ�
	public String makeTaxPiaoDeatil() throws Exception {		
        File f = new File(tmpPath, fileName);
        Writer writer = new FileWriter(f);              
		
		//���ж˽ɿ�
		String yhdjkSql="select * from DeclareInfo a where a.entrustDate='"+entrustDate+"' and a.taxOrgCode='"+taxOrgCode+"'  and traNo='"+traNo+"' ";
		logger.info("���ж˽ɿ"+yhdjkSql);
		List yhdjkList=QueryUtil.queryRowSet(yhdjkSql);
		logger.info("################   yhdjkList  = " + yhdjkList.size());		 	
		
		//�������ж˽ɿ�
		for(int i=0;i<yhdjkList.size();i++){
			Map row=(Map)yhdjkList.get(i);
			row.put("TaxVodType", "3");
			TaxPiaoInfo piaoInfo=new TaxPiaoInfo();
			piaoInfo.packTags.put("PayOpBkName", (String)piaoInfo.packTags.get("PayOpBkName")+payOpBkName);
			piaoInfo.packTags.put("TaxOrgName", (String)piaoInfo.packTags.get("TaxOrgName")+taxOrgName);			
			piaoInfo.packTags.put("PayeeOrgName", (String)piaoInfo.packTags.get("PayeeOrgName")+payBkName);
			
			if(i%2 == 1){
				writer.write(piaoInfo.initTaxPiaoInfo(row));
				writer.write("\f\n");
			}else{
				writer.write(piaoInfo.initTaxPiaoInfo(row)+"\n\n\n\n\n");
			}
		
            piaoInfo=null;            
            writer.flush();           
		}		
		writer.close();
		
		logger.info("��Ϣ�����:" + f.getAbsolutePath());					
		return ""+(yhdjkList.size());
	}
	
}
