package resoft.tips.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>����8λ�Ľ�����š�����Ϊhhmmss��Сʱ�����룩��2λ�������</p>
 * <p>����ǩԼ��֤������š��걨��ѯ�Ĳ�ѯ��ŵ�</p>
 * Author: liguoyin
 * Date: 2007-8-8
 * Time: 15:27:17
 */


public class SetTraNo implements Action {
    public int execute(Message msg) throws Exception {
        String time = DateTimeUtil.getTimeByFormat("hhmmss");
        NumberFormat nf = new DecimalFormat("00");
        String traNo = time + nf.format(Math.random() * 100);
        msg.set(traNoNodePath, traNo);
        return SUCCESS;
    }

    public void setTraNoNodePath(String traNoNodePath) {
        this.traNoNodePath = traNoNodePath;
    }

    private String traNoNodePath;
}
