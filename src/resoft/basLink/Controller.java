package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.packer.CfxPacker;

/**
 * 主控入口
 * */

public class Controller {
    private static Log logger = LogFactory.getLog(Controller.class);

    /**
     * 私有构造函数
     * */
    private Controller(){       
    }

    /**
     * 执行交易
     * */
    public void execute(Message msg) {
        TransactionFlow transFlow = TransactionFlowManager.getInstance().getTransactionFlow(msg.getValue("交易码"));
        
        if(transFlow!=null) {
            transFlow.execute(msg);
        } else {
            msg.setValue("响应码","999");
            msg.setValue("响应描述","交易不存在");            
        }
    }

    /**
     * 读取报文，并进行交易处理
     * */
    public String execute(String str) {
        Packer packer = ThreadLocalContext.getInstance().getContext().getPacker();
        if(packer==null) {
            packer = new CfxPacker();
        }
        
        String returnXml = null;
        try {
            Message msg = packer.pack(str.getBytes());
            execute(msg);
            returnXml = new String(packer.unpackByFile(msg));
        } catch(PackException e) {
            logger.error("打包错误:" + e.getMessage(),e);
            Message msg = new Message();
            msg.setValue("响应码","905");
            msg.setValue("响应描述","数据包错误");
            returnXml = new String(packer.unpack(msg));
        }
        return returnXml;
    }

    public static Controller getInstance(){
        return instance;
    }

    /** @link dependency */
    /*# resoft.basLink.Packer lnkPacker; */

    /** @link dependency */
    /*# resoft.basLink.Message lnkMessage; */

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory 
     */
    /*# private resoft.basLink.Controller _controller; */
    private static Controller instance = new Controller();
}
