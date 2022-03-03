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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.Enterprise.EnterpriseMainActivity;
import org.techtown.sns_project.Enterprise.EnterpriseMemberInfoActivity;
import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.Normal.NormalMemberInfoActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private static final String TAG = "SignUpActivity";
    private RadioGroup radioGroup;
    private static boolean isNormal, isEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.RegisterButton).setOnClickListener(onClickListener);
        findViewById(R.id.ToCommonSignInButton).setOnClickListener(onClickListener);
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
        user = mAuth.getCurrentUser();
        if(user != null) {
            user.reload();
        }
    }

    private void SignUp() {
        // EditText로 형 변환해준 이유는 일반 View는 getText 메서드를 사용할 수 없어서이다.
        String email = ((EditText)findViewById(R.id.EmailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.PasswordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.PasswordCheckEditText)).getText().toString();
        String name = ((EditText)findViewById(R.id.MemberInfoName)).getText().toString();
        String address = ((EditText)findViewById(R.id.MemberInfoAddress)).getText().toString();
        String date = ((EditText)findViewById(R.id.MemberInfoDate)).getText().toString();
        String phone = ((EditText)findViewById(R.id.MemberInfoPhone)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0 && (isNormal || isEnter)) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    user = mAuth.getCurrentUser();
                                    dbInsertion(name, address, date, phone);

                                } else {
                                    StartToast("회원가입에 실패하였습니다.");
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
                case R.id.ToCommonSignInButton:
                    StartActivity(CommonSignInActivity.class);
//                    finish();
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

    // 회원가입만 하고, (프로그램 오류 등으로)회원정보를 기입하지 못했을 경우
    // 해당 계정은 사용불가 계정이 돼버린다.
    // 이런 상황을 방지하기 위해서 회원가입 단계에서
    private void dbInsertion(String name, String address, String date, String phone) {
        String temp;
        temp = isNormal ? "users" : "enterprises";
        MemberInfoClass memberInfo = new MemberInfoClass(name, address, date, phone);
        db = FirebaseFirestore.getInstance();
        db.collection(temp).document(user.getUid()).set(memberInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                StartToast("회원가입에 성공하였습니다.");
                if(isNormal)
                    StartActivity(NormalMainActivity.class);
                else
                    StartActivity(EnterpriseMainActivity.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                StartToast("회원정보 오류로 회원가입에 실패하였습니다.");
            }
        });
    }

}
