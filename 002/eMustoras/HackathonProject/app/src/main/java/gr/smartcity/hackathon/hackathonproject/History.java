package gr.smartcity.hackathon.hackathonproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
 * Αυτή η δραστηριότητα υλοποιεί μια διεπαφή μήτρας για περατωθείσες υπηρεσίες.
 */
public class History extends AppCompatActivity {

    DatabaseConnection conn;
    ECustomService professional ;
    HashMap<String, ECustomUser> users;
    int capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        conn = (DatabaseConnection)getIntent().getSerializableExtra("DB_CONN");
        professional = (ECustomService)getIntent().getSerializableExtra("SERV_DET");

        if(initHistoryData()) initHistoryTable();
    }

    private boolean initHistoryData() {
        boolean isSuccess = false;
        try {
            String exploration_query = "select count(distinct custid) from history where wid like '" + professional.getID() +"'";
            Statement exploration_stmt = conn.con.createStatement();
            ResultSet ers = null;
            ers = exploration_stmt.executeQuery(exploration_query);

            if (!ers.next()) return false;
            capacity = Integer.parseInt(ers.getObject(1).toString());
            users = new HashMap<String, ECustomUser>(capacity);
            Log.e("CONFIG_ARRAY_SIZE", capacity+"");

            String query = "select * from customers where custid in (select custid from history where wid like '" + professional.getID() + "')";
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
                users.put(id,new ECustomUser(id, name, surname, address, tel, email));
                Log.e("USER", users.get(id).getName());
            }

            query = "select custid, acceptDate from history where wid like '" + professional.getID() + "'";
            Log.e("QUERYING3", query);
            stmt = conn.con.createStatement();
            rs = stmt.executeQuery(query);
            i = 0;

            while (rs.next()) {
                String id = rs.getObject(1).toString();
                String date = rs.getObject(2).toString();
                users.get(id).setDate(date);
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public void initHistoryTable(){
        TableLayout pendingTable = (TableLayout) findViewById(R.id.historyTable);

        int i = 0; String x; Set keyset = users.keySet();
        for (Iterator iterator = keyset.iterator(); (i++) < keyset.size(); ) {
            x = (String)iterator.next();
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView username = new TextView(this);
            TextView address = new TextView(this);
            TextView date = new TextView(this);

            username.setText(String.format("%s   ", users.get(x).getName()));
            username.setTextColor(Color.BLACK);
            address.setText(String.format("%s   ", users.get(x).getAddress()));
            address.setTextColor(Color.BLACK);
            String[] dateArr = users.get(x).getDate().split(" ");
            date.setText(String.format("%s   ", dateArr[0]));
            date.setTextColor(Color.BLACK);

            row.addView(username);
            row.addView(address);
            row.addView(date);
            pendingTable.addView(row,i);
        }
    }
}