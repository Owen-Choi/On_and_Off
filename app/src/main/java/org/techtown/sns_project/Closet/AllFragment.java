package org.techtown.sns_project.Closet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_closet_all, container, false);
        Closet_adapter = new ClosetAdapter();
        //recyclerview
        recyclerView = v.findViewById(R.id.all_Recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(Closet_adapter);

        //파베에서 옷 정보 가져와서 어뎁터에 전달
        Closet_adapter.list.clear();
        Closet_adapter.notifyDataSetChanged();
        db.collection("users").document(firebaseUser.getUid()).collection("아우터").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"), (String)List.get("clothes_type"), (String)List.get("img_url"),
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

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"), (String)List.get("clothes_type"), (String)List.get("img_url"),
                                    (String) List.get("url"));

                            Closet_adapter.addItem(data);
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        db.collection("users").document(firebaseUser.getUid()).collection("바지").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"), (String)List.get("clothes_type"), (String)List.get("img_url"),
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

                            Closet_info data = new Closet_info((String)List.get("name"),(String)List.get("brand"),(String)List.get("clothes_type"), (String)List.get("img_url"),
                                    (String) List.get("url"));

                            Closet_adapter.addItem(data);
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        //클릭시 삭제
        Closet_adapter.setOnItemClickListener(new ClosetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, String delItem, String clothes_type) {

                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setIcon(android.R.drawable.ic_menu_delete);
                ad.setTitle("삭제");
                ad.setMessage("해당 항목을 삭제하시겠습니까?");

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
                        .setOnClickListener(m -> {

                            db.collection("users").document(firebaseUser.getUid()).collection(clothes_type)
                                    .document(delItem).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Closet_adapter.removeItem(pos);
                                    dialog.dismiss();
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });


                        });


            }
        });


        return v;

    }



}