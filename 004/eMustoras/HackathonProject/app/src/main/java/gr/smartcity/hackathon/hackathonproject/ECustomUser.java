package gr.smartcity.hackathon.hackathonproject;

import java.io.Serializable;

/**
 * Αυτή η κλάση αναπαριστά ένα χρήστη της εφαρμογής.
 */
public class ECustomUser implements Serializable {
    private String id, name, surname, address, telephone, email, date;

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() { return surname; }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ECustomUser(String id, String name, String surname, String address, String telephone, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
    }

}
