package org.techtown.sns_project.Closet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllFragment extends Fragment {

    private RecyclerView recyclerView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ClosetAdapter Closet_adapter;
    HashMap<String,Object> List = new HashMap<String,Object>();
    String TAG="DONG";
    String DEFAULT_URL="https://store.musinsa.com/app/goods/";
    static ArrayList<String> listTitle = new ArrayList<>();
    static ArrayList<String> listInfo = new ArrayList<>();
    static ArrayList<String> listUrl = new ArrayList<>();
    static ArrayList<String> listImgUrl = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_closet_bottom, container, false);
        Closet_adapter = new ClosetAdapter();
        //recyclerview
        recyclerView = v.findViewById(R.id.bottom_Recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(Closet_adapter);

        db.collection("users").document(firebaseUser.getUid()).collection("아우터").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //데이터 중복 방지
                        listUrl.clear();
                        listTitle.clear();
                        listUrl.clear();
                        listInfo.clear();
                        Closet_adapter.list.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            listImgUrl.add((String)List.get("img_url"));
                            System.out.println("test"+List.get("img_url"));

                            listTitle.add((String)List.get("brand"));
                            System.out.println("test"+List.get("brand"));

                            listUrl.add((String)List.get("url"));
                            System.out.println("test"+List.get("url"));

                            listInfo.add((String) List.get("name"));
                            System.out.println("test"+List.get("name"));

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"), (String)List.get("img_url"),
                                    (String) List.get("url"));

                            Closet_adapter.addItem(data);
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        db.collection("users").document(firebaseUser.getUid()).collection("상의").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            listImgUrl.add((String)List.get("img_url"));
                            System.out.println("test"+List.get("img_url"));

                            listTitle.add((String)List.get("brand"));
                            System.out.println("test"+List.get("brand"));

                            listUrl.add((String)List.get("url"));
                            System.out.println("test"+List.get("url"));

                            listInfo.add((String) List.get("name"));
                            System.out.println("test"+List.get("name"));

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"), (String)List.get("img_url"),
                                    (String) List.get("url"));

                            Closet_adapter.addItem(data);
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        db.collection("users").document(firebaseUser.getUid()).collection("하의").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            listImgUrl.add((String)List.get("img_url"));
                            System.out.println("test"+List.get("img_url"));

                            listTitle.add((String)List.get("brand"));
                            System.out.println("test"+List.get("brand"));

                            listUrl.add((String)List.get("url"));
                            System.out.println("test"+List.get("url"));

                            listInfo.add((String) List.get("name"));
                            System.out.println("test"+List.get("name"));

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"), (String)List.get("img_url"),
                                    (String) List.get("url"));

                            Closet_adapter.addItem(data);
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        db.collection("users").document(firebaseUser.getUid()).collection("신발").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            listImgUrl.add((String)List.get("img_url"));
                            System.out.println("test"+List.get("img_url"));

                            listTitle.add((String)List.get("brand"));
                            System.out.println("test"+List.get("brand"));

                            listUrl.add((String)List.get("url"));
                            System.out.println("test"+List.get("url"));

                            listInfo.add((String) List.get("name"));
                            System.out.println("test"+List.get("name"));

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"), (String)List.get("img_url"),
                                    (String) List.get("url"));

                            Closet_adapter.addItem(data);
                        }

                        Closet_adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });


        return v;
    }
}