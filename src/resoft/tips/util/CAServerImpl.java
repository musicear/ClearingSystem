package resoft.tips.util;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.jit.assp.dsign.DSign;
import resoft.basLink.Configuration;


public class CAServerImpl implements SignerFunction 
{

    private static Log _logger =  LogFactory.getLog(CAServerImpl.class);;
    private static String DN = null;
    private DSign dSign;

    static {
    	   	Configuration conf = Configuration.getInstance();
         	String strPropFile=conf.getProperty("CAServeer", "keyPath");
         	String _DN=conf.getProperty("CAServeer", "DN");
         	CAServerImpl.init(strPropFile,  _DN);
	}
    
    private static void init(String strPropFile, String _DN)
        throws CAServerException
    {
        init(strPropFile);
        setDN(_DN);
    }

    public static void setDN(String _DN)
    {
        DN = _DN;
    }

    public static void init(String strPropFile)
        throws CAServerException
    {
        try
        {
            if(!DSign.init(strPropFile)) 
                throw new CAServerException("key路径为："+strPropFile+"。 初始化数字签名系统错误！错误原因：DSign.init() 返回错误");
        }
        catch(CAServerException cae)
        {
            throw cae;
        }
        catch(Exception ex)
        {
            _logger.error("初始化数字签名系统错误！", ex);
            throw new CAServerException("初始化数字签名系统错误！错误原因：" + ex.getMessage());
        }
    }

    public CAServerImpl()
    {
    	
        dSign = new DSign();
       
    }

    public boolean VerifyDetachSign(String sign, String data)
        throws CAServerException
    {
        try
        {
            if(sign == null || sign.length() == 0)
                throw new CAServerException("签名数据为空");
            if(data == null || data.length() == 0)
                throw new CAServerException("需验证签名的数据为空");
            long lReturn = dSign.verifyDetachedSign(sign.getBytes(), data.getBytes());
            if(lReturn != 0L)
            {
                String msg = "验证签名出错，错误代码:" + lReturn + ";错误原因:" + dSign.getErrorMessage();
                _logger.warn(msg + ";原数据:'" + data + "'签名:'" + sign + "'");
                return false;
            } else
            {
                String str = dSign.getCertInfo("VS", 0, "");
                _logger.info( "VS=" + str );
                return true;
            }
        }
        catch(CAServerException cae)
        {
            throw cae;
        }
        catch(Exception ex)
        {
            _logger.error("验证签名出错！", ex);
            throw new CAServerException("验证签名出错！错误原因：" + ex.getMessage());
        }
    }

    public String VerifyDetachSign(String sign, InputStream is)
        throws CAServerException
    {
        try
        {
            if(sign == null || sign.length() == 0)
                throw new CAServerException("签名数据为空");
            if(is == null)
                throw new CAServerException("需验证签名的数据为空");
            long lReturn = dSign.verifyDetachedSign(sign.getBytes(), is);
            if(lReturn != 0L)
            {
                String msg = "验证签名出错，错误代码：" + lReturn + "； 错误原因：" + dSign.getErrorMessage();
                throw new CAServerException(msg, lReturn);
            } else
            {
                String str = dSign.getCertInfo("VS", 0, "");
                return str;
            }
        }
        catch(CAServerException cae)
        {
            throw cae;
        }
        catch(Exception ex)
        {
            _logger.error("验证签名出错！", ex);
            throw new CAServerException("验证签名出错！错误原因：" + ex.getMessage());
        }
    }

    public String DetachSign(String data)
        throws CAServerException
    {
        try
        {
            String sign = dSign.detachSign(DN, data.getBytes( "GBK"));
            return sign;
        }
        catch(Throwable ex)
        {
            _logger.error("签名出错!", ex);
            throw new CAServerException("签名出错！错误原因：" + ex.getMessage());
        }
    }

    public String DetachSign(byte data[])
        throws CAServerException
    {
        try
        {
            String sign = dSign.detachSign(DN, data);
            return sign;
        }
        catch(Throwable ex)
        {
            _logger.error("签名出错！", ex);
            throw new CAServerException("签名出错！错误原因：" + ex.getMessage());
        }
    }

    public String DetachSign(InputStream is)
        throws CAServerException
    {
        try
        {
            String sign = dSign.detachSign(DN, is);
            return sign;
        }
        catch(Throwable ex)
        {
            _logger.error("签名出错！", ex);
            throw new CAServerException("签名出错！错误原因：" + ex.getMessage());
        }
    }

    public String GetVerifyDetachSignInfo()
        throws CAServerException
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            sb.append("证书信息：");
            sb.append("\n     证书主题：").append(dSign.getCertInfo("VS", 0, ""));
            sb.append("\n       版本号：").append(dSign.getCertInfo("VS", 3, ""));
            sb.append("\n       序列号：").append(dSign.getCertInfo("VS", 2, ""));
            sb.append("\n 有效起始日期：").append(dSign.getCertInfo("VS", 5, ""));
            sb.append("\n 有效终止日期：").append(dSign.getCertInfo("VS", 6, ""));
            sb.append("\n   颁发者主题：").append(dSign.getCertInfo("VS", 1, ""));
            sb.append("\n     电子邮件：").append(dSign.getCertInfo("VS", 4, ""));
            return sb.toString();
        }
        catch(Throwable ex)
        {
            _logger.error("获取签名证书信息出错!", ex);
            throw new CAServerException("获取签名证书信息出错，错误原因：" + ex.getMessage());
        }
    }
}
