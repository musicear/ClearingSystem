package resoft.tips.chqsh;


//import resoft.xlink.impl.DefaultMessage;
//import resoft.basLink.util.MessageSender;
//import resoft.basLink.util.MessageSendException;
//import resoft.tips.util.MessageSenderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>帐号状态查询</p>
 * Author: fanchengwei
 * 帐号结构：前4位机构号|接着2位币种|接着7、8对应账户类型|
 */
public class QueryAcct implements Action {
    
	private static final Log logger = LogFactory.getLog(QueryAcct.class);
    private static final SendMsgToBankSystem send = new SendMsgToBankSystem();
    
    public int execute(Message msg) throws Exception {
        
    	String acctNo = msg.getString("PayAcct").trim();
        String BranchNo = msg.getString("BranchNo").trim();    
        
        int result = 0;
        if(acctNo.length()<8){
        	msg.set("ReturnResult", "N");
            msg.set("AddWord", "非法的帐号，长度小于8！");
            logger.info("非法的帐号，长度小于8！");
            return FAIL;
        }
        String id = acctNo.substring(6,8);  
        logger.info("payAcct is:"+acctNo+",Id is:"+id);
                
        //首先验证是否是本网点的帐户
        try{
	        result=checkSzhh(msg);        
	        if ( result!=0 ) {
	        	return result;
	        }
        }catch(Exception e){
        	e.printStackTrace();
        	msg.set("ReturnResult", "N");
        	msg.set("AddWord", "账号错误");
        	return FAIL;
        }
                
        if(acctNo.length() == 16){
        	result = getCard(id, acctNo, msg, BranchNo);
        }else if(id.equals("04") && acctNo.length() == 15){
        	result = getCDM(id, acctNo, msg);
        }else if(id.equals("10") && acctNo.length() == 15){
        	result = getSDM(id, acctNo, msg);
        }else if(id.equals("01") && acctNo.length() == 15){
        	result = getIAC(id, acctNo, msg);
        }
        return result;
    }
    
    
    
