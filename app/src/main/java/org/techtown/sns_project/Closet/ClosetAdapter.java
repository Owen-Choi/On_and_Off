package org.techtown.sns_project.Closet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.sns_project.R;

import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.MainViewHolder> {

    private List<Closet_info> ClosetList;

    public ClosetAdapter(List<Closet_info> Data) {
        this.ClosetList = Data;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_closet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Closet_info data = ClosetList.get(position);
        holder.NameTextView.setText(data.getName());
        holder.BrandTextView.setText(data.getBrand());

        //이미지 처리 해야..... grid...?
    }

    //뷰홀더 정의
    class MainViewHolder extends RecyclerView.ViewHolder {

        private TextView NameTextView;
        private TextView BrandTextView;
        private ImageView img_url;

        public MainViewHolder(View itemView) {
            super(itemView);

            NameTextView = itemView.findViewById(R.id.item_name_text);
            BrandTextView = itemView.findViewById(R.id.item_brand_text);
            img_url = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return ClosetList.size();
    }

}