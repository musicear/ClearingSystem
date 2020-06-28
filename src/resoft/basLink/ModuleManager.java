package resoft.basLink;

/**
 * 交易实例缓存
 * */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.AbstractCacheTemplate;

public class ModuleManager extends AbstractCacheTemplate{
    private static Log logger = LogFactory.getLog(ModuleManager.class);

    protected ModuleManager(){}

    /**
     * 根据交易码得到响应处理模块
     * */
    public ModuleBase findModule(String className) {
        ModuleBase module = null;
        try {
            module = (ModuleBase) Class.forName(className).newInstance();
            return module;
        } catch(Exception e) {
            logger.error("加载类" + className + "失败",e);
        }
        return null;

        //return (ModuleBase) get(className);
    }
    /**
     * 加载新模块
     * */
    protected Object load(String key) {
        if(key==null) {
            logger.info("交易未定义:" + key);
            return null;
        }
        ModuleBase module = null;
        try {
            module = (ModuleBase) Class.forName(key).newInstance();
        } catch (Exception e) {
            logger.error("加载类" + key + "失败",e);
        }
        return module;
    }
    /**
     * 得到唯一实例
     * */
    public static ModuleManager getInstance(){
        return instance;
    }

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory 
     */
    /*# private ModuleManager _moduleManager; */
    private static ModuleManager instance = new ModuleManager();


}
