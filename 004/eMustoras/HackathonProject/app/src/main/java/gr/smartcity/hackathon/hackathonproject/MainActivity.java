package gr.smartcity.hackathon.hackathonproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Αυτή η δραστηριότητα υλοποιεί το login.
 */
public class MainActivity extends Activity {

    String z = "";
    Boolean isSuccess = false;
    String type, creds;
    DatabaseConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conn.ip = "195.251.248.152:22";
        conn.db = "p3199999";
        conn.un = "p3199999";
        conn.pass = "h@ck@thonGMx2018";

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        Button registerBtn = (Button) findViewById(R.id.registerBtn);

        ImageView imgV = (ImageView) findViewById(R.id.actMainImage);
        int id = R.drawable.background;
        new ScaleImg(imgV, id, this);

        if (getIntent().hasExtra("gr.smartcity.hackathon.hackathonproject.OK")) {
            Context con0 = getApplicationContext();
            CharSequence text = "Registration completed.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(con0, text, duration);
            toast.show();
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.Username);
                EditText password = (EditText) findViewById(R.id.Password);
                String user = username.getText().toString();
                String passwd = password.getText().toString();

                if (user.trim().equals("") || passwd.trim().equals("")) {
                    z = "Please enter your credentials!";
                    Context con = getApplicationContext();
                    CharSequence text = z;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(con, text, duration);
                    toast.show();
                } else {
                    try {
                        conn.con = connectionclass(conn.un, conn.pass, conn.db, conn.ip); // Connect to database
                        if (conn.con == null) {
                            z = "Please check your Internet Access!";
                        } else {
                            String query = "select * from login where user_name= '" + user.toString() + "' and pass_word = '" + passwd.toString() + "'  ";
                            Statement stmt = conn.con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                type = rs.getObject(3).toString();
                                creds = rs.getObject(1).toString();
                                z = "Login successful";
                                isSuccess = true;
                            } else {
                                z = "Please enter vaild credentials.";
                                isSuccess = false;
                            }
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = ex.getMessage();
                    }
                }
                if (isSuccess) {
                    if(type.equals("C")) {
                        Intent startIntent = new Intent(getApplicationContext(), Main2Activity.class);
                        startIntent.putExtra("EXTRA_USERNAME", creds);
                        startIntent.putExtra("DB_CONN", conn);
                        finish();
                        startActivity(startIntent);
                    } else if (type.equals("E")) {
                        Intent startIntent = new Intent(getApplicationContext(), ActivityForProfessionals.class);
                        startIntent.putExtra("EXTRA_USERNAME", creds);
                        startIntent.putExtra("DB_CONN", conn);
                        finish();
                        startActivity(startIntent);
                    } else {
                        Context con = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(con, "ILLEGAL: Access denied!", duration);
                        toast.show();
                    }
                } else {
                    Context con = getApplicationContext();
                    CharSequence text = z;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(con, text, duration);
                    toast.show();
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerPage);
            }
        });
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + "/" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("Error connection ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Error connection ", e.getMessage());
        } catch (Exception e) {
            Log.e("Error connection ", e.getMessage());
        }
        return connection;
    }
}