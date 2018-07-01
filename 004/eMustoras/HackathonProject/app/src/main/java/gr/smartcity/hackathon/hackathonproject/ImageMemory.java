package gr.smartcity.hackathon.hackathonproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Αυτή η κλάση υλοποιεί έναν περιέκτη εικόνων της εφαρμογής.
 */
public class ImageMemory {
    static protected List<Integer> specialists;

    public ImageMemory(int mode) {
        if(mode == 0) {
            specialists = new ArrayList<Integer>(3);
            specialists.add(R.drawable.plumber);
            specialists.add(R.drawable.freezer);
            specialists.add(R.drawable.gardener);
        } else if(mode == 1) {
            specialists = new ArrayList<Integer>(4);
            specialists.add(R.drawable.garbage);
            specialists.add(R.drawable.bogias);
            specialists.add(R.drawable.congestion);
            specialists.add(R.drawable.parking);
        }
    }

}
