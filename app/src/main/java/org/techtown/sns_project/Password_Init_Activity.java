package org.techtown.sns_project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.Board.Upload.UploadActivity;
import org.techtown.sns_project.fragment.profile.Closet.Closet_Parser;

import java.util.Objects;

public class Password_Init_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    static String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_init);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        findViewById(R.id.SendButton).setOnClickListener(onClickListener);
    }

    //인범 추가
    public class CustomDialog extends Dialog {

        private EditText et_text;
        private Context mContext;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.custom_dialog);

            // 다이얼로그의 배경을 투명으로 만든다.
            Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // 커스텀 다이얼로그의 각 위젯들을 정의한다.
            et_text = findViewById(R.id.put_text);
            TextView textView = findViewById(R.id.text);
            textView.setText("가입하신 이메일을 입력해주세요.");
            Button saveButton = findViewById(R.id.btnSave);
            Button cancelButton = findViewById(R.id.btnCancle);

            // 버튼 리스너 설정
            saveButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // '확인' 버튼 클릭시
                    // ...코드..
                    // Custom Dialog 종료
                    email = et_text.getText().toString();
                    createAlert();

                    dismiss();
                }
            });
            cancelButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // '취소' 버튼 클릭시
                    // ...코드..
                    // Custom Dialog 종료
                    StartToast("취소하셨습니다.");
                    dismiss();
                }
            });

        }

        public CustomDialog(Context mContext) {
            super(mContext);
            this.mContext = mContext;
        }


    }

    private void Send() {
//        String email = ((EditText)findViewById(R.id.EmailEditText)).getText().toString();
        // 로그인이 돼있는 상태라면 현재 이메일을 바로 사용
        // 그렇지 않다면 다이얼로그로부터 입력을 받는다.
        // 로그인화면에서 비밀번호를 재설정할 경우 로그인이 되어있지 않고,
        // 설정 화면에서 비밀번호를 재설정할 경우 로그인이 되어있기 때문에 분기를 나누는 것.


        if(user == null) {

            // 호출 - 인범 추가 커스텀 다이얼로그
            CustomDialog dlg = new CustomDialog(Password_Init_Activity.this);
            dlg.show();

            /*// 다이얼로그로 이메일 받기
            final EditText input = new EditText(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("가입하신 이메일을 입력해주세요.");
            alert.setView(input);

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StartToast("취소하셨습니다.");
                    dialogInterface.cancel();
                }
            });

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    email = input.getText().toString();
                    createAlert();
                }
            });

            alert.show();*/
        }
        else {
            email = user.getEmail();
            createAlert();
        }
    }

    private void createAlert() {
        if(email.length() > 0) {

            //인범 추가
            Dialog dilaog01; // 커스텀 다이얼로그
            dilaog01 = new Dialog(Password_Init_Activity.this);       // Dialog 초기화
            dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
            dilaog01.setContentView(R.layout.custom_dialog_2);             // xml 레이아웃 파일과 연결

            TextView textView = dilaog01.findViewById(R.id.textview);
            textView.setTextSize(13);
            textView.setText("메일을 발송하시겠습니까?");

            dilaog01.show(); // 다이얼로그 띄우기
            dilaog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경
            /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */

            // 위젯 연결 방식은 각자 취향대로~
            // '아래 아니오 버튼'처럼 일반적인 방법대로 연결하면 재사용에 용이하고,
            // '아래 네 버튼'처럼 바로 연결하면 일회성으로 사용하기 편함.
            // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

            // 왼쪽 버튼
            Button noBtn = dilaog01.findViewById(R.id.leftBtn);
            noBtn.setText("CANCEL");
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 원하는 기능 구현
                    StartToast("취소하셨습니다.");
                    dilaog01.dismiss(); // 다이얼로그 닫기
                }
            });
            // 오른쪽 버튼
            Button yesBtn = dilaog01.findViewById(R.id.rightBtn);
            yesBtn.setText("OK");
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 원하는 기능 구현
                    SendMail(email);
                    dilaog01.dismiss(); // 다이얼로그 닫기
                }
            });

            /*
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

             */

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
            switch (view.getId()) {
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
