package resoft.tips.chqsh;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.MoneyUtil;
import resoft.tips.util.FTPUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * ��ѯ�����ʽ𣬲���ӡ
 * @author fanchengwei
 *
 */
public class FileTraAmt implements Action {

	private static final Log logger = LogFactory.getLog(FileTraAmt.class);
	private static Configuration conf = Configuration.getInstance();
	private String ReturnFileName = "";
	private String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
	private String UserId = "";
	private String PayeeBankNo = "";
	private String BranchNo = "";
	private String cardInfo = "";
	private String allAmt = "";
	private String allNum = "";
	private String WorkDate = "";
	private String ChkDate = "";
	private String PreTipsWorkDate = "";
	private String CheckOrder = "";
	private Calendar date = Calendar.getInstance();
	private String sql = "";
	private String sql_bank = "";
	private String PayOpBkCode = "";
	private String rcvMsg = "";
	private int sumAccont = 0;
	private String sumAmt = "";
	//yangyuanxu add
	private String bankpayacct = "";
	private String paybankcode = "";
	private String paybankname = "";
	private String paybanknameDW = "";
	private String a_bankpayacct = "";
	private String a_paybankcode = "";
	private String a_paybankname = "";
	private String a_paybanknameDW = "";
	List queryList = null;
	Map map = null;
	SendMsgToBankSystem send = new SendMsgToBankSystem();
	public FileTraAmt()
	{
	}
	
