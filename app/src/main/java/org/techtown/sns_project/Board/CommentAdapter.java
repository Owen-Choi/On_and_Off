package org.techtown.sns_project.Board;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.sns_project.Model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Comment> mComment;
    private String postid;

    public CommentAdapter(CommentsActivity commentsActivity, List<Comment> commentList, String postid) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
