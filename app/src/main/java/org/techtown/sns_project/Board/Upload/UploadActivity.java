package org.techtown.sns_project.Board.Upload;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.techtown.sns_project.Board.Upload.closet.closet_outer_adapter;
import org.techtown.sns_project.Board.Upload.closet.closet_pants_adapter;
import org.techtown.sns_project.Board.Upload.closet.closet_shoes_adapter;
import org.techtown.sns_project.Board.Upload.closet.closet_top_adapter;
import org.techtown.sns_project.fragment.profile.Closet.Closet_info;
import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.DataFormat;
import org.techtown.sns_project.qr.ProductInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;


public class UploadActivity extends AppCompatActivity {

    private StorageTask uploadTask;
    StorageReference storageRef;



    private Uri imageUri;
    ImageView close, image_added;
    TextView upload;
    EditText description;

    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    static String userid;
    ImageButton urlImageButton, closetImageButton;
    AlertDialog.Builder builder;
    EditText input;
    String defaultString = "";
    static ArrayList<ProductInfo> list = new ArrayList<>();
    static ArrayList<Closet_info> ClosetList = new ArrayList<>();
    //옷장 리스트 리사이클러 뷰 파트
    // DB의 컬렉션이 깔끔하지 않아 리사이클러 뷰의 수가 많아졌지만 항목별로 분류할 수 있다는 장점이 있다.
    RecyclerView ClosetOuterRecyclerView;
    RecyclerView ClosetTopRecyclerView;
    RecyclerView ClosetPantsRecyclerView;
    RecyclerView ClosetShoesRecyclerView;
    LinearLayoutManager OuterlinearLayoutManager;
    LinearLayoutManager ToplinearLayoutManager;
    LinearLayoutManager PantslinearLayoutManager;
    LinearLayoutManager ShoeslinearLayoutManager;
    closet_outer_adapter outer_adapter;
    closet_top_adapter top_adapter;
    closet_pants_adapter pants_adapter;
    closet_shoes_adapter shoes_adapter;
    Dialog dialog;
    Closet_info CI;
    HashMap<String,Object> Hash = new HashMap<String,Object>();
    View dialogView;
    LayoutInflater inf;
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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

        // Recycler View part
        inf = getLayoutInflater();
        dialogView = inf.inflate(R.layout.dialog_closet_item_list, null);
        // 아우터 레이아웃 매니저
        OuterlinearLayoutManager = new LinearLayoutManager(this);
        OuterlinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 상의 레이아웃 매니저
        ToplinearLayoutManager = new LinearLayoutManager(this);
        ToplinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 하의 레이아웃 매니저
        PantslinearLayoutManager = new LinearLayoutManager(this);
        PantslinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 신발 레이아웃 매니저
        ShoeslinearLayoutManager = new LinearLayoutManager(this);
        ShoeslinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 어댑터들 초기화
        outer_adapter = new closet_outer_adapter(this);
        top_adapter = new closet_top_adapter(this);
        pants_adapter = new closet_pants_adapter(this);
        shoes_adapter = new closet_shoes_adapter(this);

        // dialog 정의
        dialog = new Dialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 높이는 내용 전체 높이만큼
        dialog.setContentView(dialogView); // Dialog에 선언했던 layout 적용
        dialog.setCanceledOnTouchOutside(true); // 외부 touch 시 Dialog 종료
        dialog.getWindow().setAttributes(lp); // 지정한 너비, 높이 값 Dialog에 적용
    }
            private void uploadImage() {
                //사진 업로드
                final ProgressDialog pd = new ProgressDialog(UploadActivity.this);
                pd.setMessage("Posting");
                pd.show();
                db.collection("users").document(user.getUid()).get().addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                userid = document.getData().get("name").toString();
                                Log.e("userid",userid);

                            }
                        });
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
                            Log.e("Checkuserid",userid);
                            if (task.isSuccessful()) { //task가 성공하면
                                Uri downloadUri = task.getResult(); //위의 return값을 받아 downloadUri에 저장
                                pd.dismiss(); //로딩창 없애기
                                String DownloadUrl = downloadUri.toString();
                                LocalDateTime now = LocalDateTime.now();
                                String postdocument_bydate = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.ENGLISH));
                                //해쉬 맵에 저장해서 컬렉션에 넣기
//                                Map<String, Object> data = new HashMap<>();
                                DataFormat dataFormat = new DataFormat(DownloadUrl, firebaseUser.getUid(),
                                        description.getText().toString(), list,userid);

                                //추가할 정보들 입력, 우선 글에대한 설명과 getUid값
//                                data.put("description", description.getText().toString());
//                                data.put("publisher", firebaseUser.getUid());
//                                data.put("ImageUrl",DownloadUrl);
//                                data.put("clothes_info", list);


                                db.collection("board").document(postdocument_bydate).set(dataFormat)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.e("temp", "onSuccess: DB Insertion success");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("temp", "onFailure: DB Insertion failed");
                                            }
                                        });

                                db.collection("users").document(user.getUid()).collection("Myboard").document(postdocument_bydate).set(dataFormat)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.e("temp", "onSuccess: DB Insertion success");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("temp", "onFailure: DB Insertion failed");
                                            }
                                        });

