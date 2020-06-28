package com.easycon.platform.net;

//import com.resoft.recis.bank.access.message.dao.RequestMessageInfo;
//import com.resoft.recis.bank.access.message.dao.RespondMessageInfo;

/**
 * 功能：调用行内核心系统
 * <p>
 * Title: JEasyClient
 * </p>
 * <p>
 * Description: EasyNet Java Client
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: BJ EASYCON
 * </p>
 *
 * @author lihb
 * @version 1.0
 */

public class JEasyClient {
	public JEasyClient() {
	}

	/**
	 * 调用动态连接库jeasyclient
	 */
	static {
		try {
			System.out.println("Before Load jeasyclient.dll");
			System.loadLibrary("jeasyclient");
		} catch (UnsatisfiedLinkError unsatisfiedlinkerror) {
			System.err.println("Cannot load jeasyclient.dll:\n "
					+ unsatisfiedlinkerror.toString());
		}
	}

	/**
	 * 是填空的不用关心，是小额系统的规定
	 *
	 * @param code
	 *            交易代码 登记操作是“s4846”; 查询操作是“s0810”;
	 * @param addr
	 *            填空;
	 * @param acctlock
	 *            填空;
	 * @param inptr
	 *            发送至服务端的数据;
	 * @param infile
	 *            发送至服务端的文件名,(文件存放在指定目录下面).暂可填空;
	 * @param outptr
	 *            接收服务端的返回数据;
	 * @param outfile
	 *            服务端返回文件(存放在指定目录下).暂为空;
	 * @return 0 表示成功;  1 表示失败;

	 */
	public native static synchronized String send_recv(String code, String addr,
			String acctlock, String inptr, String infile, String outptr,
			String outfile);

//	/**
//	 * 执行Easynet交易
//	 *
//	 * @param code
//	 *            交易代码 登记是“s4846”; 帐户查询是“s0810”
//	 * @param upMsg
//	 *            上传数据
//	 * @param downMsg
//	 *            下传数据
//	 * @return 0-成功 1-失败
//	 */
//	public int send_recv(String code, RequestMessageInfo upMsg,
//			RespondMessageInfo downMsg) {
//		if (upMsg.getInPtr() == null) {
//			upMsg.setInPtr(upMsg.fromFields());
//		}
//
//		System.out.println("UPMSG: " + upMsg.getInPtr());
//		String out = JEasyClient.send_recv(code, null, null, upMsg.getInPtr(),
//				upMsg.getInFile(), downMsg.getOutPtr(), downMsg.getOutFile());
//
//		System.out.println("DOWNMSG: " + downMsg.getOutPtr());
//		/**
//		 * 取Easynet返回结果
//		 */
//		char ret = out.charAt(0);
//
//		int iRet = 0;
//		if (ret == '0') {
//			iRet = 1;
//		}
//
//		/**
//		 * 取交易返回结果
//		 */
//		if (iRet == 0) {
//			String mOut = out.substring(1);
//			downMsg.setOutPtr(mOut);
//			downMsg.toFields(mOut);
//		}
//
//		return (iRet);
//	}
//
//	/**
//	 * 测试
//	 *
//	 * @param argv
//	 * @throws Exception
//	 */
//	public static void main(String[] argv) throws Exception {
//		try {
//			JEasyClient client = new JEasyClient();
//
//			RequestMessageInfo upMsg = new RequestMessageInfo();
//
//			upMsg.putString("_jgm", "9400740101");
//			upMsg.putString("_jym", "4846");
//			upMsg.putString("_gyh", "11");
//			upMsg.putString("SSJY", "2604");
//			upMsg.putString("__ZH", "0001019000035015");
//			upMsg.putString("__XH", "0");
//
//			String jym = "s4846";
//			RespondMessageInfo downMsg = new RespondMessageInfo();
//
//			System.out.println("BEGIN UP.....WAITING DOWN....");
//
//			int ret = client.send_recv(jym, upMsg, downMsg);
//
//			String name = downMsg.getString("S004");
//			String balance = downMsg.getString("S006");
//
//			String hostcode = downMsg.getString("_hostcode");
//			String errmsg = downMsg.getString("_errmsg");
//			System.out.println("ret=[" + ret + "],name=[" + name
//					+ "],balance=[" + balance + "]" + "_hostcode " + hostcode
//					+ "_errmsg " + errmsg);
//		} catch (RuntimeException e) {
//			// Bingle_Will_Do 自动生成 catch 块
//			e.printStackTrace();
//		}
//
//	}
}