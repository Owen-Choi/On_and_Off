package org.techtown.sns_project.fragment.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.techtown.sns_project.Closet.ClosetAdapter;
import org.techtown.sns_project.Closet.Closet_info;
import org.techtown.sns_project.R;

import java.util.ArrayList;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ItemViewHolder> {

    static ArrayList<String> mypostimgUrl_list = new ArrayList<>();

    @NonNull
    @Override //inflater는 xml파일에 정의된 정보를 실제 메모리에 할당하는 용도
    public profileAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new profileAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull profileAdapter.ItemViewHolder holder, int position) {
        holder.onBind(mypostimgUrl_list.get(position));
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView mypost_imgview;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mypost_imgview = itemView.findViewById(R.id.mypost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    }
                }
            );
        }

        void onBind(String data) {
            Glide.with(itemView.getContext()).load(data).error(R.drawable.ic_launcher_background).into(mypost_imgview);

        }
    }

    void addItem(String mypostimgUrl) {

        mypostimgUrl_list.add(mypostimgUrl);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mypostimgUrl_list.size();
    }

    void removeItem(int position) {
        mypostimgUrl_list.remove(position);
        notifyItemRemoved(position);
    }

    public void clearList() {
        mypostimgUrl_list.clear();
    }
}