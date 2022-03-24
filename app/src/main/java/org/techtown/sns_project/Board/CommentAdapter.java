package org.techtown.sns_project.Board;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.sns_project.Model.Comment;
import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

    public ArrayList<Comment> listData = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position); //뷰와 포지션값
    }

    private CommentAdapter.OnItemClickListener mListener = null;
    public CommentAdapter() {

    }

    @NonNull
    @Override
    public CommentAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //아이템 뷰를 위한 객체 성공
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.comment_item, parent, false) ;
        return new
                CommentAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.onBind(listData.get(position));
        Log.e("OnbindViewholder","success");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(Comment data)
    {
        listData.add(data);
    }

    void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder { //아이템 저장함, 성공

        public ImageView image_profile;
        public TextView username, comment;

        public ItemViewHolder(View itemView) {

            super(itemView);
            image_profile = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.description);
            comment = itemView.findViewById(R.id.comment);


        }


        void onBind(Comment data) {
            String post_comment = data.getComment();

            if(post_comment != null) {
                Log.e("post_comments", "success");
                comment.setText(post_comment);
            }
            Log.e("onbind","success");
        }
    }


}
