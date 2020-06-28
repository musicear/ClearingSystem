package resoft.xlink.comm;

/**
 * <p>服务</p>
 * User: liguoyin
 * Date: 2007-3-23
 * Time: 4:41:56
 */
public interface Service {
    /**
     * 启动服务
     * */
    public void start() throws Exception;
    /**
     * 停止服务
     * */
    public void stop() throws Exception;
    /**
     * 服务是否正在运行
     * */
    public boolean isRunning();
    /**
     * 设置属性
     * */
    public void setProperty(String name,String value);
}
