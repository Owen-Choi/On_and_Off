package org.techtown.sns_project.Normal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.CommonSignInActivity;
import org.techtown.sns_project.InitialActivity;
import org.techtown.sns_project.Normal.Setting.NormalSettingActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.Camera.ScanQR;
import org.techtown.sns_project.fragment.BoardFragment;
import org.techtown.sns_project.fragment.HomeFragment;
import org.techtown.sns_project.fragment.profile.ProfileFragment;
import org.techtown.sns_project.fragment.SearchFragment;
import org.techtown.sns_project.fragment.SomethingFragment;
import org.techtown.sns_project.pushAlarm.BackgroundAlarmService;


public class NormalMainActivity extends AppCompatActivity {
    Fragment Board_Fragment;
    Fragment Home_Fragment;
    Fragment Profile_Fragment;
    Fragment Search_Fragment;
    BottomNavigationView bottomNavigationView;
    private long backKeyPressedTime = 0;
    private Toast terminate_guide_msg;
    public int alarmCount;
    private BroadcastReceiver mReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_main);
        //findViewById(R.id.NormalQRScanButton).setOnClickListener(onClickListener);

        Intent serviceintent = new Intent( NormalMainActivity.this, BackgroundAlarmService.class );
        startService( serviceintent );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            StartActivity(CommonSignInActivity.class);
        }

        Board_Fragment = new BoardFragment();
        Home_Fragment = new HomeFragment();
        Profile_Fragment = new ProfileFragment();
        Search_Fragment = new SearchFragment();

        // 시작하면 home fragment를 띄운다.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout,Home_Fragment).commitAllowingStateLoss();
        setTitle("QR");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Home_Fragment).commitAllowingStateLoss();
                        setTitle("QR");

                        return true;
                    case R.id.nav_board:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Board_Fragment).commitAllowingStateLoss();
                        setTitle("BOARD");
                        return true;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Profile_Fragment).commitAllowingStateLoss();
                        setTitle("MY");
                        return true;
                    case R.id.nav_qr:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, Search_Fragment).commitAllowingStateLoss();
                        setTitle("SEARCH");
                        return true;

                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("woong", "hi there");
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
        inflater.inflate(R.menu.actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
            backKeyPressedTime = System.currentTimeMillis();
            terminate_guide_msg = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            terminate_guide_msg.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
            terminate_guide_msg.cancel();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

}