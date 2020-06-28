package resoft.tips.web.action;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import resoft.basLink.Message;
import resoft.common.web.AbstractAction;
import resoft.tips.util.TransCommUtil;

/**
 * <p>
 * 修改密码:发送9006报文
 * </p>
 * Author: chenjianping Date: 2007-8-14 Time: 15:19:58
 */
public class ChangeTipsPassword extends AbstractAction {

	private static final Log logger = LogFactory
			.getLog(ChangeTipsPassword.class);

	public String execute() {
		List ctp = jdbcTemplate
				.queryForList("select * from SysStatus where tipspassword='"
						+ oldPassword + "'");
		if (ctp.size() == 0) {
			setMessage("原密码输入错误");
			return ERROR;
		} else {

			Message msg = new Message();
			msg.setValue("NewPassword", newPassword);
			Message returnData;
			try {

				returnData = TransCommUtil.send("9006", msg);
			} catch (IOException e) {
				setMessage("连接后台交易系统失败");
				logger.error("连接后台交易系统失败", e);
				return ERROR;
			}
			String loginResult = returnData.getValue("LoginResult");

			if (loginResult.equals("90000")) {

				jdbcTemplate.update("update SysStatus set tipsPassword='"
						+ newPassword + "' where tipsPassword='" + oldPassword
						+ "'");
				loginResult = "修改密码成功";
			} else
				loginResult = "修改密码失败";
			setMessage("返回结果:" + loginResult);
		}
		return SUCCESS;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate jdbcTemplate;

	private String oldPassword, newPassword;
}
