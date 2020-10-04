package resoft.tips.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.util.Base64;
import org.zz.gmhelper.SM2SignerUtil;

import resoft.basLink.Configuration;

public class SM2ServerImpl implements SignerFunction {

	private static Log _logger = LogFactory.getLog(SM2ServerImpl.class);

	static {
		Configuration conf = Configuration.getInstance();
		String keyType = conf.getProperty("SM2Server", "keyType");

		if (keyType.equalsIgnoreCase("pem")) {
			String pemKeyFile = conf.getProperty("SM2Server", "pemKey");
			try {
				SM2ServerImpl.init(pemKeyFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_logger.error("֤�����pem=" + pemKeyFile);
			}
		} else if (keyType.equalsIgnoreCase("cer")) {
			String cerKeyFile = conf.getProperty("SM2Server", "cerKey");
			String priKeyFile = conf.getProperty("SM2Server", "priKey");

			try {
				SM2ServerImpl.init(cerKeyFile, priKeyFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_logger.error("֤�����cer=" + cerKeyFile + " pri=" + priKeyFile);
			}
		}
	}

	private static void init(String pemFile) throws Exception {
		SM2SignerUtil.initPem(pemFile);
	}

	private static void init(String cerKeyFile, String priKeyFile) throws Exception {
		SM2SignerUtil.initCer(cerKeyFile, priKeyFile);
	}

	@Override
	public boolean VerifyDetachSign(String sign, String data) throws CAServerException {
		// TODO Auto-generated method stub
		try {			if (sign == null || sign.length() == 0)
				throw new CAServerException("ǩ������Ϊ��");
			if (data == null || data.length() == 0)
				throw new CAServerException("����֤ǩ��������Ϊ��");
			boolean ret = SM2SignerUtil.verifyDetachedSign(Base64.decodeBase64(sign), data.getBytes("GBK"));
			if (!ret) {
				String msg = "��֤ǩ������";
				_logger.warn(msg + ";ԭ����:'" + data + "'ǩ��:'" + sign + "'");
				return false;
			} else {
				return true;
			}
		} catch (CAServerException cae) {
			throw cae;
		} catch (Exception ex) {
			_logger.error("��֤ǩ������", ex);
			throw new CAServerException("��֤ǩ����������ԭ��" + ex.getMessage());
		}
	}

	@Override
	public String DetachSign(String data) throws CAServerException {
		// TODO Auto-generated method stub
		try {
			byte[] sign = SM2SignerUtil.detachSign(data.getBytes("GBK"));
			return Base64.encodeBase64String(sign);
		} catch (Throwable ex) {
			_logger.error("ǩ������!", ex);
			throw new CAServerException("ǩ����������ԭ��" + ex.getMessage());
		}
	}

}
