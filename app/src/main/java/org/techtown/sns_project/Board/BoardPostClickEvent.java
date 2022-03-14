package org.techtown.sns_project.Board;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardPostClickEvent extends AppCompatActivity {

    private ImageView post_image;
    private TextView description;
    private TextView publisher;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String,Object> List = new HashMap<String,Object>();

    static String listImgURL2;
    static String post_description;
    static String post_publisher;
    static String post_postid;

    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listPostid = new ArrayList<>();

    public ArrayList<PostInfo> listData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);
        Intent intent = getIntent();
        listImgUrl = (ArrayList<String>)getIntent().getSerializableExtra("listImgUrl");
        listDescription = (ArrayList<String>)getIntent().getSerializableExtra("listDescription");
        listPublisher = (ArrayList<String>)getIntent().getSerializableExtra("listPublisher");
//
        int position = getIntent().getIntExtra("position",1);
        listImgURL2 = listImgUrl.get(position);
        post_description = listDescription.get(position);
        post_publisher = listPublisher.get(position);

        System.out.println(listImgUrl);

        post_image = findViewById(R.id.post_image);
        description = findViewById(R.id.description);
        publisher = findViewById(R.id.publisher);

        Glide.with(this).load(listImgURL2).error(R.drawable.ic_launcher_background).into(post_image);


        if (post_description != null)
            description.setText(post_description);
        else
        {
            description.setText("NULL");
        }


        if (post_publisher != null)
            publisher.setText(post_publisher);
        else
        {
            publisher.setText("NULL");
        }




    }


}
