package gr.smartcity.hackathon.hackathonproject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
            requestSocket = new Socket("10.101.126.48", 4202);
            this.mode = mode;
            this.id = id;

            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ActionsForMaster sendMyLocation(Vec2<Float, Float> coord) {
        coord.setMode(mode);
        ActionsForMaster thread = new ActionsForMaster(requestSocket, coord, in, out);
        thread.start();
        return thread;
    }

    public void sendMyLocation(Vec2<Float, Float> coord, String id) {
        coord.setMode(mode);
        ActionsForMaster thread = new ActionsForMaster(requestSocket, coord, id, in, out);
        thread.start();
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
}
