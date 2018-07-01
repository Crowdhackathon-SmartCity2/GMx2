package gr.smartcity.hackathon.hackathonproject;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ErasurePreview extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        synchronized (DatabaseConnection.con) {
            try {
                String confUpd_query = "update pending set confirm = 0 where requestDate not between dateadd(day,-7,getdate()) and getdate();";
                Log.e("P-EXPLORATION", confUpd_query);
                Statement exploration_stmt = DatabaseConnection.con.createStatement();
                ResultSet ers = null;
                ers = exploration_stmt.executeQuery(confUpd_query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
