package com.cmmplb.oauth2.resource.server.impl;

import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;

import javax.sql.DataSource;

/**
 * @author penglibo
 * @date 2024-07-22 17:02:32
 * @since jdk 1.8
 * 重写字段名称
 */
public class JdbcApprovalStoreImpl extends JdbcApprovalStore {

    private static final String TABLE_NAME = "oauth_approvals";

    private static final String FIELDS = "expires_at,status,last_modified_at,user_id,client_id,scope";

    private static final String WHERE_KEY = "where user_id=? and client_id=?";

    private static final String WHERE_KEY_AND_SCOPE = WHERE_KEY + " and scope=?";

    private static final String DEFAULT_ADD_APPROVAL_STATEMENT = String.format("insert into %s ( %s ) values (?,?,?,?,?,?)", TABLE_NAME, FIELDS);

    private static final String DEFAULT_GET_APPROVAL_SQL = String.format("select %s from %s " + WHERE_KEY, FIELDS, TABLE_NAME);

    private static final String DEFAULT_DELETE_APPROVAL_SQL = String.format("delete from %s " + WHERE_KEY_AND_SCOPE, TABLE_NAME);

    private static final String DEFAULT_EXPIRE_APPROVAL_STATEMENT = String.format("update %s set expires_at = ? " + WHERE_KEY_AND_SCOPE, TABLE_NAME);

    private static final String DEFAULT_REFRESH_APPROVAL_STATEMENT = String.format("update %s set expires_at=?, status=?, last_modified_at=? " + WHERE_KEY_AND_SCOPE, TABLE_NAME);

    public JdbcApprovalStoreImpl(DataSource dataSource) {
        super(dataSource);
        super.setAddApprovalStatement(DEFAULT_ADD_APPROVAL_STATEMENT);
        super.setFindApprovalStatement(DEFAULT_GET_APPROVAL_SQL);
        super.setDeleteApprovalStatment(DEFAULT_DELETE_APPROVAL_SQL);
        super.setExpireApprovalStatement(DEFAULT_EXPIRE_APPROVAL_STATEMENT);
        super.setRefreshApprovalStatement(DEFAULT_REFRESH_APPROVAL_STATEMENT);
    }
}