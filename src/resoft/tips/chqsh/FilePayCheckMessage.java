package resoft.tips.chqsh;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import resoft.basLink.util.DBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import com.sybase.jdbc3.jdbc.Convert;

import resoft.basLink.util.DBUtil;
import resoft.tips.chqxh.ACEPackUtil;
import resoft.tips.util.CurrencyUtil;

/**
 * 对账明细信息
 * @author fanchengwei
 * 
 * Update by liwei 2008.10.25
 * Description：修改处理流程，添加了对于扣款成功但未对账的处理
 */
public class FilePayCheckMessage {

	private static final Log logger = LogFactory.getLog(FilePayCheckMessage.class);
	private static final String TL_TD = "880";
	private String sql = "";
	public String payCheck = "", lineStartFormat = "    ", payOpBkCode = "";
	public Map packTags = new HashMap();
	
	public FilePayCheckMessage() {
		
	}

	/**
	 * 清算中心统计全行的对账信息
	 * @throws SQLException 
	 */
	public String initPayCheck(String WorkDate, String PreWorkDate,String BranchNo,String PayeeBankNo) throws SQLException {

		payCheck += "\n\n\n\n";
		payCheck += "                           对账结果情况";
		payCheck += "\n";
		payCheck += lineStartFormat;
		payCheck += ACEPackUtil.leftStrFormat("5", "序号", " ");
		payCheck += ACEPackUtil.leftStrFormat("14", "收款行行号", " ");
		payCheck += ACEPackUtil.leftStrFormat("10", "对账日期", " ");
		payCheck += ACEPackUtil.leftStrFormat("6", "批次", " ");
		payCheck += ACEPackUtil.leftStrFormat("6", "类型", " ");
		payCheck += ACEPackUtil.leftStrFormat("8", "总笔数", " ");
		payCheck += ACEPackUtil.leftStrFormat("20", "总金额", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "未对账笔数", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "未对账金额", " ");
		//payCheck += ACEPackUtil.leftStrFormat("8", "划款情况", " ");
		payCheck += '\n';
		
		
	    sql = "select * from paycheck where CHKDATE='" + WorkDate + "' and PayeeBankNo='"+PayeeBankNo+"'";
		
		logger.info("清算中心统计全行对账情况 SQL is ：" + sql);
		String result = "",rtsql = "",bpsql = "";	
		int noCheckCount=0;		//未对账总笔数
		double noCheckAmt=0;	//未对账总金额
		rtsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from realtimepayment where tipsWorkDate='" + WorkDate + "' and PayeeBankNo='" + PayeeBankNo + "'";
		bpsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from batchpackdetail where tipsWorkDate='" + WorkDate + "' and PayeeBankNo='" + PayeeBankNo + "'";			
		// 查询实时、批量对账信息
		List rtqueryList = null;
		List bpqueryList = null;
		try {
			logger.info("实时扣税对账情况 SQL is： " + rtsql);
			logger.info("批量扣税对账情况 SQL is： " + bpsql);
			rtqueryList = QueryUtil.queryRowSet(rtsql);
			bpqueryList = QueryUtil.queryRowSet(bpsql);
		} catch (Exception e) {
			e.printStackTrace();
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "操作数据库出错！！！";
			payCheck += '\n';
			return payCheck;
		}

		if ((rtqueryList.size() + bpqueryList.size()) == 0){
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "未能查询到相关记录！！！";
			payCheck += '\n';
			return payCheck;
		}
		
		if (rtqueryList.size() > 0 || bpqueryList.size() >0) {
			
			Map row = null;			
			//处理实时
			for(int k=0;k<rtqueryList.size();k++){
				row = (Map) rtqueryList.get(k);
				String rtstr = (String) row.get("traAmt");
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3") ){ 		//扣款成功还没有进行对账
					noCheckCount++;
					noCheckAmt= noCheckAmt + Double.parseDouble(rtstr);
				}		
			}
			//处理批量
			for (int k=0;k<bpqueryList.size();k++){
				row = (Map) bpqueryList.get(k);
				String bpstr = (String) row.get("traAmt");				
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				String checkDate=(String)row.get("CHKDATE");
				if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3")){ 		//扣款成功还没有进行对账
					noCheckCount++;
					noCheckAmt+=Double.parseDouble(bpstr);
				}
			}
		}
		
		
		
		
		// 查询对账信息
		List queryList = null;		
		try {
			queryList = QueryUtil.queryRowSet(sql);
		} catch (Exception e) {
			e.printStackTrace();
			payCheck += '\n';
			payCheck += "操作数据库出错！！！";
			payCheck += '\n';
			return payCheck;
		}
		if (queryList.size() == 0) {						
			payCheck += '\n';
			payCheck += "没有该TIPS工作日内的对账信息！！！";
			payCheck += '\n';
			return payCheck;			
		}
		//组装清算中心对账信息
		if (queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				Map row = (Map) queryList.get(i);
				payCheck += lineStartFormat + id(i);
				payCheck += ACEPackUtil.leftStrFormat("14", (String) row.get("payeeBankNo"), " ");
				payCheck += ACEPackUtil.leftStrFormat("10", (String) row.get("chkDate"), " ");
				payCheck += ACEPackUtil.leftStrFormat("6", (String) row.get("chkAcctOrd"), " ");
				String chkAcctType = (String) row.get("chkAcctType");
				if ("0".equals(chkAcctType)) {
					chkAcctType = "日间";
					noCheckCount = 0;
					noCheckAmt = 0.0;
				} else {
					chkAcctType = "日切";
				}
				String allamtcount=(String) row.get("allAmt");
				double allamtdouble=Convert.objectToDoubleValue(allamtcount);
				payCheck += ACEPackUtil.leftStrFormat("6", chkAcctType, " ");
				payCheck += ACEPackUtil.leftStrFormat("8", (String) row.get("allNum"), " ");
				payCheck += ACEPackUtil.leftStrFormat("20", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+allamtdouble)), " ");
				payCheck += ACEPackUtil.leftStrFormat("11", ""+noCheckCount , " ");
				payCheck += ACEPackUtil.leftStrFormat("11", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+noCheckAmt)), " ");
				//payCheck += ACEPackUtil.leftStrFormat("8", (String) row.get("payTreasFlag"), " ");
				payCheck += '\n';
			}
		}
		return payCheck;
	}

	/**
	 * 分网点统计对账情况
	 */
	public String initPayCheckDetail(String WorkDate, String PreWorkDate,String BranchNo){
		
		int noCheckCount=0;		//未对账总笔数
		double noCheckAmt=0;	//未对账总金额
		
		int succCount=0;		//成功对账总笔数
		double succAmt=0;		//成功对账总金额
		
		int failCount=0;		//失败总笔数
		double failAmt=0;		//失败总金额
		
		String result = "",rtsql = "",bpsql = "";		

		SendMsgToBankSystem send = new SendMsgToBankSystem();
		payCheck = "\n\n\n\n" + "                               对账结果情况" + "\n";
		payCheck += "\n";
		payCheck += lineStartFormat ;
		payCheck += ACEPackUtil.leftStrFormat("10", "TIPS日期", " "); 
		payCheck += ACEPackUtil.leftStrFormat("13", "成功对账笔数", " ");
		payCheck += ACEPackUtil.leftStrFormat("17", "成功对账金额", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "未对账笔数", " ");
		payCheck += ACEPackUtil.leftStrFormat("11", "未对账金额", " "); 
		payCheck += ACEPackUtil.leftStrFormat("9", "异常笔数", " "); 
		payCheck += ACEPackUtil.leftStrFormat("9", "异常金额", " ");
		payCheck += '\n';
		
		/*
		//与后台取进行网点机构代码转换
		sql = "select p.bkdbnkcod from ps_bkd p where p.bkdbnknpno='" + BranchNo + "'";
		payOpBkCode = send.sendMsg(sql, TL_TD + (System.currentTimeMillis() % 4 + 1));
		int i = payOpBkCode.indexOf(10);
		int j = payOpBkCode.lastIndexOf(10);
		if(!payOpBkCode.substring(i-1, i).equals("1")){
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "未能查询到开户行行行号！";
			payCheck += '\n';
			return payCheck;
		}
		try{
			payOpBkCode = payOpBkCode.substring(i + 1, j);
		}catch(Exception e){
			e.printStackTrace();
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "未能查询到开户行行行号！";
			payCheck += '\n';
			return payCheck;
		}*/		
		
		rtsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from realtimepayment where tipsWorkDate='" + WorkDate + "' and BRANCHNO='" + BranchNo.trim() + "'";
		bpsql = "select traAmt,result,checkStatus,nvl(CHKDATE,'') as CHKDATE from batchpackdetail where tipsWorkDate='" + WorkDate + "' and BRANCHNO='" + BranchNo.trim() + "'";			
		
		// 查询实时、批量对账信息
		List rtqueryList = null;
		List bpqueryList = null;
		try {
			logger.info("实时扣税对账情况 SQL is： " + rtsql);
			logger.info("批量扣税对账情况 SQL is： " + bpsql);
			rtqueryList = QueryUtil.queryRowSet(rtsql);
			bpqueryList = QueryUtil.queryRowSet(bpsql);
		} catch (Exception e) {
			e.printStackTrace();
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "操作数据库出错！！！";
			payCheck += '\n';
			return payCheck;
		}

		if ((rtqueryList.size() + bpqueryList.size()) == 0){
			payCheck += '\n';
			payCheck += lineStartFormat;
			payCheck += "未能查询到相关记录！！！";
			payCheck += '\n';
			return payCheck;
		}
		
		if (rtqueryList.size() > 0 || bpqueryList.size() >0) {
			
			Map row = null;			
			//处理实时
			for(int k=0;k<rtqueryList.size();k++){
				row = (Map) rtqueryList.get(k);
				String rtstr = (String) row.get("traAmt");
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				if ( result.equals("90000") && checkStatus.equals("1") ) {	 		//扣款成功并对账成功
					succCount++;
					succAmt= succAmt + Double.parseDouble(rtstr);
				}else if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3")){ 		//扣款成功还没有进行对账
					noCheckCount++;
					noCheckAmt= noCheckAmt + Double.parseDouble(rtstr);
				}else {														 		//异常错误扣款信息
					failAmt = failAmt + Double.parseDouble(rtstr);
					failCount++;
				}
			}
			//处理批量
			for (int k=0;k<bpqueryList.size();k++){
				row = (Map) bpqueryList.get(k);
				String bpstr = (String) row.get("traAmt");				
				result = (String) row.get("result")==null?"99090":(String) row.get("result");
				String checkStatus=((String) row.get("checkStatus"))==null?"0":(String) row.get("checkStatus");
				String checkDate=(String)row.get("CHKDATE");
				if ( result.equals("90000") && checkStatus.equals("1") ) {			//扣款成功并对账成功
					succCount++;
					succAmt+=Double.parseDouble(bpstr);
				}else if( (result.equals("90000") && checkStatus.equals("0")) || checkStatus.equals("3")	){ 		//扣款成功还没有进行对账
					noCheckCount++;
					noCheckAmt+=Double.parseDouble(bpstr);
				}else {														 		//异常错误扣款信息
					failAmt += Double.parseDouble(bpstr);
					failCount++;
				}
			}
		}
		
		logger.info("\n succCount is:"+succCount+",succAmt is:"+succAmt+",noCheckCount is:"+noCheckCount+",noCheckAmt is:"+noCheckAmt+",failCount is:"+failCount+",failAmt is:"+failAmt);
		
		payCheck += lineStartFormat;
		payCheck += ACEPackUtil.leftStrFormat("10", WorkDate, " ");		
		payCheck += ACEPackUtil.leftStrFormat("13", ""+succCount , " ");
		payCheck += ACEPackUtil.leftStrFormat("17", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+succAmt)), " ");
		payCheck += ACEPackUtil.leftStrFormat("11", ""+noCheckCount , " ");
		payCheck += ACEPackUtil.leftStrFormat("11", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+noCheckAmt)), " ");
		payCheck += ACEPackUtil.leftStrFormat("9", ""+failCount , " ");
		payCheck += ACEPackUtil.leftStrFormat("9", MoneyFormat.getMoneyFormat(CurrencyUtil.getCurrencyFormat(""+failAmt)), " ");
		payCheck += '\n';
		
		return payCheck;
	
	}
	
	//yangyuanxu add 
	public int getStatus(String BranchNo) throws SQLException{
		int len = 0;
		len=DBUtil.queryForInt("select count(*) from bank_relation where bankorgcode='"+BranchNo+"'");	
		if(len > 0)
			return 1;
		else
			return 0;
		
	}
	//yangyuanxu add 
	public String getPayeeBankNo(String BranchNo) throws SQLException{
		String PayeeBankNo = "";
		PayeeBankNo=DBUtil.queryForString("select PayeeBankNo from bank_relation where bankorgcode='"+BranchNo+"'");	
		PayeeBankNo=PayeeBankNo.trim();
		return PayeeBankNo;
	}	
		
	public String id(int i) {
		String format = "     ";
		String buf = "";
		buf = "" + (i + 1);
		buf = buf + format.substring(0, format.length() - buf.length());
		return buf;
	}
	
}