    public int execute(Message msg) throws SQLException 
    {
    	WorkDate = msg.getString("WorkDate").trim();
    	CheckOrder = msg.getString("CheckOrder").trim();
    	UserId = msg.getString("UserId").trim();
    	PayeeBankNo = msg.getString("PayeeBankNo").trim();
    	BranchNo = msg.getString("BranchNo").trim();
    	int count = 0;
    	if(BranchNo == null || UserId == null || PayeeBankNo == null )
    	{
    		logger.info("�Ƿ�������,�ֶβ�ȫ!");
			msg.set("AddWord", "�Ƿ�������,�ֶβ�ȫ��");
			msg.set("ReturnResult", "N");
			return FAIL;
    	}
    	/**
    	 * ֻ���������Ĳ���Ȩ����ӡȫ�е����㻮�
    	 * ���㲻�ܴ�ӡ����֧�еĶ��˽����
    	 */
    /*	if(!BranchNo.equals("9998") || !BranchNo.equals("8501")) {
    		
    		msg.set("AddWord", "�����߱�����Ȩ����");
			msg.set("ReturnResult", "N");
			return FAIL;    		
    	}
    */
    	//yangyuanxu add
    	String statu = this.getPayeeBankNo(BranchNo);
    	if(statu == null || (!PayeeBankNo.equals(statu) && !BranchNo.equals("0001"))){
    		msg.set("AddWord", "�����߱�����Ȩ����");
			msg.set("ReturnResult", "N");
			return FAIL;    		
    	}
    	
    	ReturnFileName = "HKDZ" + System.currentTimeMillis()+ BranchNo;    	
    	if(WorkDate.equals("")){    		
    		try{
	    		WorkDate = DBUtil.queryForString("select workdate from sysstatus");
	    		PreTipsWorkDate = DBUtil.queryForString("select preworkdate from sysstatus");
    		}catch(Exception e){
    			logger.info("��ѯ���ݿ����");
    			msg.set("AddWord", "��ѯ���ݿ����");
    			msg.set("ReturnResult", "N");
    			return FAIL;
    		}
	    	try{
	    		count = DBUtil.queryForInt("select count(*) from paycheck where chkdate='"+ WorkDate +"' and PayeeBankNo='"+PayeeBankNo+"'");
	    		logger.info("count = " + count);
	    	}catch(Exception e){
	    		msg.set("AddWord", "��ѯ���ݿ����");
				msg.set("ReturnResult", "N");
				logger.info("��ѯ���ݿ����");
				return FAIL;
	    	}
	    	if(count > 0){
	    		ChkDate = WorkDate;
	    		try{
        			sql = "select distinct chkacctord from paycheck where chkdate='"+ ChkDate +"' and PayeeBankNo='"+PayeeBankNo+"' order by chkacctord";
    	    		queryList =  QueryUtil.queryRowSet(sql);
    	    		map = (Map)queryList.get(queryList.size()-1);
    	    		CheckOrder = (String)map.get("chkacctord");
    			}catch(Exception e){
    				msg.set("AddWord", "��ѯ�������γ�������");
    				msg.set("ReturnResult", "N");
    				logger.info("��ѯ�������γ���");
    				return FAIL;
    			}
	    	}else{
	    		ChkDate = PreTipsWorkDate;
	    		try{
        			sql = "select distinct chkacctord from paycheck where chkdate='"+ ChkDate +"' and PayeeBankNo='"+PayeeBankNo+"' order by chkacctord";
    	    		queryList =  QueryUtil.queryRowSet(sql);
    	    		map = (Map)queryList.get(queryList.size()-1);
    	    		CheckOrder = (String)map.get("chkacctord");
    			}catch(Exception e){
    				msg.set("AddWord", "��ѯ�������γ�������");
    				msg.set("ReturnResult", "N");
    				logger.info("��ѯ�������γ���");
    				return FAIL;
    			}
	    	}
    	}else{
    		ChkDate = WorkDate.substring(0, 8);
    		if(CheckOrder.equals("")){
	    		try{
	    			count = DBUtil.queryForInt("select count(*) from paycheck where chkdate='"+ ChkDate +"' and PayeeBankNo='"+PayeeBankNo+"'");
	    		}catch(Exception e){
	    			msg.set("AddWord", "��ѯ���ݿ����");
					msg.set("ReturnResult", "N");
					logger.info("��ѯ���ݿ����");
					return FAIL;
	    		}
	    		if(count == 0){
	    			msg.set("AddWord", "��ǰ��������û�ж��ˣ�");
					msg.set("ReturnResult", "N");
					return FAIL;
	    		}else{
	    			try{
	        			sql = "select distinct chkacctord from paycheck where chkdate='"+ ChkDate +"' and PayeeBankNo='"+PayeeBankNo+"'";
	    	    		queryList =  QueryUtil.queryRowSet(sql);
	    	    		map = (Map)queryList.get(queryList.size()-1);
	    	    		CheckOrder = (String)map.get("chkacctord");
	    			}catch(Exception e){
	    				e.printStackTrace();
	    				msg.set("AddWord", "��ѯ�������γ�������");
	    				logger.info("��ѯ�������γ�������");
	    				msg.set("ReturnResult", "N");
	    				return FAIL;
	    			}
	    		}
    		}
    	}
    	/**
    	 * ����Ĳ����Ѿ�ȡ����Tips�������ںͶ�������
    	 */
		//��ѯ������Ϣ
    	sql = "select * from paycheck where chkdate='"+ ChkDate +"' and chkacctord='"+ CheckOrder +"' and PayeeBankNo='"+PayeeBankNo+"'";
    	//yangyuanxu add
    	sql_bank = "select * from bank_relation where PayeeBankNo='"+PayeeBankNo+"'";
    	List queryList = null;
    	//yangyuanxu add
    	List list_bank = null;
		Map row = null;
		//yangyuanxu add
		Map row_bank = null;
		logger.info("sql = " + sql);
		try{
			queryList = QueryUtil.queryRowSet(sql);
			list_bank = QueryUtil.queryRowSet(sql_bank);
			logger.info("�տ��в�ѯ���Ϊ��"+list_bank);
		}catch(Exception e){
			logger.info("��ѯ���ݿ����");
			msg.set("AddWord", "��ѯ���ݿ����");
			msg.set("ReturnResult", "N");
			return FAIL;
		}
		//yangyuanxu add
		if(list_bank.size() == 0 || list_bank == null){
			msg.set("AddWord", "��ǰ�տ����к��������");
			msg.set("ReturnResult", "N");
			return FAIL;
		}
		if(queryList.size() == 0 || queryList == null){
			msg.set("AddWord", "��ǰTips����������δ���ж��˻������ڷǷ����������������ڣ�");
			msg.set("ReturnResult", "N");
			return FAIL;
		}else{
			row = (Map)queryList.get(0);
			//yangyuanxu add
			row_bank = (Map)list_bank.get(0);
			allAmt = (String)row.get("allAmt");
			allNum = (String)row.get("allNum");
			//yangyuanxu add
			bankpayacct = (String)row_bank.get("bankpayacct"); //���λ�ʺ�
			paybankcode = (String)row_bank.get("paybankcode"); //�������к�
			paybankname = (String)row_bank.get("paybankname"); //�������������
			paybanknameDW= (String)row_bank.get("paybanknameDW"); //���λ����
			a_bankpayacct = (String)row_bank.get("a_bankpayacct");//�տλ�ʺ�
			a_paybankcode = (String)row_bank.get("a_paybankcode");//�տ����к�
			a_paybankname = (String)row_bank.get("a_paybankname");//�տ����������
			a_paybanknameDW = (String)row_bank.get("a_paybanknameDW");//�տλ����
			
			String printTimes = "";
			if(row.get("PRINTTIMES").toString().trim().equals("") || row.get("PRINTTIMES").toString().trim() == null){
				printTimes = "1";
			}else{
				printTimes = ((String)row.get("PRINTTIMES")).trim();
			}
			try{
				DBUtil.executeUpdate("update paycheck set PRINTTIMES='"+ (Integer.parseInt(printTimes)+1) +"' where chkdate='"+ ChkDate +"' and chkacctord='"+ CheckOrder +"' and PayeeBankNo='"+PayeeBankNo+"'");
			}catch(Exception e){
				msg.set("AddWord", "�������ݿ��д�ӡ����������");
				msg.set("ReturnResult", "N");
			}
//			
			if(allAmt==null||"".equals(allAmt))
				allAmt="0.00"; 
			
			File f = new File(tmpPath, ReturnFileName);
			Writer writer = null;
			try{
				writer = new FileWriter(f);
				writer.write(InitTraAmtCard(BranchNo,allNum,ChkDate,allAmt,UserId,CheckOrder,ChkDate+printTimes,bankpayacct,paybankcode,paybankname,paybanknameDW,a_bankpayacct,a_paybankcode,a_paybankname,a_paybanknameDW));			
				writer.flush();
				writer.close();
				this.ftpUpload(ReturnFileName);
				msg.set("AddWord", "�ɹ�");
				msg.set("ReturnResult", "Y");
				msg.set("ReturnFileName", ReturnFileName);
			}catch(Exception e){
				logger.info("д���ļ���������");
				msg.set("AddWord", "д���ļ�����");
				msg.set("ReturnResult", "N");
				e.printStackTrace();
				return FAIL;
			}
		}
    	return SUCCESS;
    }
	
