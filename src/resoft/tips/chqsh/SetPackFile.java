package resoft.tips.chqsh;

import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>设置回应格式</p>
 *
 * @Author:
 * Date: 2007-5-19
 * Time: 5:00:48
 */
public class SetPackFile implements Action {
    public int execute(Message msg) throws Exception {
        msg.set("packFile",packFile);
        return SUCCESS;
    }
    public void setPackFile(String packFile) {
        this.packFile = packFile;
    }
    private String packFile;
}

