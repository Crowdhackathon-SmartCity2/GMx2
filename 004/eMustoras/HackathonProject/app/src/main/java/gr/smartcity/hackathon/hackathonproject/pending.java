package gr.smartcity.hackathon.hackathonproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Αυτή η δραστηριότητα υλοποιεί μια διεπαφή μήτρας για εκκρεμείς υπηρεσίες.
 */
public class pending extends AppCompatActivity {

    DatabaseConnection conn;
    ECustomService professional ;
    int capacity, current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        conn = (DatabaseConnection)getIntent().getSerializableExtra("DB_CONN");
        professional = (ECustomService)getIntent().getSerializableExtra("SERV_DET");
        if(initPendingData()) initPendingTable();
    }

    private boolean initPendingData() {
        boolean isSuccess = false;
        try {
            String exploration_query = "select count(distinct custid) from pending where wid like '" + professional.getID() +"'";
            Log.e("QUERYING1", exploration_query);
            Statement exploration_stmt = conn.con.createStatement();
            ResultSet ers = null;
            ers = exploration_stmt.executeQuery(exploration_query);

            if (!ers.next()) return false;
            capacity = Integer.parseInt(ers.getObject(1).toString());
            ActivityForProfessionals.users1 = new HashMap<String, ECustomUser>(capacity);
            Log.e("CONFIG_ARRAY_SIZE", capacity+"");

            String query = "select * from customers where custid in (select custid from pending where wid like '" + professional.getID() + "')";
            Log.e("QUERYING2", query);
            Statement stmt = conn.con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(query);
            int i = 0;

            while (rs.next()) {
                String id = rs.getObject(1).toString();
                String name = rs.getObject(3).toString();
                String surname = rs.getObject(4).toString();
                String tel = rs.getObject(5).toString();
                String address = rs.getObject(6).toString();
                Log.e("ADDRESS1", address);
                String email = rs.getObject(7).toString();
                ActivityForProfessionals.users1.put(id,new ECustomUser(id, name, surname, address, tel, email));
                Log.e("USER", ActivityForProfessionals.users1.get(id).getName());
            }

            query = "select custid, requestDate from pending where wid like '" + professional.getID() + "'";
            Log.e("QUERYING3", query);
            stmt = conn.con.createStatement();
            rs = stmt.executeQuery(query);
            i = 0;

            while (rs.next()) {
                String id = rs.getObject(1).toString();
                String date = rs.getObject(2).toString();
                ActivityForProfessionals.users1.get(id).setDate(date);
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public void initPendingTable() {
        TableLayout pendingTable = (TableLayout) findViewById(R.id.pendingTable);

        int i = 0; String x; Set keyset = ActivityForProfessionals.users1.keySet();
        for (Iterator iterator = keyset.iterator(); (i++) < keyset.size(); ) {
            x = (String)iterator.next();
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
            row.setLayoutParams(lp);
            TextView username = new TextView(this);
            TextView address = new TextView(this);
            TextView date = new TextView(this);
            Button addBtn = new Button(this);

            username.setText(String.format("%s %s   ", ActivityForProfessionals.users1.get(x).getName(), ActivityForProfessionals.users1.get(x).getSurname()));
            username.setTextColor(Color.BLACK);
            Log.e("ADDRESS2", ActivityForProfessionals.users1.get(x).getAddress());
            address.setText(String.format("%s   ", ActivityForProfessionals.users1.get(x).getAddress()));
            address.setTextColor(Color.BLACK);
            String[] dateArr = ActivityForProfessionals.users1.get(x).getDate().split(" ");
            Log.e("DATE", dateArr[0]);
            date.setText(String.format("%s   ", dateArr[0]));
            date.setTextColor(Color.BLACK);
            addBtn.setText("Accept");

            final String actual = x;

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String v1 = professional.getID(), v2 = ActivityForProfessionals.users1.get(actual).getID();
                        String query = "insert into history(wid, custid) values ('" + v1 + "', '" + v2 + "')";
                        Statement stmt = conn.con.createStatement();
                        stmt.executeUpdate(query);
                        query = "delete from pending where wid like '" + v1 + "' and custid like '" + v2 + "'";
                        stmt = conn.con.createStatement();
                        stmt.executeUpdate(query);
                        finish();
                        startActivity(getIntent());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            row.addView(username);
            row.addView(address);
            row.addView(date);
            row.addView(addBtn);
            pendingTable.addView(row, (current++)+1);
        }
    }
}
