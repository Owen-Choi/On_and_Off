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

public class shoesFragment extends Fragment {

    private RecyclerView recyclerView;
    private org.techtown.sns_project.Closet.ClosetAdapter adapter;
    private List<org.techtown.sns_project.Closet.Closet_info> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_closet_shoes, container, false);

        //recyclerview
        recyclerView = (RecyclerView) v.findViewById(R.id.shoes_Recyclerview);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        //파베 연동 못해서 일단 임시방편삼아 수동으로 집어넣음
        list.add(new org.techtown.sns_project.Closet.Closet_info("신발 이름1","신발 브랜드1",null,null));
        list.add(new org.techtown.sns_project.Closet.Closet_info("신발 이름2","신발 브랜드2",null,null));
        list.add(new org.techtown.sns_project.Closet.Closet_info("신발 이름3","신발 브랜드3",null,null));
        list.add(new org.techtown.sns_project.Closet.Closet_info("신발 이름4","신발 브랜드4",null,null));

        adapter = new org.techtown.sns_project.Closet.ClosetAdapter(list);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);

        return v;
    }
}