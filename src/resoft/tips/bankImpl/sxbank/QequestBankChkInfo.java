package resoft.tips.bankImpl.sxbank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.action.TransferAcctSX;
import resoft.tips.bankImpl.qhdyh.SzsmCheckPackager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>请求对账文件</p>
 * Author: zhuchangwu
 * Date: 2007-8-31
 * Time: 9:37:04
 */
public class QequestBankChkInfo implements Action {
	private String sendMsg="fail";
	private String rcvMsg = "fail";
	private Socket sender=null;
	String result="",addWord="";
	private String MsgRecordData="fail";
	private static final Log logger = LogFactory.getLog(TransferAcctSX.class);
	Configuration conf = Configuration.getInstance();
	private String BANKPAYSYSHOST=conf.getProperty("BankSysConfig", "BankPaySysHost");
	private int BANKPAYSYSPORT=Integer.parseInt(conf.getProperty("BankSysConfig", "BankPaySysPort"));
	private String rcvChargeMsg;
	byte[] recPack;
	private String sendori;
    public int execute(Message msg) throws Exception {
    	//List changeDateRow=QueryUtil.queryRowSet("select workDate from ChangeDate where id=1");
    	//Map changeDateMap=(Map)changeDateRow.get(0);
    	 logger.info("      in here               ");
    	String changeDate=msg.getString("ChkDate"); 
    	 
    	String workdate=msg.getString("//CFX/HEAD/WorkDate");
    	 
    	//yangyuanxu add
    	String payeeBankNo = msg.getString("PayeeBankNo"); 
    	sendori="TITIPS"+changeDate+".txt";	 
    	SzsmCheckPackager pack=new SzsmCheckPackager();
        pack.CreateSystemHeadData(workdate); //需要改成正确的
        pack.CreateBodyData(changeDate,changeDate,changeDate);
        String sendMsg = pack.MakeUpCheckPackager();//需要实现 
        byte[]sendbt=sendMsg.getBytes();
        sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
		sender.setSoTimeout(6*1000);		
		try
		{
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
	    	
			out.write(sendbt); 
			logger.info("real  send to shenmacheck   "+sendMsg);
			out.flush();
			byte[] packageLengthbyte=new byte[10];
			in.read(packageLengthbyte,0,10);
			String lengthStr=new String(packageLengthbyte);
			int packetlength=Integer.parseInt(lengthStr);
			recPack=new byte[packetlength];	
			in.read(recPack,0,packetlength); 
			 
		}
		catch(Exception e)
		{
			logger.info("yichang               ");
			result="94999";
	         addWord="与核心通信异常";
	         return FAIL;
		}
		rcvChargeMsg = new String(recPack);
		
		pack.revXml=rcvChargeMsg.trim();
		logger.info("rece sm checkdata"+rcvChargeMsg);
    	
    	
		MsgRecordData=pack.GetParm("sys_header", "RET_CODE");
		if(MsgRecordData.equals("000000"))
		{
			 result="90000";
	         addWord="交易成功";	
	            String path=conf.getProperty("SXBankFtp", "3111TempFilePath")+"/"+sendori; 
	            logger.info("path         "+path);
	            msg.set("BankChkFileName",path);
		}
		else
		{
			result="24004";
			addWord=pack.GetParm("sys_header", "RET_MSG");
		}
    	
    	
    	
    	
    	
    	
    /*	//String payeeAcct = getPayAcct(payeeBankNo);
    	TuxedoPackager packager = new TuxedoPackager();
    	sendMsg = packager.getQequestCheckInfoStr(payeeBankNo,changeDate,3);//获取发送的字段
    	String sendori=getYwbsh(payeeBankNo)+changeDate+".txt";				//文件名称
    	logger.info("文件名称为"+sendori);
    	ChangeStringToBytes strTobytes=new ChangeStringToBytes();
    	String sendType="C";
    	byte[]sendbt=strTobytes.GetBytes(sendMsg,sendType,sendori);		//获取发送的数组
    	
    	//区分对账保本
    	
    	//Message newMsg = new DefaultMessage();
        //newMsg.set("柜员号", "666666");
        //newMsg.set("网点号", "0004");
        //newMsg.set("业务交易日",changeDate );//msg.getString("ChkDate")changeDate"2007-06-19"
        //newMsg.set("交易流水号", "00000000");
        //newMsg.set("交易码", "030619");
        //yangyuanxu add 归并账户
        //newMsg.set("LI_MACCT",payeeAcct);
        //newMsg.set("LI_DFRQ", msg.getString("ChkDate"));//msg.getString("ChkDate")20070101
        //newMsg.set("LI_LTCD", "A15");
        //newMsg.set("LI_DFID", "");
        //MessageSender sender = MessageSenderUtil.getMessageSender();
    	try
    	{	
			sender=new Socket(BANKPAYSYSHOST,BANKPAYSYSPORT);
			sender.setSoTimeout(6*1000);			
			DataOutputStream out=new DataOutputStream(sender.getOutputStream());
			DataInputStream in=new DataInputStream(sender.getInputStream());
			//发送方标识
			out.write(sendbt);
			
			logger.info("发送对账TUXEDO请求:["+sendMsg+"]");
			out.flush();
			byte[] recPack=new byte[100];			
			in.read(recPack,0,100);
			rcvMsg = new String(recPack);
			logger.info("接收对账TUXEDO应答：["+rcvMsg+"]");
			if(!rcvMsg.equals("NULL"))
			{
				byte[]msgdatabyte=new byte[7];
				for(int i=0;i<msgdatabyte.length;i++)
				{
					msgdatabyte[i]=recPack[i+57];
	    		
				}
				MsgRecordData=new String(msgdatabyte);
			}
    	}
    	catch(Exception e)
    	{					
    			e.printStackTrace();			
    			rcvMsg = "NULL";
    	}
		finally
		{
			if (sender!=null)
			{
				try
				{
					sender.close();
				}
				catch(Exception e){
					sender=null;
				}
			}
		}
		if(rcvMsg.equals("NULL")){//与核心通讯错误
    		result="94999";		
    		addWord="与主机通讯异常";
	    }
		else 
	    {	
			logger.info("结果码 是："+MsgRecordData);
			if(MsgRecordData.equals("AAAAAAA")) {	//交易成功	            
	            result="90000";
	            addWord="交易成功";	
	            
	            String path=conf.getProperty("SXBankFtp", "3111TempFilePath")+"/"+sendori;
	            msg.set("BankChkFileName",path);
	        }
			else{					//交易失败			        	
	        	if(MsgRecordData.equals("1840") || MsgRecordData.equals("2286") )//账户冻结
	        		result="24004";
	        	if(MsgRecordData.equals("1627"))//余额不足
	        	    result="24007";
	        	if(MsgRecordData.equals("1624"))//
	        	    result="24005";
	        	if(MsgRecordData.equals("2297") || MsgRecordData.equals("1096") )//
	        	    result="24008";
	        	try{
	        		addWord = DBUtil.queryForString("select addword from transresult where flag='" + MsgRecordData+"' ");
	        		if(addWord.length()>=15){
	        			addWord=addWord.substring(0,14);
	        		}
	        	}catch (Exception e){
	        		e.printStackTrace();
	        		addWord = "其它错误！";
	        	}
	        }
	    }*/
    	
        /*try {
            Message returnMsg = sender.send(newMsg);
            if("000".equals(returnMsg.getString("RespCode"))) {
            	//DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            	long dateLong=System.currentTimeMillis();//df.format(new Date());
            	String fileName=String.valueOf(dateLong)+".txt";
            	String path="";//conf.getProperty("general", "tmpPath")+"\\";
                ClassLoader  loader  =  getClass().getClassLoader();
                URL urlpath=loader.getResource(""); 
                String filePath=urlpath.getFile();
                String downPath=filePath+"filedown/";//filePath.substring(0, filePath.length()-4)+"filedown/"
                System.out.println("the downPath is:..................."+downPath);
                path=downPath+fileName;//文件路径//downPath.substring(1,downPath.length())+fileName
            	FileWriter fw=new FileWriter(path); 
            	BufferedWriter bw=new BufferedWriter(fw);
            	List list = (List) returnMsg.get("CheckInfoList");
            	for(int i=0;i<list.size();i++){
            		Message check= (Message) list.get(i);
            		String overFlag = check.getString("最后一条标志");
            		if("0".equals(overFlag)){
            			String TraDate=check.getString("LO_TXDATE");
            			String bankTraDate=TraDate.substring(0,4)+TraDate.substring(5,7)+TraDate.substring(8,10);
            			String TraTime=" ";
            			String TransNo=check.getString("LO_PTXSEQ");            			
            			String TaxOrgCode=check.getString("LO_ORGNO");
            			String TaxPayCode=" ";
            			String DebitAcct=check.getString("LO_ACCT1");
            			String TraAmt=check.getString("LO_TXAMT");
            			String lineStr=bankTraDate+"|"+TraTime+"|"+TransNo+"|"+TaxOrgCode+"|"+TaxPayCode+"|"+DebitAcct+"|"+TraAmt;
            			bw.write(lineStr);
            			bw.newLine();//断行 
            		}
            	}
            	bw.flush();//将数据更新至文件 
            	fw.close();//关闭文件流
                msg.set("BankChkFileName",path);
                return SUCCESS;
            } else {
                logger.error("返回失败。RespCode:" + returnMsg.get("RespCode") + ";RespDesc:" + returnMsg.get("RespDesc"));
                return FAIL;
            }
        } catch(MessageSendException e) {
            logger.error("请求对账文件失败",e);
            return FAIL;
        }
        */
		return SUCCESS;
    }
  //yangyuanxu add
    private String getPayAcct(String payeeBankNO) throws SQLException {
        String payAcct="";
        payAcct=DBUtil.queryForString("select payeeacct from payee_relation where payeeBankNo='"+payeeBankNO+"'");	
    	return payAcct;
    }
    
    
    public String getYwbsh(String payeeBankNo) throws SQLException{
		String ywbsh = "";
		ywbsh=DBUtil.queryForString("select ywbsh from bank_relation where PayeeBankNo='"+payeeBankNo+"'");	
		ywbsh=ywbsh.trim();
		return ywbsh;
	}
}


