package org.techtown.sns_project.Board;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.sns_project.Model.Comment;
import org.techtown.sns_project.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Comment> mComment;
    private String postid;

    public CommentAdapter(Context context, List<Comment> comments, String postid){
        mContext = context;
        mComment = comments;
        this.postid = postid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile;
        public TextView username, comment;

        public ItemViewHolder(View itemView) {

            super(itemView);
            image_profile = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.description);
            comment = itemView.findViewById(R.id.comment);


            }

        }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
