package trapleh.io.greenworld.statics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentLikeStatic {
    public static DatabaseReference likeRef= FirebaseDatabase.getInstance().getReference().child("commentlikes");

}
