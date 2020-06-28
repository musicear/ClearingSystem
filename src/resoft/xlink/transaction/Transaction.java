package resoft.xlink.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import resoft.xlink.core.Action;
import resoft.xlink.impl.InternalErrorAction;

/**
 * function: 交易
 * User: albert lee
 * Date: 2005-9-26
 * Time: 9:52:10
 * todo 在工作流控制中增加finally.可在label中增加一属性,标识是否为finally
 */
public class Transaction {

    public Transaction() {
    }

    /**
     * 添加一个label.
     */
    public void addLabel(String name) {
        labels.put(name, new Integer(activities.size() - 1));
    }

    /**
     * 增加"活动"
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 添加无任何跳转的Activity
     */
    public void addActivity(String className) {
        Activity act = new Activity(className);
        addActivity(act);
    }

    /**
     * 设置Transaction拦截器
     */
    public void setTransactionInterceptors(List interceptors) {
        this.transactionInterceptors = interceptors;
    }

    /**
     * 设置Action拦截器
     */
    public void setActionInterceptors(List interceptors) {
        this.actionInterceptors = interceptors;
    }

    /**
     * 设置内部错误的处理Action
     */
    public void setInternalErrorAction(Action internalErrorAction) {
        this.internalErrorAction = internalErrorAction;
    }

    /**
     * 得到所有的活动
     */
    public List findAllActivities() {
        return activities;
    }

    /**
     * 得到所有的label
     */
    public Map findAllLabels() {
        return labels;
    }

    /**
     * 得到所有的ActionInterceptor
     */
    public List findAllActionInterceptors() {
        return actionInterceptors;
    }

    /**
     * 得到所有的TransactionInterceptor
     */
    public List findAllTransactionInterceptors() {
        return transactionInterceptors;
    }

    /**
     * 得到系统内部错误处理的Action
     */
    public Action getInternalErrorAction() {
        return internalErrorAction;
    }


    private List activities = new ArrayList();

    private Map labels = new HashMap();

    private List actionInterceptors = new ArrayList();

    private List transactionInterceptors = new ArrayList();

    private Action internalErrorAction = new InternalErrorAction();

    /** @link dependency */
    /*# Activity lnkActivity; */
}
