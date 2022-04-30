package org.techtown.sns_project.Enterprise.QR;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class EnterpriseQRListActivity  extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView_QrList;
    static EnterpriseQRListAdapter adapter;
    HashMap<String,Object> List = new HashMap<String,Object>();
    String TAG="DONG";
    String DEFAULT_URL="https://store.musinsa.com/app/goods/";
    static ArrayList<String> listTitle = new ArrayList<>();
    static ArrayList<String> listInfo = new ArrayList<>();
    static ArrayList<String> listUrl = new ArrayList<>();
    static ArrayList<String> listPrice = new ArrayList<>();
    static ArrayList<String> listImgUrl = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_qr_list);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("ON & OFF");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView_QrList = findViewById(R.id.recyclerView_QrList);

        LinearLayoutManager QrList = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        recyclerView_QrList.setLayoutManager(QrList);
        adapter = new EnterpriseQRListAdapter();
        recyclerView_QrList.setAdapter(adapter);

        adapter.setOnItemClickListener (new EnterpriseQRListAdapter.OnItemClickListener () {

            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                String url = listUrl.get (position);
                System.out.println("DONG : "+url + "position : "+position);
                StartActivity(EnterpriseQRListClickEvent.class,position);
            }

        });

        db.collection("enterprises").document(firebaseUser.getUid()).collection("brand").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //데이터 중복 방지
                        listUrl.clear();
                        listPrice.clear();
                        listTitle.clear();
                        listUrl.clear();
                        listInfo.clear();
                        adapter.listData.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            listImgUrl.add((String)List.get("imgURL"));
                            System.out.println("test"+List.get("imgURL"));

                            listPrice.add((String)List.get("price"));
                            System.out.println("test"+List.get("price"));

                            listTitle.add((String)List.get("title"));
                            System.out.println("test"+List.get("title"));

                            listUrl.add((String)List.get("url"));
                            System.out.println("test"+List.get("url"));

                            listInfo.add((String) List.get("info"));
                            System.out.println("test"+List.get("info"));

                            ProductInfo data = new ProductInfo((String)List.get("url"),(String)List.get("imgURL"), (String)List.get("title"),
                                    (String) List.get("info"), (String)List.get("price"));

                            adapter.addItem(data);
                        }
                        System.out.println("SIZE : "+listTitle.size());
                        System.out.println("COUNT : "+adapter.getItemCount());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });







    }
    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void StartActivity(Class c, int key) {
        Intent intent = new Intent(this, c);
        intent.putExtra("position", key);
        startActivity(intent);
    }


}

