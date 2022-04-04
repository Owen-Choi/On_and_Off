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

public class NormalMemberInfoActivity extends AppCompatActivity implements MemberInfo {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_member_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.NormalMemberInfoCheckButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void SetInfo() {
        String name = ((EditText)findViewById(R.id.NameEditText)).getText().toString();
        String phone = ((EditText)findViewById(R.id.PhoneEditText)).getText().toString();
        String date = ((EditText)findViewById(R.id.DateEditText)).getText().toString();
        String address = ((EditText)findViewById(R.id.AddressEditText)).getText().toString();

        if(name.length() > 0 && phone.length() > 9 && date.length() > 5 && address.length() > 1) {
            user = mAuth.getCurrentUser();
            // Firestore 데이터베이스 인스턴스 생성
            // DB에 수월하게 접근하기 위해 일종의 데이터 프레임을 제공하는 객체를 만든다.
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfoClass memberInfo = new MemberInfoClass(name, phone, date, address);
            // user.getUid를 하는 이유는 유저마다 다른 경로(document)를 갖게 하기 위함이다.
            // 기업용 유저는 컬렉션을 구분하도록 한다.
            db.collection("users").document(user.getUid()).set(memberInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            StartToast("회원정보 등록에 성공하였습니다.");
                            StartActivity(NormalMainActivity.class);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    StartToast("회원정보 등록에 실패하였습니다.");
                }
            });
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
                case R.id.NormalMemberInfoCheckButton:
                    SetInfo();
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

}
