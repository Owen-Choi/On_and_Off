package org.techtown.sns_project.Board.Upload.url;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.fragment.profile.Closet.Closet_Parser;
import org.techtown.sns_project.R;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;

public class upload_items_adapter extends RecyclerView.Adapter<upload_items_adapter.AddedItemViewHolder> {

    static ArrayList<ProductInfo> listData = new ArrayList<>();
    Context context;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    public upload_items_adapter(Context context, FirebaseAuth firebaseAuth, FirebaseUser firebaseUser,
                                FirebaseFirestore db) {
        this.context = context;
        this.firebaseAuth = firebaseAuth;
        this.firebaseUser = firebaseUser;
        this.db = db;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position, ArrayList<ProductInfo> listData);
    }

    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(ArrayList<ProductInfo> list) {
        listData.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clearList() {
        listData.clear();
    }


    public class AddedItemViewHolder extends RecyclerView.ViewHolder {

        TextView title, category, brand;
        ImageView image;

        public AddedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            category = itemView.findViewById(R.id.itemCategory);
            brand = itemView.findViewById(R.id.itemBrand);
            image = itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String str[] = {"옷장에 추가하기", "상세정보 보기"};

                    AlertDialog.Builder builder  = new AlertDialog.Builder(context);
                    builder.setTitle("선택");
                    builder.setItems(str, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i == 0) {
                                int position = getAdapterPosition();
                                String url = listData.get(position).getURL();
                                Closet_Parser closet_Parser = new Closet_Parser(firebaseAuth, firebaseUser, db, url);
                                Log.e("woong", "onClick: 옷장에 추가");
                            }
                            else if(i == 1) {
                                int position = getAdapterPosition ();
                                if (position!=RecyclerView.NO_POSITION){
                                    if (mListener!=null){
                                        mListener.onItemClick (view,position, listData);
                                    }
                                }
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

        }
        void OnBind(ProductInfo data) {
            String titleTXT = data.getInfo();
            // 일단 임시로 price를 넣겠다.
            String categoryTXT = data.getPrice();
            String brandTXT = data.getTitle();
            title.setText(titleTXT);
            category.setText(categoryTXT);
            brand.setText(brandTXT);
            Glide.with(itemView.getContext()).load(data.getImgURL()).error(R.drawable.ic_launcher_background).into(image);
        }
    }

    @NonNull
    @Override
    public AddedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_board_added_list, parent, false);
        return new AddedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedItemViewHolder holder, int position) {
        holder.OnBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}
