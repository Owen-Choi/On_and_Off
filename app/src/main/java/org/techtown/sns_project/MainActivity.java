package org.techtown.sns_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.LogoutButton).setOnClickListener(onClickListener);
        // manifest에서 첫 화면은 MainActivity로 설정되어있는데,
        // 로그인이 되지 않은 상태면 로그인창을 띄워야 한다.
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            StartSignUpActivity();
        }
    }
    private void StartSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                // 로그아웃 버튼을 누르면 로그아웃과 동시에 SignUp 페이지로 회귀
                case R.id.LogoutButton:
                    FirebaseAuth.getInstance().signOut();
                    StartSignUpActivity();
            }
        }
    };
}