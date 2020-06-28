package com.easycon.platform.net;

//import com.resoft.recis.bank.access.message.dao.RequestMessageInfo;
//import com.resoft.recis.bank.access.message.dao.RespondMessageInfo;

/**
 * ���ܣ��������ں���ϵͳ
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
	 * ���ö�̬���ӿ�jeasyclient
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
	 * ����յĲ��ù��ģ���С��ϵͳ�Ĺ涨
	 *
	 * @param code
	 *            ���״��� �Ǽǲ����ǡ�s4846��; ��ѯ�����ǡ�s0810��;
	 * @param addr
	 *            ���;
	 * @param acctlock
	 *            ���;
	 * @param inptr
	 *            ����������˵�����;
	 * @param infile
	 *            ����������˵��ļ���,(�ļ������ָ��Ŀ¼����).�ݿ����;
	 * @param outptr
	 *            ���շ���˵ķ�������;
	 * @param outfile
	 *            ����˷����ļ�(�����ָ��Ŀ¼��).��Ϊ��;
	 * @return 0 ��ʾ�ɹ�;  1 ��ʾʧ��;

	 */
	public native static synchronized String send_recv(String code, String addr,
			String acctlock, String inptr, String infile, String outptr,
			String outfile);

//	/**
//	 * ִ��Easynet����
//	 *
//	 * @param code
//	 *            ���״��� �Ǽ��ǡ�s4846��; �ʻ���ѯ�ǡ�s0810��
//	 * @param upMsg
//	 *            �ϴ�����
//	 * @param downMsg
//	 *            �´�����
//	 * @return 0-�ɹ� 1-ʧ��
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
//		 * ȡEasynet���ؽ��
//		 */
//		char ret = out.charAt(0);
//
//		int iRet = 0;
//		if (ret == '0') {
//			iRet = 1;
//		}
//
//		/**
//		 * ȡ���׷��ؽ��
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
//	 * ����
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
//			// Bingle_Will_Do �Զ����� catch ��
//			e.printStackTrace();
//		}
//
//	}
}