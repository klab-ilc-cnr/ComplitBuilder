/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Simone Marchi
 */
public class C3P0DataSource {

    private static Logger logger = LoggerFactory.getLogger(C3P0DataSource.class);

    private static ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setJdbcUrl("jdbc:mysql://localhost:3306/LexicO?serverTimezone=UTC");
            cpds.setUser("root");
            cpds.setPassword("root");
        } catch (PropertyVetoException e) {
            logger.error(e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    private C3P0DataSource() {
    }
}
