package gr.smartcity.hackathon.hackathonproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Αυτή η δραστηριότητα υλοποιεί ένα σύνολο δημόσιων υπηρεσιών.
 */
public class PublicServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_services);
        DatabaseConnection conn = (DatabaseConnection)getIntent().getSerializableExtra("DB_CONN");
        ECustomUser user = (ECustomUser)getIntent().getSerializableExtra("USER_DET");
    }
}
