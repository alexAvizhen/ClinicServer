package com.bsuir.lagunovskaya.clinic.server.db;

import com.bsuir.lagunovskaya.clinic.server.db.processors.ResultSetProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtils {

    public static Integer generateNextIdForTable(String tableName) {
        try {
            String sqlQuery = "select max(id) + 1 as new_id from " + tableName;
            JDBCTemplate jdbcTemplate = new JDBCTemplate();
            return jdbcTemplate.executeSelect(sqlQuery, new ResultSetProcessor<Integer>() {
                @Override
                public Integer processResultSet(ResultSet resultSet) throws SQLException {
                    if (resultSet.next()) {
                        return resultSet.getInt("new_id");
                    }
                    return 1;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
