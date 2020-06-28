package resoft.basLink.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zerone.jdbc.DatabaseHelper;

import resoft.basLink.Configuration;
import resoft.xlink.core.Message;
import resoft.xlink.impl.DefaultMessage;

/**
 * function:�����ݿ��е�����ת��ΪMessage��ʽ�����ж�����¼�������ö��onMessage
 * User: albert lee
 * Date: 2005-10-24
 * Time: 18:14:46
 */
public class DB2Message {
    private static Log logger = LogFactory.getLog(DB2Message.class);

    public DB2Message(String sql, String tableName) {
        this.sql = sql;
        this.tableName = tableName;
    }

    /**
     * �������ݼ�������
     * @param dataSetItr DataSetIterator
     */
    public void setDataSetIterator(DataSetIterator dataSetItr) {
        this.dataSetItr = dataSetItr;
    }

    /**
     * ��ʼ��ѯ
     * @throws java.sql.SQLException ���ݿ�����ʧ�ܻ������ļ�������׳��쳣
     */
    public void query() throws SQLException {
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            throw new SQLException("���ݿ�����ʧ��");
        }
        String category = "key_" + tableName.toLowerCase();
        Configuration conf = Configuration.getInstance("db");
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Message msg = new DefaultMessage();
                Collection list = conf.listAllProperties(category);
                if (list == null) {
                    throw new SQLException("���ü�δ�ҵ�:" + category);
                }
                for (java.util.Iterator itr = list.iterator(); itr.hasNext();) {
                    String fieldCode = (String) itr.next();
                    String msgKey = conf.getProperty(category, fieldCode);
                    //�õ���������
                    String dataType = conf.getProperty(category + "_datatype", fieldCode);
                    String value;
                    if (dataType.equals("D")) {
                        //������
                        Date date = rs.getDate(fieldCode);
                        value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        value = rs.getString(fieldCode);
                    }
                    if (value != null && !value.equals("")) {
                        msg.set(msgKey, value);
                    }
                }
                try {
                    dataSetItr.onMessage(msg);
                } catch (Exception e) {
                    logger.error("����ʧ��", e);
                }
            }
        } finally {
            DatabaseHelper.clearup(rs, stmt, conn);
        }
    }

    /**
     * ���ݼ�������
     */
    public interface DataSetIterator {
        public void onMessage(Message msg) throws Exception;
    }

    private String sql, tableName;
    private DataSetIterator dataSetItr;//���ݼ��ĵ�����
}

