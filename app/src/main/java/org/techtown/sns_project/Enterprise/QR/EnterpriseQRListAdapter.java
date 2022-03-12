package org.techtown.sns_project.Enterprise.QR;

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

    static  ArrayList<ProductInfo> listData = new ArrayList<>();
    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
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
        this.notifyDataSetChanged();
    }

     void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_ProductInfo, txt_ProductTitle,txt_ProductPrice;
        private ImageView Img_ProductImg;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ProductInfo = itemView.findViewById(R.id.txt_ProductInfo);
            txt_ProductTitle = itemView.findViewById(R.id.txt_ProductTitle);
            txt_ProductPrice = itemView.findViewById(R.id.txt_ProductPrice);
            Img_ProductImg     = itemView.findViewById(R.id.Img_ProductImg);

            itemView.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (view,position);
                        }
                    }
                }
            });
        }

        void onBind(ProductInfo data){
            String info=data.getInfo();
            String title=data.getTitle();
            String Price=data.getPrice();
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
                txt_ProductPrice.setText(Price);
            else
            {
                txt_ProductPrice.setText("null");
            }


            Glide.with(itemView.getContext()).load(data.getImgURL()).error(R.drawable.ic_launcher_background).into(Img_ProductImg);

        }
    }

}