//                                db.collection("users").document(user.getUid()).collection("Myboard").add(dataFormat)
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                Log.e("temp", "onSuccess: DB Insertion success");
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.e("temp", "onFailure: DB Insertion failed");
//                                            }
//                                        });

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
                    // dialog recycler view
                    showAlertDialogTopic();
                    break;
            }
        }
    };

    public class CustomDialog extends Dialog {

        private EditText et_text;
        private Context mContext;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.custom_dialog);

            // 다이얼로그의 배경을 투명으로 만든다.
            Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // 커스텀 다이얼로그의 각 위젯들을 정의한다.
            et_text = findViewById(R.id.put_text);
            TextView textView = findViewById(R.id.text);
            textView.setText("UML을 입력해주세요");

            Button saveButton = findViewById(R.id.btnSave);
            Button cancelButton = findViewById(R.id.btnCancle);

            // 버튼 리스너 설정
            saveButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // '확인' 버튼 클릭시
                    // ...코드..
                    // Custom Dialog 종료
                    defaultString = et_text.getText().toString();
                    Toast.makeText(UploadActivity.this, "옷이 추가되었습니다", Toast.LENGTH_SHORT).show();
                    call_parser();

                    dismiss();
                }
            });
            cancelButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // '취소' 버튼 클릭시
                    // ...코드..
                    // Custom Dialog 종료
                    dismiss();
                }
            });

        }

        public CustomDialog(Context mContext) {
            super(mContext);
            this.mContext = mContext;
        }


    }

    private void DialogManager() {

        // 호출 - 인범 추가 커스텀 다이얼로그
        CustomDialog dlg = new CustomDialog(UploadActivity.this);
        dlg.show();
        /*
        builder = new AlertDialog.Builder(this);
        builder.setTitle("URL Input");
        input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                defaultString = input.getText().toString();
                Toast.makeText(UploadActivity.this, "옷이 추가되었습니다", Toast.LENGTH_SHORT).show();
                call_parser();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();*/
    }
    // 아래는 URL로 추가 버튼 관련 코드이다.
    ////////////////////////////////////////////////////////////////////////////////////////////////
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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 아래는 옷장에서 가져오기 버튼 관련 코드이다.
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void showAlertDialogTopic() {

        //아우터
        ClosetOuterRecyclerView = dialogView.findViewById(R.id.closetOuterRecyclerView);
        ClosetOuterRecyclerView.setLayoutManager(OuterlinearLayoutManager);
        ClosetOuterRecyclerView.setAdapter(outer_adapter);
        outer_adapter.ClearList();
        //상의
        ClosetTopRecyclerView = dialogView.findViewById(R.id.closetTopRecyclerView);
        ClosetTopRecyclerView.setLayoutManager(ToplinearLayoutManager);
        ClosetTopRecyclerView.setAdapter(top_adapter);
        top_adapter.ClearList();
        //하의
        ClosetPantsRecyclerView = dialogView.findViewById(R.id.closetPantsRecyclerView);
        ClosetPantsRecyclerView.setLayoutManager(PantslinearLayoutManager);
        ClosetPantsRecyclerView.setAdapter(pants_adapter);
        pants_adapter.ClearList();
        //신발
        ClosetShoesRecyclerView = dialogView.findViewById(R.id.closetShoesRecyclerView);
        ClosetShoesRecyclerView.setLayoutManager(ShoeslinearLayoutManager);
        ClosetShoesRecyclerView.setAdapter(shoes_adapter);
        shoes_adapter.ClearList();

        String[] categotyList = {"아우터", "상의", "바지", "신발"};
        for (String element : categotyList) {
            RecyclerViewInsertion(element);
        }
        click_setter();
        dialog.setCancelable(true);
        dialog.show(); // Dialog 출력
    }

    protected void RecyclerViewInsertion(String category) {
        db.collection("users").document(firebaseUser.getUid())
                .collection(category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Hash = (HashMap<String, Object>) document.getData();
                        CI = new Closet_info((String)Hash.get("name"),(String)Hash.get("brand"),(String)Hash.get("clothes_type"), (String)Hash.get("img_url"),
                                (String) Hash.get("url"));
                            switch (category) {
                                case "아우터":
                                    outer_adapter.ItemChange(CI);
                                    break;
                                case "상의":
                                    top_adapter.ItemChange(CI);
                                    break;
                                case "바지":
                                    pants_adapter.ItemChange(CI);
                                    break;
                                case "신발":
                                    shoes_adapter.ItemChange(CI);
                                    break;
                                default:
                                    Log.e("woong", "RecyclerViewInsertion: switch-case default log");
                                    break;
                        }
                    }
                }
                else
                    Log.e("upload", "onComplete: failed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("upload", "onFailure: fail to access DB");
            }
        });

        ClosetList.clear();
    }
    protected void click_setter() {
        outer_adapter.setOnItemClickListener(new closet_outer_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, ArrayList<Closet_info> listData) {
                String url = listData.get(position).getUrl();
                upload_parser_class parser = new upload_parser_class(url, UploadActivity.this);
                Toast.makeText(UploadActivity.this, "아우터가 추가되었습니다", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        top_adapter.setOnItemClickListener(new closet_top_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, ArrayList<Closet_info> listData) {
                String url = listData.get(position).getUrl();
                upload_parser_class parser = new upload_parser_class(url, UploadActivity.this);
                Toast.makeText(UploadActivity.this, "상의가 추가되었습니다", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        pants_adapter.setOnItemClickListener(new closet_pants_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, ArrayList<Closet_info> listData) {
                String url = listData.get(position).getUrl();
                upload_parser_class parser = new upload_parser_class(url, UploadActivity.this);
                Toast.makeText(UploadActivity.this, "하의가 추가되었습니다", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        shoes_adapter.setOnItemClickListener(new closet_shoes_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, ArrayList<Closet_info> listData) {
                String url = listData.get(position).getUrl();
                upload_parser_class parser = new upload_parser_class(url, UploadActivity.this);
                Toast.makeText(UploadActivity.this, "신발이 추가되었습니다", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}





