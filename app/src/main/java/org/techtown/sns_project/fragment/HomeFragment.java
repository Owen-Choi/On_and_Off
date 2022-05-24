package org.techtown.sns_project.fragment;

import static android.os.SystemClock.sleep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import org.techtown.sns_project.Board.BoardAdapter;
import org.techtown.sns_project.Board.LikeBoardPostClickEvent;
import org.techtown.sns_project.Board.Upload.UploadActivity;
import org.techtown.sns_project.Camera.Activity_codi;
import org.techtown.sns_project.Enterprise.QR.EnterpriseQRListAdapter;
import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.Normal.Home.HomeFragmentLikeListAdpater;
import org.techtown.sns_project.Normal.Home.HomeFragmentQrListAdpater;
import org.techtown.sns_project.Normal.Home.LikeBoardInfo;
import org.techtown.sns_project.Normal.Home.QrListInfo;
import org.techtown.sns_project.R;
import org.techtown.sns_project.Camera.ScanQR;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    private View view;
    private String TAG = "homefragmentTag";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView QrImage ;
    RecyclerView recyclerView_LikeList;
    RecyclerView recyclerView_QrList;
    LikeDataFormat df;
    QrDataFormat Qdf;
    static int nrlikes =0;
    int ranking=10;
    int Qranking=10;
    int num=0;
    static HomeFragmentLikeListAdpater adapter;
    static HomeFragmentQrListAdpater Qadapter;
    public static ArrayList<String> listQrInfo = new ArrayList<>();
    public static ArrayList<String> listQrImgUrl = new ArrayList<>();
    public static ArrayList<String> listQrTitle = new ArrayList<>();
    public static ArrayList<String> listQrEid = new ArrayList<>();
    public static ArrayList<String> listUrl = new ArrayList<>();

    public static ArrayList<Integer> listCount = new ArrayList<>();
    public static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<Integer> listLike = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    public static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<ArrayList<ProductInfo>> listOfList = new ArrayList<>();
    static ArrayList<LikeBoardInfo> likeRank = new ArrayList<>();
    static ArrayList<QrListInfo> QrRank = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        QrImage = view.findViewById(R.id.Qr_image);
        recyclerView_LikeList = view.findViewById(R.id.recyclerView_LikeList);
        LinearLayoutManager LikeList = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL, false);
        recyclerView_LikeList.setLayoutManager(LikeList);
        adapter = new HomeFragmentLikeListAdpater();
        recyclerView_LikeList.setAdapter(adapter);

        recyclerView_QrList = view.findViewById(R.id.recyclerView_QrList);
        LinearLayoutManager QrList = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL, false);
        recyclerView_QrList.setLayoutManager(QrList);
        Qadapter = new HomeFragmentQrListAdpater();
        recyclerView_QrList.setAdapter(Qadapter);
