package com.nannan.jdbc.pool;

import lombok.Data;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Data
public class PooledDataSource implements DataSource {
    private List<PooledConnection> connections = null;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize = 2;
    private Properties connectionProperties;


    private void initPool(){
        this.connections = new ArrayList<>(initialSize);
        try{
            for(int i=0 ; i<initialSize ; i++){
                Connection connection = DriverManager.getConnection(url, username, password);
                PooledConnection pooledConnection = new PooledConnection(connection, false);
                this.connections.add(pooledConnection);

                System.out.println("********add connection pool*********");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }


    protected Connection getConnectionFromDriver(String userName, String password) throws SQLException{
        Properties properties = new Properties();
        if(connectionProperties != null){
            properties.putAll(connectionProperties);
        }
        if(username != null){
            properties.setProperty("user", userName);
        }

        if(password != null){
            properties.setProperty("password", password);
        }

        if(this.connections == null){
            initPool();
        }

        PooledConnection availableConnection = getAvailableConnection();

        while(availableConnection == null){
            availableConnection = getAvailableConnection();
            if(availableConnection == null){
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return availableConnection;
    }

    private PooledConnection getAvailableConnection() throws SQLException{
        for(PooledConnection pooledConnection : this.connections){
            if (!pooledConnection.isActive()){
                pooledConnection.setActive(true);
                return pooledConnection;
            }
        }

        return null;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
