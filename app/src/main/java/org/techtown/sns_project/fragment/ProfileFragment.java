package org.techtown.sns_project.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.sns_project.Closet.ClosetMainActivity;
import org.techtown.sns_project.Closet.Closet_Parser;
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
    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    private String profile;

    Map<String, Object> pro = new HashMap<>();

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
/*

        //기존 프사 정보가 파베 필드에 있다면 자동 설정해주기(=users 필드에 저장되어있는 uri 절대값 가져오기).
        //근데 파베이 프사 정보가 없으면(신규 가입자 처럼) 마페 들어갈 때 프사 정보를 못 불러와서 오류나서 전체 주석해놓음. 일단 프사 정보를 필드에서 storage로 바꾸는 작업 해야할듯
        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        profile = document.getData().get("myprofile").toString();
                        tempFile = new File(profile);
                        setImage();

                        System.out.println(userNick);
                    }else {
                        //파베에 정보 없으면 기본이미지로 프사 설정하도록 짜봤는데도 오류남..
                    }
                });
*/

        //프사 클릭시 설정
        view.findViewById(R.id.circle_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프사 클릭시 권한 받기
                tedPermission();
                if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }


    //프사
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //갤러리에서 사진 가져오는 거 취소했을 때 오류 안나게 해주는 코드
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        //갤러리에서 사진을 잘 가져왔을 때
        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                //갤러리에서 가져온 사진의 절대 경로를 파베 users 필드안에 값을 넣는다
                this.profile=cursor.getString(column_index);
                pro.put("myprofile", cursor.getString(column_index));
                db.collection("users").document(firebaseUser.getUid())
                        .set(pro, SetOptions.merge());
                //
                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        }
    }

    /**
     *  앨범에서 이미지 가져오기
     */
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     *  tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {

        CircleImageView imageView = view.findViewById(R.id.circle_img);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView.setImageBitmap(originalBm);

        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        tempFile = null;

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
