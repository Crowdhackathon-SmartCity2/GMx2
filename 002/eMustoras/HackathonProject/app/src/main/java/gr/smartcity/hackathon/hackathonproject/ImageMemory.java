package gr.smartcity.hackathon.hackathonproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Αυτή η κλάση υλοποιεί έναν περιέκτη εικόνων της εφαρμογής.
 */
public class ImageMemory {
    static protected List<Integer> specialists;

    public ImageMemory() {
        specialists = new ArrayList<Integer>(3);
        specialists.add(R.drawable.plumber);
        specialists.add(R.drawable.freezer);
        specialists.add(R.drawable.gardener);
    }

}
