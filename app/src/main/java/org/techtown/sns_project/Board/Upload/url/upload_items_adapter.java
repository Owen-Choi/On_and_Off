package org.techtown.sns_project.Board.Upload.url;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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

                    // ?????? ??????
                    String str[] = {"????????? ????????????", "???????????? ??????"};

                    Dialog dilaog01; // ????????? ???????????????
                    dilaog01 = new Dialog(context);       // Dialog ?????????
                    dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
                    dilaog01.setContentView(R.layout.custom_dialog_2);             // xml ???????????? ????????? ??????

                    TextView textView = dilaog01.findViewById(R.id.textview);
                    textView.setText("Selected Clothes");

                    dilaog01.show(); // ??????????????? ?????????
                    dilaog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // ?????? ??????
                    /* ??? ?????? ?????? ????????? ???????????? ????????? ???????????? ??????. */

                    // ?????? ?????? ????????? ?????? ????????????~
                    // '?????? ????????? ??????'?????? ???????????? ???????????? ???????????? ???????????? ????????????,
                    // '?????? ??? ??????'?????? ?????? ???????????? ??????????????? ???????????? ??????.
                    // *????????? ???: findViewById()??? ??? ?????? -> ?????? ????????? ??????????????? ????????? ????????? ??????.

                    // ?????? ??????
                    Button noBtn = dilaog01.findViewById(R.id.leftBtn);
                    noBtn.setText("????????? ??????");
                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // ????????? ?????? ??????
                            int position = getAdapterPosition();
                            String url = listData.get(position).getURL();
                            Closet_Parser closet_Parser = new Closet_Parser(firebaseAuth, firebaseUser, db, url);
                            Log.e("woong", "onClick: ????????? ??????");
                            dilaog01.dismiss(); // ??????????????? ??????
                        }
                    });
                    // ????????? ??????
                    Button yesBtn = dilaog01.findViewById(R.id.rightBtn);
                    yesBtn.setText("?????? ??????");
                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // ????????? ?????? ??????
                            int position = getAdapterPosition ();
                            if (position!=RecyclerView.NO_POSITION){
                                if (mListener!=null){
                                    mListener.onItemClick (view,position, listData);
                                }
                            }
                            dilaog01.dismiss(); // ??????????????? ??????
                        }
                    });

/*
                    String str[] = {"????????? ????????????", "???????????? ??????"};

                    AlertDialog.Builder builder  = new AlertDialog.Builder(context);
                    builder.setTitle("??????");
                    builder.setItems(str, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i == 0) {
                                int position = getAdapterPosition();
                                String url = listData.get(position).getURL();
                                Closet_Parser closet_Parser = new Closet_Parser(firebaseAuth, firebaseUser, db, url);
                                Log.e("woong", "onClick: ????????? ??????");
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
                    */

                }
            });

        }
        void OnBind(ProductInfo data) {
            String titleTXT = data.getInfo();
            // ?????? ????????? price??? ?????????.
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
