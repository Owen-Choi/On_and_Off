package org.techtown.sns_project.Closet;

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
import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ItemViewHolder> {

    static ArrayList<Closet_info> list = new ArrayList<>();


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_closet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(list.get(position));

        //이미지 처리 해야..... grid...?
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView NameTextView;
        private TextView BrandTextView;
        private ImageView img_url;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            NameTextView = itemView.findViewById(R.id.item_name_text);
            BrandTextView = itemView.findViewById(R.id.item_brand_text);
            img_url = itemView.findViewById(R.id.imageView);


        }

        void onBind(Closet_info data) {
            String title = data.getBrand();
            String info = data.getName();
            if (info != null)
                NameTextView.setText(info);
            else {
                NameTextView.setText("null");
            }

            if (title != null)
                BrandTextView.setText(title);
            else {
                BrandTextView.setText("null");
            }


            Glide.with(itemView.getContext()).load(data.getImg_url()).error(R.drawable.ic_launcher_background).into(img_url);

        }
    }
    void addItem(Closet_info data) {
        // 외부에서 item을 추가시킬 함수입니다.
        list.add(data);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}

