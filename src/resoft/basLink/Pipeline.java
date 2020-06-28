package resoft.basLink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;

/**
 * function: 用于通讯的队列
 * //@todo 是否可以检测连接是否超时？然后关闭之。服务器刚起的时候容易出现
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
     * 传送请求
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
     * 是否有响应数据
     * */
    public boolean isRequestEmpty() {
        return reqQueue.isEmpty();
    }
    /**
     * 取回请求数据
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
     * 提供响应数据
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
     * 是否有响应数据
     * */
    public boolean isResponseEmpty() {
        return resQueue.isEmpty();
    }
    /**
     * 取回响应数据
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

    private LinkedQueue reqQueue;//用于存储请求数据
    private LinkedQueue resQueue;//用于存储处理成功的数据
}
