package gr.smartcity.hackathon.hackathonproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView imgV3 = (ImageView) findViewById(R.id.regActImg);
        int id = R.drawable.background3;
        new ScaleImg(imgV3, id, this);

        Button register = (Button) findViewById(R.id.rCompleted);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText rUsername = (EditText) findViewById(R.id.rUsername);
                EditText rPassword = (EditText) findViewById(R.id.rPassword);
                EditText rConfirmedPassword = (EditText) findViewById(R.id.rConfirmedPassword);
                EditText email = (EditText) findViewById(R.id.rEmail);
               /* TextView textView= (TextView) findViewById(R.id.textView);
                textView.setText(rPassword+"");*/
                String email2 = email.getText().toString();
                String username = rUsername.getText().toString();
                String password = rPassword.getText().toString();
                String password2 = rConfirmedPassword.getText().toString();
                if (password.equals(password2) && password != null && !password.equals("") && username != null && !username.equals("") && email != null && !email.equals("") && email2.contains("@"))/*will be adjusted to database data*/ {

                    Intent intent1 = new Intent(getApplicationContext(), Main2Activity.class);
                    intent1.putExtra("gr.smartcity.hackathon.hackathonproject.MainActivity.OK", "Registration completed successfully.");
                    startActivity(intent1); //going to the next page
                    //missing code
                } else {
                    Context con2 = getApplicationContext();
                    CharSequence text = "Error";

                    if (!password.equals(password2)) {
                        text = "Passwords dont match.";
                    } else if (password.equals("") || username.equals("") || email.equals("") || password2.equals("") || password == null || username == null || email == null) {
                        text = "Fields please.";
                    } else if (!email2.contains("@")) {
                        text = "Wrong email format.";
                    }
                   /*
                        Context con2 = getApplicationContext();
                        CharSequence text="Passwords dont match.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast=Toast.makeText(con2,text,duration);
                        toast.show();
                    */
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(con2, text, duration);
                    toast.show();
                }
            }
        });
    }
}
