package org.techtown.sns_project.fragment.profile;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.techtown.sns_project.R;

import java.util.ArrayList;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ItemViewHolder> {

    static ArrayList<MyProfile_info> data_list = new ArrayList<>();

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    private profileAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(profileAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override //inflater는 xml파일에 정의된 정보를 실제 메모리에 할당하는 용도
    public profileAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new profileAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull profileAdapter.ItemViewHolder holder, int position) {
        holder.onBind(data_list.get(position));
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView mypost_imgview;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mypost_imgview = itemView.findViewById(R.id.mypost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (v,position);
                        }
                    }
                    }
                }
            );
        }

        void onBind(MyProfile_info data) {
            Glide.with(itemView.getContext()).load(data.getImgURL()).error(R.drawable.ic_launcher_background).into(mypost_imgview);

        }
    }

    void addItem(MyProfile_info data) {
        data_list.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    void removeItem(int position) {
        data_list.remove(position);
        notifyItemRemoved(position);
    }

    public void clearList() {
        data_list.clear();

    }
}