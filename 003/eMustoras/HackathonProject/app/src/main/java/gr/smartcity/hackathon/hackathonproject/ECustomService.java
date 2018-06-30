package gr.smartcity.hackathon.hackathonproject;

import java.io.Serializable;

/**
 * Αυτή η κλάση αναπαριστά ένα επαγγελματία της εφαρμογής.
 */
public class ECustomService implements Serializable {
    private String id, name, surname, description, imageURL, telephone;
    private int imageID;
    private boolean availability;
    private float rating, distance;

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getImageID() {
        return imageID;
    }

    public String getTelephone() {
        return telephone;
    }

    public float getRating() {
        return rating;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setRating(float rating) { this.rating = rating; }

    public void setImageID(int id) { this.imageID = id; }

    public ECustomService(String id, String name, String surname, String description, String imageURL, String telephone, float rating, boolean availability) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.imageURL = imageURL;
        this.telephone = telephone;
        this.rating = rating;
        this.availability = availability;
        this.distance = -1;
    }

    public ECustomService(String id, String name, String surname, String description, String imageURL, String telephone, float rating, int imageID, boolean availability) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.imageURL = imageURL;
        this.telephone = telephone;
        this.rating = rating;
        this.imageID = imageID;
        this.availability = availability;
        this.distance = -1;
    }

    public ECustomService(String id, String name, String description, String imageURL, String telephone, float rating, boolean availability) {
        this.id = id;
        this.name = name;
        this.surname = "";
        this.description = description;
        this.imageURL = imageURL;
        this.telephone = telephone;
        this.rating = rating;
        this.availability = availability;
        this.distance = -1;
    }

}
