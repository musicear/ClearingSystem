package resoft.xlink.transaction;

import java.util.Map;

/**
 * <p>交易管理，包括加载等</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 3:38:22
 */
public interface TransactionManager {
    /**
     * 根据交易码得到交易序列
     * */
    public Transaction getTransaction(String transCode);
    /**
     * 设置交易的全局变量
     * */
    public void setGlobalProperties(Map properties);
}
