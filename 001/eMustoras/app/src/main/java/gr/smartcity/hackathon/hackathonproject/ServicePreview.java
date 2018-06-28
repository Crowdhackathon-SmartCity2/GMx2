package gr.smartcity.hackathon.hackathonproject;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ServicePreview extends AppCompatActivity implements ItemSelectionListener<Preview_DATA> {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LocationManager locationManager;
    private LocationListener locationListener;
    static protected HashMap<String, Float> distances;
    Vec2<Float, Float> coord;
    DatabaseConnection conn;
    String serviceType, specialty, table, restcommand = "";
    ECustomUser user;
    ImageMemory mem;

    private static ArrayList<Preview_DATA> listData = new ArrayList<Preview_DATA>();
    static protected HashMap<String, Float> ratings = new HashMap<String,Float>();
    ECustomService conf[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_preview);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        initLocationService();

        conn = (DatabaseConnection) getIntent().getSerializableExtra("DB_CONN");
        user = (ECustomUser) getIntent().getSerializableExtra("USER_DET");
        serviceType = (String) getIntent().getSerializableExtra("SER_TYPE");
        if(serviceType.equals("private")) {
            specialty = (String) getIntent().getSerializableExtra("SPEC");
            table = "workers";
            restcommand = "where specialty like '" + specialty + "' and";
        } else if(serviceType.equals("allprivate")) {
            restcommand = "where";
            table = "workers";
        }
        ActionBar actionBar = getActionBar();
        TextView text1 = (TextView) findViewById(R.id.textView1);
        text1.setText("Your specialist; just in time " + user.getName() + "!");

        initActualData();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(listData);
        recyclerView.setAdapter(adapter);
        adapter.setItemSelectionListener(this);
    }

    private synchronized void onUpdate() {
        for(Preview_DATA data: listData) {
            String worker = data.professionalID();
            String newdescription = data.getDescription();
            String[] descriptionelements = newdescription.split("\n");
            newdescription = "";
            boolean distanceUpdate = distances.containsKey(worker), ratingUpdate;
            float newRatingValue = conf[data.getID()].getRating();
            synchronized (ratings) {
                ratingUpdate = ratings.containsKey(worker);
                if(ratingUpdate) newRatingValue = ratings.get(worker);
            }
            for(int k = 0; k < descriptionelements.length; k++) {
                if((!descriptionelements[k].contains("Απόσταση") || !distanceUpdate)) {
                    if (!descriptionelements[k].contains("Βαθμολογία") || !ratingUpdate)
                        newdescription += (descriptionelements[k] + "\n");
                }
            }
            if(ratingUpdate) {
                newdescription += "Βαθμολογία: " + newRatingValue + "/5\n";
                conf[data.getID()].setRating(newRatingValue);
            }
            if(distanceUpdate) {
                data.setDescription(newdescription + "Απόσταση: " + distances.get(worker));
                conf[data.getID()].setDistance(distances.get(worker));
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(listData);
        recyclerView.setAdapter(adapter);
        adapter.setItemSelectionListener(this);
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
                    Toast.makeText(ServicePreview.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}
            };

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private boolean initActualData() {
        boolean isSuccess = false;
        mem = new ImageMemory();
        try {
            String exploration_query = "select count(*) from " + table + " " + restcommand + " available = 1;";
            Log.e("QUERYING DB1", exploration_query);
            Statement exploration_stmt = conn.con.createStatement();
            ResultSet ers = null;
            ers = exploration_stmt.executeQuery(exploration_query);

            if (!ers.next()) return false;
            int capacity = Integer.parseInt(ers.getObject(1).toString());
            conf = new ECustomService[capacity];
            Log.e("CONFIG_ARRAY_SIZE", capacity + "");

            String query = "select * from " + table + " " + restcommand + " available = 1;";
            Log.e("QUERYING DB2", query);
            Statement stmt = conn.con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(query);
            int i = 0, j = 0;

            while (rs.next()) {
                int smallInt = Integer.parseInt(rs.getObject(5).toString());
                boolean availability = (smallInt > 0) ? true : false;
                if (availability) {
                    String id = rs.getObject(1).toString();
                    String name = rs.getObject(2).toString();
                    String surname = rs.getObject(3).toString();
                    String tel = rs.getObject(4).toString();
                    String description = rs.getObject(6).toString();
                    String images = rs.getObject(7).toString();
                    float rating = Float.parseFloat(rs.getObject(10).toString());
                    int imageindex = Integer.parseInt(id.substring(2, 3));
                    conf[i] = new ECustomService(id, name, surname, description, images, tel, rating, mem.specialists.get(imageindex-1), availability);
                    Log.e("CONFIGURATION", (i) + ". " + conf[i].getName());
                    URL url = null;
                    listData.add(new Preview_DATA(id, mem.specialists.get(imageindex - 1), name + " " + surname, "Τηλέφωνο: " + tel));
                    if (rating != 0) {
                        listData.get(i).setDescription(listData.get(i).getDescription() + "\nΒαθμολογία: " + rating + "/5");
                        synchronized (ratings) {
                            Log.e("ADDING_SCORE", id + " scored " + rating + " points!");
                            ratings.put(id, rating);
                        }
                    }
                    if (distances != null && distances.containsKey(id)) {
                        listData.get(i).setDescription(listData.get(i).getDescription() + "\nΑπόσταση: " + distances.get(id));
                        conf[i].setDistance(distances.get(id));
                    }
                    listData.get(i).setID(i++);
                    isSuccess = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu2, menu2);
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listData = new ArrayList<Preview_DATA>();
    }

    @Override
    public void onItemSelected(Preview_DATA item) {

    }
}