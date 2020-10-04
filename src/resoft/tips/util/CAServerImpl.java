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
                throw new CAServerException("key·��Ϊ��"+strPropFile+"�� ��ʼ������ǩ��ϵͳ���󣡴���ԭ��DSign.init() ���ش���");
        }
        catch(CAServerException cae)
        {
            throw cae;
        }
        catch(Exception ex)
        {
            _logger.error("��ʼ������ǩ��ϵͳ����", ex);
            throw new CAServerException("��ʼ������ǩ��ϵͳ���󣡴���ԭ��" + ex.getMessage());
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
                throw new CAServerException("ǩ������Ϊ��");
            if(data == null || data.length() == 0)
                throw new CAServerException("����֤ǩ��������Ϊ��");
            long lReturn = dSign.verifyDetachedSign(sign.getBytes(), data.getBytes());
            if(lReturn != 0L)
            {
                String msg = "��֤ǩ�������������:" + lReturn + ";����ԭ��:" + dSign.getErrorMessage();
                _logger.warn(msg + ";ԭ����:'" + data + "'ǩ��:'" + sign + "'");
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
            _logger.error("��֤ǩ������", ex);
            throw new CAServerException("��֤ǩ����������ԭ��" + ex.getMessage());
        }
    }

    public String VerifyDetachSign(String sign, InputStream is)
        throws CAServerException
    {
        try
        {
            if(sign == null || sign.length() == 0)
                throw new CAServerException("ǩ������Ϊ��");
            if(is == null)
                throw new CAServerException("����֤ǩ��������Ϊ��");
            long lReturn = dSign.verifyDetachedSign(sign.getBytes(), is);
            if(lReturn != 0L)
            {
                String msg = "��֤ǩ������������룺" + lReturn + "�� ����ԭ��" + dSign.getErrorMessage();
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
            _logger.error("��֤ǩ������", ex);
            throw new CAServerException("��֤ǩ����������ԭ��" + ex.getMessage());
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
            _logger.error("ǩ������!", ex);
            throw new CAServerException("ǩ����������ԭ��" + ex.getMessage());
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
            _logger.error("ǩ������", ex);
            throw new CAServerException("ǩ����������ԭ��" + ex.getMessage());
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
            _logger.error("ǩ������", ex);
            throw new CAServerException("ǩ����������ԭ��" + ex.getMessage());
        }
    }

    public String GetVerifyDetachSignInfo()
        throws CAServerException
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            sb.append("֤����Ϣ��");
            sb.append("\n     ֤�����⣺").append(dSign.getCertInfo("VS", 0, ""));
            sb.append("\n       �汾�ţ�").append(dSign.getCertInfo("VS", 3, ""));
            sb.append("\n       ���кţ�").append(dSign.getCertInfo("VS", 2, ""));
            sb.append("\n ��Ч��ʼ���ڣ�").append(dSign.getCertInfo("VS", 5, ""));
            sb.append("\n ��Ч��ֹ���ڣ�").append(dSign.getCertInfo("VS", 6, ""));
            sb.append("\n   �䷢�����⣺").append(dSign.getCertInfo("VS", 1, ""));
            sb.append("\n     �����ʼ���").append(dSign.getCertInfo("VS", 4, ""));
            return sb.toString();
        }
        catch(Throwable ex)
        {
            _logger.error("��ȡǩ��֤����Ϣ����!", ex);
            throw new CAServerException("��ȡǩ��֤����Ϣ��������ԭ��" + ex.getMessage());
        }
    }
}
