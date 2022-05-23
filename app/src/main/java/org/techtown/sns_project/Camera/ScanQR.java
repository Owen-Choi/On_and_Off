package org.techtown.sns_project.Camera;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.sns_project.Normal.Search.ListViewAdapter;
import org.techtown.sns_project.Normal.Search.SearchTitleClass;
import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ScanQR extends AppCompatActivity {
    private IntentIntegrator qrScan;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    java.util.HashMap<String,Object> HashMap = new HashMap<String,Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();



               /* new IntentIntegrator(ScanQR.this).initiateScan();*/
                qrScan = new IntentIntegrator(ScanQR.this);
                qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
                qrScan.setPrompt("QR 코드를 인식해주세요.");
                qrScan.initiateScan();

        /* new IntentIntegrator(this).initiateScan();*/
/*        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("Sample Text!");
        qrScan.initiateScan();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                finish();
                // todo
            } else {
                // todo

                String key = result.getContents();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collectionGroup("brand").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            HashMap = (HashMap<String, Object>) documentSnapshot.getData();

                            if(((String)HashMap.get("url")).contains(key))
                            {
                                System.out.println("TETETETE"+key);
                                HashMap.put("count", Integer.parseInt(HashMap.get("count").toString()) + 1);
                                System.out.println("TESTSTST"+HashMap);
                                db.collection("enterprises").document((String)HashMap.get("eid")).collection("brand").document(key).update(HashMap);
                            }

                        }

                        }});




                Intent intent = new Intent(getApplicationContext(),Activity_codi.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(ArrayList<String> arrayList) {

        }
    };
}