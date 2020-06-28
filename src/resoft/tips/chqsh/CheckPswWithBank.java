package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * 与银行后台进行密码校验，涉及柜员密码权限校验和账户卡密码校验
 * @author fanchengwei
 *
 */
public class CheckPswWithBank implements Action {

		private static final Log logger = LogFactory.getLog(CheckProveDelete.class);
		SendMsgToBankSystem send = new SendMsgToBankSystem();
	    public int execute(Message msg) 
	    {
	        String Teller = msg.getString("Teller");		//柜员号
	        String PayAcct = msg.getString("PayAcct");		//卡号
	        String PassWord = msg.getString("Password");	//密码
	        String UserId = msg.getString("UserId");
	        msg.set("Password", "");
	        //logger.info("CardNo = " + Teller.length() + "      " + "ReturnPassWord = " + msg.getString("Password"));
	        String RcvMsg = null;
	        String id = "E";
	        if(Teller != null)
	        {
	        	RcvMsg = send.sendChk(Teller, PassWord, UserId);
	        	id = RcvMsg.substring(37, 38);
	        }
	        else
	        {
	        	RcvMsg = send.sendMsg((PayAcct+"                    ").substring(0, 19) + (PassWord+"          ").substring(0, 8));
	        	id = RcvMsg.substring(37, 38);
	        }
	        logger.info(RcvMsg);
//	        System.out.println(RcvMsg);
//	        String id = RcvMsg.substring(37,38);						//后台交易结果
	        String MSG_DATA = RcvMsg.substring(81, 85);					//处理结果描述
	        if(id.equals("N"))
	        {
	        	 msg.set("ReturnResult", "Y");
	        	 msg.set("AddWord", "密码正确");
	        }
	        else
	        {
	        	try
	        	{
	        		MSG_DATA = DBUtil.queryForString("select addword from transresult where flag='"+ MSG_DATA +"'");
	        		logger.info("MSG_DATA   is   length  =  " + MSG_DATA.getBytes().length);
	        		logger.info("MSG_DATA   is    =  [" + MSG_DATA + "]");
	        		if(MSG_DATA.getBytes().length>60)
	        		{
	        			MSG_DATA = MSG_DATA.substring(0, 30);
	        		}
	        	}
	        	catch(Exception e)
	        	{
	        		MSG_DATA = "帐号和密码不相符！！";
	        	}
	        	 msg.set("ReturnResult", "N");
//	        	 msg.set("ReturnResult", "Y");
	        	 msg.set("AddWord", MSG_DATA);
	        }
	        return SUCCESS;
	    }
	    public CheckPswWithBank()
	    {
	    }
	    public static void main(String args[])
	    {
	    	CheckPswWithBank cp = new CheckPswWithBank();
	    	Message msg = null;
	    	cp.execute(msg);
	    }
}
