package org.techtown.sns_project.fragment.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.Normal.Setting.NormalSettingActivity;
import org.techtown.sns_project.fragment.profile.Closet.ClosetMainActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.DataFormat;
import org.techtown.sns_project.fragment.profile.Bookmark.bookmark;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private View view;
    private String TAG = "profile ";
    private int alarmCount = 0;
    //파이어베이스
    FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
    FirebaseFirestore db;

    //recyclerview
    private RecyclerView recyclerView;
    private profileAdapter profileAdapter;

    //닉네임 관련 변수
    TextView textView;
    String userNick;

    //프사 관련 변수
    static CircleImageView imageView;
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

    // 알람 표시 관련 변수 - 철웅 추가
    TextView alarm_textView;

    //프래그먼트의 생명주기
    //https://ddangeun.tistory.com/50 -> 프래그먼트 생명주기 참고
    //이 시점부터 fragment is added 상태이다.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Log.e(TAG, "onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG, "onCreate()");
    }

    //이 시점에 onCreateView 가 있다.

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.e(TAG, "onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
        scatter();

    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.e(TAG, "onDestroyView()");
    }
    // onDestroyView 다음으로 onCreateView 로 넘어가는 경우가 있다.

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.e(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.e(TAG, "onDetach()");
    }
    //이 시점부터 fragment is destroyed 상태이다.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        //클릭시 옷장 main activity로 이동
        view.findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);

        //클릭시 북마크 main activity로 이동
        view.findViewById(R.id.bookmarkButton).setOnClickListener(onClickListener);

        //클릭시 설정 화면으로 이동
        view.findViewById(R.id.Temp_Setting_Button).setOnClickListener(onClickListener);

        //프사
        imageView = view.findViewById(R.id.circle_img);

        //파베 연동
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //recyclerview
        recyclerView = view.findViewById(R.id.recycler_mypost);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        profileAdapter = new profileAdapter();
        recyclerView.setAdapter(profileAdapter);

        //나의 포스트 클릭 이벤트
        profileAdapter.setOnItemClickListener(new profileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent intent = new Intent(getContext(), BoardPostClickEvent.class);
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
        textView = (TextView) view.findViewById(R.id.userNickname);
        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        System.out.println("userID"+firebaseUser.getUid());
                        userNick = document.getData().get("name").toString();
                        view.findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);
                        textView.setText(userNick);

                        System.out.println(userNick);
                    }
                });

        //storage에서 프사 가져오기
        setFireBaseProfileImage(firebaseUser.getUid());

        //프사 클릭시 설정
        view.findViewById(R.id.circle_img).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //프사 클릭시 권한 받기
                tedPermission();



                android.app.AlertDialog.Builder ad = new android.app.AlertDialog.Builder(view.getContext(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                ad.setTitle("프로필 사진 설정")
                        .setIcon(R.drawable.ic_noun_selecting_1833829)
                        .setItems(selectOption, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //갤러리 ㄱㄱ
                                        if (isPermission) goToAlbum();
                                        break;
                                    case 1:
                                        imageView.setImageResource(R.drawable.ic_baseline_android_24);
                                        //storage에 프사 정보 삭제
                                        delProfile(firebaseUser.getUid());
                                        break;
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });

        return view;
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

                            if (ismypost.equals(firebaseUser.getUid())) {

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

    /**
     * 갤러리 접근해서 crop하기
     */
    private void goToAlbum() {

        CropImage.activity()
                .start(getContext(), this);
    }

    //갤러리에서 가져온 사진 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                photoUri = result.getUri();
                uploadFile();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    //업로드할 uri가 있으면 수행
    private void uploadFile() {

        if (photoUri != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setTitle("프로필 업로드 중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //user id
            String filename_GetUid = firebaseUser.getUid();

            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://toyproject-sns.appspot.com/").child("profile_images/" + filename_GetUid);
            //올라가거라...
            storageRef.putFile(photoUri)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //이미지 설정
                            setFireBaseProfileImage(firebaseUser.getUid());
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(view.getContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(view.getContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
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
                Glide.with(view.getContext()).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //실패시
                imageView.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
    }

    //storage 프사 정보 삭제
    public static void delProfile(String filename_GetUid) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageFef = storage.getReference();

        StorageReference desertRef = storageFef.child("profile_images/" + filename_GetUid);
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                imageView.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
    }

    /**
     * 갤러리 권한 설정
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    /*
     * 옷장, 북마크, 설정 이동 이벤트
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ClosetButton:
                    StartActivity(ClosetMainActivity.class);
                    break;

                case R.id.bookmarkButton:
                    StartActivity(bookmark.class);
                    break;

                case R.id.Temp_Setting_Button:
                    StartActivity(NormalSettingActivity.class);
                    break;
            }
        }
    };

    private void StartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }
}
