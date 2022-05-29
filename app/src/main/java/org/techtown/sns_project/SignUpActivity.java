package org.techtown.sns_project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.Enterprise.EnterpriseMainActivity;
import org.techtown.sns_project.Normal.NormalMainActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private static final String TAG = "SignUpActivity";
    private RadioGroup radioGroup;
    private static boolean isNormal, isEnter;
    private long backKeyPressedTime = 0;
    private Toast terminate_guide_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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
            CheckSignUpCondition(email, password, passwordCheck);
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
    // 이런 상황을 방지하기 위해서 회원가입 단계에서 회원정보를 입력받도록 하겠다.
    private void dbInsertion(String name, String address, String date, String phone) {
        String temp;
        temp = isNormal ? "users" : "enterprises";
        if(name.length() > 0 && address.length() > 0 && date.length() >= 6 && phone.length() >= 8) {
            MemberInfoClass memberInfo = new MemberInfoClass(name, address, date, phone);
            db = FirebaseFirestore.getInstance();
            db.collection(temp).document(user.getUid()).set(memberInfo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            StartToast("회원가입에 성공하였습니다.");
                            if (isNormal)
                                StartActivity(NormalMainActivity.class);
                            else
                                StartActivity(EnterpriseMainActivity.class);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    StartToast("모든 조건이 만족되었지만 회원가입에 실패하였습니다.");
                    user.delete();
                }
            });
        } else {
            CheckSignUpMemberInfoCondition(name, address, date, phone);
        }
    }

    private void CheckSignUpMemberInfoCondition(String name, String address, String date, String phone) {
        // 메서드가 호출되는 시점에는 회원가입이 이루어진 상태이다.
        // 따라서 중간에 문제가 생겼다면 해당 계정을 삭제해주어야 한다.
        Log.e("temp", "CheckSignUpMemberInfoCondition: " + user.getEmail());
        user.delete();
        if(isNormal) {
            if(name.length() <= 0) {
                StartToast("회원 이름의 길이를 확인해주세요 : 1자 이상");
            }
            else if(address.length() <= 0) {
                StartToast("회원 주소지의 길이를 확인해주세요 : 1자 이상");
            }
            else if(date.length() < 6) {
                StartToast("생년월일의 길이를 확인해주세요 : 6자 이상");
            }
            else if(phone.length() < 8) {
                StartToast("전화번호의 길이를 확인해주세요 : 8자 이상");
            }
        }
        else {
            if(name.length() <= 0) {
                StartToast("기업 이름의 길이를 확인해주세요 : 1자 이상");
            }
            else if(address.length() <= 0) {
                StartToast("기업 주소지의 길이를 확인해주세요 : 1자 이상");
            }
            else if(date.length() < 6) {
                StartToast("설립일의 길이를 확인해주세요 : 6자 이상");
            }
            else if(phone.length() < 8) {
                StartToast("기업 전화번호의 길이를 확인해주세요 : 8자 이상");
            }
        }
    }

    private void CheckSignUpCondition(String email, String password, String passwordCheck) {
        if(email.length() <= 0) {
            StartToast("이메일 길이를 확인해주세요 : 1자 이상");
        }
        else if(password.length() <= 0) {
            StartToast("비밀번호 길이를 확인해주세요 : 1자 이상");
        }
        else if(passwordCheck.length() <= 0) {
            StartToast("비밀번호 확인 문자를 확인해주세요 : 1자 이상");
        }
        else if(!isNormal && !isEnter)
            StartToast("회원 유형을 선택해주세요.");
    }

}
