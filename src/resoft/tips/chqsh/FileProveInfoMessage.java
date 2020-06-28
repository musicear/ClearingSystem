package resoft.tips.chqsh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.QueryUtil;

import resoft.tips.chqxh.ACEPackUtil;

/**
 * 批量查询协议信息
 */

public class FileProveInfoMessage {
		
	private static final Log logger = LogFactory.getLog(FileProveInfoMessage.class);
	private String sql="";
	public String proveInfo="",lineStartFormat="";	
	
	public Map packTags=new HashMap();
	
	public FileProveInfoMessage(){
		
	}
	
	public String initProveInfo(String VerifyResult , String BranchNo){
		
		proveInfo+=lineStartFormat;
		proveInfo+=ACEPackUtil.leftStrFormat("5","序号"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("9","签订时间"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("18","付款帐户"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("60","缴款单位名称"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("20","协议书号"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("10","签订机构"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("22","纳税人编码"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("60","纳税人名称"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("20","征收机关代码"," ");
		proveInfo+=ACEPackUtil.leftStrFormat("60","征收机关名称"," ");			
		proveInfo+= '\n';				
		
		if("0001".equals(BranchNo)){		//清算中心统计全行的信息
			sql = "select p.PayAcct,p.HandOrgName,p.ProtocolNo,p.taxpaycode,p.taxorgcode,t.taxorgname,p.sendtime,p.branchno,p.taxpayname from proveinfo p,taxorgmng t where p.taxorgcode=t.taxorgcode and p.EnabledFlag='Y' and p.verifyresult='"+ VerifyResult +"' order by sendtime DESC";
		}else{								//分网点统计协议信息
			sql = "select p.PayAcct,p.HandOrgName,p.ProtocolNo,p.taxpaycode,p.taxorgcode,t.taxorgname,p.sendtime,p.branchno,p.taxpayname from proveinfo p,taxorgmng t where p.taxorgcode=t.taxorgcode and p.EnabledFlag='Y' and p.verifyresult='"+ VerifyResult +"' and branchno='"+ BranchNo +"' order by sendtime DESC";
		}
		logger.info("查询三方协议SQL is：\n"+sql+"\n");
		
		List queryList = null;
		try{
			queryList = QueryUtil.queryRowSet(sql);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("操作数据库出错！！");
			proveInfo = ACEPackUtil.leftStrFormat("20","记录数目："," ") + "\n" + proveInfo;
			return proveInfo;
		}
		
		if(queryList.size() == 0){
			proveInfo= "记录数目：0 \n" + proveInfo;
			proveInfo+= "没有查询到相关记录！！！";
			proveInfo+= '\n';
			return proveInfo;
		}
		if (queryList.size()>0){			
			for ( int i=0; i<queryList.size(); i++ ) {				
				Map row=(Map)queryList.get(i);				
				proveInfo+=lineStartFormat + id(i);
				String sendtime = (String)row.get("sendtime");
				proveInfo+=ACEPackUtil.leftStrFormat("9",sendtime.substring(0, 8)," ");
				proveInfo+=ACEPackUtil.leftStrFormat("18",(String)row.get("PayAcct")," ");
				proveInfo+=ACEPackUtil.leftStrFormat("60",(String)row.get("HandOrgName")," ");
				proveInfo+=ACEPackUtil.leftStrFormat("20",(String)row.get("ProtocolNo")," ");
				proveInfo+=ACEPackUtil.leftStrFormat("10",(String)row.get("branchno")," ");
				proveInfo+=ACEPackUtil.leftStrFormat("22",(String)row.get("taxpaycode")," ");				
				proveInfo+=ACEPackUtil.leftStrFormat("60",(String)row.get("taxpayname")," ");
				proveInfo+=ACEPackUtil.leftStrFormat("20",(String)row.get("taxorgcode")," ");
				proveInfo+=ACEPackUtil.leftStrFormat("60",(String)row.get("taxorgname")," ");	
				proveInfo+= '\n';
			}
		}
		proveInfo = ACEPackUtil.leftStrFormat("20","记录数目："+queryList.size()," ") + "\n" + proveInfo;
		return proveInfo;
	}
	
	public String id(int i){
		String format = "     ";
		String buf = "";
		buf = "" + (i + 1);
		buf = buf + format.substring(0, format.length()-buf.length());
		return buf;
	}	
}
