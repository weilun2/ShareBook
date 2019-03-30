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
    private List<Integer> rate;



    public User(String userID, String username, String email, Uri image) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.image = image;
        this.rate = new ArrayList<>();
    }

    public User(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.rate = new ArrayList<>();
    }

    public User() {

    }

    public List<Integer> getRate() {
        return rate;
    }

    public void setRate(List<Integer> rate) {
        this.rate = rate;
    }
    public Double average(){
        Integer sum = 0;
        if (rate!=null && !rate.isEmpty()){
            for (Integer it: rate){
                sum+=it;
                
            }
            return sum.doubleValue()/rate.size();
        }
        return -1.0;
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
        rate = new ArrayList<Integer>();
        in.readList(rate,Integer.class.getClassLoader());



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
        dest.writeList(rate);
    }
}
