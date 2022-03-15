package org.techtown.sns_project.Board.Upload;

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

import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;

public class upload_items_adapter extends RecyclerView.Adapter<upload_items_adapter.AddedItemViewHolder> {

    static ArrayList<ProductInfo> listData = new ArrayList<>();
    Context context;
    public upload_items_adapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    void addItem(ProductInfo data) {
        // 동혁코드에서 따옴. 외부에서 아이템 추가해주는 코드.
        listData.add(data);
        this.notifyDataSetChanged();
    }

    public class AddedItemViewHolder extends RecyclerView.ViewHolder {

        TextView title, category, brand;
        ImageView image;

        public AddedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            category = itemView.findViewById(R.id.itemCategory);
            brand = itemView.findViewById(R.id.itemBrand);
            image = itemView.findViewById(R.id.itemImage);
        }
        void OnBind(ProductInfo data) {
            String titleTXT = data.getInfo();
            // 일단 임시로 price를 넣겠다.
            String categoryTXT = data.getPrice();
            String brandTXT = data.getTitle();

            title.setText(titleTXT);
            category.setText(categoryTXT);
            brand.setText(brandTXT);
            Glide.with(itemView.getContext()).load(data.getImgURL()).error(R.drawable.ic_launcher_background).into(image);
        }
    }

    @NonNull
    @Override
    public AddedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_board_added_list, parent, false);
        return new AddedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedItemViewHolder holder, int position) {
        holder.OnBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}
