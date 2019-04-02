package ca.ualberta.cmput301w19t05.sharebook.models;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User
 * this class stores user information, each instance refers to a user which contains userID, user name, email, image, and ratting by other users.
 * <p>
 * Public Methods:
 * getters & setters
 */

public class User implements Parcelable {
    private String userID;
    private String username;
    private String email;
    private List<Long> rates;


    public User(String userID, String username, String email, Uri image, int rate_count, double rate) {
        this.userID = userID;
        this.username = username;
        this.email = email;
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


    public void setRates(ArrayList<Long> rates) {
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

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email",email);
        result.put("userID",userID);
        result.put("username",username);
        result.put("rates",rates);
        return result;
    }
}
