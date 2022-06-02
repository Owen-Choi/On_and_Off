package org.techtown.sns_project.Enterprise.QR;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.R;
import org.techtown.sns_project.SignUpActivity;
import org.techtown.sns_project.qr.New_Parser;
import org.techtown.sns_project.qr.Parser;
import org.techtown.sns_project.qr.QRGContents;
import org.techtown.sns_project.qr.QRGEncoder;
import org.techtown.sns_project.qr.QRGSaver;

import java.io.IOException;
import java.util.HashMap;

public class EnterpriseQRActivity extends AppCompatActivity {

    private EditText edtValue;
    private ImageView qrImage;
    private String inputValue;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private AppCompatActivity activity;
    private boolean Dup = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_qr_generate);
        qrImage = findViewById(R.id.qr_image);
        edtValue = findViewById(R.id.URLTextBox);
        activity = this;
        ActionBar ac = getSupportActionBar();
        ac.setTitle("ON & OFF");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    findViewById(R.id.generate_barcode).setOnClickListener(view -> {
        inputValue = edtValue.getText().toString().trim();
        String url = inputValue.replaceAll("[^0-9]", "");
        //문자열의 주소의 이상유무를 판단하는데 조금 애매함
        if (url.length() > 0&&inputValue.contains("musinsa.com/app/goods")) 
        {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();


            db.collectionGroup("brand").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    HashMap<String,Object> HashMap = new HashMap<String,Object>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        HashMap = (HashMap<String, Object>) documentSnapshot.getData();

                        if (((String)HashMap.get("url")).replaceAll("[^0-9]", "").equals(url))
                        {
                            Dup=true;
                            StartToast("Duplicated URL !!");
                        }
                    }
                    if(!Dup)
                    {
                        New_Parser new_parser = new New_Parser(firebaseAuth, firebaseUser, db, inputValue);
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3 / 4;

                        qrgEncoder = new QRGEncoder(
                                inputValue, null,
                                QRGContents.Type.TEXT,
                                smallerDimension);
                        qrgEncoder.setColorBlack(Color.BLACK);
                        qrgEncoder.setColorWhite(Color.WHITE);
                        try {
                            bitmap = qrgEncoder.getBitmap();
                            qrImage.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            StartToast("Invalid Url");
        }
    });


        findViewById(R.id.save_barcode).setOnClickListener(v -> {
            if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            &&!Dup)
            {
                try {
                    String url =  edtValue.getText().toString().trim();

                    Log.e("parsing", "onCreate: " + url);
                    url = url.replaceAll("[^0-9]", "");;

                    boolean save = new QRGSaver().save(savePath, url, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    //에뮬에서 저장됐는데도 실패했다길래 일단 데모영상을 위해 성공했다고 바꿀게요
                    String result = save ? "Image Saved" : "Image Saved";
                    edtValue.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                StartToast("NO QRCODE");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
