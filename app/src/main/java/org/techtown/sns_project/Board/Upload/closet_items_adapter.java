package org.techtown.sns_project.Board.Upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;

public class closet_items_adapter extends RecyclerView.Adapter<closet_items_adapter.ClosetItemViewHolder> {
    Context context;
    static ArrayList<closet_info> listData = new ArrayList<>();
    public closet_items_adapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void ItemChange(ArrayList<closet_info> list) {
        listData = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClosetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_board_added_list, parent, false);
        return new ClosetItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClosetItemViewHolder holder, int position) {
        holder.OnBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ClosetItemViewHolder extends RecyclerView.ViewHolder {
        TextView Brand, Name;
        ImageView image;

        public ClosetItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.itemTitle);
            Brand = itemView.findViewById(R.id.itemCategory);
            image = itemView.findViewById(R.id.itemImage);
        }
        void OnBind(closet_info data) {
            String NameTXT = data.getName();
            // 일단 임시로 price를 넣겠다.
            String BrandTXT = data.getBrand();
            Brand.setText(BrandTXT);
            Name.setText(NameTXT);
            Glide.with(itemView.getContext()).load(data.getImg_url()).error(R.drawable.ic_launcher_background).into(image);
        }
    }
}
