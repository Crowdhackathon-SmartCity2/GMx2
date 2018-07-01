package gr.smartcity.hackathon.hackathonproject;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Αυτή η δραστηριότητα αποτελεί νήμα επικοινωνίας με το server για την ανεύρεση τοποθεσίας.
 */
public class ActionsForMaster extends Thread {
    Socket connection = null;
    ObjectInputStream in;
    ObjectOutputStream out;
    Vec2<Float, Float> packet;
    Vec2<String, ArrayList<String>> coords;
    String id;
    int mode;

    public ActionsForMaster(Socket s, Vec2<Float, Float> packet, ObjectInputStream in, ObjectOutputStream out, int mode) {
        connection = s;
        this.packet = packet;
        this.in = in;
        this.out = out;
        this.mode = mode;
    }

    public ActionsForMaster(Socket s, Vec2<Float, Float> packet, String id, ObjectInputStream in, ObjectOutputStream out, int mode) {
        connection = s;
        this.packet = packet;
        this.id = id;
        this.in = in;
        this.out = out;
        this.mode = mode;
    }

    public ActionsForMaster(Vec2<String, ArrayList<String>> packet, Socket s,  ObjectInputStream in, ObjectOutputStream out, int mode) {
        connection = s;
        this.coords = packet;
        this.in = in;
        this.out = out;
        this.mode = mode;
    }

    public void run() {
        if(mode == 0)
            this.sendQueryToMaster();
        else if(mode == 2) {
            this.sendResultsToMaster();
        }
         else if(mode == 1) {
            sendResultsToMasterPub();
        } else if (mode == 3) {
            registerMap();
        }
    }

    public void sendResultsToMaster(){
        try {
            out.writeObject(packet);
            out.writeObject(id);
            out.flush();
            ServicePreview.distances = (HashMap<String, Float>) in.readObject();
        } catch (IOException | ClassNotFoundException  e){
            e.printStackTrace();
        }
    }

    public void sendResultsToMasterPub(){
        try {
            out.writeObject(packet);
            out.writeObject(id);
            out.flush();
            PublicServices.distances = (HashMap<String, Float>) in.readObject();
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

    public void registerMap() {
        try {
            coords.setMode('M');
            out.writeObject(coords);
            Log.e("THREAD", "Writing packet...");
            out.flush();
            ActivityForProfessionals.destinations = (HashMap<String, Vec2<Float, Float>>)in.readObject();
        } catch (IOException |ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}

