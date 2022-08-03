package ru.clevertec.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool implements ConnectionReleaser {

    private static ConnectionPool instance;
    private final BlockingQueue<ReusableConnection> connectionPool;
    private final BlockingQueue<ReusableConnection> activeConnections;

    private ConnectionPool() {
        this(PropsKt.DEFAULT_POOL_SIZE);
    }

    private ConnectionPool(int poolSize) {
        connectionPool = new LinkedBlockingQueue<>(poolSize);
        activeConnections = new LinkedBlockingQueue<>(poolSize);
        registerDriver();
        for (int i = 0; i < poolSize; i++) {
            try {
                Connection conn = DriverManager.getConnection(
                        String.format(PropsKt.URL, PropsKt.DATABASE_NAME),
                        PropsKt.USER,
                        PropsKt.PASSWORD
                );
                connectionPool.offer(new ReusableConnection(conn, this));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerDriver() {
        try {
            Class.forName(PropsKt.DRIVER);
        } catch (ClassNotFoundException e) {
        }
    }

    public Connection getConnection() {
        ReusableConnection connection;
        try {
            connection = connectionPool.take();
            activeConnections.put(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    @Override
    public void releaseConnection(Connection conn) {
        if (conn instanceof ReusableConnection && activeConnections.remove(conn)) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            connectionPool.offer((ReusableConnection) conn);
        }
    }

    public void destroyPool() {
        for (int i = 0; i < PropsKt.DEFAULT_POOL_SIZE; i++) {
            try {
                connectionPool.take().reallyClose();
            } catch (InterruptedException e) {

            }
        }
    }

    public static ConnectionPool getInstance(Integer size) {
        if(instance == null){
            synchronized (ConnectionPool.class) {
                if(instance == null){
                    if(size == null) instance = new ConnectionPool();
                    else instance = new ConnectionPool(size);
                }
            }
        }
        return instance;
    }
}
