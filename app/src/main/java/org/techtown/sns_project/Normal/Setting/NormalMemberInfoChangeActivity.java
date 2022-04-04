package org.techtown.sns_project.Normal.Setting;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.MemberInfo;
import org.techtown.sns_project.MemberInfoClass;
import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.SignInActivity;

public class NormalMemberInfoChangeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_member_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.NormalMemberInfoCheckButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void ChangeMemberInfo() {
        // 먼저 조건을 확인하고, 문제가 없다면 기존의 DB를 지운 뒤 새로 대입해주는 과정을 거친다.
        String name = ((EditText)findViewById(R.id.NameEditText)).getText().toString();
        String phone = ((EditText)findViewById(R.id.PhoneEditText)).getText().toString();
        String date = ((EditText)findViewById(R.id.DateEditText)).getText().toString();
        String address = ((EditText)findViewById(R.id.AddressEditText)).getText().toString();

        if(CheckMemberInfoCondition(name, address, date, phone)) {
            db.collection("users").document(firebaseUser.getUid()).delete();
            MemberInfoClass memberInfo = new MemberInfoClass(name, phone, date, address);
            // user.getUid를 하는 이유는 유저마다 다른 경로(document)를 갖게 하기 위함이다.
            // 기업용 유저는 컬렉션을 구분하도록 한다.
            db.collection("users").document(firebaseUser.getUid()).set(memberInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            StartToast("회원정보 변경에 성공하였습니다, 다시 로그인해주세요");
                            firebaseAuth.signOut();
                            StartActivity(SignInActivity.class);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    StartToast("회원정보 변경에 실패하였습니다.");
                }
            });
        }
    }

    private boolean CheckMemberInfoCondition(String name, String address, String date, String phone) {
        if(name.length() <= 0) {
            StartToast("회원 이름의 길이를 확인해주세요 : 1자 이상");
            return false;
        }
        else if(address.length() <= 0) {
            StartToast("회원 주소지의 길이를 확인해주세요 : 1자 이상");
            return false;
        }
        else if(date.length() < 6) {
            StartToast("생년월일의 길이를 확인해주세요 : 6자 이상");
            return false;
        }
        else if(phone.length() < 8) {
            StartToast("전화번호의 길이를 확인해주세요 : 8자 이상");
            return false;
        }
        return true;
    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        // 동일한 창이 여러번 뜨게 만드는 것이 아니라 기존에 켜져있던 창을 앞으로 끌어와주는 기능.
        // 이 플래그를 추가하지 않을 경우 창들이 중복돼서 계속 팝업되게 된다.
        // 메인화면을 띄우는 모든 코드에서 이 플래그를 추가해줘야 하는 것 같다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // view에서 id를 받아오는데
            switch (view.getId()) {
                // id가 RegisterButton에서 받아온 아이디라면 :
                case R.id.NormalMemberInfoCheckButton:
                    ChangeMemberInfo();
                    break;
            }
        }
    };

}
