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
import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Message;

public class PrintCollectReport {
	private static final Log logger = LogFactory.getLog(PrintCollectReport.class);
	private static Configuration conf = Configuration.getInstance();
	
	private String title="",tipsDate="",fileName,rowInfo="",payOpBkName="",bankOrgCode="";
	String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	//String tmpPath = "D:\\projects\\TIPS\\fromSVN\\CommonTips\\classes\\temp";
	private String sql="";
	int startIndex=0,endIndex=0;
	String[] titleList={"��������","��������","��������","�ܱ���","�ܽ��","�������֧���к�","��������","��������","�ܱ���","�ܽ��"};
	String[] deatilListFormat={"10","10","8","20","20","20","10","20","20","8"};
	String[] deatilList={"chkDate","chkAcctOrd","chkAcctType","allNum","allAmt","payeeBankNo","chkDate","chkAcctType","sumAllNum","sumAllAmt"};
	public String makeReport(Message temp) throws Exception{
		
		tipsDate=temp.getString("TipsDate");		//TIPS����
		fileName=temp.getString("ReturnFileName");	//�ļ���	
		bankOrgCode=temp.getString("BankOrgCode");	//������
		payOpBkName=temp.getString("PayOpBkName");	//��������
				
		sql="select * from paycheck where chkDate='"+tipsDate+"' order by chkAcctOrd ";
		logger.info("��ѯ������ϸsql��"+sql);
		List queryList=QueryUtil.queryRowSet(sql);
		if (queryList.size()>0) {
			//��ӡ�����嵥
			String printType="1";
			File f = new File(tmpPath, fileName);
	        Writer writer = new FileWriter(f);
	        writer.write(makeTitle(printType));
	        for(int i=0;i<queryList.size();i++){
	        	writer.write(makeRowFormat(printType,(Map)queryList.get(i)));
	        	writer.flush();
	        }
	        writer.write("\f");	        
	        
	        //��ӡ������ϸ�嵥
	        printType="2";
	        writer.write(makeTitle(printType));
	        sql="select payeeBankNo,chkDate,chkAcctType,sum(allNum) as sumAllNum,sum(allAmt) as sumAllAmt from payCheck where chkDate='"+tipsDate+"' group by payeeBankNo,chkDate,chkAcctType ";
	        logger.info(sql);
	        List queryCollectList=QueryUtil.queryRowSet(sql);
	        for(int i=0;i<queryCollectList.size();i++){
	        	writer.write(makeRowFormat(printType,(Map)queryCollectList.get(i)));
	        	writer.flush();
	        }
	        
	        writer.close();
	        
	        logger.info("��Ϣ�����:" + f.getAbsolutePath());		
		}			
		return ""+queryList.size();
	}
	
	
	public String makeRowFormat(String type,Map temp){
		rowInfo="";
		if (type.equals("1")) {	
			startIndex=0;
			endIndex=5;
		}else {			
			startIndex=5;
			endIndex=deatilListFormat.length;
		}
		for (int i=startIndex;i<endIndex;i++){
			if (deatilList[i].equals("chkAcctType")) {
				String chkAcctType="";
				if (((String)temp.get(deatilList[i])).equals("0")) {
					chkAcctType="�ռ�";
				}else{
					chkAcctType="����";
				}
				rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], chkAcctType, " ");;
			}else {
				rowInfo+=ACEPackUtil.leftStrFormat(deatilListFormat[i], (String)temp.get(deatilList[i]), " ");
			}
			rowInfo+="     ";
		}
		rowInfo+="\n";
		return rowInfo;
	}
	
	//��ӡ�ı���
	public String makeTitle(String type){
		title="\n";		
		if (type.equals("1")) {
			title+=ACEPackUtil.leftStrFormat("20", "", " ")+"��Ͽ����TIPS�����嵥";
			startIndex=0;
			endIndex=5;
		}else {
			title+=ACEPackUtil.leftStrFormat("20", "", " ")+"��Ͽ����TIPS���˻����嵥";
			startIndex=5;
			endIndex=deatilListFormat.length;
		}
		title+="\n\n";
		title+="�����ţ�"+bankOrgCode+",�������ƣ�"+payOpBkName+"      ��ӡ���ڣ�"+DateTimeUtil.getDateTimeString()+"\n";						
		title+="\n";		
		for (int i=startIndex;i<endIndex;i++) {			
			title+=ACEPackUtil.leftStrFormat(deatilListFormat[i], titleList[i], " ");
			title+="     ";			
		}
		title+="\n";
		return title;
	}
	
}
