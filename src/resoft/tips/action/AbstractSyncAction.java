package resoft.tips.action;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>抽象类。在其中增加方法setResultNodePath、setAddWordNodePath</p>
 * Author: liguoyin
 * Date: 2007-6-6
 * Time: 13:05:34
 */
public abstract class AbstractSyncAction implements Action {
    public abstract int execute(Message message) throws Exception ;
  
    public void setResultNodePath(String resultNodePath) {
        this.resultNodePath = resultNodePath;
    }

    public void setAddWordNodePath(String addWordNodePath) {
        this.addWordNodePath = addWordNodePath;
    }

    public String getResultNodePath() {
        return resultNodePath;
    }

    public String getAddWordNodePath() {
        return addWordNodePath;
    }

    private String resultNodePath,addWordNodePath;
}
