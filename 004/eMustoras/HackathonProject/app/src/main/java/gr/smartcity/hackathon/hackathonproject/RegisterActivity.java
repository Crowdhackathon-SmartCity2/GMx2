package gr.smartcity.hackathon.hackathonproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getIntent().hasExtra("com.example.user.quicklauncher.SOMETHING")) {
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageView imgV = (ImageView) findViewById(R.id.backgroundImg);
        int id = R.drawable.background;
        new ScaleImg(imgV, id, this);

        Button registration = (Button) findViewById(R.id.registerBtn);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText = (EditText) findViewById(R.id.nameText);
                EditText surnameText = (EditText) findViewById(R.id.surnameText);
                EditText usernameText = (EditText) findViewById(R.id.usernameText);
                EditText passwordText = (EditText) findViewById(R.id.passwordText);
                EditText passVerifText = (EditText) findViewById(R.id.passwordVerifText);
                EditText mailText = (EditText) findViewById(R.id.emailText);
                EditText telephoneText = (EditText) findViewById(R.id.telephoneText);
                EditText municipalityText = (EditText) findViewById(R.id.municipalityText);

                String name = nameText.getText().toString();
                String surname = surnameText.getText().toString();
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String passVerif = passVerifText.getText().toString();
                String mail = mailText.getText().toString();
                String telephone = telephoneText.getText().toString();
                String municipality = municipalityText.getText().toString();

                if (name.equals("") || surname.equals("") || username.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your credentials!", Toast.LENGTH_SHORT).show();
                } else if (!(password.equals(passVerif)) && password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Your passwords don't match!", Toast.LENGTH_SHORT).show();
                } else if (mail.equals("") || !(mail.contains("@"))){
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                } else if (!(telephone.length() == 10)) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
                } else {

                    // Pass info to database

                }

            }
        });
    }

}