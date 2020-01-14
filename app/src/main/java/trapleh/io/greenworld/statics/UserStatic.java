package trapleh.io.greenworld.statics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserStatic {
    public static DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("users");
    public static FirebaseUser  currentUser = FirebaseAuth.getInstance().getCurrentUser();
}
