package resoft.tips.chqsh;

import java.util.HashMap;
import java.util.Map;
//import resoft.basLink.util.DBUtil;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;


public class testSoc  implements Action{

//	double i = 0;
//	double j = 0;
	Map params = new HashMap();
	String PayAcct = "";
	String TraAmt = "";
	String RcvMsg = "fail";
	String TR_CODE = "fail";	//交易码
	String JRN_NO = "fail"; 	// 交易日志号
	String VCH_NO = "fail"; 	// 传票号
	String MSG_DATA = "fail"; 	// 输出数据串
	SendMsgToBankSystem send = new SendMsgToBankSystem();
	
	//与核心中间业务平台发送和接收消息
	 public int execute(Message msg) 
	 {
		 /**
		 SendMsgToBankSystem send = new SendMsgToBankSystem();
//		 String SQL = "select bkdbnkcod from ps_bkd where bkdbnknpno=" + "5801";
		 String rcvMsg = "";
		 rcvMsg = send.sendMsg("select bkdbnkcod,bkdlname from ps_bkd where bkdbnknpno='9998'" ,"8801");
//		 rcvMsg = send.sendMsg(SQL, "8801");
		 System.out.println(rcvMsg);
		 int i = rcvMsg.indexOf(10) + 1;
		 
		 System.out.println("[" + rcvMsg.substring(i).trim() + "]");
		 */
		try{
			/**
			String SQL = "select p.bkdbnkcod,p.bkdsuprlist,c.cdmac_nam,p.bkdlname from cdm c,ps_bkd p where  p.bkdbnknpno=c.cdmgacbk and c.cdmac_no='060101040001912'";
//			String SQL = "select bkdbnkcod from ps_bkd where bkdbnknpno='9998'";
			SQL = "select s.sdmgacbk,p.bkdbnkcod,p.bkdsuprlist,s.SDMACNAM,p.bkdlname from sdm s,ps_bkd p where p.bkdbnknpno=s.SDMGACBK and s.SDMACTNO='200101100000896'";
      		String rcvMsg = send.sendMsg(SQL,"880" + (System.currentTimeMillis()%4 + 1) );
      		int i = rcvMsg.indexOf(10);
      		int j = rcvMsg.lastIndexOf(10);
      	
      		System.out.println(rcvMsg);
      		System.out.println(rcvMsg.substring(i-1, i));
      		System.out.println(rcvMsg.substring(i+1, j));
      		rcvMsg = rcvMsg.substring(i + 1, j - 2);
      		System.out.println(rcvMsg);
    		String str = rcvMsg.substring(24);
      		
    		
    		for(int k=0;k<str.length();k++)
    		{
    			if(str.substring(k, k+1).equals(" "))
    			{
    				String s = str.substring(k);
    				System.out.println("开户银行为：" + s.trim());
    				break;
    			}
    		}
			RcvMsg = send.sendMsg("6221341800194035   87654321");
			System.out.println(RcvMsg);
			System.out.println(RcvMsg.length());
			RcvMsg = send.sendMsg("020101040001333", "16.01");
			*/
		
			//RcvMsg = send.sendMsg(PayAcct, TraAmt);
			TR_CODE = "0" + RcvMsg.substring(11, 14);
			JRN_NO = RcvMsg.substring(54, 61);
			VCH_NO = RcvMsg.substring(69, 77);
			MSG_DATA = RcvMsg.substring(81, 85);
			params.put("PayAcct", PayAcct);
			params.put("TraAmt", TraAmt);
			params.put("TR_CODE", TR_CODE);
			params.put("JRN_NO", JRN_NO);
			params.put("VCH_NO", VCH_NO);
			params.put("result", MSG_DATA);
			if(RcvMsg.equals("fail"))
			{
				params.put("FLAG", "N");
			}
			else
			{
				if (MSG_DATA.equals("9004")) {
					params.put("FLAG", "Y");
				} else {					
					params.put("FLAG", "O");
				}
			}
			
			//DBUtil.insert("test", params);
			
			TR_CODE = JRN_NO = VCH_NO = MSG_DATA = "fail";
			
		}catch(Exception e){
			//添加对核心的错误处理，确保只有明确的成功交易才返回，否则返回失败						
			e.printStackTrace();	
			return FAIL;
		}
		return SUCCESS;
	}
	public static void main(String[] args){
		testSoc test=new testSoc();
		Message msg = null;
		test.execute(msg);
		
	}
}
