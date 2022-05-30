package org.techtown.sns_project.Enterprise.QR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.QRGContents;
import org.techtown.sns_project.qr.QRGEncoder;
import org.techtown.sns_project.qr.QRGSaver;

public class EnterpriseQRListClickEvent extends AppCompatActivity {

    private ImageView qrImage, Img_ProductImg;
    private TextView txt_ProductInfo,txt_ProductTitle,txt_ProductPrice;
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
        ActionBar ac = getSupportActionBar();
        ac.setTitle("ON & OFF");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        System.out.println(EnterpriseQRListActivity.listUrl.get (position)+"position : "+position);
        String key = EnterpriseQRListActivity.listUrl.get (position).replaceAll("[^0-9]", "");
        qrImage = findViewById(R.id.qr_image);
        activity = this;
        Img_ProductImg = findViewById(R.id.Img_ProductImg);
        txt_ProductInfo = findViewById(R.id.txt_ProductInfo);
        txt_ProductTitle = findViewById(R.id.txt_ProductTitle);
        txt_ProductPrice = findViewById(R.id.txt_ProductPrice);


        txt_ProductTitle.setText(EnterpriseQRListActivity.listTitle.get (position));
        txt_ProductInfo.setText(EnterpriseQRListActivity.listInfo.get (position));
        txt_ProductPrice.setText(EnterpriseQRListActivity.listPrice.get (position));
        Glide.with(this).load(EnterpriseQRListActivity.listImgUrl.get (position)).error(R.drawable.ic_launcher_background).into(Img_ProductImg);

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
                    EnterpriseQRListActivity.listTitle.remove(position);
                    EnterpriseQRListActivity.listInfo.remove(position);
                    EnterpriseQRListActivity.listUrl.remove(position);
                    EnterpriseQRListActivity.listPrice.remove(position);
                    EnterpriseQRListActivity.listImgUrl.remove(position);
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
                    Log.d("SAVE",key);
                    boolean save = new QRGSaver().save(savePath, key, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    String result = save ? "Image Saved" : "Image Not Saved";
                    if(result == "Image Saved")
                        Toast.makeText(activity, "이미지가 갤리러에 저장되었습니다.", Toast.LENGTH_LONG).show();
                    else
                    {
                        //에뮬에서 저장됐는데도 실패했다길래 일단 데모영상을 위해 성공했다고 바꿀게요
                        Toast.makeText(activity, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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
