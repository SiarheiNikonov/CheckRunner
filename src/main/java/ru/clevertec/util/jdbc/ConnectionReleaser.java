package ru.clevertec.util.jdbc;

import java.sql.Connection;

public interface ConnectionReleaser {
    void releaseConnection(Connection conn);
}
