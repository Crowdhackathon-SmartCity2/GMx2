package gr.smartcity.hackathon.hackathonproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main2Activity extends AppCompatActivity {

    ECustomUser user;
    DatabaseConnection conn;
    String creds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView imgV2 = (ImageView) findViewById(R.id.actMain2Img);
        int id = R.drawable.background2;
        new ScaleImg(imgV2, id, this);

        TextView test = (TextView) findViewById(R.id.welcome);
        String text = getIntent().getExtras().getString("gr.smartcity.hackathon.hackathonproject.OK");
        creds = getIntent().getStringExtra("EXTRA_USERNAME");
        conn = (DatabaseConnection)getIntent().getSerializableExtra("DB_CONN");
        if(seekCustomerDatabase())
            test.setText(test.getText() + " " + user.getSurname() + " " + user.getName());
        else test.setText(test.getText() + " " + creds);

        Button publicBtn = (Button) findViewById(R.id.publicBtn);
        Button privateBtn = (Button) findViewById(R.id.privateBtn);
        Button emergencyBtn = (Button) findViewById(R.id.emergencyBtn);

        publicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent startIntent = new Intent(getApplicationContext(), PublicServices.class);
                //startIntent.putExtra("DB_CONN", conn);
                //startIntent.putExtra("USER_DET", user);
                //startActivity(startIntent);
            }
        });

        privateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), ServicePreview.class);
                startIntent.putExtra("DB_CONN", conn);
                startIntent.putExtra("USER_DET", user);
                startActivity(startIntent);
            }
        });

        emergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent startIntent = new Intent(getApplicationContext(), EmergencyServices.class);
               // startActivity(startIntent);
            }
        });
    }

    public boolean seekCustomerDatabase() {
        boolean isSuccess = false;
        try {
            String query = "select * from customers where logindata= '" + creds +"'";
            Log.e("QUERYING... ", query);
            Statement stmt = conn.con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                String id = rs.getObject(1).toString();
                String name = rs.getObject(3).toString();
                String surname = rs.getObject(4).toString();
                String tel = rs.getObject(5).toString();
                String address = rs.getObject(6).toString();
                String email = rs.getObject(7).toString();
                user = new ECustomUser(id, name, surname, address, tel, email);
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
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
}
