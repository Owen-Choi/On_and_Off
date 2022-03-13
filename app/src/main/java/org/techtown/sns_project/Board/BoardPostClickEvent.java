package org.techtown.sns_project.Board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.BoardFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardPostClickEvent extends AppCompatActivity {

    private ImageView post_image;
    private TextView description;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String,Object> List = new HashMap<String,Object>();

    static String listImgURL2;
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPulbisher = new ArrayList<>();
    static ArrayList<String> listImgUrl = new ArrayList<>();

    public ArrayList<PostInfo> listData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);
        Intent intent = getIntent();
        listImgUrl = (ArrayList<String>)getIntent().getSerializableExtra("listImgUrl");
        listDescription = (ArrayList<String>)getIntent().getSerializableExtra("listDescription");
        listPulbisher = (ArrayList<String>)getIntent().getSerializableExtra("listPulbisher");

        int position = getIntent().getIntExtra("position",1);
        listImgURL2 = listImgUrl.get(position);



        System.out.println(listImgUrl);

        post_image = findViewById(R.id.post_image);
        description = findViewById(R.id.description);
        Glide.with(this).load(listImgURL2).error(R.drawable.ic_launcher_background).into(post_image);


       /* db.collection("enterprises").document(firebaseUser.getUid()).collection("brand").get().*/
       /* db.collection("board").document(firebaseUser.getUid()).collection("board_Data").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listDescription.clear();
                        listImgURL.clear();
                        listPulbisher.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            PostInfo data = new PostInfo((String) List.get("publisher"), (String) List.get("ImageUrl"), (String) List.get("description"));
                            listData.add(data);
                        }


                    }
                }

        if (description != null)
            description.setText(description);

        else
        {
            post_description.setText("NULL");
        }
*/
}
    }
