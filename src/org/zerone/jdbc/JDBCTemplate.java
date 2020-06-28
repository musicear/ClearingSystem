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
     * ��ѯ���ݿ⡣����ص�RowCallbackHandler
     *
     * @param sql     String   Ҫ��ѯ�����
     * @param handler RowCallbackHandler �ص��ӿ�
     */
    public void query(String sql, RowCallbackHandler handler) {
    	sql=sql.toUpperCase();
        //�������Ƿ�Ϸ�
        if (sql == null || sql.equals("")) {
            logger.fatal("����:sql����");
            throw new IllegalArgumentException("");
        }
        //�õ�����
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            logger.info("�������ݿ����");
            throw new JDBCException("�������ݿ����");
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
            logger.fatal("���ݿ��ѯʧ��.sql���Ϊ��" + ":" + sql, e);
            throw new JDBCException("���ݿ��ѯʧ��");
        } finally {
            DatabaseHelper.clearup(rs, pstmt, conn);
        }
    }

    /**
     * ��ѯ���ݿ�
     *
     * @param psc     PreparedStatementCreator   ����PreparedStatement�Ľӿ�
     * @param handler RowCallbackHandler     �������
     */
    public void query(PreparedStatementCreator psc, RowCallbackHandler handler) {
        //�������Ƿ�Ϸ�
        if (psc == null || handler == null) {
            logger.fatal("��������");
            throw new IllegalArgumentException("��������");
        }
        //
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null) {
            logger.info("�������ݿ����");
            throw new JDBCException("�������ݿ����");
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
            logger.fatal("���ݿ��ѯʧ��.sql���Ϊ��", e);
            throw new JDBCException("���ݿ��ѯʧ��");
        } finally {
            DatabaseHelper.clearup(rs, pstmt, conn);
        }
    }
}
