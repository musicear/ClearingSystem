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
 * <p>主控入口</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:58:09
 */
public class Controller {
    private static Log logger = LogFactory.getLog(Controller.class);

    /**
     * 执行交易
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
     * 读取报文，并进行交易处理
     * */
    public byte[] execute(byte[] packet) {
        Packager packager = ThreadLocalContext.getInstance().getContext().getPacker();
        if(packager ==null) {
            throw new RuntimeException("未设置打包器");
        }

        byte[] respPacket;
        Message msg = new DefaultMessage();
        try {
            msg = packager.unpack(packet);
            execute(msg);
            respPacket = packager.pack(msg);
        } catch(PackException e) {
            logger.error("打包错误:" + e.getMessage(),e);
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
     * 设置表示交易码的名字。如“交易码”或叫“业务类型”等等。标识不同交易的一个名称
     * */
    public void setNameOfTransCode(String nameOfTransCode) {
        this.nameOfTransCode = nameOfTransCode;
    }

    /**
     * 设置系统内部错误处理的Action
     * */
    public void setInternalErrorAction(Action internalErrorAction) {
        this.internalErrorAction = internalErrorAction;
    }
    /**
     * 设置全局属性。用来替换流程描述中的${}
     * */
    public void setGlobalProperties(Map globalProperties) {
        this.globalProperties = globalProperties;
    }

    /**
     * 设置全部属性读取实现类
     * */
    public void setGlobalPropertiesReader(String className) {
        try {
            GlobalPropertiesReader reader = (GlobalPropertiesReader) Class.forName(className).newInstance();
            globalProperties = reader.getAllProperties();
        } catch (Exception e) {
            logger.error("加载全局属性读取实现类失败：" + className,e);
        }
    }

    private Action internalErrorAction = new InternalErrorAction();

    private String nameOfTransCode;

    private Map globalProperties = new HashMap();


}
