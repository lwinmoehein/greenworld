package trapleh.io.greenworld.statics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserStatic {
    public static DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("users");
}
