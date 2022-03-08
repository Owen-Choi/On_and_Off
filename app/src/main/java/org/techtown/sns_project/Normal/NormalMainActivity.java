package org.techtown.sns_project.Normal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.sns_project.CommonSignInActivity;
import org.techtown.sns_project.Normal.Setting.NormalSettingActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.SignInActivity;
import org.techtown.sns_project.cameraexample.ScanQR;
import org.techtown.sns_project.fragment.BoardFragment;
import org.techtown.sns_project.fragment.HomeFragment;
import org.techtown.sns_project.fragment.ProfileFragment;
import org.techtown.sns_project.fragment.SearchFragment;
import org.techtown.sns_project.fragment.SomethingFragment;

public class NormalMainActivity extends AppCompatActivity {
    Fragment Board_Fragment;
    Fragment Home_Fragment;
    Fragment Profile_Fragment;
    Fragment Search_Fragment;
    Fragment Something_Fragment;
    BottomNavigationView bottomNavigationView;
    SearchView searchView;
    ListView listView;
    String[] name = {"Hong ui sung", "Lim dong hyeok", "Lim im bum", "Shin jun young", "choi cheol woong"};

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_main);
        //findViewById(R.id.NormalQRScanButton).setOnClickListener(onClickListener);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            StartActivity(CommonSignInActivity.class);
        }

        Board_Fragment = new BoardFragment();
        Home_Fragment = new HomeFragment();
        Profile_Fragment = new ProfileFragment();
        Search_Fragment = new SearchFragment();
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
                                .replace(R.id.main_layout, Search_Fragment).commitAllowingStateLoss();
                        return true;
                    case R.id.nav_something:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout,Something_Fragment).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });

        //여기서부터는 검색어 리스트 띄우는 화면
        listView = findViewById(R.id.keyword_listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        listView.setAdapter(arrayAdapter);
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
        // 여기서부터는 검색관련 메뉴 코드
        MenuItem search = menu.findItem(R.id.SearchMenu);
        searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("상품명을 검색해주세요");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("temp", "onClick: click");
                // 여기서 리스트뷰가 담긴 프래그먼트를 띄워주자.
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.e("close", "onClose: 닫음");
                return true;
                // 닫을 경우 리스트뷰가 담긴 프래그먼트를 다른 프래그먼트로 전환해주자.
                // 큐인가? 이용해서 전에 보던 화면으로 전환하기
            }
        });

        return super.onCreateOptionsMenu(menu);
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