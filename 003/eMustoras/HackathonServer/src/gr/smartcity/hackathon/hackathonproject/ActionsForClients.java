
package gr.smartcity.hackathon.hackathonproject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ActionsForClients extends Thread {
    Socket connection = null;
    ObjectInputStream in;
    ObjectOutputStream out;
    Vec2 initial;
    Vec2<Float, Float> coord;
    Vec2<String, Integer> rating;
           

    public ActionsForClients(Socket s) {
        try {
            connection = s;
            in = new ObjectInputStream(connection.getInputStream());
            out = new ObjectOutputStream(connection.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        /* Read user coordinates
         */
        try {
            initial = (Vec2)in.readObject();
            System.out.println("Mode = " + initial.getMode());
            if(initial.getMode() == 'P') {                                        // Professional Greeting
                coord = (Vec2<Float, Float>) initial;
                System.out.println("Professional coordinates: " + coord);
                String id = (String)in.readObject();
                synchronized(HackathonServer.professionals) {
                    HackathonServer.professionals.put(id, coord);
                }
                System.out.println(HackathonServer.professionals.get(id));
            } else if (initial.getMode() == 'C') {                            // Customer Greeting
                coord = (Vec2<Float, Float>) initial;
                System.out.println("User coordinates: " + coord);
                Collection<Vec2<Float, Float>> set = HackathonServer.professionals.values();
                Set<String> keys = HackathonServer.professionals.keySet();
                Iterator<String> keyGetter = keys.iterator();
                HashMap<String, Float> dist = new HashMap<String, Float>();
                float radius = 6371000;
                for(Vec2<Float, Float> loc: set) {
                    float prod1 = (float)(Math.pow((Math.sin((loc.getTValue() - coord.getTValue())/2)),2));
                    float prod2_1 = (float)(Math.cos(loc.getTValue() ) * Math.cos(coord.getTValue() ) );
                    float prod2_2 = (float)(Math.pow((Math.sin((loc.getYValue() - coord.getYValue())/2)),2));
                    float prod2 = prod2_1 * prod2_2;
                    float prod_brack = (float)Math.sqrt(prod1 + prod2);
                    float arcsin = (float)Math.asin(prod_brack);
                    float diff = 2 * radius * arcsin;
                    dist.put(keyGetter.next(), diff);
                } 
                out.writeObject(dist);
                System.out.println("Distances stored: " + dist.size());
            } else if (initial.getMode() == 'R') {                            // Rating Assignment
                rating = (Vec2<String, Integer>) initial;
                ArrayList<Integer> x = HackathonServer.ratings.get(rating.getTValue());
                if(x == null) x = new ArrayList<Integer>();
                x.add(rating.getYValue());
                int counter = 0, total = 0;
                float average;
                for(int i = 0; i < x.size(); i++) {
                    Integer y = new Integer(x.get(i));
                    counter = y.intValue();
                    total = total + counter;
                }
                System.out.println("Total of : " + total + " out of " + x.size()  + " elements.");
                average = ((float)total)/x.size();
                out.writeObject(average);
                synchronized(HackathonServer.ratings) {
                    HackathonServer.ratings.put(rating.getTValue(), x);
                }
                System.out.println("Calculating average: " + average);
            } else if (initial.getMode() == 'I') {                            // Rating Initialization
                Vec2<HashMap<String, Float>, Byte> temp = (Vec2<HashMap<String, Float>, Byte>) initial;
                Set<String> keys = temp.getTValue().keySet();
                synchronized(HackathonServer.ratings) {
                    for(String k: keys) {
                        if(HackathonServer.ratings.get(k) == null) {
                            ArrayList x = new ArrayList<Integer>();
                            x.add(temp.getTValue().get(k));
                            HackathonServer.ratings.put(k, x);
                        }
                    }
                 }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendQueries(Vec2<Float, Float> result) {
        try {
            out.writeObject(result);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}