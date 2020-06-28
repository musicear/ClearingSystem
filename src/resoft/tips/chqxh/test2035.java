package resoft.tips.chqxh;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>���˲���</p>
 * User: liwei
 * Date: 2007-09-10
 * Time: 09:06:06
 */
public class test2035 implements Action {
	private static Configuration conf = Configuration.getInstance();			
	private static final Log logger = LogFactory.getLog(test2035.class);
	String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");	
    public int execute(Message msg) throws Exception {    	
    	String testFlag=DBUtil.queryForString("select flag from test2033 where status='2035' ");
    	if (testFlag.equals("Y")){
    		DBUtil.executeUpdate("update test2033 set flag='N' where status='2035' ");
	    	String tipsDate=DateTimeUtil.getDateString();
	    	String tipsOrd=radomPiChi().substring(0,4);    
	    	String fileName=("TIPSDZ"+tipsOrd);   	
	    	List rowSet = QueryUtil.queryRowSet("select * from test2033 where status='1' and MarkId='AAAAAAA' order by TraNo "); 
	    	if (rowSet.size()>0){
	    		ACE2035 ace2035=new ACE2035();
	    		//��ʼ�����˻�����Ϣ
	    		ace2035.packTags.put("FileName", fileName);
	    		ace2035.packTags.put("TipsDate",tipsDate);
	    		ace2035.packTags.put("TipsOrd",tipsOrd);
	    		ace2035.packTags.put("SumCount",""+rowSet.size());
	    		String sumAmt=DBUtil.queryForString("select sum(cast(traamt as decimal(15,2))) from test2033 where status='1' and MarkId='AAAAAAA'");
	    		ace2035.packTags.put("SumAmt", sumAmt);
	    		
	    		File f = new File(tmpPath, fileName);    		
	    		Writer writer = new FileWriter(f);
		    	for(int i=0;i<rowSet.size();i++){
		    		//��ʼ��������ϸ��Ϣ
		    		ACE2035Deatil ace2035Deatil=new ACE2035Deatil();
		    		Map row=(Map)rowSet.get(i);
		    		ace2035Deatil.packTags.put("TipsDate", tipsDate);
		    		ace2035Deatil.packTags.put("TipsOrd", tipsOrd);
		    		ace2035Deatil.packTags.put("TaxUserNo", ACEPackUtil.leftStr("32",(String)row.get("TaxPayCode")));
		    		ace2035Deatil.packTags.put("PayAcct", ACEPackUtil.leftStr("19", (String)row.get("payacct")));
		    		ace2035Deatil.packTags.put("TaxOrgCode", ACEPackUtil.leftStr("12", (String)row.get("taxOrgCode")));
		    		ace2035Deatil.packTags.put("TrsNo", ACEPackUtil.leftStr("10",(String)row.get("traNo")));
		    		ace2035Deatil.packTags.put("TrsAmt", ((String)row.get("traAmt")).replaceAll("\\.", ""));	    		
		    		
		    		//��������һ�ʲ��� "�س�"
		    		if(rowSet.size()!=(i+1)){
		    			writer.write(ace2035Deatil.initTransBody()+"\r\n");	
		    		}else {
		    			writer.write(ace2035Deatil.initTransBody());
		    		}
		    		
		    		writer.flush();
		    		//�޸�״̬
		    		//DBUtil.executeUpdate("update test2033 set status='2' where traNo='"+(String)row.get("traNo")+"'");
		    	}	    
	            writer.close();
	            DBUtil.executeUpdate("update test2033 set status='2' where status='1' and MarkId='AAAAAAA'");
	            
	            ace2035.initPack();
	            logger.info("������Ϣ��"+ace2035.packTags);
	            //��װ����������Ϣ
		    	msg.set("ACESendObj", ace2035);
		    	SendMsgToBankSys sender=new SendMsgToBankSys(msg);
		    	sender.sendMsg();
		    		    	
	            logger.info("�����ļ������:" + f.getAbsolutePath());
	    	}
    	}
        return SUCCESS;
    }
    
    public static String radomPiChi(){    	    	
    	String tra="";    	
    	NumberFormat nf = new DecimalFormat("0000");
    	tra+= nf.format(Math.random() * 1000);
        return tra;
    }    
    public static void main(String[] args){
    	for(int i=0;i<100;i++){
    		System.out.println(test2035.radomPiChi());
    	}
    }
    
}
