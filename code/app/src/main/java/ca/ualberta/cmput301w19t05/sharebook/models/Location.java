package ca.ualberta.cmput301w19t05.sharebook.models;

/**
 * Location
 *      store location information
 *
 * todo: this class needs to be re-designed when a map api is chosen for this project
 */
public class Location {
    private double let;
    private double lon;
    private String city;
    private String postalCode;
    private String description;

    public Location(double let, double lon, String city, String postalCode, String description) {
        this.let = let;
        this.lon = lon;
        this.city = city;
        this.postalCode = postalCode;
        this.description = description;
    }

    public double getLet() {
        return let;
    }

    public void setLet(double let) {
        this.let = let;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
