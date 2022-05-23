package org.techtown.sns_project.Normal.Search;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.zxing.integration.android.IntentResult;

import org.techtown.sns_project.R;
import org.techtown.sns_project.Camera.Activity_codi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<SearchTitleClass> titlesList;
    private ArrayList<SearchTitleClass> arraylist;
    RelativeLayout itemList;

    public ListViewAdapter(Context context, ArrayList<SearchTitleClass> titlesList) {
        mContext = context;
        this.titlesList = new ArrayList<SearchTitleClass>();
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchTitleClass>();
        this.arraylist.addAll(titlesList);
        Log.e("adapter", "ListViewAdapter: " + arraylist.size());
    }

    public class ViewHolder {
        TextView Brand;
        TextView Title;
    }

    @Override
    public int getCount() {

        return titlesList.size();
    }

    @Override
    public SearchTitleClass getItem(int position) {
        return titlesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.Brand = (TextView) view.findViewById(R.id.Brand_name);
            holder.Title = (TextView) view.findViewById(R.id.Title_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        System.out.println("HOLDER : "+titlesList.get(position).getBrand()+titlesList.get(position).getTitle());
        if(!titlesList.isEmpty())
        {

            holder.Brand.setText(titlesList.get(position).getBrand());
            holder.Title.setText(titlesList.get(position).getTitle());
        }
        itemList = (RelativeLayout) view.findViewById(R.id.search_item);
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("clicked", " : " + titlesList.get(position).getTitle());
                Log.e("clicked", " : " + titlesList.get(position).getUrl());
                // url로 화면을 띄우는 클래스를 활용해서 화면 띄워주는 부분.
                Intent intent = new Intent(mContext.getApplicationContext(), Activity_codi.class);
                String key =  titlesList.get(position).getUrl().replaceAll("[^0-9]", "");
                intent.putExtra("key", key);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        // 새 텍스트가 추가됐으니 일단 기존의 리스트를 비우고 일치하는 단어로 이루어진 리스트를 새로 만든다.

        // 입력된 텍스트의 길이가 0이면(입력이 안됐으면) 다시 이전의 리스트들을 띄운다.
        if (charText.length() == 0) {
            titlesList.clear();
            notifyDataSetChanged();
        } else {
            titlesList.clear();
            for (SearchTitleClass wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.getBrand().toLowerCase(Locale.getDefault()).contains(charText)) {
                    titlesList.add(wp);
                    System.out.println("test : "+wp.getBrand()+wp.getTitle());
                }
            }
            notifyDataSetChanged();
        }

    }
}
