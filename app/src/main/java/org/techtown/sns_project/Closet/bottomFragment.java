package org.techtown.sns_project.Closet;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.Camera.Activity_codi;
import org.techtown.sns_project.Enterprise.QR.EnterpriseQRListActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class bottomFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private ClosetAdapter Closet_adapter;
    HashMap<String,Object> List = new HashMap<String,Object>();
    String TAG="DONG";

    final CharSequence[] selectOption = {"코디 보기", "항목 삭제하기"};


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

        //파베에서 옷 정보 가져와서 어뎁터에 전달
        Closet_adapter.list.clear();
        Closet_adapter.notifyDataSetChanged();
        db.collection("users").document(firebaseUser.getUid()).collection("바지").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //데이터 중복 방지

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();
                            Closet_info data = new Closet_info(
                                    (String)List.get("name"),
                                    (String)List.get("brand"),
                                    (String)List.get("clothes_type"),
                                    (String)List.get("img_url"),
                                    (String) List.get("url"));

                            Closet_adapter.addItem(data);
                        }

                        Closet_adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        //클릭시 삭제 or 코디
        Closet_adapter.setOnItemClickListener(new ClosetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, String delItem, String clothes_type, ArrayList<Closet_info> list) {

                AlertDialog.Builder ad = new AlertDialog.Builder(v.getContext());
                ad.setTitle("Menu")
                        .setIcon(R.drawable.ic_noun_menu_4719158)
                        .setItems(selectOption, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //코디 보여주기
                                        // listData에서 URL 가져와서 파싱된 화면 띄워주자.
                                        Intent intent = new Intent(v.getContext(), Activity_codi.class);
                                        String key =  list.get(pos).getUrl().replaceAll("[^0-9]", "");
                                        intent.putExtra("key", key);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        v.getContext().startActivity(intent);
                                        break;
                                    case 1:
                                        //항목 삭제
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

                                        break;
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();


            }
        });

        return v;
    }
}