    public static int checkSzhh(Message msg) throws Exception{
    	
    	SendMsgToBankSystem send = new SendMsgToBankSystem();
    	String rcvMsg = "";
    	String SQL = "";
    	
    	String payAcct = msg.getString("PayAcct").trim();
        String BranchNo = msg.getString("BranchNo").trim(); 
    	/**
    	 * 卡账户的处理
    	 * 首先，查询MCA--卡帐对照表获取帐号和户名，由于卡和账户是一对多的形式，所以限定只有专用码为‘99’的卡才可以签署三方协议
    	 * 第二，查询SDM---活期分户帐获取总帐行号
    	 * 最后，通过总行行号查询PS_BKD表获取帐号的开户行行号和付款开户行行号。
    	 */
    	if(payAcct.length() == 16){
    		SQL = "select lpad(p.brtbr,12),'313653000013',s.SDMACNAM,lpad(p.brtbrnam,60) from MCA m,brt p,SDM s where m.MCAEPTCOD='99' and p.brtbr=s.SDMGACBK and m.MCAACTNO=s.SDMACTNO and m.MCACRDNO='"+ payAcct +"'";
    		logger.info("卡账户的处理 查询SQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );
    		logger.info("卡账户的处理，后台TUXEDO返回信息:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		if(i < 0) {
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "未能查询到帐号信息！");
                logger.info("未能查询到帐号信息！");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			e.printStackTrace();
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "未能取到开户行行号和付款开户行行号！");
                logger.info("未能取到开户行行号和付款开户行行号！");
                return FAIL;
    		}
    	}
    	/**
    	 * 支票账户的处理
    	 *帐号的 7、8位是 04 的帐号查询 cdm 表获取缴款单位名称。
    	 *查询ps_bkd 表获取付款行行号和付款开户行行号。
    	 */
    	if( payAcct.substring(6, 8).equals("04") && payAcct.length() == 15 ){
    		SQL = "select lpad(p.brtbr,12),'313653000013',c.cdmac_nam,lpad(p.brtbrnam,60) from cdm c,brt p where p.brtbr=c.cdmgacbk and c.cdmac_no='"+ payAcct +"'";
    		logger.info("支票账户的处理 查询SQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );         //p.bkdagdbkno
    		logger.info("支票账户的处理，后台TUXEDO返回信息:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		if(i < 0){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "未能查询到帐号信息！");
                logger.info("未能查询到帐号信息！");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			e.printStackTrace();
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "未能取到开户行行号和付款开户行行号！");
                logger.info("未能取到开户行行号和付款开户行行号！");
                return FAIL;
    		}
    	}
    	/**
    	 * 储蓄活期账户的处理
    	 * 帐号的7、8位是 10 的帐号查询 sdm 表获取缴款单位名称。
    	 * 查询ps_bkd 表获取付款行行号和付款开户行行号。
    	 */
    	if(payAcct.substring(6, 8).equals("10") && payAcct.length() == 15){
    		SQL = "select lpad(p.brtbr,12),'313653000013',s.SDMACNAM,lpad(p.brtbrnam,60) from sdm s,brt p where p.brtbr=s.SDMGACBK and s.SDMACTNO='"+ payAcct +"'";
    		logger.info("储蓄活期账户的处理 查询SQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );
    		logger.info("储蓄活期账户的处理，后台TUXEDO返回信息:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		if(i < 0){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "未能查询到帐号信息！");
                logger.info("未能查询到帐号信息！");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "当前机构下的开户帐号不存在！");
                logger.info("未能取到开户行行号和付款开户行行号！");
                return FAIL;
    		}
    	}
    	/**
    	 * 内部账户的处理
    	 * 帐号的7、8位是 01 的帐号查询 iac 表获取缴款单位名称。
    	 * 查询ps_bkd 表获取付款行行号和付款开户行行号。
    	 */
    	if( payAcct.substring(6, 8).equals("01") && payAcct.length() == 15 ){
    		SQL = "select lpad(p.brtbr,12),'313653000013',i.IACAC_NAM,lpad(p.brtbrnam,60) from iac i,brt p where p.brtbr=i.IACGACBR and i.iacac_no='"+ payAcct +"'";
    		logger.info("内部账户的处理 查询SQL is:"+SQL);
    		rcvMsg = send.sendMsg(SQL,"8801" );
    		logger.info("内部账户的处理，后台TUXEDO返回信息:["+rcvMsg+"]");
    		int i = rcvMsg.indexOf(10);
    		logger.info("@@@@@@@@:"+i);
    		if(i < 0){
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "未能查询到帐号信息！");
                logger.info("未能查询到帐号信息！");
                return FAIL;
    		}
    		int j = rcvMsg.lastIndexOf(10);
    		try{
    			rcvMsg = rcvMsg.substring(i+1,j-1);
    		}catch(Exception e){
    			e.printStackTrace();
    			msg.set("ReturnResult", "N");
                msg.set("AddWord", "未能取到开户行行号和付款开户行行号！");
                logger.info("未能取到开户行行号和付款开户行行号！");
                return FAIL;
    		}
    	}  
    	    	
    	String str = rcvMsg.substring(24);
    	String PayOpBkName = "开户行不存在！";
    	String handOrgName = "";
    	if(str.length()>0){
    		for(int k=0;k<str.length();k++){
    			if(str.substring(k, k+1).equals(" ")){
    				handOrgName = str.substring(0, k);
    				PayOpBkName = str.substring(k).trim();
    				break;
    			}
    		}
    	}
    	
    	//不允许跨机构签订协议
    	if(!(rcvMsg.substring(0, 12)).trim().equals(BranchNo)){
    		msg.set("ReturnResult", "N");
            msg.set("AddWord", "请到开户行签订协议！");
            return FAIL;
    	}
    	
    	msg.set("payOpBkCode", (rcvMsg.substring(0, 12)).trim());
    	msg.set("handOrgName", handOrgName);
    	msg.set("PayOpBkName", PayOpBkName);
    	
    	return SUCCESS;
    }
    
    
    
    /**
     * 卡账户查询需要分别查询卡的状态和卡对应的账号的状态。
     * 分别查询MCA、SDM
     */
    public int getCard(String id,String acctNo,Message msg,String BranchNo)
    {
    	String SQL = "";
    	String rcvMsg = "NULL";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(acctNo.length() == 16)
         {
         	SQL = "select s.sdmgacbk,m.MCAACTSTS,s.SDMACTSTS,s.SDMACNAM from MCA m,SDM s where m.MCAACTNO=s.SDMACTNO and m.MCAEPTCOD='99' and m.MCACRDNO='"+ acctNo +"'";
         	rcvMsg = send.sendMsg(SQL, "8801");
         	if(!rcvMsg.equals("NULL"))
         	{
         		i = rcvMsg.indexOf(10);
         		if(i < 0)
        		{
        			msg.set("AddWord", "未能查询到账户信息！");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
         		count = Integer.parseInt(rcvMsg.substring(i-1,i));
         		if(count > 0)
         		{
         			j = rcvMsg.lastIndexOf(10);
         			String str = rcvMsg.substring(i+1, i+5);
         			if(!str.equals(BranchNo))
         			{
         				msg.set("ReturnResult", "N");
         	            msg.set("AddWord", "此帐号未在本行开户，请到开户行办理业务！");
         	            logger.info("此帐号未在本行开户，请到开户行办理业务！");
         	            return FAIL;
         			}
         			String cardStatus = rcvMsg.substring(i+5, i+6);
         			status = rcvMsg.substring(i+6, i+16);
         			PayAcctName = rcvMsg.substring(i+16 , j-1).trim();
 	            	msg.set("AcctName", PayAcctName);
         			if("0".equals(cardStatus)
         					&&status.substring(0, 1).equals("1")&&status.substring(1, 2).equals("1")||status.substring(1, 2).equals("2")
                 			&&status.substring(2,3).equals("1")&&status.substring(3, 4).equals("1")||status.substring(3, 4).equals("3")
                 			&&status.subSequence(4, 5).equals("1")||status.substring(4, 5).equals("2")&&status.subSequence(5, 6).equals("1")||status.substring(5, 6).equals("2")
                 			&&status.subSequence(6, 7).equals("1")&&status.subSequence(7, 8).equals("1")
                 			&&status.subSequence(8, 9).equals("1")||status.subSequence(8, 9).equals("2")||status.subSequence(8, 9).equals("3"))
         			{
         				msg.set("AddWord", "账户状态正常");
 	                	msg.set("ReturnResult", "Y");
 	                	return SUCCESS;
         			}
         			if("1".equals(cardStatus) || "2".equals(cardStatus))
         			{
         				msg.set("AddWord", "账户冻结");
 	                	msg.set("ReturnResult", "N");
 	                	return FAIL;
         			}
         			if("3".equals(cardStatus) || "4".equals(cardStatus))
         			{
         				msg.set("AddWord", "账户止付");
 	                	msg.set("ReturnResult", "N");
 	                	return FAIL;
         			}
         			if("5".equals(cardStatus) || "6".equals(cardStatus))
         			{
         				msg.set("AddWord", "账户销户或者抹户");
 	                	msg.set("ReturnResult", "N");
 	                	return FAIL;
         			}
         			msg.set("AddWord", "帐号状态其他错误");
         			msg.set("ReturnResult", "N");
                 	return FAIL;
         		}
         	}
         	else
         	{
         		msg.set("ReturnResult", "N");
                 msg.set("AddWord", "发送银行核心系统失败");
                 return FAIL;
         	}
         }
    	 msg.set("ReturnResult", "N");
         msg.set("AddWord", "帐号不存在");
         return FAIL;
    }
    /**
     * 04代表支票账户（即对公账户），查询CDM表。
     */
    public int getCDM(String id,String acctNo,Message msg)
    {
    	String SQL = "";
    	String rcvMsg = "";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(id.equals("04"))
        {
        	SQL = "select CDMAC_STS,CDMAC_NAM from CDM where CDMAC_NO = '"+acctNo+"'";
        	logger.info("验证账号状态 sql is:"+SQL);
        	rcvMsg = send.sendMsg(SQL,"8801" );
        	logger.info("读取TUXEDO数据：["+rcvMsg+"]");
        	if(!rcvMsg.equals("NULL"))
        	{
        		i = rcvMsg.indexOf(10);
        		if(i < 0)
        		{
        			msg.set("AddWord", "未能查询到账户信息！");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
            	count = Integer.parseInt(rcvMsg.substring(i-1, i));
	        	if(count>0)
	        	{
	        		j = rcvMsg.lastIndexOf(10);
	        		logger.info("i          ==              " + i);
        			logger.info("j          ==              " + j);
        			logger.info("rcvMsg          ==              " + rcvMsg);
	            	status = rcvMsg.substring(i+1, i+2);
	            	PayAcctName = rcvMsg.substring(i+2, j-1);
	            	msg.set("AcctName", PayAcctName.trim());
	            	if(status.equals("0"))
	            	{
	            		msg.set("AddWord", "账户状态正常");
	                	msg.set("ReturnResult", "Y");
	                	return SUCCESS;
	            	}
	            	if(status.equals("1"))
	            	{
	            		msg.set("AddWord", "账户全封！");
	            		msg.set("ReturnResult", "N");
	            		return FAIL;
	            	}
	            	if(status.equals("2")||status.equals("3")||status.equals("4"))
	            	{	            		
	            		msg.set("AddWord", "账户不可用！");
	            		msg.set("ReturnResult", "N");
	            		return FAIL;
	            	}
	        	}
	        	else
	        	{
	        		msg.set("AddWord", "账户不存在");
	            	msg.set("ReturnResult", "N");
	            	return FAIL;
	        	}
        	}
        	else
        	{
        		msg.set("ReturnResult", "N");
                msg.set("AddWord", "发送银行核心系统失败");
                return FAIL;
        	}
        }
    	 msg.set("ReturnResult", "N");
         msg.set("AddWord", "帐号不存在");
         return FAIL;
    }
    /**
     * 10代表卡账户，查询SDM表(SDM---活期分户帐)
     */
    public int getSDM(String id,String acctNo,Message msg)
    {
    	String SQL = "";
    	String rcvMsg = "";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(id.equals("10"))
        {
        	SQL = "select SDMACTSTS,SDMACNAM from SDM where SDMACTNO = '"+acctNo+"'";
        	rcvMsg = send.sendMsg(SQL,"8801");
        	if(!rcvMsg.equals("NULL"))
        	{
        		i = rcvMsg.indexOf(10);
        		logger.info("i          ==              " + i);
        		logger.info("rcvMsg          ==              [" + rcvMsg + "]");
        		if(i < 0)
        		{
        			msg.set("AddWord", "未能查询到账户信息！");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
        		count = Integer.parseInt(rcvMsg.substring(i-1, i));
        		if(count>0)
        		{
        			j = rcvMsg.lastIndexOf(10);       			
        			logger.info("j          ==              " + j);      			
                	status = rcvMsg.substring(i+1, i+10);
                	PayAcctName = rcvMsg.substring(i+11, j-1);
                	msg.set("AcctName", PayAcctName.trim());
                	if(status.substring(0, 1).equals("1")&&status.substring(1, 2).equals("1")||status.substring(1, 2).equals("2")
                			&&status.substring(2,3).equals("1")&&status.substring(3, 4).equals("1")||status.substring(3, 4).equals("3")
                			&&status.subSequence(4, 5).equals("1")||status.substring(4, 5).equals("2")&&status.subSequence(5, 6).equals("1")||status.substring(5, 6).equals("2")
                			&&status.subSequence(6, 7).equals("1")&&status.subSequence(7, 8).equals("1")
                			&&status.subSequence(8, 9).equals("1")||status.subSequence(8, 9).equals("2")||status.subSequence(8, 9).equals("3"))
                	{
                		msg.set("AddWord", "账户状态正常！");
	                	msg.set("ReturnResult", "Y");
	                	return SUCCESS;
                	}
                	else
                	{
                		msg.set("AddWord", "账户状态错误");
	                	msg.set("ReturnResult", "N");
	                	return FAIL;
                	}
        		}
        		else
        		{
        			msg.set("AddWord", "账户不存在");
        			msg.set("ReturnResult", "N");
        			return FAIL;
        		}
        	}
	        else
	        {
	        	msg.set("ReturnResult", "N");
	            msg.set("AddWord", "发送银行核心系统失败");
	            return FAIL;
	        }
        }
    	 msg.set("ReturnResult", "N");
         msg.set("AddWord", "帐号不存在");
         return FAIL;
    }
    /**
     * IAC---通用分户帐
     */
    public int getIAC(String id,String acctNo,Message msg)
    {
    	String SQL = "";
    	String rcvMsg = "";
    	String status = "";
    	String PayAcctName = "";
    	int i = 0;
    	int j = 0;
    	int count = 0;
    	if(id.equals("01"))
        {
        	SQL = "select IACAC_STS,IACAC_NAM from IAC where IACAC_NO = '"+ acctNo +"'";
        	rcvMsg = send.sendMsg(SQL,"8801" );
        	if(!rcvMsg.equals("NULL"))
        	{
        		i = rcvMsg.indexOf(10);
        		if(i < 0)
        		{
        			msg.set("AddWord", "未能查询到账户信息！");
                	msg.set("ReturnResult", "N");
                	return FAIL;
        		}
        		count = Integer.parseInt(rcvMsg.substring(i-1, i));
        		if(count>0)
        		{
        			j = rcvMsg.lastIndexOf(10);
                	status = rcvMsg.substring(i+1, i+2);
                	PayAcctName = rcvMsg.substring(i+2, j-1);
                	msg.set("AcctName", PayAcctName.trim());
                	if(status.equals("2"))
                	{
                		msg.set("AddWord", "账户状态正常");
	                	msg.set("ReturnResult", "Y");
	                	return SUCCESS;
                	}
                	if(status.equals("1"))
                	{
                		msg.set("AddWord", "账户已销户");
            			msg.set("ReturnResult", "N");
            			return FAIL;
                	}
                	if(status.equals("3"))
                	{
                		msg.set("AddWord", "全冻户");
            			msg.set("ReturnResult", "N");
            			return FAIL;
                	}
                	if(status.equals("4"))
                	{
                		msg.set("AddWord", "收款户（只收不贷）");
            			msg.set("ReturnResult", "N");
            			return FAIL;
                	}
        		}
        		else
        		{
        			msg.set("AddWord", "账户不存在");
        			msg.set("ReturnResult", "N");
        			return FAIL;
        		}
        	}
	        else
	        {
	        	msg.set("ReturnResult", "N");
	            msg.set("AddWord", "发送银行核心系统失败");
	            return FAIL;
	        }
        }
        msg.set("ReturnResult", "N");
        msg.set("AddWord", "帐号不存在");
        return FAIL;
    }
}


