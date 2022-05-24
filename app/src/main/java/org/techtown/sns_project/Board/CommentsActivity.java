package org.techtown.sns_project.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.techtown.sns_project.Board.profile.ProfileActivity;
import org.techtown.sns_project.Model.Comment;
import org.techtown.sns_project.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    EditText addcomment;
    TextView post;
    CircleImageView image_profile;

    String commentid; //getuid : 사용자의 키값


    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String post_document,post_publisher;
    static HashMap<String,Object> List;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();

        post_publisher = intent.getStringExtra("post_publisher");
        post_document = intent.getStringExtra("post_document");
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter();
        //commentAdapter = new CommentAdapter(this, commentList, postid);

        recyclerView.setAdapter(commentAdapter);

        post = findViewById(R.id.post);
        addcomment = findViewById(R.id.add_comment);
        image_profile = findViewById(R.id.image_profile);

        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference(); //스토리지를 참조한다
        storageRef.child("profile_images/" + user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                Glide.with(getApplicationContext()).load(uri).into(image_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //실패시
                image_profile.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });

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

        commentAdapter.setOnItemClickListener (new CommentAdapter.OnItemClickListener()
        {
            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick (View v,int position){
                System.out.println("position"+position);
                StartActivity(position);
            }
        });

    }
    private void StartActivity(int position) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("post_publisher",commentList.get(position).getGetuid());
        System.out.println("post_publisher"+commentList.get(position).getGetuid());
        startActivity(intent);
    }
    private void addComment(){


        CollectionReference CommentsRef = db.collection("board").document(post_document).collection("Comments");
        CollectionReference CommentsRef_user = db.collection("users").document(user.getUid()).collection("board").document(post_document).collection("Comments");


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("commentid", commentid);
        hashMap.put("getuid", user.getUid());

        CommentsRef.document(user.getUid())
                .set(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Comments","Document written success");
                        finish();
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
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

                            Comment data = new Comment((String)List.get("comment"),(String)List.get("commentid"),(String)List.get("getuid"));
                            commentList.add(data);
                            commentAdapter.addItem(data);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }
                });

            }



}
