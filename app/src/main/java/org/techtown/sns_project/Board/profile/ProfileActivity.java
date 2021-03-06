package org.techtown.sns_project.Board.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;

import org.techtown.sns_project.Board.BoardPostClickEvent;
import org.techtown.sns_project.Board.profile.Closet.ClosetMainActivity;
import org.techtown.sns_project.Camera.Activity_codi;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.DataFormat;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private View view;
    private String TAG = "profile ";

    //??????????????????
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    String post_publisher;
    //recyclerview
    private RecyclerView recyclerView;
    private org.techtown.sns_project.Board.profile.profileAdapter profileAdapter;

    //????????? ?????? ??????
    TextView textView;
    String userNick;

    //?????? ?????? ??????
    CircleImageView imageView;
    private Uri photoUri;
    private Boolean isPermission = true;
    final CharSequence[] selectOption = {"???????????? ?????? ??????", "?????? ???????????? ??????"};
    private HashMap<String, Object> List;

    //?????? ????????? ?????? ??????
    public static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    public static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<ArrayList<ProductInfo>> listOfList = new ArrayList<>();
    DataFormat df;

    //?????????????????? ????????????
    //https://ddangeun.tistory.com/50 -> ??????????????? ???????????? ??????
    //??? ???????????? fragment is added ????????????.



    //??? ????????? onCreateView ??? ??????.



    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");

        //???????????? ?????? ???????????? ????????? ???????????? ????????????
        scatter();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e(TAG, "onResume()");
    }

    //??? ???????????? fragment is active ????????????.

    @Override
    public void onPause() {
        super.onPause();
        //Log.e(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.e(TAG, "onStop()");
    }


    // onDestroyView ???????????? onCreateView ??? ???????????? ????????? ??????.

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.e(TAG, "onDestroy()");
    }



    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.e(TAG, "onCreateView()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile_fragment);
        Intent intent = getIntent();

        post_publisher = intent.getStringExtra("post_publisher");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //????????? ?????? main activity??? ??????
        findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);
        //??????
        imageView = findViewById(R.id.circle_img);

        //?????? ??????
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //recyclerview
        recyclerView = findViewById(R.id.recycler_mypost);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        profileAdapter = new profileAdapter();
        recyclerView.setAdapter(profileAdapter);

        //?????? ????????? ?????? ?????????
        profileAdapter.setOnItemClickListener(new org.techtown.sns_project.Board.profile.profileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent intent = new Intent(getApplicationContext(), BoardPostClickEvent.class);
                intent.putExtra("position", position);
                intent.putExtra("listImgUrl", listImgUrl);
                intent.putExtra("listPublisher", listPublisher);
                intent.putExtra("listDescription", listDescription);
                intent.putExtra("listOfList", listOfList);
                intent.putExtra("listDocument", listDocument);

                System.out.println("Start activity :" + listImgUrl);

                startActivity(intent);
            }
        });


        //?????? ??? ???????????? ????????? ????????? ??????
        textView = (TextView)findViewById(R.id.userNickname);
        db.collection("users").document(post_publisher).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        System.out.println("userID"+post_publisher);
                        userNick = document.getData().get("name").toString();
                        findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);
                        textView.setText(userNick);

                        System.out.println(userNick);
                    }
                });

        //storage?????? ?????? ????????????
        setFireBaseProfileImage(post_publisher);

        //?????? ????????? ??????

    }

    public void scatter(){
        //???????????? ?????? ???????????? ????????? ???????????? ????????????
        profileAdapter.clearList();
        profileAdapter.notifyDataSetChanged();
        db.collection("board").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //????????? ?????? ??????
                        listImgUrl.clear();
                        listDescription.clear();
                        listPublisher.clear();
                        listOfList.clear();
                        listDocument.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();
                            String ismypost = (String) List.get("publisher");

                            if (ismypost.equals(post_publisher)) {

                                df = document.toObject(DataFormat.class);
                                listImgUrl.add(df.getImageUrl());
                                listPublisher.add(df.getPublisher());
                                listDescription.add(df.getDescription());
                                listDocument.add(document.getId());
                                listOfList.add(df.getList());

                                MyProfile_info data = new MyProfile_info(
                                        (String) List.get("publisher"),
                                        (String) List.get("imageUrl"),
                                        (String) List.get("description"));
                                profileAdapter.addItem(data);
                            }
                        }
                        profileAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    //storage?????? ????????? ?????? ????????? ???????????? img view??? ?????????
    private void setFireBaseProfileImage(String filename_GetUid) {

        FirebaseStorage storage = FirebaseStorage.getInstance(); //???????????? ??????????????? ?????????,
        //??????????????? ????????? ?????????.
        StorageReference storageRef = storage.getReference(); //??????????????? ????????????
        storageRef.child("profile_images/" + filename_GetUid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //?????????
                Glide.with(getApplicationContext()).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //?????????
                imageView.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
    }



    /*
     * ?????? ?????? ?????????
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ClosetButton:
                    Intent intent = new Intent(getApplicationContext(), ClosetMainActivity.class);
                    intent.putExtra("post_publisher", post_publisher);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void StartActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}
