package org.techtown.sns_project.Board.Upload.closet;

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

import org.techtown.sns_project.fragment.profile.Closet.Closet_info;
import org.techtown.sns_project.R;

import java.util.ArrayList;

public class closet_outer_adapter extends RecyclerView.Adapter<closet_outer_adapter.ClosetOuterItemViewHolder> {
    Context context;
    static ArrayList<Closet_info> listData = new ArrayList<>();
    public closet_outer_adapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position, ArrayList<Closet_info> listData);
    }
    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void ItemChange(Closet_info ci) {
        listData.add(ci);
        this.notifyDataSetChanged();
    }

    public void ClearList() {
        listData.clear();
    }

    @NonNull
    @Override
    public ClosetOuterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_board_added_closet_list, parent, false);
        return new ClosetOuterItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClosetOuterItemViewHolder holder, int position) {
        holder.OnBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ClosetOuterItemViewHolder extends RecyclerView.ViewHolder {
        TextView Brand, Name;
        ImageView image;

        public ClosetOuterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.ClosetitemTitle);
            Brand = itemView.findViewById(R.id.ClosetItemCategory);
            image = itemView.findViewById(R.id.ClosetItemImage);

            itemView.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (view,position, listData);
                        }
                    }
                }
            });

        }
        void OnBind(Closet_info data) {
            String NameTXT = data.getName();
            // 일단 임시로 price를 넣겠다.
            String BrandTXT = data.getBrand();
            Brand.setText(BrandTXT);
            Name.setText(NameTXT);
            Glide.with(itemView.getContext()).load(data.getImg_url()).error(R.drawable.ic_launcher_background).into(image);
        }
    }
}
