package org.techtown.sns_project.Normal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.CommonSignInActivity;
import org.techtown.sns_project.Enterprise.Setting.EnterpriseSettingActivity;
import org.techtown.sns_project.Normal.Setting.NormalSettingActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.SignInActivity;
import org.techtown.sns_project.SignUpActivity;
import org.techtown.sns_project.cameraexample.ScanQR;
import org.techtown.sns_project.fragment.BoardFragment;
import org.techtown.sns_project.fragment.HomeFragment;
import org.techtown.sns_project.fragment.ProfileFragment;
import org.techtown.sns_project.fragment.QRFragment;
import org.techtown.sns_project.fragment.SomethingFragment;

public class NormalMainActivity extends AppCompatActivity {
    Fragment Board_Fragment;
    Fragment Home_Fragment;
    Fragment Profile_Fragment;
    Fragment QR_Fragment;
    Fragment Something_Fragment;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_main);
        //findViewById(R.id.NormalQRScanButton).setOnClickListener(onClickListener);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            StartActivity(SignInActivity.class);
        }

        Board_Fragment = new BoardFragment();
        Home_Fragment = new HomeFragment();
        Profile_Fragment = new ProfileFragment();
        QR_Fragment = new QRFragment();
        Something_Fragment = new SomethingFragment();

        // 시작하면 home fragment를 띄운다.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout,Home_Fragment).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Home_Fragment).commitAllowingStateLoss();
                        return true;
                    case R.id.nav_board:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Board_Fragment).commitAllowingStateLoss();
                        return true;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Profile_Fragment).commitAllowingStateLoss();
                        return true;
                    case R.id.nav_qr:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,QR_Fragment).commitAllowingStateLoss();
                        return true;
                    case R.id.nav_something:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Something_Fragment).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });
//test

    }
    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
//    View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch(view.getId()) {
//                case R.id.NormalQRScanButton:
//                    StartActivity(ScanQR.class);
//            }
//        }
//    };
    // 메뉴 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.HomeFragQRButton:
                    StartActivity(ScanQR.class);
                    break;
            }
        }
    };

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