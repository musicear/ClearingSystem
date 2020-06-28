// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Controller.java

package test;

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
import resoft.xlink.transaction.TransactionExecutor;
import resoft.xlink.transaction.TransactionManager;
import resoft.xlink.transaction.TransactionNotExistsException;

// Referenced classes of package resoft.xlink.comm.helper:
//            ThreadLocalContext, Context

public class Controller
{

    public Controller()
    {
        internalErrorAction = new InternalErrorAction();
        globalProperties = new HashMap();
    }

    public void execute(Message msg)
    {
    	logger.info(msg.get(nameOfTransCode));
        TransactionManager transManager = new DefaultTransactionManager();
        if(globalProperties == null)
            System.out.println(" the globalProperties is null..........................");
        transManager.setGlobalProperties(globalProperties);
        resoft.xlink.transaction.Transaction trans = transManager.getTransaction((String)msg.get(nameOfTransCode));
        if(trans != null)
        {
            TransactionExecutor transExecutor = new DefaultTransactionExecutor();
            transExecutor.execute(trans, msg);
        } else
        {
            msg.set("exception", new TransactionNotExistsException(""));
            try
            { 
                internalErrorAction.execute(msg);
            }
            catch(Exception e) { }
        }
    }

    public byte[] execute(byte packet[])
    {
        Packager packager = ThreadLocalContext.getInstance().getContext().getPacker();
        if(packager == null)
            throw new RuntimeException("\u672A\u8BBE\u7F6E\u6253\u5305\u5668");
        Message msg = new DefaultMessage();
        byte respPacket[];
        try
        {
            msg = packager.unpack(packet);
            execute(msg);
            respPacket = packager.pack(msg);
        }
        catch(PackException e)
        {
            logger.error("\u6253\u5305\u9519\u8BEF:" + e.getMessage(), e);
            msg.set("exception", e);
            try
            {
                internalErrorAction.execute(msg);
            }
            catch(Exception e1) { }
            respPacket = packager.pack(msg);
        }
        return respPacket;
    }

    public void setNameOfTransCode(String nameOfTransCode)
    {
        this.nameOfTransCode = nameOfTransCode;
    }

    public void setInternalErrorAction(Action internalErrorAction)
    {
        this.internalErrorAction = internalErrorAction;
    }

    public void setGlobalProperties(Map globalProperties)
    {
        this.globalProperties = globalProperties;
    }

    public void setGlobalPropertiesReader(String className)
    {
        try
        {
            GlobalPropertiesReader reader = (GlobalPropertiesReader)Class.forName(className).newInstance();
            globalProperties = reader.getAllProperties();
        }
        catch(Exception e)
        {
            logger.error("\u52A0\u8F7D\u5168\u5C40\u5C5E\u6027\u8BFB\u53D6\u5B9E\u73B0\u7C7B\u5931\u8D25\uFF1A" + className, e);
        }
    }
 

    private static Log logger;
    private Action internalErrorAction;
    private String nameOfTransCode;
    private Map globalProperties;

    static 
    {
        logger = LogFactory.getLog(resoft.xlink.comm.helper.Controller.class);
    }
}
