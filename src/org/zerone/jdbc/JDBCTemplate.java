package org.zerone.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JDBCTemplate {
    private Log logger = LogFactory.getLog(JDBCTemplate.class);

    /**
     * 查询数据库。结果回调RowCallbackHandler
     *
     * @param sql     String   要查询的语句
     * @param handler RowCallbackHandler 回调接口
     */
    public void query(String sql, RowCallbackHandler handler) {
    	sql=sql.toUpperCase();
        //检测参数是否合法
        if (sql == null || sql.equals("")) {
            logger.fatal("参数:sql错误");
            throw new IllegalArgumentException("");
        }
        //得到连接
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            logger.info("连接数据库错误");
            throw new JDBCException("连接数据库错误");
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                handler.process(rs);
            }
        } catch (SQLException e) {
            logger.fatal("数据库查询失败.sql语句为：" + ":" + sql, e);
            throw new JDBCException("数据库查询失败");
        } finally {
            DatabaseHelper.clearup(rs, pstmt, conn);
        }
    }

    /**
     * 查询数据库
     *
     * @param psc     PreparedStatementCreator   创建PreparedStatement的接口
     * @param handler RowCallbackHandler     结果处理
     */
    public void query(PreparedStatementCreator psc, RowCallbackHandler handler) {
        //检查参数是否合法
        if (psc == null || handler == null) {
            logger.fatal("参数错误");
            throw new IllegalArgumentException("参数错误");
        }
        //
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            logger.info("连接数据库错误");
            throw new JDBCException("连接数据库错误");
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = psc.createPreparedStatement(conn);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                handler.process(rs);
            }
        } catch (SQLException e) {
            logger.fatal("数据库查询失败.sql语句为：", e);
            throw new JDBCException("数据库查询失败");
        } finally {
            DatabaseHelper.clearup(rs, pstmt, conn);
        }
    }
}
