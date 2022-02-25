package org.techtown.sns_project.Normal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.CommonSignInActivity;
import org.techtown.sns_project.Enterprise.Setting.EnterpriseSettingActivity;
import org.techtown.sns_project.Normal.Setting.NormalSettingActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.SignUpActivity;
import org.techtown.sns_project.cameraexample.ScanQR;

public class NormalMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_main);
        findViewById(R.id.NormalQRScanButton).setOnClickListener(onClickListener);
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
                case R.id.NormalQRScanButton:
                    StartActivity(ScanQR.class);
            }
        }
    };
    // 메뉴 코드
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
                StartActivity(NormalSettingActivity.class);
                break;
            case R.id.LogoutMenu:
                FirebaseAuth.getInstance().signOut();
                StartActivity(CommonSignInActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}