package org.techtown.sns_project.Closet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.List;


public class bottomFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClosetAdapter adapter;
    private List<Closet_info> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_closet_bottom, container, false);

        //recyclerview
        recyclerView = (RecyclerView) v.findViewById(R.id.bottom_Recyclerview);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        //파베 연동 못해서 일단 임시방편삼아 수동으로 집어넣음
        list.add(new Closet_info("바지 이름1","바지 브랜드1",null,null));
        list.add(new Closet_info("바지 이름2","바지 브랜드2",null,null));
        list.add(new Closet_info("바지 이름3","바지 브랜드3",null,null));
        list.add(new Closet_info("바지 이름4","바지 브랜드4",null,null));

        adapter = new ClosetAdapter(list);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);

        return v;
    }
}