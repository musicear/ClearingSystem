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
 * <p>三方协议查询</p>
 * Author: liwei
 * Date: 2007-08-09
 * Time: 11:02:00
 */

public class SfxyCx implements Action {
	
	private static final Log logger = LogFactory.getLog(SfxyCx.class);
	private static Configuration conf = Configuration.getInstance();
	
	String sqlWhere="",tempFileData="";
    
	//回执明细信息:纳税人编码(20)+登记日期(8)+委托日期(8)+付款帐号(19)+付款账户名称(62)+纳税人名称(62)+征收机关代码(12)+征收机关名称(62)+协议号(60)+撤销日期(8)+登记柜员(6)+删除柜员(6)+验证标志(1)  	
	private String []sfxyLists={"TaxPayCode","SendTime","SendTime","PayAcct","handOrgName","TaxPayName","TaxOrgCode","TaxOrgName","ProtocolNo","RemoveTime","InputTeller","RemoveTeller","VerifyResult"};
	private String []sfxyLenFormat={"20","8","8","19","62","62","12","62","60","8","6","6","1"};
	
    public int execute(Message msg) throws Exception {    	    	
    	ACEPackager ace2009=(ACE2009)msg.get("ACEObj");
    	//初始化ACE交易报文体
    	ace2009.makeTransBody();
    	msg.set("BankOrgCode", (String)ace2009.pkTHHeadList.get("BankOrgCode"));//机构代码
    	msg.set("InputTeller", (String)ace2009.pkTHHeadList.get("InputTeller"));//柜员号
    	
    	String taxPayCode=(String)ace2009.pkTHBodyList.get("TaxPayCode");		//纳税人编码
    	String payAcct=(String)ace2009.pkTHBodyList.get("PayAcct");				//付款帐号 
    	//模糊查询
    	if (!taxPayCode.trim().equals("")) {
    		sqlWhere=" and a.taxPayCode='"+taxPayCode+"' "; 
    	}
    	if (!payAcct.trim().equals("")) {
    		sqlWhere=" and a.payAcct='"+payAcct+"' ";
    	}
    	
    	//查询是否有协议信息    	
    	int count=DBUtil.queryForInt("select count(*) from ProveInfo a where 1=1 "+sqlWhere);
    	logger.info("信合三方协议查询sql："+"select count(*) from ProveInfo a where 1=1 "+sqlWhere);
        if(count>0){
        	//查询笔数
        	msg.set("sfxyCount", ACEPackUtil.getFieldFormatDef("16", ""+count));
        	List queryList = QueryUtil.queryRowSet("select a.TaxPayCode,a.SendTime,a.SendTime,a.PayAcct,a.handOrgName,a.TaxPayName,a.TaxOrgCode,c.TaxOrgName,a.ProtocolNo,a.RemoveTime,a.InputTeller,a.RemoveTeller,a.VerifyResult,a.EnabledFlag from ProveInfo a,BankMng b,taxOrgMng c where a.payBkCode=b.ReckBankNo and a.taxOrgCode=c.taxOrgCode "+sqlWhere);
        	logger.info("信合三方协议查询查询明细："+"select * from ProveInfo a,BankMng b,taxOrgMng c where a.payBkCode=b.ReckBankNo and a.taxOrgCode=c.taxOrgCode "+sqlWhere);
        	if(queryList.size()>0){
        		for(int i=0;i<queryList.size();i++){
	        		Map row=(Map)queryList.get(i);	        		
	        		String temp="";
	        		for(int j=0;j<(sfxyLists.length-1);j++){
	        			temp+=ACEPackUtil.getFieldFormatDef(sfxyLenFormat[j], (String)row.get(sfxyLists[j]));
	        			//logger.info("["+sfxyLists[i]+":"+ACEPackUtil.getFieldFormatDef(sfxyLenFormat[j], (String)row.get(sfxyLists[j]))+"]");
	        		}	        		
	        		String verifyResult=(String)row.get("VerifyResult");//0:验证通过，1:验证未通过
	        		String enabledFlag=(String)row.get("EnabledFlag");	//Y:有效协议，N:无效协议
	        		logger.info("EnabledFlag val:["+enabledFlag+"],verifyResult:["+verifyResult+"]");
	        		String proveFlag="";
	        		if (enabledFlag.equals("Y")) {	//协议有效
	        			if (verifyResult.equals("0")) {
	        				proveFlag="1";		//协议有效并通过验证
	        			}else {
	        				proveFlag="0";		//协议有效,登记未验证
	        			}
	        		}else {
	        			if (verifyResult.equals("0")) {
	        				proveFlag="2";		//验证撤销
	        			}else {
	        				proveFlag="3";		//未验证撤销
	        			}
	        		}
	        		temp+=proveFlag;
	        		//添加明细信息
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
        	msg.set("AddWord", "协议不存在");        	
        }
    	    	
    	/**
    	 * 交易返回信息
    	 * 		成功：状态码[3]|笔数[16]|文件名[19]
    	 * 		失败：状态码[3]|错误信息描述
    	 * */
    	if (ace2009.tradeStatus.equals("000")) {//成功
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("sfxyCount")+"|"+getSfxyFileName(msg));
    	}else {
    		msg.set("ACETrandBody", msg.get("VCResult")+"|"+msg.getString("AddWord"));
    	}
    	return SUCCESS;
    }
    
    //将协议查询信息生成临时文件
    public String getSfxyFileName(Message msg) throws IOException{
    	//文件名：TIPS[4]+机构号[6]+柜员号[6]+"000"[3]		len:19
    	String fileName = "TIPS"+msg.getString("BankOrgCode")+msg.getString("InputTeller")+"000";
        String tmpPath = conf.getProperty("BankSysConfig", "TempFilePath");
        File f = new File(tmpPath, fileName);
        try {
            Writer writer = new FileWriter(f);
            writer.write((String)msg.getString("TempFileData"));            
            writer.close();
            logger.info("协议明细信息：["+(String)msg.getString("TempFileData")+"]");            
            logger.info("消息存放于:" + f.getAbsolutePath());
        } catch (IOException e) {            
            logger.error("消息内容为：" + msg.getString("TempFileData"));
            e.printStackTrace();
            throw e;
        }
        return fileName;    	
    }    
}
