package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.packer.CfxPacker;

/**
 * �������
 * */

public class Controller {
    private static Log logger = LogFactory.getLog(Controller.class);

    /**
     * ˽�й��캯��
     * */
    private Controller(){       
    }

    /**
     * ִ�н���
     * */
    public void execute(Message msg) {
        TransactionFlow transFlow = TransactionFlowManager.getInstance().getTransactionFlow(msg.getValue("������"));
        
        if(transFlow!=null) {
            transFlow.execute(msg);
        } else {
            msg.setValue("��Ӧ��","999");
            msg.setValue("��Ӧ����","���ײ�����");            
        }
    }

    /**
     * ��ȡ���ģ������н��״���
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
            logger.error("�������:" + e.getMessage(),e);
            Message msg = new Message();
            msg.setValue("��Ӧ��","905");
            msg.setValue("��Ӧ����","���ݰ�����");
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
