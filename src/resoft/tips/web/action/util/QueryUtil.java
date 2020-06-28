package resoft.tips.web.action.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import resoft.tips.domain.KeyValuePair;

/**
 * <p>≤È—Ø∏®÷˙¿‡</p>
 * Author: liguoyin
 * Date: 2007-8-15
 * Time: 19:02:57
 */
public class QueryUtil {

    public Collection findAll(String sql) {
        sql = sql.toUpperCase();
    	return jdbcTemplate.query(sql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                String code = rs.getString(1);
                String name = rs.getString(2);

                return new KeyValuePair(code, name);
            }
        });
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private JdbcTemplate jdbcTemplate;
}
