/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

import java.sql.Connection;
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

    private static final Logger logger = LoggerFactory.getLogger(SQLConnection.class);

    public static LexicalSense getUsemInformation(String idUsem) throws SQLException {
        LexicalSense ls = null;
        //String sql = "select comment, exemple, definition from usem where idUsem = ?;";
        String sql = "select REPLACE(comment,'’','\\'') as comment, REPLACE(exemple,'’','\\'') as exemple, REPLACE(definition, '’','\\'') as definition from usem where idUsem = ?;";
        Connection conn = null;
        try {
            conn = C3P0DataSource.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idUsem);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String comment = rs.getString(Utils.COMMENT);
                String example = rs.getString(Utils.EXAMPLE);
                String definition = rs.getString(Utils.DEFINITION);
                ls = new LexicalSense();
                ls.setId(idUsem);
                ls.addCreator(Utils.LEXICO);
                ls.setComment((comment!=null?comment.trim():null));
                ls.setExample((example!=null?example.trim():null));
                ls.setDefinition((definition!=null?definition.trim():null));
            } else {
                logger.warn(String.format("No result for usem=(%s)", idUsem));
            }
        } catch (SQLException e) {
            logger.error(String.format("SQL: %s, Error: %s", sql,e.getLocalizedMessage()));
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return ls;
    }

    public static List<UsemRel> getRelByUsem(String idUsem) throws SQLException {

        List<UsemRel> rels = null;
        String sql = "select idUsemTarget, idRsem from usemrel where idUsem = ?;";
        Connection conn = null;
        try {
            conn = C3P0DataSource.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, idUsem);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rels == null) {
                    rels = new ArrayList<>();
                }
                String idUsemTrg = rs.getString(Utils.IDUSEMTRG);
                String idRelation = rs.getString(Utils.IDREL);

                UsemRel rel = new UsemRel();
                rel.setIdUsem(idUsem);
                rel.setRelation(idRelation);
                rel.setIdUsemTarget(idUsemTrg);
                rels.add(rel);
                // do something with the extracted data...
            }
        } catch (SQLException e) {
            logger.error("No result for: " + idUsem);
            logger.error(e.getLocalizedMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return rels;
    }
}
