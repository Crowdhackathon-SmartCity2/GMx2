package gr.smartcity.hackathon.hackathonproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Αυτή η δραστηριότητα υλοποιεί την εκπομπή ενημερώσεων (Android Notifications)
 * παρακολουθώντας την ενημέρωση των εκκρεμών εργασιών στη βάση δεδομένων SQL Server, που είναι
 * ευγενική χορηγία του καθηγητή Ε. Ι. Γιαννακουδάκη.
 */
public class NotificationPreview extends AsyncTask<Void, Void, Void> {

    NotificationManager manager;
    Resources resources;
    Context context;
    int requestsAmt = 0;
    String id1;

    private static int id = 0;

    public NotificationPreview(String id1, NotificationManager manager, Resources resources, Context context) {
        this.id1 = id1;
        this.manager = manager;
        this.resources = resources;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.e("NOT_ASYNCTASK", "Enabling notification services for " + id1 + ".");
        synchronized (DatabaseConnection.con) {
            try {
                String exploration_query = "select count(distinct custid) from pending where wid like '" + id1 + "'";
                Log.e("EXPLORATION1", exploration_query);
                Statement exploration_stmt = DatabaseConnection.con.createStatement();
                ResultSet ers = null;
                ers = exploration_stmt.executeQuery(exploration_query);

                if (ers.next()) requestsAmt = Integer.parseInt(ers.getObject(1).toString());
                Log.e("INITIAL_REQUESTS", ers.getObject(1).toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        do {
            try {
                Thread.sleep(75000); //1.25 minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (DatabaseConnection.con) {
                try {
                    String exploration_query = "select count(distinct custid) from pending where wid like '" + id1 + "'";
                    Log.e("EXPLORATION2", exploration_query);
                    Statement exploration_stmt = DatabaseConnection.con.createStatement();
                    ResultSet ers = null;
                    int previous;
                    ers = exploration_stmt.executeQuery(exploration_query);

                    if (ers.next()) {
                        previous = requestsAmt;
                        requestsAmt = Integer.parseInt(ers.getObject(1).toString());
                        if(previous < requestsAmt) //Log.e("SENDING NOTIFICATION", "Splendid: you got " + requestsAmt + " requests!");
                            notificationCall("Splendid: you got " + requestsAmt + " requests!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } while(true);
    }

    public void notificationCall(String notifText){
        Bitmap largeIcon = BitmapFactory.decodeResource(resources, android.R.drawable.ic_dialog_alert);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setLargeIcon(largeIcon)
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(notifText);

        Notification notif =notificationBuilder.build();
        try{
            manager.notify(id++,notif);
        }
        catch(IllegalArgumentException e){
            Log.e("ERROR DECLARATION",e.toString());
        }

    }

}

