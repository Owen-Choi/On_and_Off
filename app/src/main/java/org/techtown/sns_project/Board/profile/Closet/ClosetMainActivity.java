package org.techtown.sns_project.Board.profile.Closet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.R;


public class ClosetMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //옷 추가시 프래그먼트 새로그침을 위한 변수
    static String WhatFragment;
    String post_publisher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);
        Intent intent = getIntent();
        post_publisher = intent.getStringExtra("post_publisher");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //뒤로가기
        //getSupportActionBar().setTitle("Closet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        //옷장 옷 추가 버튼
        findViewById(R.id.main_write_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(ClosetMainActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setMessage("옷의 url을 입력하세요");


                final EditText et = new EditText(ClosetMainActivity.this);
                ad.setView(et);
                ad.setCancelable(false);
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = ad.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(v -> {
                            Boolean wantToCloseDialog = true;

                            String inputValue = et.getText().toString();
                            String url = inputValue.replaceAll("[^0-9]", "");
                            if (url.length() > 0) {

                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Closet_Parser closet_Parser = new Closet_Parser(firebaseAuth, post_publisher, db, inputValue);

                                if(closet_Parser.result==1){
                                    StartToast(" add on closet success! ");



                                    dialog.dismiss();

                                }else{
                                    StartToast(" invalid clothes type ");
                                    dialog.dismiss();
                                }

                            } else {
                                StartToast("Please type URL");
                            }
                        });
            }
        });
*/
        //네비게이션바
        bottomNavigationView = findViewById(R.id.bottomNavi);

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new AllFragment()).commit(); //FrameLayout에 fragment.xml 띄우기
        setTitle("ALL");
        Bundle bundle = new Bundle();
        bundle.putString("post_publisher",post_publisher);
        System.out.println("PUBLISHER"+post_publisher);
        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.item_fragment5:
                        AllFragment AllFragment = new AllFragment();
                        AllFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, AllFragment).commit();
                        setTitle("ALL");
                        break;
                    case R.id.item_fragment1:
                        outerFragment outerFragment = new outerFragment();
                        outerFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, outerFragment).commit();
                        setTitle("OUTER");
                        break;
                    case R.id.item_fragment2:
                        topFragment topFragment = new topFragment();
                        topFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, topFragment).commit();
                        setTitle("TOP");
                        break;
                    case R.id.item_fragment3:
                        bottomFragment bottomFragment = new bottomFragment();
                        bottomFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, bottomFragment).commit();
                        setTitle("BOTTOM");
                        break;
                    case R.id.item_fragment4:
                        shoesFragment shoesFragment = new shoesFragment();
                        shoesFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,shoesFragment).commit();
                        setTitle("SHOES");
                        break;

                }
                return true;
            }
        });

    }
    //옷 추가했을 때 새로고침을 위해,,
    public static void whatFragment(String v){
        WhatFragment = v;
    }


    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}