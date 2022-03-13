package org.techtown.sns_project.Board;
//BoardFragment의 adapter

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.R;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ItemViewHolder> {

    public ArrayList<PostInfo> listData = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position); //뷰와 포지션값
    }

    private OnItemClickListener mListener = null;

    //viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성., layout만 받아옴
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public BoardAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_fragment_item, parent, false);
        return new
                BoardAdapter.ItemViewHolder(view);
    }

    //생성된 viewholder에 데이터를 바인딩해준다
    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(PostInfo data)
    {
        listData.add(data);
    }

    void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    //layout에 있는 item들의 정보를 받아옴
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView post_image;
        private TextView post_description;

        public ItemViewHolder(View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.post_image);
            post_description = itemView.findViewById(R.id.description);

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


        void onBind(PostInfo data) {
           /* String post_descrpition = data.getDescrpition();

            if (post_description != null)
                post_description.setText(post_descrpition);
            else
            {
                post_description.setText("NULL");
            }*/



            Glide.with(itemView.getContext()).load(data.getImgURL()).error(R.drawable.ic_launcher_background).into(post_image);


        }

    }
}