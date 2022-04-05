package org.techtown.sns_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.sns_project.Board.BoardAdapter;
import org.techtown.sns_project.Board.BoardPostClickEvent;
import org.techtown.sns_project.Enterprise.QR.EnterpriseQRListAdapter;
import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.Normal.Home.HomeFragmentLikeListAdpater;
import org.techtown.sns_project.Normal.Home.LikeBoardInfo;
import org.techtown.sns_project.R;
import org.techtown.sns_project.Camera.ScanQR;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    private View view;
    private String TAG = "프래그먼트";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView_LikeList;
    DataFormat df;
    static int nrlikes =0;
    int ranking=10;
    int num=0;
    static HomeFragmentLikeListAdpater adapter;
    public static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<Integer> listLike = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    public static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<ArrayList<ProductInfo>> listOfList = new ArrayList<>();
    ArrayList<LikeBoardInfo> likeRank = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView_LikeList = view.findViewById(R.id.recyclerView_LikeList);
        LinearLayoutManager LikeList = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL, false);
        recyclerView_LikeList.setLayoutManager(LikeList);
        adapter = new HomeFragmentLikeListAdpater();
        recyclerView_LikeList.setAdapter(adapter);
        adapter.setOnItemClickListener (new HomeFragmentLikeListAdpater.OnItemClickListener () {

            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {

                StartActivity(BoardPostClickEvent.class,position);
            }
        });
        db.collectionGroup("board").get().
                addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        listImgUrl.clear();
                        listDescription.clear();
                        listPublisher.clear();
                        listOfList.clear();
                        listDocument.clear();
                        adapter.listData.clear();
int count=0;
                        for(QueryDocumentSnapshot document : task.getResult()) {

//             List = (HashMap<String, Object>) document.getData();
                            df = document.toObject(DataFormat.class);
                            listImgUrl.add(df.getImageUrl());
                            listPublisher.add(df.getPublisher());
                            listDescription.add(df.getDescription());
                            listDocument.add(document.getId());
                            listOfList.add(df.getList());
                            listLike.add(df.getLike());
                            count++;
                            System.out.println(count+"COUNT"+df.getLike()+df.getPublisher()+ df.getImageUrl()+ df.getDescription());
                            if (num < ranking) {
                                LikeBoardInfo data = new LikeBoardInfo(df.getPublisher(), df.getImageUrl(), df.getDescription(), df.getLike());
                                likeRank.add(num, data);
                                num++;
                                if (num == ranking) {
                                    Collections.sort(likeRank, new BoardLikeComparator());

                                    for(int i=0; i<10; i++)
                                        System.out.println("test"+likeRank.get(i).getLike());

                                }

                            } else {
                                for (int i = 0; i < ranking; i++) {
                                    if (likeRank.get(i).getLike() < df.getLike()) {
                                        LikeBoardInfo data = new LikeBoardInfo(df.getPublisher(), df.getImageUrl(), df.getDescription(), df.getLike());
                                        likeRank.add(i, data);
                                        likeRank.remove(ranking + 1);
                                    }
                                }
                            }
                        }
                        adapter.addItemList(likeRank);
                        adapter.notifyDataSetChanged();
                    }


                });
          return view;
    }



    private void StartActivity(Class<BoardPostClickEvent> boardPostClickEventClass, int position) {
        Intent intent = new Intent(getContext(),boardPostClickEventClass);
        intent.putExtra("position",position);
        intent.putExtra("listImgUrl",listImgUrl);
        intent.putExtra("listPublisher",listPublisher);
        intent.putExtra("listDescription",listDescription);
        intent.putExtra("listOfList", listOfList);
        intent.putExtra("listDocument",listDocument);

        System.out.println("Start activity :" + listImgUrl);

        startActivity(intent);
    }
    private int nrLikes(String post_document) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference likesRef = db.collection("board").document(post_document).collection("Likes");
        likesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        nrlikes = 0;
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
                                nrlikes++;
                            }
                            System.out.println("NRLIKES"+nrlikes);
                        } else {
                            Log.d("nrlikes", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return nrlikes;
    }
    class BoardLikeComparator implements Comparator<LikeBoardInfo> {
        @Override
        public int compare(LikeBoardInfo f1, LikeBoardInfo f2) {
            System.out.println("TEST"+f1.getLike() + f2.getLike());
            if (f1.like > f2.like) {
                return 1;
            } else if (f1.getLike() < f2.getLike()) {
                return -1;
            }
            return 0;
        }
    }
    }

