package org.techtown.sns_project.fragment.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.techtown.sns_project.R;

import java.util.ArrayList;

public class CustomChoiceListViewAdapter<ListViewItem> extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    //private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;
    // data
    private ArrayList<MyProfile_info> data_list = new ArrayList<MyProfile_info>() ;

    // ListViewAdapter의 생성자
    public CustomChoiceListViewAdapter() {

    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bookmark_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ImageView = (ImageView) convertView.findViewById(R.id.postimg) ;
        TextView name = (TextView) convertView.findViewById(R.id.name) ;
        TextView description = (TextView) convertView.findViewById(R.id.description) ;

        Glide.with(convertView.getContext()).load(data_list.get(pos).getImgURL()).error(R.drawable.ic_launcher_background).into(ImageView);
        name.setText(data_list.get(pos).getPublisher());
        description.setText(data_list.get(pos).getDescrpition());

        return convertView;
    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return data_list.size() ;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return data_list.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(MyProfile_info data) {
        //ListViewItem item = new ListViewItem();
        data_list.add(data);
        this.notifyDataSetChanged();

    }

    public void clearList() {
        data_list.clear();
    }

}


















