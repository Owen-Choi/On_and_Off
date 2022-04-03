package org.techtown.sns_project.fragment.profile.Bookmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.DataFormat;
import org.techtown.sns_project.fragment.profile.Bookmark.bookmark;
import org.techtown.sns_project.fragment.profile.MyProfile_info;

import java.util.ArrayList;

public class bookmark_ListViewAdapter<ListViewItem> extends BaseAdapter {

    // data
    private ArrayList<MyProfile_info> data_list = new ArrayList<MyProfile_info>() ;
    private ArrayList<DataFormat> datafomat_list = new ArrayList<DataFormat>() ;
    private ArrayList<DocumentSnapshot> snapshots_list = new ArrayList<DocumentSnapshot>() ;


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View itemView, ViewGroup parent) {

        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.bookmark_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ImageView = (ImageView) itemView.findViewById(R.id.postimg) ;
        TextView name = (TextView) itemView.findViewById(R.id.name) ;
        TextView description = (TextView) itemView.findViewById(R.id.description) ;

        Glide.with(itemView.getContext()).load(data_list.get(position).getImgURL()).error(R.drawable.ic_launcher_background).into(ImageView);
        name.setText(data_list.get(position).getPublisher());
        description.setText(data_list.get(position).getDescrpition());

        //리스트뷰 클릭이벤트를 위한 노가다;;
        bookmark.listImgUrl.add(datafomat_list.get(position).getImageUrl());
        bookmark.listPublisher.add(datafomat_list.get(position).getPublisher());
        bookmark.listDescription.add(datafomat_list.get(position).getDescription());
        bookmark.listDocument.add(snapshots_list.get(position).getId());
        bookmark.listOfList.add(datafomat_list.get(position).getList());

        return itemView;
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
    public void addItem(MyProfile_info data, DataFormat df, DocumentSnapshot documentSnapshot) {
        data_list.add(data);
        datafomat_list.add(df);
        snapshots_list.add(documentSnapshot);
        this.notifyDataSetChanged();

    }

    public void clearList() {
        data_list.clear();
        datafomat_list.clear();
        snapshots_list.clear();

    }

}





