package org.techtown.sns_project.Enterprise;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.ProductInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EnterpriseQRListActivity  extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView_QrList;
    EnterpriseQRListAdapter adapter;
    HashMap<String,Object> List = new HashMap<String,Object>();
    String TAG="DONG";
    String DEFAULT_URL="https://store.musinsa.com/app/goods/";
    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listInfo = new ArrayList<>();
    ArrayList<String> listUrl = new ArrayList<>();
    ArrayList<String> listPrice = new ArrayList<>();
    ArrayList<String> listImgUrl = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_qr_list);

        recyclerView_QrList = findViewById(R.id.recyclerView_QrList);

        LinearLayoutManager QrList = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        recyclerView_QrList.setLayoutManager(QrList);
        adapter = new EnterpriseQRListAdapter();
        recyclerView_QrList.setAdapter(adapter);
        db.collection("enterprises").document(firebaseUser.getUid()).collection("brand").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
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


}

