package org.techtown.sns_project.Board.Upload.closet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.sns_project.Closet.Closet_info;
import org.techtown.sns_project.R;

import java.util.ArrayList;

public class closet_shoes_adapter extends RecyclerView.Adapter<closet_shoes_adapter.ClosetShoesItemViewHolder>{
    Context context;
    static ArrayList<Closet_info> listData = new ArrayList<>();
    public closet_shoes_adapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void ItemChange(Closet_info ci) {
        listData.add(ci);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClosetShoesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_board_added_closet_list, parent, false);
        return new ClosetShoesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClosetShoesItemViewHolder holder, int position) {
        holder.OnBind(listData.get(position));
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ClosetShoesItemViewHolder extends RecyclerView.ViewHolder {
        TextView Brand, Name;
        ImageView image;

        public ClosetShoesItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.ClosetitemTitle);
            Brand = itemView.findViewById(R.id.ClosetItemCategory);
            image = itemView.findViewById(R.id.ClosetItemImage);
        }
        void OnBind(Closet_info data) {
            String NameTXT = data.getName();
            String BrandTXT = data.getBrand();
            Brand.setText(BrandTXT);
            Name.setText(NameTXT);
            Glide.with(itemView.getContext()).load(data.getImg_url()).error(R.drawable.ic_launcher_background).into(image);
        }
    }
}