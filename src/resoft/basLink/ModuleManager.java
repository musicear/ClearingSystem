package resoft.basLink;

/**
 * ����ʵ������
 * */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import resoft.basLink.util.AbstractCacheTemplate;

public class ModuleManager extends AbstractCacheTemplate{
    private static Log logger = LogFactory.getLog(ModuleManager.class);

    protected ModuleManager(){}

    /**
     * ���ݽ�����õ���Ӧ����ģ��
     * */
    public ModuleBase findModule(String className) {
        ModuleBase module = null;
        try {
            module = (ModuleBase) Class.forName(className).newInstance();
            return module;
        } catch(Exception e) {
            logger.error("������" + className + "ʧ��",e);
        }
        return null;

        //return (ModuleBase) get(className);
    }
    /**
     * ������ģ��
     * */
    protected Object load(String key) {
        if(key==null) {
            logger.info("����δ����:" + key);
            return null;
        }
        ModuleBase module = null;
        try {
            module = (ModuleBase) Class.forName(key).newInstance();
        } catch (Exception e) {
            logger.error("������" + key + "ʧ��",e);
        }
        return module;
    }
    /**
     * �õ�Ψһʵ��
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
