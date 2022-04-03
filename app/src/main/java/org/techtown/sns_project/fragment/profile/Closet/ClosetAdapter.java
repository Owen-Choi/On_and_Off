package org.techtown.sns_project.fragment.profile.Closet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.sns_project.R;

import java.util.ArrayList;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ItemViewHolder> {

    static ArrayList<Closet_info> list = new ArrayList<>();

    public interface OnItemClickListener{
        void onItemClick(View v, int pos, String delItem, String clothes_type, ArrayList<Closet_info> list);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override //inflater는 xml파일에 정의된 정보를 실제 메모리에 할당하는 용도
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_closet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(list.get(position));
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    String clothes_type = list.get(pos).getClothes_type();
                    String delItem = list.get(pos).getUrl().replaceAll("[^0-9]", "");
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 매서드 호출.

                        if(mListener != null){
                            mListener.onItemClick(v, pos, delItem, clothes_type, list);
                        }
                    }
                }
            });


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
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
}

