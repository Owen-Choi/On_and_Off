package org.techtown.sns_project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.Enterprise.EnterpriseMemberInfoActivity;
import org.techtown.sns_project.Enterprise.EnterpriseSignInActivity;
import org.techtown.sns_project.Normal.NormalMemberInfoActivity;
import org.techtown.sns_project.Normal.NormalSignInActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";
    private RadioGroup radioGroup;
    private static boolean isNormal, isEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.RegisterButton).setOnClickListener(onClickListener);
        findViewById(R.id.ToNormalMemberSignInView).setOnClickListener(onClickListener);
        findViewById(R.id.ToEnterpriseMemberSignInView).setOnClickListener(onClickListener);
        radioGroup = findViewById(R.id.UserChoiceRadioButtonGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.NormalUserButton:
                        Log.d(TAG, "onCheckedChanged: 일반 유저");
                        changeState(1);
                        break;
                    case R.id.EnterpriseUserButton:
                        Log.d(TAG, "onCheckedChanged: 기업 유저");
                        changeState(2);
                        break;
                }
            }
        });
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            currentUser.reload();
        }
    }

    private void SignUp() {
        // EditText로 형 변환해준 이유는 일반 View는 getText 메서드를 사용할 수 없어서이다.
        String email = ((EditText)findViewById(R.id.EmailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.PasswordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.PasswordCheckEditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0 && (isNormal || isEnter)) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    StartToast("회원가입에 성공하였습니다.");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    MemberInfochecker(user);
                                    // 성공했을 때 UI 로직
                                } else {
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    StartToast("회원가입에 실패하였습니다.");
                                    // Toast 일단 생략, 실패했을 때 UI로직
                                }
                            }
                        });
            } else {
                // Toast 창 띄워주기
                StartToast("비밀번호가 일치하지 않습니다.");
            }
        }
        else {
            StartToast("빈칸이 있는지 확인해주세요.");
        }
    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // 로그아웃을 누른 뒤 회원가입창에서 다시 뒤로가기를 누르면 메인 액티비티 창이 또
    // 나타나는 현상을 없애기 위함.
    // 회원가입 창에서 뒤로가기를 누르면 앱을 종료하는 코드
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // view에서 id를 받아오는데
            switch (view.getId()) {
                // id가 RegisterButton에서 받아온 아이디라면 :
                case R.id.RegisterButton:
                    SignUp();
                    break;
                case R.id.ToNormalMemberSignInView:
                    StartActivity(NormalSignInActivity.class);
                    break;
                case R.id.ToEnterpriseMemberSignInView:
                    StartActivity(EnterpriseSignInActivity.class);
                    break;
            }
        }
    };

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    // 일반용 유저와 기업용 유저의 전환을 위함.
    private static void changeState(int id) {
        if(id == 1) {
            isNormal = true;
            isEnter = false;
        }
        else if(id == 2){
            isNormal = false;
            isEnter = true;
        }
    }

    // 여기서는 회원의 종류와 회원정보의 유무에 따라 다른 화면을 띄워준다.
    private void MemberInfochecker(FirebaseUser user) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        String temp;
        temp = isNormal ? "users" : "enterprises";
        DocumentReference documentReference = fb.collection(temp)
                .document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if(document.exists())
                            // 이미 등록한 회원 정보가 있다면 바로 로그인화면으로 이동하도록 한다.
                            if(isNormal)
                                StartActivity(NormalSignInActivity.class);
                            else
                                StartActivity(EnterpriseSignInActivity.class);
                        else
                            // 등록한 회원정보가 없다면 회원 등록 화면으로 이동하도록 한다.
                            if(isNormal)
                                StartActivity(NormalMemberInfoActivity.class);
                            else
                                StartActivity(EnterpriseMemberInfoActivity.class);
                    }
                }
            }
        });
    }

}
