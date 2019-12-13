package com.bsuir.lagunovskaya.clinic.server.db.processors;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementPostProcessor {
    void fillParamsOfPreparedStatement(PreparedStatement preparedStatement) throws SQLException;
}
