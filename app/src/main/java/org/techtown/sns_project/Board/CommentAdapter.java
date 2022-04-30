package org.techtown.sns_project.Board;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.techtown.sns_project.Model.Comment;
import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.DataFormat;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

    public ArrayList<Comment> listData = new ArrayList<>();
    public ArrayList<String> listComments = new ArrayList<>();
    public interface OnItemClickListener {
        void onItemClick(View v, int position); //뷰와 포지션값
    }

    private CommentAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
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
        this.notifyDataSetChanged();
    }

    void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder { //아이템 저장함, 성공

        public TextView username, comment;
        public CircleImageView image_profile;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);

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



        void onBind(Comment data) {
            String post_comment = data.getComment();
            String commentid = data.getCommentid();
            String getuid = data.getGetuid();

            FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
            //다운로드는 주소를 넣는다.
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            StorageReference storageRef = storage.getReference(); //스토리지를 참조한다
            storageRef.child("profile_images/" + getuid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //성공시
                    Glide.with(itemView.getContext()).load(uri).into(image_profile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //실패시
                    image_profile.setImageResource(R.drawable.ic_baseline_android_24);
                }
            });
            if(post_comment != null) {
                Log.e("post_comments", "success");
                comment.setText(post_comment);
            }
            if(commentid != null) {
                Log.e("commentid", "success");
                username.setText(commentid);
            }

            Log.e("onbind","success");
        }
    }


}
