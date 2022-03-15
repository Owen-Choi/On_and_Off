package org.techtown.sns_project.Normal.Search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<SearchTitleClass> titlesList = null;
    private ArrayList<SearchTitleClass> arraylist;
    RelativeLayout itemList;

    public ListViewAdapter(Context context, List<SearchTitleClass> titlesList) {
        mContext = context;
        this.titlesList = titlesList;
        // 이건 무슨 역할이지?
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchTitleClass>();
        this.arraylist.addAll(titlesList);
        Log.e("adapter", "ListViewAdapter: " + arraylist.size());
    }

    public class ViewHolder {
        TextView name;
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
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(titlesList.get(position).getTitle());
        itemList = (RelativeLayout) view.findViewById(R.id.search_item);
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("clicked", " : " + arraylist.get(position).getTitle());
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        // 새 텍스트가 추가됐으니 일단 기존의 리스트를 비우고 일치하는 단어로 이루어진 리스트를 새로 만든다.
        titlesList.clear();
        // 입력된 텍스트의 길이가 0이면(입력이 안됐으면) 다시 이전의 리스트들을 띄운다.
        if (charText.length() == 0) {
            titlesList.addAll(arraylist);
        } else {
            for (SearchTitleClass wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    titlesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
