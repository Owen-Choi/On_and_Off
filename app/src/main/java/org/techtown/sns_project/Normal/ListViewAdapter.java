package org.techtown.sns_project.Normal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public ListViewAdapter(Context context, List<SearchTitleClass> animalNamesList) {
        mContext = context;
        this.titlesList = animalNamesList;
        // 이건 무슨 역할이지?
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchTitleClass>();
        this.arraylist.addAll(animalNamesList);
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
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        titlesList.clear();
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
