package resoft.basLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * function: ģ������
 * User: albert lee
 * Date: 2005-9-26
 * Time: 9:52:10
 * todo �ڹ���������������finally.����label������һ����,��ʶ�Ƿ�Ϊfinally
 */
public class TransactionFlow {
    private static Log logger = LogFactory.getLog(TransactionFlow.class);

    public TransactionFlow() {
    }

    /**
     * ���һ��label.
     *
     * */    
    public void addLabel(String name) {
        labels.put(name,new Integer(activities.size() - 1));
    }

    /**
     * ����"�"
     * */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }
    /**
     * ������κ���ת��Activity
     * */
    public void addActivity(String className) {
        Activity act = new Activity(className);
        addActivity(act);
    }
    /**
     * ����Transaction������
     * */
    public void setTransactionInterceptors(List interceptors) {
        this.transactionInterceptors = interceptors;
    }
    /**
     * ����Module������
     * */
    public void setModuleInterceptors(List interceptors) {
        this.moduleInterceptors = interceptors;
    }

    /**
     * ִ������
     * */
    public void execute(Message msg) {
        boolean executeTransaction = true;
        int index = 0;
        //ִ��before
        for(index = 0;index<transactionInterceptors.size();index++) {
            TransactionInterceptor interceptor = (TransactionInterceptor) transactionInterceptors.get(index);
            executeTransaction = interceptor.before(msg);
            if(!executeTransaction) {
                break;
            }
        }
        //ִ��transaction
        if(executeTransaction) {
            executeInternal(msg);
        }
        //ִ��afterTransaction
        index --;
        for(;index>=0;index--) {
            TransactionInterceptor interceptor = (TransactionInterceptor) transactionInterceptors.get(index);
            interceptor.after(msg);
        }
    }

    /**
     * ִ��Module����
     * */
    private void executeInternal(Message msg) {
        //@todo how to detect dead loop?
        int index = 0;
        Activity act = (Activity) activities.get(index);

        //
        while(act!=null) {
            ModuleBase module = ModuleManager.getInstance().findModule(act.getName());
            try {

                //call interceptor 's before
                for(Iterator itr = moduleInterceptors.iterator();itr.hasNext();) {
                    ModuleInterceptor interceptor = (ModuleInterceptor) itr.next();
                    boolean continueFlag = interceptor.beforeModule(module,msg);
                    if(!continueFlag) {
                        break;
                    }
                }
                int retValue = module.execute(msg);
                //call interceptor's after
                for(int i = moduleInterceptors.size() - 1;i>=0;i--) {
                    ModuleInterceptor interceptor = (ModuleInterceptor) moduleInterceptors.get(i);
                    interceptor.afterModule(module,msg,retValue);
                }

                String on = Integer.toString(retValue);
                String to = act.getTransitionTo(on);
                if(to.equals("")) {
                    //������ֵ��������δ����,��Ĭ������ִ��
                    index ++;
                    if(index>=activities.size()) {
                        //ִ�����
                        break;
                    } else {
                        act = (Activity) activities.get(index);
                    }
                } else {
                    Integer temp = (Integer) labels.get(to);
                    if(temp==null) {
                        logger.error("δ�ҵ�label:" + to);
                        break;
                    } else {
                        //��ת��label
                        index = temp.intValue() + 1;
                        if(index>=activities.size()) {
                            break;
                        }
                        act = (Activity) activities.get(index);
                    }

                }
            } catch (Exception e) {
                logger.error("Moduleִ�д���:" + module.getClass().getName(),e);
                msg.setValue("��Ӧ��","999");
                msg.setValue("��Ӧ����","BasLinkִ�д���");
                break;
            }
        }
    }


    private List activities = new ArrayList();

    private Map labels = new HashMap();

    private List moduleInterceptors = new ArrayList();

    private List transactionInterceptors = new ArrayList();

    /** @link dependency */
    /*# Activity lnkActivity; */
}
