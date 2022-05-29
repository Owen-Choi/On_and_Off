package org.techtown.sns_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.Enterprise.EnterpriseMainActivity;
import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.pushAlarm.BackgroundAlarmService;

public class InitialActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        startLoading();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(user != null){
            // 로그인이 되어있다면 회원 정보가 등록됐는지 본다.
            Log.e("Automatic SignIn", "user exist");
            String[] temp = {"users", "enterprises"};
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            for (String tempPath : temp) {
                DocumentReference documentReference = fb.collection(tempPath).document(user.getUid());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document != null) {
                                if(document.exists()) {
                                    if (tempPath.equals("users")) {
                                        StartToast("일반 회원으로 자동 로그인합니다.");
                                        StartActivity(NormalMainActivity.class);
                                    }
                                    else if (tempPath.equals("enterprises")) {
                                        StartToast("기업회원으로 자동 로그인합니다.");
                                        StartActivity(EnterpriseMainActivity.class);
                                    } else {
                                        // 유저는 있는데 db가 없는 상황이 있다.
                                        // 회원탈퇴 기능이 추가되면서 간간히 발생한다.
                                        // 그런 상황을 위한 디펜시브 코드이다.
                                        Log.e("Unknown Error", "onComplete: login error");
                                        FirebaseAuth.getInstance().signOut();
                                        StartActivity(CommonSignInActivity.class);
                                    }
                                }
//                                else{
//                                    Log.e("temp", "왜 조건문에 필터링이 안되는거지?");
//                                    StartActivity(CommonSignInActivity.class);
//                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("temp", "onFailure: null value");
                    }
                });
            }
        }
        else
            StartActivity(CommonSignInActivity.class);
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



}
