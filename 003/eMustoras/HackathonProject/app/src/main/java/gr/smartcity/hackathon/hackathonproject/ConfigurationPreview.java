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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Αυτή η δραστηριότητα υλοποιεί την αναλυτική προεπισκόπιση μιας ιδιωτικής υπηρεσίας.
 */
public class ConfigurationPreview extends AppCompatActivity {

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
        Button rate = (Button)findViewById(R.id.submit);
        ImageView image = (ImageView)findViewById(R.id.config_image);
        Drawable draw = getResources().getDrawable(serv.getImageID());
        Log.e("DRAWABLE", serv.getImageID()+"");
        image.setBackground(draw);
        text1.setText(serv.getName() + " " + serv.getSurname());
        text2.setText(serv.getDescription() + "\nΤηλέφωνο: " + serv.getTelephone());
        if(serv.getRating() != 0) text2.setText(text2.getText() + "\nΒαθμολογία: " + serv.getRating() + "/5");
        if(serv.getDistance() != -1) text2.setText(text2.getText() + "\nΑπόσταση: " + serv.getDistance());

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

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
                        String query = "insert into pending(wid, custid) values ('" + v1 + "', '" + v2 + "')";
                        Statement stmt = conn.con.createStatement();
                        stmt.executeUpdate(query);
                    } else Toast.makeText(ConfigurationPreview.this, "You have already submitted a request!", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void rateMe(View view){
        float numOfStars = ratingBar.getRating();
        Toast.makeText(getApplicationContext(),
                "Thank you. Rated:" + String.valueOf(numOfStars), Toast.LENGTH_LONG).show();
        Vec2<String, Integer> rating = new Vec2<String, Integer>(serv.getID(), (int)numOfStars);
        ServerConnection myconn = new ServerConnection();
        myconn.initialize('R');
        myconn.informOnRatings(rating);
    }

}
