package resoft.basLink;

/**
 * function:
 * User: albert lee
 * Date: 2005-9-22
 * Time: 8:52:12
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
}
