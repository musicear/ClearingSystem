package resoft.tips.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * ���ڳ��������ɽ�����ţ���ˮ�ŵ�
 * @author chenlujia
 *
 */
public class GenerateTransNo {
	public static String generate() {
		String time = DateTimeUtil.getTimeByFormat("hhmmss");
		NumberFormat nf = new DecimalFormat("00");
		String traNo = time + nf.format(Math.random() * 100);
		return traNo;
	}
}
