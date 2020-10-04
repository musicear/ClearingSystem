package resoft.tips.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.infosec.netsign.agent.NetSignAgent;
import cn.com.infosec.netsign.agent.NetSignResult;
import resoft.basLink.Configuration;

public class NetSignServerImpl implements SignerFunction {

	private static Log _logger = LogFactory.getLog(NetSignServerImpl.class);

	static {
		Configuration conf = Configuration.getInstance();
		String propFile = conf.getProperty("NetSignServer", "propfile");
		
		try {
			NetSignAgent.initialize(propFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_logger.error("ǩ������������prop=" + propFile);
		}
	}

	@Override
	public boolean VerifyDetachSign(String signedText, String data) throws CAServerException {
		// TODO Auto-generated method stub
		try {
			if (signedText == null || signedText.length() == 0)
				throw new CAServerException("ǩ������Ϊ��");
			if (data == null || data.length() == 0)
				throw new CAServerException("����֤ǩ��������Ϊ��");
			
			// base64�����TSA
			String tsaText = null;
			// �Ƿ���Ҫ����ǩ��֤��
			boolean needCert = false;
			// ��ǩ��
			NetSignResult result = NetSignAgent.detachedVerify( data.getBytes(), signedText , tsaText , needCert );
			return true;
		} catch (Exception ex) {
			_logger.error("��֤ǩ������", ex);
			System.out.println( "errorMsg:"+ ex.getMessage() );
			return false;
		}
	}

	@Override
	public String DetachSign(String data) throws CAServerException {
		// TODO Auto-generated method stub

		try {
			// ǩ��ԭ��
			byte[] laintext = data.getBytes("gbk");
			// ǩ��֤��DN��null��ʾ�÷�����Ĭ��֤�����ǩ��
			String subject = null;
			// ժҪ�㷨��null��ʾ�÷�����Ĭ�ϵ�ժҪ�㷨
			String digestAlg = null;
			// �Ƿ���TSAǩ��
			boolean useTSA = false;
			// ǩ��
			NetSignResult result = NetSignAgent.detachedSignature(laintext, subject, digestAlg, useTSA);
			// ��ȡbase64������ǩ�����
			return result.getStringResult(NetSignResult.SIGN_TEXT);
			}
			catch (Exception ex) {
			ex.printStackTrace();
			_logger.error("ǩ������!", ex);
			throw new CAServerException("ǩ����������ԭ��" + ex.getMessage());
		}
	}
}
