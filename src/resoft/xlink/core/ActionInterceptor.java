package resoft.xlink.core;

/**
 * <p>Action执行拦截器</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 2:51:14
 */
public interface ActionInterceptor {
    /**
     * 执行每个module之前
     * @return boolean 是否继续执行标志。true为继续执行，false为停止执行
     * */
    public boolean beforeAction(Action action,Message msg);
    /**
     * 执行每个module之后
     * @param Action action 模块类
     * @param msg Message
     * @param result String  执行结果
     * */
    public void afterAction(Action action,Message msg,int result);
}
