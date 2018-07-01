package gr.smartcity.hackathon.hackathonproject;

public class ECustomPreview {
    protected ECustomUser user;
    protected Vec2<Float, Float> coord;

    ECustomPreview(ECustomUser user, float lat, float lon) {
        this.user = user;
        this.coord = new Vec2<>(lat, lon);
    }

    public float getLatitude(){
        if(coord != null) return coord.getTValue();
            else return 0.0F;
    }

    public float getLongitude() {
        if(coord != null) return coord.getYValue();
            else return 0.0F;
    }

    public String getName() {
        if(user != null) return user.getName() + " " + user.getSurname();
            else return null;
    }

    public String getDescription() {
        if(user != null) return ("ID: " + user.getID() + "\nAddress: " + user.getAddress() + "\nTelephone: " + user.getTelephone() + "\nE-mail: " + user.getEmail());
            else return null;
    }
}