/*

        // 인범 This is on and off 애니메이션
        RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) view.findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(35);

        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, " ON", "  &", "OFF ");
        rotatable.setSize(35);
        rotatable.setAnimationDuration(500);

        rotatingTextWrapper.setContent("This is ?", rotatable);
        // 여기까지
*/

        db.collectionGroup("board").get().
                addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        listImgUrl.clear();
                        listDescription.clear();
                        listPublisher.clear();
                        listOfList.clear();
                        listDocument.clear();
                        adapter.listData.clear();
                        likeRank.clear();
                        int count=0;
                        num =0;
                        for(QueryDocumentSnapshot document : task.getResult()) {

//             List = (HashMap<String, Object>) document.getData();
                            df = document.toObject(LikeDataFormat.class);
                            listImgUrl.add(df.getImageUrl());
                            listPublisher.add(df.getPublisher());
                            listDescription.add(df.getDescription());
                            listDocument.add(document.getId());
                            listOfList.add(df.getList());
                            listLike.add(df.getNrlikes());
                            count++;
                            Log.d(TAG,count+"COUNT"+"LIKE:"+df.getNrlikes()+
                                    "\nPublisher:"+df.getPublisher()+ "\nImageUrl:"+
                                    df.getImageUrl()+ "\ndescription:"+ df.getDescription()
                                    +"\ndocumentID:"+document.getId());

                            if (num < ranking) {
                                LikeBoardInfo data = new LikeBoardInfo(df.getPublisher(), df.getImageUrl(), df.getDescription(), df.getNrlikes(),document.getId());
                                likeRank.add(data);
                                num++;
                                Collections.sort(likeRank, new BoardLikeComparator());
                            } else {
                                for (int i = 0; i < ranking; i++) {
                                    if (likeRank.get(i).getNrlikes() < df.getNrlikes()) {
                                        LikeBoardInfo data = new LikeBoardInfo(df.getPublisher(), df.getImageUrl(), df.getDescription(), df.getNrlikes(), document.getId());
                                        likeRank.add(i, data);
                                        likeRank.remove(ranking-1);
                                        break;
                                    }
                                }
                            }
                        }

                        adapter.addItemList(likeRank);
                        adapter.notifyDataSetChanged();
                    }


                });
        adapter.setOnItemClickListener (new HomeFragmentLikeListAdpater.OnItemClickListener () {
            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                StartActivity(LikeBoardPostClickEvent.class,position);
            }
        });

        QrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScanQR.class);
                startActivity(intent);
            }
        });


        db.collectionGroup("brand").get().
                addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        listUrl.clear();
                        listCount.clear();
                        listQrInfo.clear();
                        listQrTitle.clear();
                        listQrImgUrl.clear();
                        listQrEid.clear();
                        Qadapter.listData.clear();
                        QrRank.clear();
                        int count=0;
                        int qnum =0;
                        for(QueryDocumentSnapshot document : task.getResult()) {

//             List = (HashMap<String, Object>) document.getData();
                            Qdf = document.toObject(QrDataFormat.class);
                            listUrl.add(Qdf.getUrl());
                            listCount.add(Qdf.getcount());
                            listQrInfo.add(Qdf.getinfo());
                            listQrTitle.add(Qdf.gettitle());
                            listQrImgUrl.add(Qdf.getImgURL());
                            listQrEid.add(Qdf.geteid());
                            count++;
                            Log.d("TESTSTSTST",Qdf.getUrl()+Qdf.getcount());

                            if (qnum < Qranking) {
                                QrListInfo data = new QrListInfo(Qdf.getinfo(), Qdf.getImgURL(), Qdf.gettitle(), Qdf.getcount(),Qdf.geteid(),Qdf.getUrl());
                                QrRank.add(data);
                                qnum++;
                                Collections.sort(QrRank, new QrComparator());
                            } else {
                                for (int i = 0; i < Qranking; i++) {
                                    if (QrRank.get(i).getcount() < Qdf.getcount()) {
                                        QrListInfo data = new QrListInfo(Qdf.getinfo(), Qdf.getImgURL(), Qdf.gettitle(), Qdf.getcount(),Qdf.geteid(),Qdf.getUrl());
                                        QrRank.add(i, data);
                                        QrRank.remove(Qranking-1);
                                        break;
                                    }
                                }
                            }
                        }
                        Qadapter.addItemList(QrRank);
                        Qadapter.notifyDataSetChanged();}


                });
        Qadapter.setOnItemClickListener (new HomeFragmentQrListAdpater.OnItemClickListener () {
            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                StartQActivity(Activity_codi.class,position);
            }
        });



        return view;
    }




    private void StartActivity(Class<LikeBoardPostClickEvent> LikeBoardPostClickEventClass, int position) {
        Intent intent = new Intent(getContext(),LikeBoardPostClickEventClass);
        ArrayList<LikeBoardInfo> listData = adapter.getItem();
        intent.putExtra("listImgUrl",listData.get(position).getImgURL());
        intent.putExtra("listPublisher",listData.get(position).getPublisher());
        intent.putExtra("listDescription",listData.get(position).getDescrpition());
        intent.putExtra("listDocument",listData.get(position).getDocument());

        startActivity(intent);
    }

    private void StartQActivity(Class c, int position) {
        Intent intent = new Intent(getContext(),c);
        ArrayList<QrListInfo> listData = Qadapter.getItem();
        intent.putExtra("key",listData.get(position).getUrl());
        startActivity(intent);
    }

    class BoardLikeComparator implements Comparator<LikeBoardInfo> {
        @Override
        public int compare(LikeBoardInfo f1, LikeBoardInfo f2) {
            if (f1.getNrlikes() < f2.getNrlikes()) {
                return 1;
            } else if (f1.getNrlikes() > f2.getNrlikes()) {
                return -1;
            }
            return 0;
        }
    }

    class QrComparator implements Comparator<QrListInfo> {
        @Override
        public int compare(QrListInfo f1, QrListInfo f2) {
            if (f1.getcount() < f2.getcount()) {
                return 1;
            } else if (f1.getcount() > f2.getcount()) {
                return -1;
            }
            return 0;
        }
    }




}

