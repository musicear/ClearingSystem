package resoft.tips.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.Configuration;

/**
 * <p/>
 * ǩ����
 * </p>
 * User: tangzhiyu Date: 2007-07-17 Time: 14:23:20
 */
public class Signer {
	private static final Log logger = LogFactory.getLog(Signer.class);

	private static Signer instance = new Signer();

	private static SignerFunction signerImpl = null;

	static {
		Configuration conf = Configuration.getInstance();
		String className = conf.getProperty("general", "SignerImpl");
		try {
			signerImpl = (SignerFunction) Class.forName(className).newInstance();
		} catch (Exception e) {
			logger.error("�����ࣺ" + className + "ʧ��", e);
		}
	}

	private Signer() {
	}

	/**
	 * �õ�Ψһʵ��
	 */
	public static Signer getInstance() {
		return instance;
	}

	/**
	 * ��������ԭ�ĵ�����ǩ��
	 */
	public String detachSign(String xml) {

		int iEnd = xml.lastIndexOf("</CFX>");
		if (iEnd < 0) {
			StringBuffer msg = new StringBuffer();
			msg.append("���ұ��������,End:").append(iEnd);
			logger.error(msg.toString());
			throw new CAServerException(msg.toString());
		}
		String data = xml.substring(0, iEnd + 6);
		String sign;
		sign = signerImpl.DetachSign(data);
		return sign;
	}

	/**
	 * У��ǩ��
	 */
	public boolean verifySign(String xml) {

		String signData;
		int dataEndIndex = xml.lastIndexOf("</CFX>");
		int signBeginIndex = xml.lastIndexOf("<!--");
		if (signBeginIndex < 0) {
			// ����û��ǩ����Ϣ
			return true;
		}
		int signEndIndex = xml.lastIndexOf("-->");
		signData = xml.substring(signBeginIndex + 4, signEndIndex);

		if (dataEndIndex < 0) {
			StringBuffer msg = new StringBuffer();
			msg.append("���ұ��������,End:").append(dataEndIndex);
			logger.error(msg.toString());
			throw new CAServerException(msg.toString());
		}
		String data = xml.substring(0, dataEndIndex + 6);

		return signerImpl.VerifyDetachSign(signData, data);
	}

	private static String readFromFile(String fileName , String code )
	{

		String str = "";

		File file = new File(fileName);

		try {

			FileInputStream in = new FileInputStream(file);

			// size Ϊ�ִ��ĳ��� ������һ���Զ���

			int size = in.available();

			byte[] buffer = new byte[size];

			in.read(buffer);

			in.close();

			str = new String(buffer, code );

		} catch (IOException e) {

			// TODO Auto-generated catch block

			return null;
		}
		return str;
	}

	public static void main(String[] args) throws IOException {
		String xml;
		boolean isVerfiy = true;
		if (args.length < 2) {
			System.out.println("Usage: " + Signer.class.getName() + " [s|v] filename");
			return;
		}

		isVerfiy = args[0].equalsIgnoreCase("v");
		xml = readFromFile(args[1], "gbk");
		if (!isVerfiy) {
			String sign = getInstance().detachSign(xml);
			System.out.println("xml:\n" + xml);
			System.out.println("signed data:\n" + sign);
		} else {
			System.out.println("xml:\n" + xml);
			System.out.println("verfied result:\n" + getInstance().verifySign(xml));
		}
	}
}
