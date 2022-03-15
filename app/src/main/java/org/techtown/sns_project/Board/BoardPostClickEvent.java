package org.techtown.sns_project.Board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BoardPostClickEvent extends AppCompatActivity {

    public ImageView image_profile, post_image, like, comment, save, more;
    public TextView username, likes, publisher, description, comments;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference likesRef;

    HashMap<String,Object> List = new HashMap<String,Object>();
    HashMap<String, Object> Map_like = new HashMap<>();
    static boolean liked = true;
    static String listImgURL2;
    static String post_description;
    static String post_publisher;
    static String post_postid,post_document;
    static int nrlikes;

    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listPostid = new ArrayList<>();
    static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<Integer> listNrlikeds = new ArrayList<Integer>();
    public ArrayList<PostInfo> listData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);
        Intent intent = getIntent();
        listImgUrl = (ArrayList<String>)getIntent().getSerializableExtra("listImgUrl");
        listDescription = (ArrayList<String>)getIntent().getSerializableExtra("listDescription");
        listPublisher = (ArrayList<String>)getIntent().getSerializableExtra("listPublisher");
        listDocument = (ArrayList<String>)getIntent().getSerializableExtra("listDocument");



        int position = getIntent().getIntExtra("position",1);
        listImgURL2 = listImgUrl.get(position);
        post_description = listDescription.get(position);
        post_publisher = listPublisher.get(position);
        post_document = listDocument.get(position);


        System.out.println(listImgUrl);

        post_image = findViewById(R.id.post_image);
        description = findViewById(R.id.description);
        publisher = findViewById(R.id.publisher);
        username = findViewById(R.id.username);
        like = findViewById(R.id.like);
        comment = findViewById(R.id.comment);
        save = findViewById(R.id.save);
        likes = findViewById(R.id.likes);
        comments = findViewById(R.id.comments);
        more = findViewById(R.id.more);


        Glide.with(this).load(listImgURL2).error(R.drawable.ic_launcher_background).into(post_image);

        //글 설명
        if (post_description != null)
            description.setText(post_description);
        else
        {
            description.setText("NULL");
        }

        //Publisher
        if (post_publisher != null)
            publisher.setText(post_publisher);
        else
        {
            publisher.setText("NULL");
        }

        //publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
        isLiked(user.getUid(),like);
        nrLikes(likes);

      /* isSaved(post.getPostid(), holder.save);
        getCommetns(post.getPostid(), holder.comments);*/


        //Like
        CollectionReference likesRef = db.collection("board").document(post_document).collection("Likes");
        //If click like button
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(liked){
                    nrlikes++;
                    likes.setText(String.valueOf(nrlikes) + "likes");
                    like.setImageResource(R.drawable.ic_liked);
                    Map_like.put("user",user.getUid());

                    likesRef.document(user.getUid())
                            .set(Map_like)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("like","Document written success");
                                }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Like","Fail",e);
                                }
                            });
                    liked = false;
                }
                else{
                    nrlikes--;
                    likes.setText(String.valueOf(nrlikes) + "likes");
                    like.setImageResource(R.drawable.ic_like);
                    likesRef.document(user.getUid())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Unlike", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Unlike", "Error deleting document", e);
                                }
                            });
                    liked = true;
                }

            }
        });


    }

    private void nrLikes(TextView likes) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference likesRef = db.collection("board").document(post_document).collection("Likes");
        likesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                nrlikes = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                nrlikes++;
                            }
                        } else {
                            Log.d("nrlikes", "Error getting documents: ", task.getException());
                        }
                        System.out.println(nrlikes);
                        likes.setText(String.valueOf(nrlikes) + "likes");
                    }
                });


    }

    private void isLiked(String uid, ImageView like) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference likesRef = db.collection("board").document(post_document).collection("Likes");
        likesRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        like.setImageResource(R.drawable.ic_liked);
                        liked = false;
                    } else {
                        like.setImageResource(R.drawable.ic_like);
                        liked = true;
                    }
                } else {
                    Log.d("isliked", "Failed with: ", task.getException());
                }
            }
        });




    }




}
