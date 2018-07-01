package gr.smartcity.hackathon.hackathonproject;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Αυτή η κλάση υλοποιεί την συγχρονισμένη διασύνδεση της εφαρμογής με τον server.
 */
public class ServerConnection {

    Socket requestSocket;
    ObjectInputStream in;
    ObjectOutputStream out;
    String id;
    char mode;

    public void initialize(char mode) {
        try {
            requestSocket = new Socket("10.101.87.124", 4202);
            this.mode = mode;
            this.id = id;

            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ActionsForMaster sendMyLocation(Vec2<Float, Float> coord, int mode1) {
        coord.setMode(mode);
        ActionsForMaster thread = new ActionsForMaster(requestSocket, coord, in, out, mode1);
        thread.start();
        return thread;
    }

    public ActionsForMaster sendMyLocation(Vec2<Float, Float> coord, String id, int mode1) {
        coord.setMode(mode);
        ActionsForMaster thread = new ActionsForMaster(requestSocket, coord, id, in, out, mode1);
        thread.start();
        return thread;
    }

    public void informOnRatings(Vec2<String, Integer> rating) {
        rating.setMode(mode);
        ActionsForRating thread = new ActionsForRating(requestSocket, rating, in, out);
        thread.start();
    }

    public void initializeRatings() {
        synchronized (ServicePreview.ratings) {
            Vec2<HashMap<String, Float>, Byte> rating = new Vec2<>(ServicePreview.ratings, new Byte("0"));
            rating.setMode(mode);
            ActionsForRating thread = new ActionsForRating(requestSocket, in, out, rating);
            thread.start();
        }
    }

    public void initializeRatings2() {
        synchronized (PublicServices.ratings) {
            Vec2<HashMap<String, Float>, Byte> rating = new Vec2<>(ServicePreview.ratings, new Byte("0"));
            rating.setMode(mode);
            ActionsForRating thread = new ActionsForRating(requestSocket, in, out, rating);
            thread.start();
        }
    }

    public void initializeMap(Vec2<String, ArrayList<String>> coords, int mode1) {
        coords.setMode(mode);
        Log.e("CONN_DET", "Starting mapping thread...");
        ActionsForMaster thread = new ActionsForMaster(coords, requestSocket, in, out, mode1);
        thread.start();
    }
}
