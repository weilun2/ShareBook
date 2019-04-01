package ca.ualberta.cmput301w19t05.sharebook.models;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String userID;
    private String username;
    private String email;
    private Uri image;
    public List<Long> rates;
    public User(String userID, String username, String email, Uri image) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.image = image;


    }

    public User(String userID, String username, String email, Uri image, int rate_count, double rate) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.image = image;

    }


    public User(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.rates = new ArrayList<>();
    }

    public List<Long> getRates() {
        return rates;
    }

    public void setRates(List<Long> rates) {
        if (rates==null){
            this.rates = new ArrayList<>();
        }else{
            this.rates = rates;
        }

    }

    public User() {

    }
    public double average(){
        double sum = 0.0;
        if(rates!=null && !rates.isEmpty()) {
            for (Long mark : rates) {
                sum += mark;
            }
            return sum / rates.size();
        }
        return sum;
    }
    public int getRateCount(){
        if (rates==null){
            return 0;
        }
        else {
            return rates.size();
        }
    }

    public Uri getImage() {
        return image;
    }

    public void setUserimage(Uri image) {
        this.image = image;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public void editShelf(Intent data){

    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in) {
        userID = in.readString();
        username = in.readString();
        email = in.readString();
        image = Uri.parse(in.readString());


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(String.valueOf(image));
    }
}
