package resoft.tips.web.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import resoft.common.web.AbstractAction;
import resoft.tips.domain.KeyValuePair;
/**
 * <p>
 * 获取报文种类
 * </p>
 * Author: chenjianping Date: 2007-7-15 Time: 15:19:58
 */
public class QueryMsgNo extends AbstractAction {
	public String execute() throws Exception {
		msgNoList = jdbcTemplate.query("select * from MsgNoMng",
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						String code = rs.getString("code");
						String name = rs.getString("name");

						return new KeyValuePair(code, name);
					}
				});

		return SUCCESS;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List getMsgNoList() {
		return msgNoList;
	}

	private List msgNoList;

	private JdbcTemplate jdbcTemplate;

}
