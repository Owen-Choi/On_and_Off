package org.techtown.sns_project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.RegisterButton).setOnClickListener(onClickListener);
        findViewById(R.id.ToSignInView).setOnClickListener(onClickListener);
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

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    StartToast("회원가입에 성공하였습니다.");
                                    FirebaseUser user = mAuth.getCurrentUser();
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
                case R.id.ToSignInView:
                    StartActivity();
                    break;
            }
        }
    };

    private void StartActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
