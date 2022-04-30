package org.techtown.sns_project.Normal.Home;

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

public class HomeFragmentLikeListAdpater extends RecyclerView.Adapter<HomeFragmentLikeListAdpater.ItemViewHolder> {

    public ArrayList<LikeBoardInfo> listData = new ArrayList<>();
    //아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }

    //리스너 객체 참조 변수
    private HomeFragmentLikeListAdpater.OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드

    public void setOnItemClickListener(HomeFragmentLikeListAdpater.OnItemClickListener listener) {
        this.mListener = listener;
    }
    @NonNull
    @Override
    public HomeFragmentLikeListAdpater.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.like_rank, viewGroup, false);
        return new
                HomeFragmentLikeListAdpater.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragmentLikeListAdpater.ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.onBind(listData.get(i));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public ArrayList<LikeBoardInfo> getItem(){ return listData;}
    public void addItemList(ArrayList<LikeBoardInfo> data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData=data;
        this.notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView Like_Image;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Like_Image = itemView.findViewById(R.id.like_image);
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

        void onBind(LikeBoardInfo data){
            Glide.with(itemView.getContext()).load(data.getImgURL()).error(R.drawable.ic_launcher_background).into(Like_Image);
        }
    }

}
