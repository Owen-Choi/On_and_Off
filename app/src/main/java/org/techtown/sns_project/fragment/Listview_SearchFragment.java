package org.techtown.sns_project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.sns_project.R;

public class Listview_SearchFragment extends Fragment {

    View view;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String[] name = {"Hong ui sung", "Lim dong hyeok", "Lim im bum", "Shin jun young", "choi cheol woong"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);

        listView = view.findViewById(R.id.keyword_listView);
//        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
        listView.setAdapter(arrayAdapter);
        return view;
    }
}
