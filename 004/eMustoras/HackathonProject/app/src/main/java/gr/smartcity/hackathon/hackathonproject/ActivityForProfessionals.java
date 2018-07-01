package gr.smartcity.hackathon.hackathonproject;

import android.Manifest;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Αυτή η δραστηριότητα υλοποιεί την αρχική σελίδα επιλογών του επαγγελματία.
 */
public class ActivityForProfessionals extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private LocationManager locationManager;
    private LocationListener locationListener;

    ECustomService professional;
    Vec2<Float, Float> coord;
    DatabaseConnection conn;
    String creds;
    private static int id = 0;
    static protected HashMap<String, ECustomUser> users1;
    static protected HashMap<String, ECustomUser> users2;
    static protected HashMap<String, Vec2<Float, Float>> destinations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_professionals);
        ErasurePreview oldPending = new ErasurePreview();
        oldPending.execute();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        initLocationService();

        ImageView imgProf = (ImageView) findViewById(R.id.profImg);
        int id = R.drawable.background3;
        new ScaleImg(imgProf, id, this);

        TextView test = (TextView) findViewById(R.id.textView2);
        String text = getIntent().getExtras().getString("gr.smartcity.hackathon.hackathonproject.OK");
        creds = getIntent().getStringExtra("EXTRA_USERNAME");
        conn = (DatabaseConnection)getIntent().getSerializableExtra("DB_CONN");

        ActionBar actionBar = getActionBar();

        if(seekProfessionalDatabase())
            test.setText(test.getText() + " " + professional.getSurname() + " " + professional.getName());
        else test.setText(test.getText() + " " + creds);

        initNotificationService();
        initNotificationService2();

        Button completedBtn = (Button) findViewById(R.id.jobsCompleted);
        Button pendingBtn = (Button) findViewById(R.id.jobsPending);
        Button mapButton = (Button) findViewById(R.id.jobsPendingMap);

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(professional.getID().contains("W")) {
                    Intent startIntent = new Intent(getApplicationContext(), History.class);
                    startIntent.putExtra("DB_CONN", conn);
                    startIntent.putExtra("SERV_DET", professional);
                    startActivity(startIntent);
                } else if (professional.getID().contains("P")) {
                    Intent startIntent = new Intent(getApplicationContext(), HistoryPub.class);
                    startIntent.putExtra("DB_CONN", conn);
                    startIntent.putExtra("SERV_DET", professional);
                    startActivity(startIntent);
                }
            }
        });

        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(professional.getID().contains("W")) {
                    Intent startIntent = new Intent(getApplicationContext(), pending.class);
                    startIntent.putExtra("DB_CONN", conn);
                    startIntent.putExtra("SERV_DET", professional);
                    startActivity(startIntent);
                } else if (professional.getID().contains("P")) {
                    Intent startIntent = new Intent(getApplicationContext(), PendingPub.class);
                    startIntent.putExtra("DB_CONN", conn);
                    startIntent.putExtra("SERV_DET", professional);
                    startActivity(startIntent);
                }
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table = "";
                if(professional.getID().contains("W")) table = "pending";
                    else if (professional.getID().contains("P")) table = "public_pending";
                if(!table.equals("")) formMapQuery(table);
                    else Toast.makeText(getApplicationContext(), "Error connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLocationService() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    float lat = (float)location.getLatitude();
                    float lon = (float)location.getLongitude();
                    coord = new Vec2<Float, Float>(lat, lon);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(ActivityForProfessionals.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            Log.v("isGPSEnabled", "=" + isGPSEnabled);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void initNotificationService() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationPreview notificationSystem = new NotificationPreview(professional.getID(), notificationManager, getResources(), this);
        notificationSystem.execute();
    }

    private void initNotificationService2() {
        NotificationManager notificationManager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationPreview2 notificationSystem = new NotificationPreview2(professional.getID(), notificationManager2, getResources(), this);
        notificationSystem.execute();
    }

    public boolean seekProfessionalDatabase() {
        boolean isSuccess = false;
            try {
                String query = "select * from workers where logindata= '" + creds +"'";
                Log.e("QUERYING... ", query);
                Statement stmt = conn.con.createStatement();
                ResultSet rs = null;
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    String id = rs.getObject(1).toString();
                    String name = rs.getObject(2).toString();
                    String surname = rs.getObject(3).toString();
                    String tel = rs.getObject(4).toString();
                    String description = rs.getObject(6).toString();
                    String images = rs.getObject(7).toString();
                    int smallInt = Integer.parseInt(rs.getObject(5).toString());
                    boolean availability = (smallInt > 0) ? true : false;
                    float rating = Float.parseFloat(rs.getObject(10).toString());
                    professional = new ECustomService(id, name, surname, tel, description, images, rating, availability);
                    isSuccess = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if (!isSuccess) {
            try {
                String query = "select * from public_services where logindata= '" + creds +"'";
                Log.e("QUERYING... ", query);
                Statement stmt = conn.con.createStatement();
                ResultSet rs = null;
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    String id = rs.getObject(1).toString();
                    String name = rs.getObject(2).toString();
                    String tel = rs.getObject(3).toString();
                    String description = rs.getObject(5).toString();
                    String images = rs.getObject(7).toString();
                    int smallInt = Integer.parseInt(rs.getObject(4).toString());
                    boolean availability = (smallInt > 0) ? true : false;
                    float rating = Float.parseFloat(rs.getObject(6).toString());
                    professional = new ECustomService(id, name,tel, description, images, rating, availability);
                    isSuccess = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public boolean formMapQuery(String table) {
        if(table.equals("pending") && users1 != null) {
            //fetch coordinates from server
        } else if(table.equals("public_pending") && users2 != null) {
            ArrayList<String> keys = new ArrayList<>();
            keys.addAll(users2.keySet());
            Log.e("LOCAL_PTR", keys.size() + " elem.");
            Vec2<String, ArrayList<String>> coords = new Vec2<String, ArrayList<String>>(professional.getID(),keys);
            ServerConnection myconn = new ServerConnection();
            myconn.initialize('M');
            myconn.initializeMap(coords, 3);
            Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
            mapIntent.putExtra("DEST_PIN", destinations);
            mapIntent.putExtra("DEST_MY", coord);
            startActivity(mapIntent);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu2, menu2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.coordButton: {
                // first check for permissions
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                                ,10);
                    }
                    break;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) {
                    float longitude = (float) location.getLongitude();
                    float latitude = (float) location.getLatitude();
                    coord = new Vec2<Float, Float>(longitude, latitude);
                    Log.e("LOCATION", coord.toString());
                    ServerConnection myconn = new ServerConnection();
                    myconn.initialize('P');
                    myconn.sendMyLocation(coord, professional.getID(), 0);
                } else if(coord != null) {
                    ServerConnection myconn = new ServerConnection();
                    myconn.initialize('P');
                    myconn.sendMyLocation(coord, professional.getID(), 0);
                    Log.e("LOCATION", coord.toString());
                } else Log.e("LOCATION ERROR", location + "");

            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void notificationCall(String notifText){
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_alert);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setLargeIcon(largeIcon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(notifText);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif =notificationBuilder.build();
        try{
            notificationManager.notify(id++,notif);
        }
        catch(IllegalArgumentException e){
            Log.e("ERROR DECLARATION",e.toString());
        }

    }
}


