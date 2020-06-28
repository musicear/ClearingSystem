package resoft.xlink.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.xlink.core.Action;
import resoft.xlink.core.ActionInterceptor;
import resoft.xlink.core.ActionManager;
import resoft.xlink.core.Message;
import resoft.xlink.transaction.Activity;
import resoft.xlink.transaction.Transaction;
import resoft.xlink.transaction.TransactionExecutor;
import resoft.xlink.transaction.TransactionInterceptor;

/**
 * <p>默认的交易调度器</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:59:30
 */
public class DefaultTransactionExecutor implements TransactionExecutor {
    private static final Log logger = LogFactory.getLog(DefaultTransactionExecutor.class);

    public DefaultTransactionExecutor() {
    }

    /**
     * 执行交易
     */
    public void execute(Transaction transaction, Message msg) {
        List transactionInterceptors = transaction.findAllTransactionInterceptors();
        boolean executeTransaction = true;
        int index;
        //执行before
        for (index = 0; index < transactionInterceptors.size(); index++) {
            TransactionInterceptor interceptor = (TransactionInterceptor) transactionInterceptors.get(index);
            executeTransaction = interceptor.before(msg);
            if (!executeTransaction) {
                break;
            }
        }
        //执行transaction
        if (executeTransaction) {
            executeInternal(transaction,msg);
        }
        //执行afterTransaction
        index --;
        for (; index >= 0; index--) {
            TransactionInterceptor interceptor = (TransactionInterceptor) transactionInterceptors.get(index);
            interceptor.after(msg);
        }
    }

    /**
     * 执行Module序列
     */
    private void executeInternal(Transaction transaction,Message msg) {
        List actionInterceptors = transaction.findAllActionInterceptors();
        Map labels = transaction.findAllLabels();
        //@todo how to detect dead loop?
        ActionManager actionManager = new DefaultActionManager();
        List activities = transaction.findAllActivities();
        int index = 0;
        Activity act = (Activity) activities.get(index);

        //
        while (act != null) {
            Action action = actionManager.createAction(act.getName(), act.listAllProperties());
            try {
                //call interceptor 's before
                for (Iterator itr = actionInterceptors.iterator(); itr.hasNext();) {
                    ActionInterceptor interceptor = (ActionInterceptor) itr.next();
                    boolean continueFlag = interceptor.beforeAction(action, msg);
                    if (!continueFlag) {
                        break;
                    }
                }
                int retValue = action.execute(msg);
                //call interceptor's after
                for (int i = actionInterceptors.size() - 1; i >= 0; i--) {
                    ActionInterceptor interceptor = (ActionInterceptor) actionInterceptors.get(i);
                    interceptor.afterAction(action, msg, retValue);
                }

                String on = Integer.toString(retValue);
                String to = act.getTransitionTo(on);
                if (to.equals("")) {
                    //若返回值在流程中未定义,则默认向下执行
                    index ++;
                    if (index >= activities.size()) {
                        //执行完毕
                        break;
                    } else {
                        act = (Activity) activities.get(index);
                    }
                } else {
                    Integer temp = (Integer) labels.get(to);
                    if (temp == null) {
                        logger.error("未找到label:" + to);
                        break;
                    } else {
                        //跳转到label
                        index = temp.intValue() + 1;
                        if (index >= activities.size()) {
                            break;
                        }
                        act = (Activity) activities.get(index);
                    }

                }
            } catch (Exception e) {
                logger.error("Action执行错误:" + action.getClass().getName(), e);
                try {
                    transaction.getInternalErrorAction().execute(msg);
                } catch (Exception e1) {                    
                }
                break;
            }
        }
    }

}
