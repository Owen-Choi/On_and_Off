package org.techtown.sns_project.fragment.profile.Bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.Board.BoardPostClickEvent;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.DataFormat;
import org.techtown.sns_project.fragment.profile.MyProfile_info;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class bookmark extends AppCompatActivity {

    //파이어베이스
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db ;

    //Listview
    ListView listview;
    bookmark_ListViewAdapter adapter;

    //리스트 뷰 클릭 이벤트를 위한 intent에 담을 변수
    public static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    public static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<ArrayList<ProductInfo>> listOfList = new ArrayList<>();
    DataFormat df;

    //내가 save한 포스트id 담을 변수
    static ArrayList<String> MysavepostID = new ArrayList<>();
    HashMap<String, Object> List;

    String tag = "bookmark ";

    //Activity의 생명주기
    //수업 강의 자료 참고
    @Override
    protected void onStart() {
        super.onStart();

        //파베에서 내가 save한 게시물id 가져오기
        scatter();

        Log.e(tag,"In the onStart() event");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Log.e(tag,"In the onRestart() event");
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Log.e(tag,"In the onResume() event");
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Log.e(tag,"In the onPause() event");
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Log.e(tag,"In the onStop() event");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Log.e(tag,"In the onDestroy() event");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Log.e(tag,"In the onCreate() event");

        //타이틀 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //파베 연동
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //Listview
        listview = (ListView) findViewById(R.id.listview1);
        adapter = new bookmark_ListViewAdapter();
        listview.setAdapter(adapter);

        //북마크 아이템 뷰 클릭 이벤트
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), BoardPostClickEvent.class);
                intent.putExtra("position", position);
                intent.putExtra("listImgUrl", listImgUrl);
                intent.putExtra("listPublisher", listPublisher);
                intent.putExtra("listDescription", listDescription);
                intent.putExtra("listOfList", listOfList);
                intent.putExtra("listDocument", listDocument);
                System.out.println("Start activity :" + listImgUrl);
                startActivity(intent);

            }
        });

    }

    public void scatter(){
        //파베에서 내가 save한 게시물id 가져오기
        adapter.clearList();
        adapter.notifyDataSetChanged();
        db.collection("users").document(firebaseUser.getUid()).
                collection("board_saves").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        //중복 방지
                        MysavepostID.clear();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            //내가 저장한 post ID가 저장 되됨
                            MysavepostID.add(document.getId());
                        }
                    }

                    //파베에서 내가 저장한 게시글 정보를 가져와서 어뎁터에 data 전달
                    for(int i = 0; i < MysavepostID.size(); i++){

                        db.collection("board").document(MysavepostID.get(i)).get().
                                addOnCompleteListener(task2 -> {
                                    if (task.isSuccessful()) {
                                        //데이터 중복 방지
                                        listImgUrl.clear();
                                        listDescription.clear();
                                        listPublisher.clear();
                                        listOfList.clear();
                                        listDocument.clear();

                                        DocumentSnapshot document = task2.getResult();
                                        List = (HashMap<String, Object>) document.getData();

                                        df = document.toObject(DataFormat.class);

                                        if(List!=null) { //게시글 삭제했을 때 board_saves 에서까지 굳이 삭제 안되도 일 북마크 돌아가게끔 설정단. 다만 파베에 데이터가 계속 쌓일뿐,,

                                            MyProfile_info data = new MyProfile_info(
                                                    (String) List.get("userid"),
                                                    (String) List.get("imageUrl"),
                                                    (String) List.get("description"));

                                            //밑에 클릭 이벤트에서 position 변수 매치가 안되서 3개 다 넘김
                                            adapter.addItem(data, df, document);
                                        }
                                    }
                                });
                    }
                });
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}