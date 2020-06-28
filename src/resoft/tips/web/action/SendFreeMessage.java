package resoft.tips.web.action;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import resoft.common.web.AbstractAction;

/**
 * <p/>
 * 发送自由格式报文
 * </p>
 * Author: wangchenglong Date: 2007-7-23 Time: 14:19:58
 */

public class SendFreeMessage extends AbstractAction {


    public String execute() throws Exception {

        String sql = "select max(ID) from sentfreemessage";
        int count = jdbcTemplate.queryForInt(sql);
        count += 1;

        jdbcTemplate.update("insert into sentfreemessage(id,desNodeCode,content) values(" + count + ",'" + srcNodeCode + "','" + content + "')");
        setMessage("发送成功");

        return SUCCESS;
    }


    public void setSrcNodeCode(String srcNodeCode) {
        this.srcNodeCode = srcNodeCode;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private JdbcTemplate jdbcTemplate;

    private String srcNodeCode,content;

}