	public String InitTraAmtCard(String BranchNo,String allNum,String iadac_dat,String allAmt,String UserId,String CheckOrder,String TipsWorkDate,String BankPayAcct,String PayBankCode,String PayBankName,String PayBankNameDW,String a_BankPayAcct,String a_PayBankCode,String a_PayBankName,String a_PayBankNameDW){
		String forMat = "��������������������";
		String upTraAmt = "";
		String buf = "";
        int count_DW=0;
        int count_N=0;
        String PayBankNameDW_buf="";
		String PayBankName_buf="";
        count_DW=PayBankNameDW.getBytes().length;
        count_N=PayBankName.getBytes().length;
        for(int i=count_DW;i<33;i++){
        	PayBankNameDW_buf=PayBankNameDW_buf+" ";
        }
        for(int i=count_N;i<33;i++){
        	PayBankName_buf=PayBankName_buf+" ";
        }
        //String cq_PayBankNameDW="�� ";
		//String cq_PayBankName="                         ";
		//String fh_PayBankName="                 ";
		//String fh_PayBankNameDW="��                ";
		
		
		//if(BranchNo.equals("9998")){
			//PayBankNameDW_buf=cq_PayBankNameDW;
			//PayBankName_buf=cq_PayBankName;
		//}else{
		//	PayBankNameDW_buf=fh_PayBankNameDW;
		//	PayBankName_buf=fh_PayBankName;
		//}
		int count = 0;
		String WorkDate = TipsWorkDate.substring(0, 4) + "��" + TipsWorkDate.substring(4, 6) + "��" + TipsWorkDate.substring(6, 8) + "��";

		if(CheckOrder.equals("1001")){
			CheckOrder += "���ռ䣩";
		}else{
			CheckOrder += "�����У�";
		}
		upTraAmt = MoneyUtil.amountToChinese(Double.parseDouble(allAmt));
		buf = "";
		count = 0;
		allAmt=MakeUpTwoDot(allAmt);
		for(int i=allAmt.length();i>0;i--){
			buf = allAmt.substring(i-1, i);
			count++;
			if(buf.equals(".")){
				count = 0;
			}
			if(count == 3 && i != 1){
				count = 0;
				allAmt = allAmt.substring(0, i-1) + "," + allAmt.substring(i-1);
			}
		}
		cardInfo = "\n";
		cardInfo += "                                       �ʽ����㻮��ƾ֤" + "\n\n";
		cardInfo += forMat.substring(0, 3) + "��Tips�������ڣ�" + WorkDate+ "                   ���Σ�" + CheckOrder;
		cardInfo += forMat.substring(0, 3) + "\n";
		cardInfo += forMat.substring(0, 3) + "�����λȫ�ƣ�"+PayBankNameDW+PayBankNameDW_buf+"�տλȫ�ƣ�" +a_PayBankNameDW +"\n";
		cardInfo += forMat.substring(0, 3) + "�����λ�ʺţ�"+BankPayAcct+"                  �տλ�ʺţ�"+a_BankPayAcct+"\n";
		cardInfo += forMat.substring(0, 3) + "����������У�"+PayBankName+PayBankName_buf+"�տ�����У�"+a_PayBankName+ "\n";
		cardInfo += forMat.substring(0, 3) + "���������кţ�  "+PayBankCode+"                     �տ����кţ�"+a_PayBankCode+ "\n";	
		cardInfo += forMat.substring(0, 3) + "���������ң�Сд������" + allAmt + "\n";
		cardInfo += forMat.substring(0, 3) + "���������ң���д����" + upTraAmt + "\n";
		cardInfo += forMat.substring(0, 3) + "������ԭ��˰�����뻮�ɹ���" + "\n";
		cardInfo += forMat.substring(0, 3) + "��ժҪ��"+PayBankCode+"��" + allNum + "��" + iadac_dat + "��" + CheckOrder.substring(0, 4) + "\n";
		cardInfo += forMat.substring(0, 3) + "����Ŀ���裩��28000" + "\n";
		cardInfo += forMat.substring(0, 3) + "����Ŀ��������110" + "\n";
		cardInfo += forMat.substring(0, 3) + "����ƣ������������������������ˣ������������������ˣ�" + "\n";
		cardInfo += forMat.substring(0, 3) + "      ��ӡ���ڣ�" + date.get(Calendar.YEAR) + "��" + (Integer.parseInt("" + date.get(Calendar.MONTH))+ 1) + "��" + date.get(Calendar.DAY_OF_MONTH) + "��         ��" + TipsWorkDate.substring(8).trim() + "�δ�ӡ           ��Ա�ţ� " + UserId;
		cardInfo += "          " + "\n";
		return cardInfo;	
	}

