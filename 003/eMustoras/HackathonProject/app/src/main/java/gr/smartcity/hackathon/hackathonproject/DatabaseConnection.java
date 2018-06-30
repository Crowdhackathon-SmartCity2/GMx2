package gr.smartcity.hackathon.hackathonproject;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Αυτή η κλάση υλοποιεί την συγχρονισμένη διασύνδεση της εφαρμογής με τη βάση δεδομένων.
 * Χρησιμοποίηθηκε βάση σε SQL Server, με την ευγενική χορηγία του καθηγητή Ε. Ι. Γιαννακουδάκη
 * και το Οικονομικό Πανεπιστήμιο Αθηνών.
 */
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
