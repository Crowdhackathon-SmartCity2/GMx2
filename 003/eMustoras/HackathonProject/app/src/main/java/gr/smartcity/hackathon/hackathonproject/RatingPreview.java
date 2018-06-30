package gr.smartcity.hackathon.hackathonproject;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RatingPreview extends AsyncTask<Void, Void, Void> {
    String workID;
    Float rate;

    public RatingPreview(String workID, Float rate){
        this.workID = workID;
        this.rate = rate;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        synchronized (DatabaseConnection.con) {
            try {
                String ratingUpd_query = "UPDATE workers SET rating = " + rate + " WHERE id like '" + workID + "'";
                Log.e("EXPLORATION1", ratingUpd_query);
                Statement exploration_stmt = DatabaseConnection.con.createStatement();
                ResultSet ers = null;
                ers = exploration_stmt.executeQuery(ratingUpd_query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
