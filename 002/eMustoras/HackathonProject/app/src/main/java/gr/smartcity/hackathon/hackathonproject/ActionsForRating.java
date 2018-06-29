package gr.smartcity.hackathon.hackathonproject;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Αυτή η δραστηριότητα αποτελεί νήμα επικοινωνίας με το server για την καταχώρηση αξιολογήσεων.
 */
public class ActionsForRating extends Thread {
    Socket connection = null;
    ObjectInputStream in;
    ObjectOutputStream out;
    Vec2<String, Integer> packet;
    Vec2<HashMap<String, Float>, Byte> packet1;

    public ActionsForRating(Socket s, ObjectInputStream in, ObjectOutputStream out,  Vec2<HashMap<String, Float>, Byte> packet) {
        connection = s;
        this.packet1 = packet;
        this.in = in;
        this.out = out;
    }

    public ActionsForRating(Socket s, Vec2<String, Integer> packet, ObjectInputStream in, ObjectOutputStream out) {
        connection = s;
        this.packet = packet;
        this.in = in;
        this.out = out;
    }

    public void run() {
        if(packet == null) {
            try {
                out.writeObject(packet1);
                out.flush();
                Log.e("INIT_STATE", "flush ok");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                out.writeObject((Vec2<String, Integer>)packet);
                out.flush();
                Log.e("PACK_STATE", "flush ok");
                Float average = (Float) in.readObject();
                Log.e("RATING_AVG", average + "");
                try {
                    String ratingUpd_query = "UPDATE workers SET rating = " + average + " WHERE id like '" + packet.getTValue() + "'";
                    Log.e("EXPLORATION1", ratingUpd_query);
                    Statement exploration_stmt = DatabaseConnection.con.createStatement();
                    ResultSet ers = null;
                    ers = exploration_stmt.executeQuery(ratingUpd_query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
