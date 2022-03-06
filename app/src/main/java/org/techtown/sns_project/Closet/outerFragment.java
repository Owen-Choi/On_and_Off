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


public class outerFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClosetAdapter adapter;
    private List<Closet_info> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_closet_outer, container, false);

        //recyclerview
        recyclerView = (RecyclerView) v.findViewById(R.id.outer_Recyclerview);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        //파베 연동 못해서 일단 임시방편삼아 수동으로 집어넣음
        list.add(new Closet_info("아우터 이름1","아우터 브랜드1",null));
        list.add(new Closet_info("아우터 이름2","아우터 브랜드2",null));


        adapter = new ClosetAdapter(list);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);

        return v;

    }
}