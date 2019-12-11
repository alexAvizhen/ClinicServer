package com.bsuir.lagunovskaya.clinic.server.db.processors;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetProcessor<T> {
    T processResultSet(ResultSet resultSet) throws SQLException;
}
