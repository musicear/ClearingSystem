package resoft.tips.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import resoft.tips.util.DateTimeUtil;
import resoft.xlink.core.Action;
import resoft.xlink.core.Message;

/**
 * <p>生成8位的交易序号。规则为hhmmss（小时分钟秒）加2位随机号码</p>
 * <p>用于签约验证撤销序号、申报查询的查询序号等</p>
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
