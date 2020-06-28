package resoft.tips.web.action;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import resoft.common.web.AbstractAction;
import resoft.tips.domain.SysStatus;
/**
 * <p>
 * 查看系统状态
 * </p>
 * Author: chenjianping Date: 2007-7-15 Time: 15:19:58
 */
public class QuerySysStatus extends AbstractAction {

	public String execute() throws Exception {

		sysStatus = (SysStatus) jdbcTemplate.queryForObject(
				"select * from SysStatus", new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						SysStatus obj = new SysStatus();
						obj.setLoginStatus(rs.getString("loginStatus"));
						obj.setWorkDate(rs.getString("workdate"));
						obj.setSysStatus(rs.getString("sysstatus"));
						return obj;
					}
				});
		return SUCCESS;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public SysStatus getSysStatus() {
		return sysStatus;
	}

	private SysStatus sysStatus;

	private JdbcTemplate jdbcTemplate;

}
