package org.techtown.sns_project.Enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.R;
import org.techtown.sns_project.SignUpActivity;

public class EnterpriseMainActivity extends AppCompatActivity {
    private final String TAG = "MainActivityDB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_main);
        findViewById(R.id.EnterpriseMemberLogoutButton).setOnClickListener(onClickListener);
        // manifest에서 첫 화면은 MainActivity로 설정되어있는데,
        // 로그인이 되지 않은 상태면 로그인창을 띄워야 한다.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            StartActivity(SignUpActivity.class);
        }
    }
    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                // 로그아웃 버튼을 누르면 로그아웃과 동시에 SignUp 페이지로 회귀
                case R.id.EnterpriseMemberLogoutButton:
                    FirebaseAuth.getInstance().signOut();
                    StartActivity(SignUpActivity.class);
            }
        }
    };
}