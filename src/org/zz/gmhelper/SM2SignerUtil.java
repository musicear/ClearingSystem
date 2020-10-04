package org.zz.gmhelper;

import java.io.FileReader;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.zz.gmhelper.cert.SM2CertUtil;
import org.zz.gmhelper.test.util.FileUtil;

public class SM2SignerUtil extends GMBaseUtil {

	private static BCECPublicKey pubKey;
	private static ECPrivateKeyParameters priKeyParameters;

	public static boolean verifyDetachedSign(byte[] signData, byte[] orgData) throws Exception {
		return SM2Util.verify(pubKey, orgData, signData);
	}

	public static byte[] detachSign(byte[] data) throws Exception {
		byte[] sign = SM2Util.sign(priKeyParameters, data);
		return sign;
	}

	public static void initCer(String cerKeyPath, String priKeyPath) throws Exception {
		X509Certificate cert = SM2CertUtil.getX509Certificate(cerKeyPath);
		pubKey = SM2CertUtil.getBCECPublicKey(cert);
		byte[] priKeyData = FileUtil.readFile(priKeyPath);
		priKeyParameters = BCECUtil.convertSEC1ToECPrivateKey(priKeyData);

		return;
	}

	public static void initPem(String pemFilePath) throws Exception {
		PEMParser pemParser = new PEMParser(new FileReader(pemFilePath));
		Object object;

		while ((object = pemParser.readObject()) != null) {
			if (object instanceof X509CertificateHolder) {
				X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC")
						.getCertificate((X509CertificateHolder) object);
				pubKey = SM2CertUtil.getBCECPublicKey(cert);
			} else if (object instanceof PEMKeyPair) {
				KeyPair kp = new JcaPEMKeyConverter().setProvider("BC").getKeyPair((PEMKeyPair) object);

				priKeyParameters = BCECUtil.convertPrivateKeyToParameters((BCECPrivateKey) kp.getPrivate());
			} else if (object instanceof SubjectPublicKeyInfo) {
				SubjectPublicKeyInfo keyinfo = (SubjectPublicKeyInfo) object;
				pubKey = BCECUtil.createPublicKeyFromSubjectPublicKeyInfo(keyinfo);
			} else {
				System.out.println("unknown pem object:" + object.getClass().getName());
			}
		}
		pemParser.close();
	}

}