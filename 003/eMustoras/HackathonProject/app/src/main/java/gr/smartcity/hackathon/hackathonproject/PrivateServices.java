package gr.smartcity.hackathon.hackathonproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Αυτή η δραστηριότητα υλοποιεί ένα σύνολο ιδιωτικών υπηρεσιών.
 */
public class PrivateServices extends AppCompatActivity {

    DatabaseConnection conn;
    ECustomUser user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_services);
        conn = (DatabaseConnection)getIntent().getSerializableExtra("DB_CONN");
        user = (ECustomUser)getIntent().getSerializableExtra("USER_DET");
        Button plumbers = (Button)findViewById(R.id.button1);
        plumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ServicePreview.class);
                intent.putExtra("DB_CONN", conn);
                intent.putExtra("USER_DET", user);
                intent.putExtra("SER_TYPE", "private");
                intent.putExtra("SPEC", "plumber");
                startActivity(intent);
            }
        });

        Button freezers = (Button)findViewById(R.id.button2);
        freezers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ServicePreview.class);
                intent.putExtra("DB_CONN", conn);
                intent.putExtra("USER_DET", user);
                intent.putExtra("SER_TYPE", "private");
                intent.putExtra("SPEC", "freezer");
                startActivity(intent);
            }
        });

        Button gardeners = (Button)findViewById(R.id.button3);
        gardeners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ServicePreview.class);
                intent.putExtra("DB_CONN", conn);
                intent.putExtra("USER_DET", user);
                intent.putExtra("SER_TYPE", "private");
                intent.putExtra("SPEC", "gardener");
                startActivity(intent);
            }
        });

        Button allservices = (Button)findViewById(R.id.button4);
        allservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ServicePreview.class);
                intent.putExtra("DB_CONN", conn);
                intent.putExtra("USER_DET", user);
                intent.putExtra("SER_TYPE", "allprivate");
                startActivity(intent);
            }
        });
    }
}
