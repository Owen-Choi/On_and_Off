package org.techtown.sns_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class InitialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            StartActivity(CommonSignInActivity.class);
        }
        else{
            // 로그인이 되어있다면 회원 정보가 등록됐는지 본다.
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
                                if (document.exists()) {
                                    if (tempPath.equals("users"))
                                        StartActivity(NormalMainActivity.class);
                                    else if (tempPath.equals("enterprises"))
                                        StartActivity(EnterpriseMainActivity.class);
                                }
                                else {
                                    // 유저는 있는데 db가 없는 상황이 있다.
                                    // 회원탈퇴 기능이 추가되면서 간간히 발생한다.
                                    // 그런 상황을 위한 디펜시브 코드이다.
                                    FirebaseAuth.getInstance().signOut();
                                    StartActivity(CommonSignInActivity.class);
                                }
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
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }






}
