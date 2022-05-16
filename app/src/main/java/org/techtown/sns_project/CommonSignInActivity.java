package org.techtown.sns_project;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.royrodriguez.transitionbutton.TransitionButton;

import org.techtown.sns_project.Enterprise.EnterpriseMainActivity;
import org.techtown.sns_project.Normal.NormalMainActivity;
public class CommonSignInActivity extends AppCompatActivity implements SignInActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    //
    private TransitionButton transitionButton;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_sign_in);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        //dlsqja
        transitionButton = findViewById(R.id.SignInButton);
        transitionButton.setOnClickListener(onClickListener);

        findViewById(R.id.CommonSignUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.FindPassWord).setOnClickListener(onClickListener);
        //findViewById(R.id.ToPasswordInitButton).setOnClickListener(onClickListener);
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    public void SignIn() {
        String email = ((EditText) findViewById(R.id.EmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordEditText)).getText().toString();
        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                firebaseUser = mAuth.getCurrentUser();
                                db = FirebaseFirestore.getInstance();

                                //인범 뾰로롱 로그인 애니메이션 코드
                                transitionButton.startAnimation();
                                // Do your networking task or background work here.
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean isSuccessful = true;

                                        // Choose a stop animation if your call was succesful or not
                                        if (isSuccessful) {
                                            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                                @Override
                                                public void onAnimationStopEnd() {
                                                    CheckUser(firebaseUser, db); // 채크 ㄱㄱ

                                                }
                                            });
                                        } else {
                                            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                                        }
                                    }
                                }, 2000);
                                //여기까지 애니메이션 코드

                            } else {
                                //인범 뾰로롱 로그인 애니메이션 코드
                                transitionButton.startAnimation();
                                transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                                //여기까지 애니메이션 코드
                                StartToast("로그인 실패 : 로그인 정보가 일치하지 않습니다.");
                            }
                        }
                    });
        } else {
            //인범 뾰로롱 로그인 애니메이션 코드
            transitionButton.startAnimation();
            transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            //여기까지 애니메이션 코드
            StartToast("빈칸을 확인해주세요.");
        }
    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // view에서 id를 받아오는데
            switch (view.getId()) {
                case R.id.SignInButton:
                    SignIn();
                    break;
//                case R.id.ToPasswordInitButton:
//                    StartActivity(Password_Init_Activity.class);
                case R.id.CommonSignUpButton:
                    StartActivity(SignUpActivity.class);
                    break;
                case R.id.FindPassWord:
                    StartActivity(Password_Init_Activity.class);
                    break;
            }
        }
    };

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        // 동일한 창이 여러번 뜨게 만드는 것이 아니라 기존에 켜져있던 창을 앞으로 끌어와주는 기능.
        // 이 플래그를 추가하지 않을 경우 창들이 중복돼서 계속 팝업되게 된다.
        // 메인화면을 띄우는 모든 코드에서 이 플래그를 추가해줘야 하는 것 같다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 일단 내버려두고 사용처가 늘어나면 따로 클래스로 빼버리자
    private void CheckUser(FirebaseUser firebaseUser, FirebaseFirestore db) {
        String[] temp = {"users", "enterprises"};
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        for (String tempPath : temp) {
            // 문제1 : 멤버 정보가 없다면 로그인이 안된다.
            DocumentReference documentReference = fb.collection(tempPath).document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null)
                            if (document.exists()) {
                                if (tempPath.equals("users")) {
                                    StartActivity(NormalMainActivity.class);
                                    StartToast("일반 회원으로 로그인합니다.");
                                } else if (tempPath.equals("enterprises")) {
                                    StartActivity(EnterpriseMainActivity.class);
                                    StartToast("기업 회원으로 로그인합니다.");
                                }
                            }
                    }
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // 로그인화면에서 뒤로가기를 누르면 종료된다.
    @Override
    public void onBackPressed() {
        finishAffinity();
//        super.onBackPressed();
    }
}
