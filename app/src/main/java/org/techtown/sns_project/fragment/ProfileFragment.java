package org.techtown.sns_project.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.sns_project.Closet.ClosetMainActivity;

import org.techtown.sns_project.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private View view;
    private String TAG = "프래그먼트";

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //닉넴
    TextView textView;
    String userNick;

    //프사
    CircleImageView imageView;
    private Uri photoUri;
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        //클릭시 옷장 main activity로 이동
        view.findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);

        //파베 연동
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //필드 값 가져와서 프로필 닉네임 설정
        textView = (TextView) view.findViewById(R.id.userNickname);
        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        userNick = document.getData().get("name").toString();
                        view.findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);
                        textView.setText(userNick);

                        System.out.println(userNick);
                    }
                });

        //storage에서 프사 가져오기
        getFireBaseProfileImage(firebaseUser.getUid());

        //프사 클릭시 설정
        view.findViewById(R.id.circle_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프사 클릭시 권한 받기
                tedPermission();
                //갤러리 ㄱㄱ
                if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    /**
     *  앨범에서 이미지 가져오기
     */
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    //프사
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //갤러리에서 사진 가져오는 거 취소했을 때 오류 안나게 해주는 코드
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        //갤러리에서 사진을 잘 가져왔을 때
        if (requestCode == PICK_FROM_ALBUM) {

            photoUri=data.getData();
            uploadFile();
        }
    }

    private void uploadFile() {
        //업로드할 uri가 있으면 수행
        if (photoUri != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setTitle("파베 업로드중...");
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
                            getFireBaseProfileImage(firebaseUser.getUid());
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
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(view.getContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFireBaseProfileImage(String filename_GetUid){
        //우선 디렉토리 파일 하나 만든다.
        File file = view.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profile_img");//이미지를 저장할 수 있는 디렉토리
        //구분할 수 있게 /toolbar_image 폴더에 넣어준다.
        //이 파일안에 저 디렉토리가 있는지 확인
        if(!file.isDirectory()){ //디렉토리가 없으면,
            file.mkdir(); //디렉토리를 만든다.
        }
        downloadImg(filename_GetUid); //이미지 다운로드해서 가져오기 메서드
    }

    /**이미지 다운로드해서 가져오기 메서드 */
    private void downloadImg(String filename_GetUid){
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference(); //스토리지를 참조한다
        storageRef.child("profile_images/"+filename_GetUid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                imageView = view.findViewById(R.id.circle_img);
                Glide.with(view.getContext()).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //실패시
                imageView = view.findViewById(R.id.circle_img);
                imageView.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
    }

    /**
     *  갤러리 권한 설정
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

    /**
     *  옷장 이동 이벤트
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ClosetButton:
                    StartActivity(ClosetMainActivity.class);
            }
        }
    };
    private void StartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }
}
