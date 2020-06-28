package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;

/**
 * function: ����ͨѶ�Ķ���
 * //@todo �Ƿ���Լ�������Ƿ�ʱ��Ȼ��ر�֮�������������ʱ�����׳���
 * User: albert lee
 * Date: 2005-9-26
 * Time: 23:26:59
 */
public class Pipeline {
    private static Log logger = LogFactory.getLog(Pipeline.class);
    private static final int Wait_Time = 5000;
    private Pipeline() {
        reqQueue = new LinkedQueue();
        resQueue = new LinkedQueue();
    }

    public static Pipeline getInstance() {
        return instance;
    }
    /**
     * ��������
     * */
    public void offerRequest(Context context,byte[] bytes) {
        try {
            reqQueue.offer(new CommData(context,bytes),Wait_Time);
//            this.notifyAll();
        } catch (InterruptedException e) {
            logger.error("offerRequest error",e);
        }
    }
    /**
     * �Ƿ�����Ӧ����
     * */
    public boolean isRequestEmpty() {
        return reqQueue.isEmpty();
    }
    /**
     * ȡ����������
     * */
    public CommData takeRequest() {
        try {


//            if(isRequestEmpty()) {
//                this.wait();
//            }

            CommData data = (CommData) reqQueue.take();
            ThreadLocalContext.getInstance().setContext(data.getContext());
            return data;
        } catch (InterruptedException e) {
            logger.error("takeRequest error",e);
        }
        return null;
    }
    /**
     * �ṩ��Ӧ����
     * */
    public void offerResponse(Context context,byte[] bytes) {
        try {
            resQueue.offer(new CommData(context,bytes),Wait_Time);

            //this.notifyAll();
        } catch (InterruptedException e) {
            logger.error("offerResponse error",e);
        }
    }
    /**
     * �Ƿ�����Ӧ����
     * */
    public boolean isResponseEmpty() {
        return resQueue.isEmpty();
    }
    /**
     * ȡ����Ӧ����
     * */
    public CommData takeResponse() {
        try {
            CommData data = (CommData) resQueue.take();
            ThreadLocalContext.getInstance().setContext(data.getContext());
            return data;
        } catch (InterruptedException e) {
            logger.error("takeResponse error",e);
        }
        return null;
    }


    private static Pipeline instance = new Pipeline();

    private LinkedQueue reqQueue;//���ڴ洢��������
    private LinkedQueue resQueue;//���ڴ洢����ɹ�������
}
