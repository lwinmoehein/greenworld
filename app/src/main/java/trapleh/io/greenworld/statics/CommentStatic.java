package trapleh.io.greenworld.statics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentStatic {
    public static DatabaseReference commentRef= FirebaseDatabase.getInstance().getReference().child("comments");
}
