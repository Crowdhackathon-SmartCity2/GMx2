
package gr.smartcity.hackathon.hackathonproject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class HackathonServer {
    
    static HashMap<String, Vec2<Float, Float>> professionals;
    static HashMap<String, Vec2<Float, Float>> users;
    static HashMap<String, ArrayList<Integer>> ratings;

    public static void main(String args[]) {
        new HackathonServer(4202).startMaster();
        //new HackathonServer(4203).startMaster();
    }
    
    public int port_for_android;

    /* Define the socket that is used to handle the connection */
    ServerSocket androidSocket;

    public HackathonServer(int n3) {
        professionals = new HashMap<String,Vec2<Float,Float>>();
        users = new HashMap<String, Vec2<Float, Float>>();
        ratings = new HashMap<String, ArrayList<Integer>>();
        port_for_android = n3;
    }

    private void initialize() {
        /* Create Server Socket */
        try {
            androidSocket=new ServerSocket(port_for_android);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Master is Online!");
    }

    public void startMaster() {
        this.initialize();
        this.executeMaster();
    }

    public void executeMaster() {
        try {
            while (true) {
                /* Read the necessary information the master requires from the workers */
                try {
                    Socket connection = androidSocket.accept();
                    Thread t = new ActionsForClients(connection);
                    System.out.println("Android device successfully connected!");
                    t.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) { //CTRL + D to force stop
            e.printStackTrace();
            System.err.println("Server ending connection!");
        } finally {
            try {
                androidSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