	public String InitTraAmtCard(String sumAccont,String sumAmt,String ChkDate,String ChkAcctOrd,String UserId){
		String forMat = "��������������������";
		String upTraAmt = "";
		String buf = "";
		int count = 0;
		String WorkDate = ChkDate.substring(0, 4) + "��" + ChkDate.substring(4, 6) + "��" + ChkDate.substring(6) + "��";

		if(ChkAcctOrd.equals("1001")){
			ChkAcctOrd += "���ռ䣩";
		}else{
			ChkAcctOrd += "�����У�";
		}
		upTraAmt = MoneyUtil.amountToChinese(Double.parseDouble(sumAmt));
		buf = "";
		count = 0;
		
		sumAmt=MakeUpTwoDot(sumAmt);
		for(int i=sumAmt.length();i>0;i--){
			buf = sumAmt.substring(i-1, i);
			count++;
			if(buf.equals(".")){
				count = 0;
			}
			if(count == 3 && i != 1){
				count = 0;
				sumAmt = sumAmt.substring(0, i-1) + "," + sumAmt.substring(i-1);
			}
		}
		cardInfo = "\n\n";
		cardInfo += "                                       �ʽ����㻮�" + "\n\n\n";
		cardInfo += forMat.substring(0, 5) + "      Tips�������ڣ�" + WorkDate+ "                               ���Σ�" + ChkAcctOrd;
		cardInfo += forMat.substring(0, 5) + "\n\n";
		cardInfo += forMat.substring(0, 5) + "���ܱ�����" + sumAccont + "\n";
		cardInfo += forMat.substring(0, 5) + "���������ң�Сд������" + sumAmt + "\n";
		cardInfo += forMat.substring(0, 5) + "���������ң���д����" + upTraAmt + "\n";
		cardInfo += "\n";
		cardInfo += forMat.substring(0, 5) + "      ��ӡ���ڣ�" + date.get(Calendar.YEAR) + "��" + date.get(Calendar.MONTH) + "��" + date.get(Calendar.DAY_OF_MONTH) + "��";
		cardInfo += "                                    ��Ա�ţ� " + UserId;
		cardInfo += "          " + "\n";
		return cardInfo;	
	}
	
