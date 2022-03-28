package org.techtown.sns_project.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.Model.Comment;
import org.techtown.sns_project.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    EditText addcomment;
    ImageView image_profile;
    TextView post;

    String commentid; //getuid : 사용자의 키값

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String post_document;
    static HashMap<String,Object> List;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();

        post_document = intent.getStringExtra("post_document");
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter();
        //commentAdapter = new CommentAdapter(this, commentList, postid);

        recyclerView.setAdapter(commentAdapter);

        post = findViewById(R.id.post);
        addcomment = findViewById(R.id.add_comment);
        image_profile = findViewById(R.id.image_profile);


        db.collection("users").document(user.getUid()).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        commentid = document.getData().get("name").toString();

                    }
                });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();

                }
            }
        });

//        getImage();
        readComments();

    }

    private void addComment(){


        CollectionReference CommentsRef = db.collection("board").document(post_document).collection("Comments");


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("commentid", commentid);

        CommentsRef.document(user.getUid())
                .set(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Comments","Document written success");
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Comments","Fail",e);
                    }
                });
        addcomment.setText("");

        }


    private void readComments(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        CollectionReference CommentsRef = db.collection("board").document(post_document).collection("Comments");
        CommentsRef.get().
                addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        commentList.clear();
                        commentAdapter.listData.clear();

                        for(QueryDocumentSnapshot document : task.getResult()) {
                            List = (HashMap<String, Object>) document.getData();
                            //Comment data = new Comment((String)List.get("comment"),(String)List.get("publisher"),(String)List.get("commentid"));
                            Comment data = new Comment((String)List.get("comment"),(String)List.get("commentid"));
                            commentAdapter.addItem(data);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }
                });

            }




}
