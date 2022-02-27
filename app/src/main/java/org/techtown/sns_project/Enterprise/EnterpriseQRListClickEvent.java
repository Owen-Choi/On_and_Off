package org.techtown.sns_project.Enterprise;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.R;
import org.techtown.sns_project.SignUpActivity;
import org.techtown.sns_project.qr.New_Parser;
import org.techtown.sns_project.qr.Parser;
import org.techtown.sns_project.qr.QRGContents;
import org.techtown.sns_project.qr.QRGEncoder;
import org.techtown.sns_project.qr.QRGSaver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnterpriseQRListClickEvent extends AppCompatActivity {

    private ImageView qrImage;
    private TextView txt_ProductUrl;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private AppCompatActivity activity;
    private String TAG = "DONG";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_qr_list_click);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        System.out.println(EnterpriseQRListActivity.listUrl.get (position)+"position : "+position);
        String key = EnterpriseQRListActivity.listUrl.get (position).replace("https://store.musinsa.com/app/goods/","");
        qrImage = findViewById(R.id.qr_image);
        txt_ProductUrl = findViewById(R.id.txt_ProductUrl);
        activity = this;
        txt_ProductUrl.setText(key);

        if (key.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    key, null,
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
        } else {
            StartToast("ERROR!");
        }



        findViewById(R.id.delete_barcode).setOnClickListener(v -> {
            db.collection("enterprises").document(firebaseUser.getUid()).collection("brand")
                    .document(key).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    EnterpriseQRListActivity.adapter.removeItem(position);
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    finish();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });



        });

        findViewById(R.id.save_barcode).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {

                    boolean save = new QRGSaver().save(savePath, key, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    String result = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        });
    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
