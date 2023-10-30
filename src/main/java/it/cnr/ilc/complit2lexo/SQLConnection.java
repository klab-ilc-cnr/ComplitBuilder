/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author simone
 */
public class SQLConnection {

    private static Logger logger = LoggerFactory.getLogger(SQLConnection.class);

    private static String connectionUrl = "jdbc:mysql://localhost:3306/simplelexicon?serverTimezone=UTC";

    public static List<UsemRel> getRelByUsem(String idUsem) {

        List<UsemRel> rels = null;
        String sql = "select idUsemTarget, idRsem from usemrel where idUsem = ?;";
        try {
            Connection conn = DriverManager.getConnection(connectionUrl, "root", "root");

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idUsem);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rels == null) {
                    rels = new ArrayList<>();
                }
                String idUsemTrg = rs.getString("idUsemTarget");
                String idRelation = rs.getString("idRSem");

                UsemRel rel = new UsemRel();
                rel.setIdUsem(idUsem);
                rel.setRelation(idRelation);
                rel.setIdUsemTarget(idUsemTrg);
                rels.add(rel);
                // do something with the extracted data...
            }
            return rels;
        } catch (SQLException e) {
            logger.error("No result for: " + idUsem);
            // logger.error( e.getLocalizedMessage());
        }
        return null;
    }
}
