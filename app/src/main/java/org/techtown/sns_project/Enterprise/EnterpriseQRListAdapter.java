package org.techtown.sns_project.Enterprise;

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

public class EnterpriseQRListAdapter extends RecyclerView.Adapter<EnterpriseQRListAdapter.ItemViewHolder> {

    private ArrayList<ProductInfo> listData = new ArrayList<>();

    @NonNull
    @Override
    public EnterpriseQRListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_enterprise_qr_list_item, viewGroup, false);
        return new
                EnterpriseQRListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnterpriseQRListAdapter.ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.onBind(listData.get(i));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    void addItem(ProductInfo data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_ProductInfo, txt_ProductTitle;
        private ImageView Img_ProductImg;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ProductInfo = itemView.findViewById(R.id.txt_ProductInfo);
            txt_ProductTitle = itemView.findViewById(R.id.txt_ProductTitle);
            Img_ProductImg     = itemView.findViewById(R.id.Img_ProductImg);

        }

        void onBind(ProductInfo data){
            String info=data.getInfo();
            String title=data.getTitle();
            String Price=data.getTitle();
            if(info!=null)
                txt_ProductInfo.setText(info);
            else
            {
                txt_ProductInfo.setText("null");
            }

            if(title!=null)
                txt_ProductTitle.setText(title);
            else
            {
                txt_ProductTitle.setText("null");
            }
            if(Price!=null)
                txt_ProductTitle.setText(Price);
            else
            {
                txt_ProductTitle.setText("null");
            }


            Glide.with(itemView.getContext()).load(data.getImgURL()).error(R.drawable.ic_launcher_background).into(Img_ProductImg);

        }
    }
}
