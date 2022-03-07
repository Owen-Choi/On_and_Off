package org.techtown.sns_project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.fragment.BoardFragment;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends Activity {

    private StorageTask uploadTask;
    StorageReference storageRef;


    private Uri imageUri;
    ImageView close, image_added;
    TextView upload;
    EditText description;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        upload = findViewById(R.id.upload);
        description = findViewById(R.id.description);

        //close: 게시판 화면으로 돌아가기
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadActivity.this, NormalMainActivity.class));
                finish();
            }
        });

        //upload: 사진 업로드 하기
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        //1대1로 잘라주고 imageuri 값 설정
        CropImage.activity()
                .setAspectRatio(4, 5)
                .start(UploadActivity.this);

    }
            private void uploadImage() { //사진 업로드

                final ProgressDialog pd = new ProgressDialog(UploadActivity.this);
                pd.setMessage("Posting");
                pd.show();

                if (imageUri != null) {
                    String GetUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference("post/" + GetUid); //storgae의 저장경로
                    final StorageReference ref = storageRef.child(System.currentTimeMillis() + ".jpg"); //이미지의 파일이름
                    uploadTask = ref.putFile(imageUri); //storage에 file을 업로드, uri를 통해서
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) { //task가 성공하면
                                Uri downloadUri = task.getResult(); //위의 return값을 받아 downloadUri에 저장
                                pd.dismiss(); //로딩창 없애기
                                String DownloadUrl = downloadUri.toString();
                                //해쉬 맵에 저장해서 컬렉션에 넣기
                                Map<String, Object> data = new HashMap<>();

                                //추가할 정보들 입력, 우선 글에대한 설명과 getUid값
                                data.put("description", description.getText().toString());
                                data.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                data.put("ImageUrl",DownloadUrl);

                                db.collection("board").document(firebaseUser.getUid()).collection("board_Data").add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                startActivity(new Intent(UploadActivity.this, NormalMainActivity.class)); //다시 게시판 화면으로 돌아간다
                                finish();

                            } else {
                                Toast.makeText(UploadActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                } else {
                    Toast.makeText(UploadActivity.this, "No ImageSelected!", Toast.LENGTH_SHORT).show();
                }

            }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            image_added.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this, NormalMainActivity.class));
            finish();
        }
    }
}





