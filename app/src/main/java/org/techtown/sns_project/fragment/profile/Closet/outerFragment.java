package org.techtown.sns_project.fragment.profile.Closet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.Camera.Activity_codi;
import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.HashMap;

public class outerFragment extends Fragment {

    static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    static FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private static ClosetAdapter Closet_adapter;

    static HashMap<String,Object> List = new HashMap<String,Object>();
    final CharSequence[] selectOption = {"코디 보기", "항목 삭제하기"};

    static String TAG="DONG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_closet_outer, container, false);

        //recyclerview
        recyclerView = v.findViewById(R.id.outer_Recyclerview);
        Closet_adapter = new ClosetAdapter();
        recyclerView.setAdapter(Closet_adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);

        //파베에서 옷 정보 가져와서 어뎁터에 전달
        scatter();
        ClosetMainActivity.whatFragment("outer");

        //클릭시 삭제 or 코디
        Closet_adapter.setOnItemClickListener(new ClosetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, String delItem, String clothes_type, ArrayList<Closet_info> list) {


                Dialog dilaog01; // 커스텀 다이얼로그
                dilaog01 = new Dialog(getContext());       // Dialog 초기화
                dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
                dilaog01.setContentView(R.layout.custom_dialog_2);             // xml 레이아웃 파일과 연결

                TextView textView = dilaog01.findViewById(R.id.textview);
                textView.setText("Menu");
                textView.setTextSize(25);

                dilaog01.show(); // 다이얼로그 띄우기
                dilaog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경
                /* 이 함수 안에 원하는 디자인과 기능을 구현하면 된다. */

                // 위젯 연결 방식은 각자 취향대로~
                // '아래 아니오 버튼'처럼 일반적인 방법대로 연결하면 재사용에 용이하고,
                // '아래 네 버튼'처럼 바로 연결하면 일회성으로 사용하기 편함.
                // *주의할 점: findViewById()를 쓸 때는 -> 앞에 반드시 다이얼로그 이름을 붙여야 한다.

                // 오른쪽 버튼
                Button noBtn = dilaog01.findViewById(R.id.rightBtn);
                noBtn.setText("코디 보기");
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 원하는 기능 구현
                        //코디 보여주기
                        // listData에서 URL 가져와서 파싱된 화면 띄워주자.
                        Intent intent = new Intent(v.getContext(), Activity_codi.class);
                        String key =  list.get(pos).getUrl().replaceAll("[^0-9]", "");
                        intent.putExtra("key", key);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        dilaog01.dismiss(); // 다이얼로그 닫기
                    }
                });
                // 왼쪽 버튼
                Button yesBtn = dilaog01.findViewById(R.id.leftBtn);
                yesBtn.setText("항목 삭제");
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 원하는 기능 구현
                        //항목 삭제
                        db.collection("users").document(firebaseUser.getUid()).collection(clothes_type)
                                .document(delItem).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Closet_adapter.removeItem(pos);
                                dilaog01.dismiss();
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                        dilaog01.dismiss(); // 다이얼로그 닫기
                    }
                });

 /*
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
*/
            }
        });

        return v;
    }

    //파베에서 옷 정보 가져와서 어뎁터에 전달
    public static void scatter(){

        Closet_adapter.list.clear();
        Closet_adapter.notifyDataSetChanged();
        db.collection("users").document(firebaseUser.getUid()).collection("아우터").get().
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
    }
}