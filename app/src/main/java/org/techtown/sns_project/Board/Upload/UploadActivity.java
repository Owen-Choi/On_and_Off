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
import org.techtown.sns_project.fragment.DataFormat;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
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

    ImageButton urlImageButton, closetImageButton;
    AlertDialog.Builder builder;
    EditText input;
    String defaultString = "";
    static ArrayList<ProductInfo> list = new ArrayList<>();
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
        // image button part
        urlImageButton = findViewById(R.id.URLImageButton);
        urlImageButton.setOnClickListener(onClickListener);
        closetImageButton = findViewById(R.id.ClosetImageButton);
        closetImageButton.setOnClickListener(onClickListener);
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
                .setAspectRatio(1, 1)
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
//                                Map<String, Object> data = new HashMap<>();
                                DataFormat dataFormat = new DataFormat(DownloadUrl, firebaseUser.getUid(),
                                        description.getText().toString(), list);

                                //추가할 정보들 입력, 우선 글에대한 설명과 getUid값
//                                data.put("description", description.getText().toString());
//                                data.put("publisher", firebaseUser.getUid());
//                                data.put("ImageUrl",DownloadUrl);
//                                data.put("clothes_info", list);

                                db.collection("board").document(firebaseUser.getUid()).collection("board_Data").add(dataFormat)
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
        builder = new AlertDialog.Builder(this);
        builder.setTitle("URL Input");
        input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                defaultString = input.getText().toString();
                call_parser();
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

    // 여기서 parser를 만들면 아래의 parsing 메서드를 parser에서 호출한다.
    // 따로 메서드로 뺀 이유는 OnClickListener 안에서는 Context 문제가 발생했기 때문이다.
    private void call_parser() {
        upload_parser_class parser = new upload_parser_class(defaultString, this);

    }

    // upload parser class에서 pi를 받아와서 어뎁터애 넣는다.
    public void parsing_injection(ProductInfo pi) {
        // 업로드 시점에 리사이클러뷰에 추가하는 코드는 쓰레드 / 비동기 백그라운드 문제로 불가능하지만
        // pi의 리스트를 가지고 있을 수는 있다. 이 리스트만 게시글에 전달하면
        // 게시글에서는 옷 정보를 띄울 수 있다.
        list.add(pi);
    }

    public ArrayList<ProductInfo> getList() {
        return list;
    }

}





