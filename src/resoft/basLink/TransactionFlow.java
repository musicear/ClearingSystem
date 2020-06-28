package resoft.basLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * function: 模块序列
 * User: albert lee
 * Date: 2005-9-26
 * Time: 9:52:10
 * todo 在工作流控制中增加finally.可在label中增加一属性,标识是否为finally
 */
public class TransactionFlow {
    private static Log logger = LogFactory.getLog(TransactionFlow.class);

    public TransactionFlow() {
    }

    /**
     * 添加一个label.
     *
     * */    
    public void addLabel(String name) {
        labels.put(name,new Integer(activities.size() - 1));
    }

    /**
     * 增加"活动"
     * */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }
    /**
     * 添加无任何跳转的Activity
     * */
    public void addActivity(String className) {
        Activity act = new Activity(className);
        addActivity(act);
    }
    /**
     * 设置Transaction拦截器
     * */
    public void setTransactionInterceptors(List interceptors) {
        this.transactionInterceptors = interceptors;
    }
    /**
     * 设置Module拦截器
     * */
    public void setModuleInterceptors(List interceptors) {
        this.moduleInterceptors = interceptors;
    }

    /**
     * 执行序列
     * */
    public void execute(Message msg) {
        boolean executeTransaction = true;
        int index = 0;
        //执行before
        for(index = 0;index<transactionInterceptors.size();index++) {
            TransactionInterceptor interceptor = (TransactionInterceptor) transactionInterceptors.get(index);
            executeTransaction = interceptor.before(msg);
            if(!executeTransaction) {
                break;
            }
        }
        //执行transaction
        if(executeTransaction) {
            executeInternal(msg);
        }
        //执行afterTransaction
        index --;
        for(;index>=0;index--) {
            TransactionInterceptor interceptor = (TransactionInterceptor) transactionInterceptors.get(index);
            interceptor.after(msg);
        }
    }

    /**
     * 执行Module序列
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
                    //若返回值在流程中未定义,则默认向下执行
                    index ++;
                    if(index>=activities.size()) {
                        //执行完毕
                        break;
                    } else {
                        act = (Activity) activities.get(index);
                    }
                } else {
                    Integer temp = (Integer) labels.get(to);
                    if(temp==null) {
                        logger.error("未找到label:" + to);
                        break;
                    } else {
                        //跳转到label
                        index = temp.intValue() + 1;
                        if(index>=activities.size()) {
                            break;
                        }
                        act = (Activity) activities.get(index);
                    }

                }
            } catch (Exception e) {
                logger.error("Module执行错误:" + module.getClass().getName(),e);
                msg.setValue("响应码","999");
                msg.setValue("响应描述","BasLink执行错误");
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
