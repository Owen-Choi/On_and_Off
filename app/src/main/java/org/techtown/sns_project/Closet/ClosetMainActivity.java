package org.techtown.sns_project.Closet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.New_Parser;


public class ClosetMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);

        //뒤로가기
        getSupportActionBar().setTitle("Closet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Closet_Parser closet_Parser = new Closet_Parser(firebaseAuth, firebaseUser, db, inputValue);

                                if(closet_Parser.result==1){
                                    StartToast(closet_Parser.URL + " is add on closet ! ");
                                    dialog.dismiss();

                                }else{
                                    StartToast(closet_Parser.URL + " is invalid clothes type ");
                                    dialog.dismiss();
                                }

                            } else {
                                StartToast("Please type URL");
                            }
                        });
            }
        });

        //네비게이션바
        bottomNavigationView = findViewById(R.id.bottomNavi);

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new AllFragment()).commit(); //FrameLayout에 fragment.xml 띄우기

        //바텀 네비게이션뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.item_fragment5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new AllFragment()).commit();
                        break;
                    case R.id.item_fragment1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new outerFragment()).commit();
                        break;
                    case R.id.item_fragment2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new topFragment()).commit();
                        break;
                    case R.id.item_fragment3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new bottomFragment()).commit();
                        break;
                    case R.id.item_fragment4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new shoesFragment()).commit();
                        break;

                }
                return true;
            }
        });

    }

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}