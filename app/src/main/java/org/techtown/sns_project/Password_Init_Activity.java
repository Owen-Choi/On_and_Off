package org.techtown.sns_project;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.Normal.NormalMainActivity;

public class Password_Init_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser fb;
    static String tempEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_init);
        mAuth = FirebaseAuth.getInstance();
        fb = mAuth.getCurrentUser();
        findViewById(R.id.SendButton).setOnClickListener(onClickListener);
    }


    private void Send() {
//        String email = ((EditText)findViewById(R.id.EmailEditText)).getText().toString();
        // 로그인이 돼있는 상태라면 현재 이메일을 바로 사용
        // 그렇지 않다면 다이얼로그로부터 입력을 받는다.
        // 로그인화면에서 비밀번호를 재설정할 경우 로그인이 되어있지 않고,
        // 설정 화면에서 비밀번호를 재설정할 경우 로그인이 되어있기 때문에 분기를 나누는 것.
        String email;
        if(fb == null) {
            // 다이얼로그로 이메일 받기
            final EditText input = new EditText(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("가입하신 이메일을 입력해주세요.");
            alert.setView(input);

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StartToast("취소하셨습니다.");
                }
            });

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tempEmail = input.getText().toString();
                }
            });
            alert.show();
           email = tempEmail;
        }
        else {
            email = fb.getEmail();
        }
        if(email.length() > 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setCancelable(true);
            alert.setMessage("메일을 발송하시겠습니까?");
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StartToast("취소하셨습니다.");
                }
            });
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SendMail(email);
                }
            });
            alert.show();
        }
        else
            StartToast("빈칸을 확인해주세요.");
    }

    private void SendMail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    StartToast("이메일이 정상적으로 발송되었습니다.");
                    mAuth.signOut();
                    StartActivity(CommonSignInActivity.class);
                    // 로그인 화면으로 돌아갔으면 좋겠는데..
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                StartToast("존재하지 않는 이메일입니다.");
            }
        });
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
                case R.id.SendButton:
                    Send();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
