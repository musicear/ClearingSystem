package resoft.tips.util;

public interface SignerFunction {
	 public boolean VerifyDetachSign(String sign, String data)
		        throws CAServerException;
	 public String DetachSign(String data)
		        throws CAServerException;
}
