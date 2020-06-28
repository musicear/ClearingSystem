package resoft.basLink;

/**
 * function: 交易执行拦截器
 * User: albert lee
 * Date: 2005-10-15
 * Time: 20:04:31
 */
public interface ModuleInterceptor {
    /**
     * 执行每个module之前
     * */
    public boolean beforeModule(ModuleBase module,Message msg);
    /**
     * 执行每个module之后
     * @param module ModuleBase 模块类
     * @param msg Message
     * @param result String  执行结果
     * */
    public void afterModule(ModuleBase module,Message msg,int result);
}
