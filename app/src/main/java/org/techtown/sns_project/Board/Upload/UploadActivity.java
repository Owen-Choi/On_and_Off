package org.techtown.sns_project.Board.Upload;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.cameraexample.ScanQR;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private StorageTask uploadTask;
    StorageReference storageRef;

    private Uri imageUri;
    ImageView close, image_added;
    TextView upload;
    EditText description;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    // parsing과 recycler view에 관련된 변수들
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageButton urlImageButton, closetImageButton;
    AlertDialog.Builder builder;
    EditText input;
    String defaultString = "";
    ProductInfo pi;
    static upload_items_adapter UIA;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        upload = findViewById(R.id.upload);
        description = findViewById(R.id.description);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        // recycler view part
        recyclerView = findViewById(R.id.AlreadyAddedView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(UIA);
        // image button part
        urlImageButton = findViewById(R.id.URLImageButton);
        urlImageButton.setOnClickListener(onClickListener);
        closetImageButton = findViewById(R.id.ClosetImageButton);
        closetImageButton.setOnClickListener(onClickListener);
        builder = new AlertDialog.Builder(this);
        input = new EditText(this);
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
            private void uploadImage() {
                //사진 업로드
                final ProgressDialog pd = new ProgressDialog(UploadActivity.this);
                pd.setMessage("Posting");
                pd.show();
                if (imageUri != null) {
                    String GetUid = firebaseUser.getUid();
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
                                data.put("publisher", firebaseUser.getUid());
                                data.put("ImageUrl",DownloadUrl);
                                
                                db.collection("board").document(firebaseUser.getUid()).collection("board_Data").add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.e("temp", "onSuccess: DB Insertion success");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("temp", "onFailure: DB Insertion failed");
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
        Log.e("temp", "onActivityResult: " + requestCode + " " + resultCode +" " + data);
        Log.e("temp", "onActivityResult: " + firebaseUser.getEmail());
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                imageUri = result.getUri();
                image_added.setImageURI(imageUri);
            }
            else {
                Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadActivity.this, NormalMainActivity.class));
                finish();
            }
        }
        else
            Log.e("out", "onActivityResult: 첫 조건문 out");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.URLImageButton:
                    // dialog input
                    DialogManager();
                    break;
                case R.id.ClosetImageButton:

                    break;
            }
        }
    };

    private void DialogManager() {
        builder.setTitle("URL Input");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                defaultString = input.getText().toString();
                parsing();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void parsing() {
        upload_parser_class parser = new upload_parser_class(defaultString);
        // 이 코드가 동작해야 한다.
        pi = parser.getProductInfo();
        Log.e("woong", "parsing: " + pi.getInfo());
    }

}





