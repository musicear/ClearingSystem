package resoft.xlink.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import resoft.xlink.core.Action;
import resoft.xlink.impl.InternalErrorAction;

/**
 * function: ����
 * User: albert lee
 * Date: 2005-9-26
 * Time: 9:52:10
 * todo �ڹ���������������finally.����label������һ����,��ʶ�Ƿ�Ϊfinally
 */
public class Transaction {

    public Transaction() {
    }

    /**
     * ���һ��label.
     */
    public void addLabel(String name) {
        labels.put(name, new Integer(activities.size() - 1));
    }

    /**
     * ����"�"
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * ������κ���ת��Activity
     */
    public void addActivity(String className) {
        Activity act = new Activity(className);
        addActivity(act);
    }

    /**
     * ����Transaction������
     */
    public void setTransactionInterceptors(List interceptors) {
        this.transactionInterceptors = interceptors;
    }

    /**
     * ����Action������
     */
    public void setActionInterceptors(List interceptors) {
        this.actionInterceptors = interceptors;
    }

    /**
     * �����ڲ�����Ĵ���Action
     */
    public void setInternalErrorAction(Action internalErrorAction) {
        this.internalErrorAction = internalErrorAction;
    }

    /**
     * �õ����еĻ
     */
    public List findAllActivities() {
        return activities;
    }

    /**
     * �õ����е�label
     */
    public Map findAllLabels() {
        return labels;
    }

    /**
     * �õ����е�ActionInterceptor
     */
    public List findAllActionInterceptors() {
        return actionInterceptors;
    }

    /**
     * �õ����е�TransactionInterceptor
     */
    public List findAllTransactionInterceptors() {
        return transactionInterceptors;
    }

    /**
     * �õ�ϵͳ�ڲ��������Action
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
