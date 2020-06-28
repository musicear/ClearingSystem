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
 * 获取发起机构名称
 * </p>
 * Author: chenjianping Date: 2007-7-15 Time: 15:19:58
 */
public class QueryNodeMng extends AbstractAction {

	public String execute() throws Exception {
		NodeMngList = jdbcTemplate.query("select * from NodeMng",
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						String nodeCode = rs.getString("nodeCode");
						String nodeName = rs.getString("nodeName");

						return new KeyValuePair(nodeCode, nodeName);
					}
				});

		return maptype;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List getNodeMngList() {
		return NodeMngList;
	}

	public String getMaptype() {
		return maptype;
	}

	public void setMaptype(String maptype) {
		this.maptype = maptype;
	}

	private String maptype;

	private List NodeMngList;

	private JdbcTemplate jdbcTemplate;

}