	private String MakeUpTwoDot(String sumAmt2) {
		// TODO Auto-generated method stub
		String sumAmt3="";
		int count=0;
		String buff = "";
		for(int i=sumAmt2.length();i>0;i--){
			buff = sumAmt2.substring(i-1, i);
			count++;
			if(buff.equals("."))
			{
				if(count==2)
				{
					sumAmt3=sumAmt2+"0";
					return sumAmt3;
				}
				if(count==3)
					return sumAmt2;
			}
		}
		
		return sumAmt2+".00";
	}

	//yangyuanxu add 
	public String getPayeeBankNo(String BranchNo) throws SQLException{
		String PayeeBankNo = "";
		PayeeBankNo=DBUtil.queryForString("select PayeeBankNo from bank_relation where bankorgcode='"+BranchNo+"'");	
		PayeeBankNo=PayeeBankNo.trim();
		return PayeeBankNo;
	}	
	
	public void ftpUpload(String filename) throws Exception{
		FTPUtil ftp = new FTPUtil();
		ftp.setServer(conf.getProperty("SXBankFtp", "FtpServer"));
		ftp.setPort(Integer.parseInt(conf.getProperty("SXBankFtp", "FtpPort")));
		ftp.setPath(conf.getProperty("SXBankFtp", "FtpPath"));
		ftp.setUser(conf.getProperty("SXBankFtp", "FtpUser"));
		ftp.setPassword(conf.getProperty("SXBankFtp", "FtpPassword"));
		ftp.setLocalpath(conf.getProperty("SXBankFtp", "TempFilePath"));
		ftp.upload(filename);
	}
	
	public static void main(String args[]) throws Exception
	{
		Message msg = null;
		FileTraAmt ft = new FileTraAmt();
		ft.execute(msg);
	}
}