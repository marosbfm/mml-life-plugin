package sk.maroskomml.lifeplugin.model;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;

public class DBCPDataSource {
    private static final BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl("jdbc:mysql://localhost:3306/mmldb");
        ds.setUsername("minecraft");
        ds.setPassword("mmlPwd12");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws Exception {
        return ds.getConnection();
    }

    private DBCPDataSource() {
    }

}
