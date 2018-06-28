package gr.smartcity.hackathon.hackathonproject;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection implements Serializable {
    static protected Connection con;
    static protected String un, pass, db, ip;

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
