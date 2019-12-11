package com.bsuir.lagunovskaya.clinic.server.db.processors;

import java.sql.PreparedStatement;

public interface PreparedStatementPostProcessor {
    void fillParamsOfPreparedStatement(PreparedStatement preparedStatement);
}
