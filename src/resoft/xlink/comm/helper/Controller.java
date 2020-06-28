package resoft.xlink.comm.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.comm.GlobalPropertiesReader;
import resoft.xlink.comm.PackException;
import resoft.xlink.comm.Packager;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;
import resoft.xlink.impl.DefaultTransactionExecutor;
import resoft.xlink.impl.DefaultTransactionManager;
import resoft.xlink.impl.InternalErrorAction;
import resoft.xlink.transaction.Transaction;
import resoft.xlink.transaction.TransactionExecutor;
import resoft.xlink.transaction.TransactionManager;
import resoft.xlink.transaction.TransactionNotExistsException;

/**
 * <p>�������</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:58:09
 */
public class Controller {
    private static Log logger = LogFactory.getLog(Controller.class);

    /**
     * ִ�н���
     * */
    public void execute(Message msg) {
        TransactionManager transManager = new DefaultTransactionManager();
        transManager.setGlobalProperties(globalProperties);
        Transaction trans = transManager.getTransaction((String) msg.get(nameOfTransCode));

        if(trans !=null) {
            TransactionExecutor transExecutor = new DefaultTransactionExecutor();
            transExecutor.execute(trans,msg);
        } else {
            msg.set("exception",new TransactionNotExistsException(""));
            try {
                internalErrorAction.execute(msg);
            } catch (Exception e) {
            }
        }
    }

    /**
     * ��ȡ���ģ������н��״���
     * */
    public byte[] execute(byte[] packet) {
        Packager packager = ThreadLocalContext.getInstance().getContext().getPacker();
        if(packager ==null) {
            throw new RuntimeException("δ���ô����");
        }

        byte[] respPacket;
        Message msg = new DefaultMessage();
        try {
            msg = packager.unpack(packet);
            execute(msg);
            respPacket = packager.pack(msg);
        } catch(PackException e) {
            logger.error("�������:" + e.getMessage(),e);
            msg.set("exception",e);
            try {
                internalErrorAction.execute(msg);
            } catch (Exception e1) {
            }
            respPacket = packager.pack(msg);
        }
        return respPacket;
    }

    /**
     * ���ñ�ʾ����������֡��硰�����롱��С�ҵ�����͡��ȵȡ���ʶ��ͬ���׵�һ������
     * */
    public void setNameOfTransCode(String nameOfTransCode) {
        this.nameOfTransCode = nameOfTransCode;
    }

    /**
     * ����ϵͳ�ڲ��������Action
     * */
    public void setInternalErrorAction(Action internalErrorAction) {
        this.internalErrorAction = internalErrorAction;
    }
    /**
     * ����ȫ�����ԡ������滻���������е�${}
     * */
    public void setGlobalProperties(Map globalProperties) {
        this.globalProperties = globalProperties;
    }

    /**
     * ����ȫ�����Զ�ȡʵ����
     * */
    public void setGlobalPropertiesReader(String className) {
        try {
            GlobalPropertiesReader reader = (GlobalPropertiesReader) Class.forName(className).newInstance();
            globalProperties = reader.getAllProperties();
        } catch (Exception e) {
            logger.error("����ȫ�����Զ�ȡʵ����ʧ�ܣ�" + className,e);
        }
    }

    private Action internalErrorAction = new InternalErrorAction();

    private String nameOfTransCode;

    private Map globalProperties = new HashMap();


}
