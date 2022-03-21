package org.techtown.sns_project.fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.Closet.ClosetMainActivity;
import org.techtown.sns_project.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private View view;
    private String TAG = "프래그먼트";

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userNick;

    TextView textView;
    CircleImageView imageView;
    private static final int PICK_FROM_ALBUM = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        view.findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);

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

        //프사 클릭시 설정
        imageView = (CircleImageView) view.findViewById(R.id.circle_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
            });
        return view;
    }

    //프사
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            String uri = photoUri.toString();
            System.out.println("fffffffffffffffffffffffffffffffffffffffffffffffffff"+photoUri);

            imageView = view.findViewById(R.id.circle_img);

            imageView.setImageURI(photoUri);

        }
    }


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
