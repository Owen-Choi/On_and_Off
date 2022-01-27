package org.techtown.sns_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.LogoutButton).setOnClickListener(onClickListener);
        // manifest에서 첫 화면은 MainActivity로 설정되어있는데,
        // 로그인이 되지 않은 상태면 로그인창을 띄워야 한다.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            StartActivity(SignUpActivity.class);
        }
        else {
            for(UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                if(name != null) {
                    // 이름이 없어도 null은 아닌 것 같다. 따라서 length로 판별하도록 한다.
                    if(name.length() == 0)
                        StartActivity(MemberInfoActivity.class);
                }
            }
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
                case R.id.LogoutButton:
                    FirebaseAuth.getInstance().signOut();
                    StartActivity(SignUpActivity.class);
            }
        }
    };
}