package trapleh.io.greenworld.statics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostStatic {
    public static DatabaseReference postRef= FirebaseDatabase.getInstance().getReference().child("posts");
    public static StorageReference postImageRef= FirebaseStorage.getInstance().getReference().child("posts/postimages/");


}
