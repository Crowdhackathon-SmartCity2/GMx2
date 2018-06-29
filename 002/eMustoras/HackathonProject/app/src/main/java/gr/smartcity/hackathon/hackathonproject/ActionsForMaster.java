package gr.smartcity.hackathon.hackathonproject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Αυτή η δραστηριότητα αποτελεί νήμα επικοινωνίας με το server για την ανεύρεση τοποθεσίας.
 */
public class ActionsForMaster extends Thread {
    Socket connection = null;
    ObjectInputStream in;
    ObjectOutputStream out;
    Vec2<Float, Float> packet;
    String id;

    public ActionsForMaster(Socket s, Vec2<Float, Float> packet, ObjectInputStream in, ObjectOutputStream out) {
        connection = s;
        this.packet = packet;
        this.in = in;
        this.out = out;
    }

    public ActionsForMaster(Socket s, Vec2<Float, Float> packet, String id, ObjectInputStream in, ObjectOutputStream out) {
        connection = s;
        this.packet = packet;
        this.id = id;
        this.in = in;
        this.out = out;
    }

    public void run() {
        if(id == null) this.sendResultsToMaster();
        else this.sendQueryToMaster();
    }

    public void sendResultsToMaster(){
        try {
            out.writeObject(packet);
            out.flush();
            ServicePreview.distances = (HashMap<String, Float>) in.readObject();
        } catch (IOException | ClassNotFoundException  e){
            e.printStackTrace();
        }
    }

    public void sendQueryToMaster(){
        try {
            out.writeObject(packet);
            out.writeObject(id);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

