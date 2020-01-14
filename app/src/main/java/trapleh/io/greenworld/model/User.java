package trapleh.io.greenworld.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable {

    public String username;
    public String phoneNo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String phoneNo) {
        this.username = username;
        this.phoneNo = phoneNo;
    }

}