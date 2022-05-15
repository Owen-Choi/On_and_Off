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

    //파이어베이스
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    String post_publisher;
    //recyclerview
    private RecyclerView recyclerView;
    private org.techtown.sns_project.Board.profile.profileAdapter profileAdapter;

    //닉네임 관련 변수
    TextView textView;
    String userNick;

    //프사 관련 변수
    CircleImageView imageView;
    private Uri photoUri;
    private Boolean isPermission = true;
    final CharSequence[] selectOption = {"앨범에서 사진 선택", "기본 이미지로 변경"};
    private HashMap<String, Object> List;

    //나의 포스트 관련 변수
    public static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    public static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<ArrayList<ProductInfo>> listOfList = new ArrayList<>();
    DataFormat df;

    //프래그먼트의 생명주기
    //https://ddangeun.tistory.com/50 -> 프래그먼트 생명주기 참고
    //이 시점부터 fragment is added 상태이다.



    //이 시점에 onCreateView 가 있다.



    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");

        //파베에서 내가 포스트한 게시글 가져와서 뿌려주기
        scatter();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e(TAG, "onResume()");
    }

    //이 시점부터 fragment is active 상태이다.

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


    // onDestroyView 다음으로 onCreateView 로 넘어가는 경우가 있다.

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
        //클릭시 옷장 main activity로 이동
        findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);
        //프사
        imageView = findViewById(R.id.circle_img);

        //파베 연동
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //recyclerview
        recyclerView = findViewById(R.id.recycler_mypost);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        profileAdapter = new profileAdapter();
        recyclerView.setAdapter(profileAdapter);

        //나의 포스트 클릭 이벤트
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


        //필드 값 가져와서 프로필 닉네임 설정
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

        //storage에서 프사 가져오기
        setFireBaseProfileImage(post_publisher);

        //프사 클릭시 설정

    }

    public void scatter(){
        //파베에서 내가 포스트한 게시글 가져와서 뿌려주기
        profileAdapter.clearList();
        profileAdapter.notifyDataSetChanged();
        db.collection("board").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //데이터 중복 방지
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

    //storage에서 저장된 프사 정보를 가져와서 img view에 뿌리기
    private void setFireBaseProfileImage(String filename_GetUid) {

        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference(); //스토리지를 참조한다
        storageRef.child("profile_images/" + filename_GetUid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                Glide.with(getApplicationContext()).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //실패시
                imageView.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
    }



    /*
     * 옷장 이동 이벤트
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
