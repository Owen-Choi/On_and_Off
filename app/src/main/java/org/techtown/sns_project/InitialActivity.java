package org.techtown.sns_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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
        // main 말고 전용 xml 만들 예정.
        setContentView(R.layout.activity_normal_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            StartActivity(SignUpActivity.class);
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
                            if(document != null)
                                if(document.exists()) {
                                    if(tempPath.equals("users"))
                                        StartActivity(NormalMainActivity.class);
                                    else if(tempPath.equals("enterprises"))
                                        StartActivity(EnterpriseMainActivity.class);
                                }
                        }
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
