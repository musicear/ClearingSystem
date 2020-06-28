package resoft.tips.chqsh;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;
import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
/**
 * <p>一个TIPS工作日内其他程序的处理</p>
 * Author: liwei
 * Date: 2007-11-27
 * Time: 09:36:06
 */
public class ProcessTipsWork implements Action{	
	
	private static final Log logger = LogFactory.getLog(ProcessTipsWork.class);
	
	private static Configuration conf = Configuration.getInstance();
	
	public int execute(Message msg) throws Exception {
        //对账信息
		String tipsDate = msg.getString("ChkDate");        		//对账日期
		String chkAcctOrd=msg.getString("ChkAcctOrd");			//对账批次
        String chkAcctType=msg.getString("ChkAcctType");		//对账类型
        //String AllNum=msg.getString("//CFX/MSG/BatchHead3111/AllNum");
        String AllNum=msg.getString("AllNum");   
        logger.info("AllNum is: "+ AllNum);
        //修改对于扣税信息的总账行行号        
        try{
        	//处理实时
        	String updateSql="UPDATE realtimepayment a SET a.branchno=" +
        			"(	SELECT p.branchno FROM proveinfo p " +
        			"	WHERE  p.enabledflag='Y' AND a.protocolno=p.protocolno" +
        			") ";
        	logger.info("修改实时扣税信息的总账行行号 SQL is：\n"+updateSql+"\n");
        	DBUtil.executeUpdate(updateSql);
        	//处理批量
        	updateSql="UPDATE batchpackdetail a SET a.branchno=" +
			"(	SELECT p.branchno FROM proveinfo p " +
			"	WHERE  p.enabledflag='Y' AND a.protocolno=p.protocolno" +
			") ";
        	logger.info("修改批量扣税信息的总账行行号 SQL is：\n"+updateSql+"\n");
        	DBUtil.executeUpdate(updateSql);
        }catch(Exception ex){
        	logger.info("修改扣税信息总账行行号错误");
        	ex.printStackTrace();
        }
        
        if(chkAcctType.equals("1") && AllNum.equals(getAllNum(tipsDate,chkAcctOrd))){		//0:日间;1:日切
        	
        	//将没有处理完的批量包停止处理
        	DBUtil.executeUpdate("update batchPackage set procFlag='6' where (procFlag!='5' and procFlag!='6') and stopFlag='Y' ");
        	
  
        }
        return SUCCESS;
    }
	
	//比较两个日期之间的天数
	public static int compDate(String smallDate,String bigDate) {
        int day=0; 
        SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
        Date xxx1=new Date();
        Date xxx2=new Date();
        try {
            xxx1=sf.parse(smallDate);
            xxx2=sf.parse(bigDate);
            day = (int) ((xxx2.getTime() - xxx1.getTime())/3600/24/1000);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return day;
    } 
	
	public String getAllNum(String chkDate,String chkAcctOrd) throws SQLException{
    	String sql="";
    	sql = "select count(*) from paycheckdetail where chkdate='" + chkDate + "' and chkacctord='" + chkAcctOrd + "'";
		String AllNum = DBUtil.queryForString(sql);
		return AllNum;
    }
	
}
