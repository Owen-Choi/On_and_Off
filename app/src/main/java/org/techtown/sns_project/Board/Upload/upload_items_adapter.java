package org.techtown.sns_project.Board.Upload;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class upload_items_adapter extends RecyclerView.Adapter<upload_items_adapter.AddedItemViewHolder> {

    String url;
    Context context;
    public upload_items_adapter(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    public class AddedItemViewHolder extends RecyclerView.ViewHolder {
        public AddedItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public AddedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AddedItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
