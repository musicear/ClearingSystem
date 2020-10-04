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
			_logger.error("签名服务器错误！prop=" + propFile);
		}
	}

	@Override
	public boolean VerifyDetachSign(String signedText, String data) throws CAServerException {
		// TODO Auto-generated method stub
		try {
			if (signedText == null || signedText.length() == 0)
				throw new CAServerException("签名数据为空");
			if (data == null || data.length() == 0)
				throw new CAServerException("需验证签名的数据为空");
			
			// base64编码的TSA
			String tsaText = null;
			// 是否需要返回签名证书
			boolean needCert = false;
			// 验签名
			NetSignResult result = NetSignAgent.detachedVerify( data.getBytes(), signedText , tsaText , needCert );
			return true;
		} catch (Exception ex) {
			_logger.error("验证签名出错！", ex);
			System.out.println( "errorMsg:"+ ex.getMessage() );
			return false;
		}
	}

	@Override
	public String DetachSign(String data) throws CAServerException {
		// TODO Auto-generated method stub

		try {
			// 签名原文
			byte[] laintext = data.getBytes("gbk");
			// 签名证书DN，null表示用服务器默认证书进行签名
			String subject = null;
			// 摘要算法，null表示用服务器默认的摘要算法
			String digestAlg = null;
			// 是否做TSA签名
			boolean useTSA = false;
			// 签名
			NetSignResult result = NetSignAgent.detachedSignature(laintext, subject, digestAlg, useTSA);
			// 获取base64编码后的签名结果
			return result.getStringResult(NetSignResult.SIGN_TEXT);
			}
			catch (Exception ex) {
			ex.printStackTrace();
			_logger.error("签名出错!", ex);
			throw new CAServerException("签名出错！错误原因：" + ex.getMessage());
		}
	}
}
