package co.touchlab.dogify.models;

public class Breed {
    private String name;
    private String imageURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Breed(String name, String url) {

        this.name = name;
        this.imageURL = url;
    }
}
