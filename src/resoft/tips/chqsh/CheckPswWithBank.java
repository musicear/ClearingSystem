package resoft.tips.chqsh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.DBUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * �����к�̨��������У�飬�漰��Ա����Ȩ��У����˻�������У��
 * @author fanchengwei
 *
 */
public class CheckPswWithBank implements Action {

		private static final Log logger = LogFactory.getLog(CheckProveDelete.class);
		SendMsgToBankSystem send = new SendMsgToBankSystem();
	    public int execute(Message msg) 
	    {
	        String Teller = msg.getString("Teller");		//��Ա��
	        String PayAcct = msg.getString("PayAcct");		//����
	        String PassWord = msg.getString("Password");	//����
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
//	        String id = RcvMsg.substring(37,38);						//��̨���׽��
	        String MSG_DATA = RcvMsg.substring(81, 85);					//����������
	        if(id.equals("N"))
	        {
	        	 msg.set("ReturnResult", "Y");
	        	 msg.set("AddWord", "������ȷ");
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
	        		MSG_DATA = "�ʺź����벻�������";
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
