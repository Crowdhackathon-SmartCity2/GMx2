package gr.smartcity.hackathon.hackathonproject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Αυτή η δραστηριότητα υλοποιεί την αναλυτική προεπισκόπιση μιας δημόσιας υπηρεσίας.
 */
public class ConfigurationActivity2 extends AppCompatActivity {

    ECustomUser user;
    DatabaseConnection conn;
    ECustomService serv;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_preview);

        conn = (DatabaseConnection) getIntent().getSerializableExtra("DB_CONN");
        user = (ECustomUser) getIntent().getSerializableExtra("USER_DET");
        serv = (ECustomService) getIntent().getSerializableExtra("SERV_DET");
        TextView text1 = (TextView)findViewById(R.id.textView1);
        TextView text2 = (TextView)findViewById(R.id.textViewDet);
        Button reserv = (Button)findViewById(R.id.button2);
        TextView t1 = (TextView)findViewById(R.id.rate_me);
        Button rate = (Button)findViewById(R.id.submit);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setVisibility(View.GONE);
        rate.setVisibility(View.GONE);
        t1.setVisibility(View.GONE);
        ImageView image = (ImageView)findViewById(R.id.config_image);
        Drawable draw = getResources().getDrawable(serv.getImageID());
        Log.e("DRAWABLE", serv.getImageID()+"");
        image.setBackground(draw);
        text1.setText(serv.getName() + " " + serv.getSurname());
        text2.setText(serv.getDescription() + "\nΤηλέφωνο: " + serv.getTelephone());
        if(serv.getRating() != 0) text2.setText(text2.getText() + "\nΒαθμολογία: " + serv.getRating() + "/5");
        if(serv.getDistance() != -1) text2.setText(text2.getText() + "\nΑπόσταση: " + serv.getDistance());

        reserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String v1 = serv.getID(), v2 = user.getID();
                    String exploration_query = "select confirm from public_pending where wid like '" + v1 + "' and custid like '" + v2 + "'";
                    Statement exploration_stmt = conn.con.createStatement();
                    ResultSet ers = null;
                    ers = exploration_stmt.executeQuery(exploration_query);

                    if (!ers.next()) {
                        String query = "insert into public_pending(wid, custid) values ('" + v1 + "', '" + v2 + "')";
                        Statement stmt = conn.con.createStatement();
                        stmt.executeUpdate(query);
                    } else Toast.makeText(ConfigurationActivity2.this, "You have already submitted a request!", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
