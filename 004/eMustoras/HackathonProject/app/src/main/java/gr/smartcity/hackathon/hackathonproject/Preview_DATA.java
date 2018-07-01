package gr.smartcity.hackathon.hackathonproject;

/**
 * This class implements a dymamic graphical data container for Municipal Services.
 */
public class Preview_DATA {
    private int nodalID;
    private int ImageId;
    private String id;
    private String name;
    private String description;

    public Preview_DATA(String id, int imageId, String name, String description) {
        this.id = id;
        ImageId = imageId;
        this.name = name;
        this.description = description;
    }

    public void setID(int ID) {
        this.nodalID = ID;
    }

    public String professionalID() {
        return this.id;
    }

    public int getID() {
        return this.nodalID;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
