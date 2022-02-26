package org.techtown.sns_project.Enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.CommonSignInActivity;
import org.techtown.sns_project.Enterprise.Setting.EnterpriseSettingActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.SignUpActivity;

public class EnterpriseMainActivity extends AppCompatActivity {
    private final String TAG = "MainActivityDB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_main);
        findViewById(R.id.EnterpsireQRButton).setOnClickListener(onClickListener);
        // manifest에서 첫 화면은 MainActivity로 설정되어있는데,
        // 로그인이 되지 않은 상태면 로그인창을 띄워야 한다.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            StartActivity(SignUpActivity.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case  R.id.SettingMenu:
                StartActivity(EnterpriseSettingActivity.class);
                break;
            case R.id.LogoutMenu:
                FirebaseAuth.getInstance().signOut();
                StartActivity(CommonSignInActivity.class);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.EnterpsireQRButton:
                    StartActivity(EnterpriseQRActivity.class);
                    break;
            }
        }
    };
}