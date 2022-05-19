package org.techtown.sns_project.Camera;

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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ItemViewHolder> {

    private ArrayList<PostDTO> listData = new ArrayList<>();


    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private PostAdapter.OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(PostAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.onBind(listData.get(i));
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.posted_codi, viewGroup, false);
        return new
                ItemViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }
    void addItem(PostDTO data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_userid, txt_description;
        private ImageView Img_PostingImg;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            txt_ProductBrand = itemView.findViewById(R.id.txt_ProductBrand);
//            txt_ProductTitle = itemView.findViewById(R.id.txt_ProductTitle);
//            Img_ProductImg     = itemView.findViewById(R.id.Img_ProductImg);
//            txt_ProductPrice= itemView.findViewById(R.id.txt_ProductPrice);
            txt_userid = itemView.findViewById(R.id.txt_userid);
            txt_description = itemView.findViewById(R.id.txt_nrlikes);
            Img_PostingImg = itemView.findViewById(R.id.Img_PostingImg);

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

        void onBind(PostDTO data){
            txt_userid.setText(data.getUserid());
            txt_description.setText(data.getDescription());
            Glide.with(itemView.getContext()).load(data.getImageUrl()).error(R.drawable.ic_launcher_background).into(Img_PostingImg);

        }
    }
}