package org.techtown.sns_project.Board;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.techtown.sns_project.Board.CommentsActivity;
import org.techtown.sns_project.Board.Upload.url.upload_items_adapter;
import org.techtown.sns_project.Board.profile.ProfileActivity;
import org.techtown.sns_project.Camera.Activity_codi;
import org.techtown.sns_project.Enterprise.QR.EnterpriseQRListActivity;
import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.BoardFragment;
import org.techtown.sns_project.qr.ProductInfo;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardPostClickEvent extends AppCompatActivity {


    public ImageView post_image, like, comment, save, more;
    public TextView username, likes, publisher, description, comments;
    CircleImageView image_profile;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    HashMap<String,Object> List = new HashMap<String,Object>();

    static String listImgURL2;
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    static ArrayList<String> listImgUrl = new ArrayList<>();
    ArrayList<ArrayList<ProductInfo>>listOfList = new ArrayList<>();
    static ArrayList<String> listPostid = new ArrayList<>();
    static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<Integer> listNrlikeds = new ArrayList<Integer>();
    public ArrayList<PostInfo> listData = new ArrayList<>();
    HashMap<String, Object> Map_like = new HashMap<>();
    HashMap<String, Object> Map_save = new HashMap<>();
    static boolean liked = true;
    static boolean saved = true;
    static String post_description;
    static String post_publisher, userNick_publisher;
    static String post_postid,post_document;
    static int nrlikes;


    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    upload_items_adapter UIA;
    Context mContext;
    ArrayList<ProductInfo>list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        listImgUrl = (ArrayList<String>) getIntent().getSerializableExtra("listImgUrl");
        listDescription = (ArrayList<String>) getIntent().getSerializableExtra("listDescription");
        listPublisher = (ArrayList<String>) getIntent().getSerializableExtra("listPublisher");
        listOfList = (ArrayList<ArrayList<ProductInfo>>) getIntent().getSerializableExtra("listOfList");
        listDocument = (ArrayList<String>) getIntent().getSerializableExtra("listDocument");

        int position = getIntent().getIntExtra("position", 1);
        listImgURL2 = listImgUrl.get(position);
        list = listOfList.get(position);
        post_description = listDescription.get(position);
        post_publisher = listPublisher.get(position);
        post_document = listDocument.get(position);

        // 최신화가 안된 게시글을 누르면 nullPointerException 앱이 종료된다. 디비를 한번 날려야 할 필요가 있다.
        //recycler view part
        //패키지 구성을 조금 잘못했는데, UIA는 옷장과 url로 등록된 정보를 공통적으로 관리하는 recycler view이다.
        recyclerView = findViewById(R.id.AddedItemList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        UIA = new upload_items_adapter(this, firebaseAuth, user, db);
        recyclerView.setAdapter(UIA);
        Duplicate_Removal();
        UIA.clearList();
        UIA.addItem(list);
        UIA.setOnItemClickListener(new upload_items_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, ArrayList<ProductInfo> listData) {
                // listData에서 URL 가져와서 파싱된 화면 띄워주자.
                Intent intent = new Intent(getApplicationContext(), Activity_codi.class);
                String key = listData.get(position).getURL().replaceAll("[^0-9]", "");
                intent.putExtra("key", key);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
        UIA.notifyDataSetChanged();


        post_image = findViewById(R.id.post_image);
        description = findViewById(R.id.description);
        publisher = findViewById(R.id.publisher);
        username = findViewById(R.id.username);
        like = findViewById(R.id.like);
        comment = findViewById(R.id.comment);
        save = findViewById(R.id.save);
        likes = findViewById(R.id.likes);
        comments = findViewById(R.id.comments);
        more = findViewById(R.id.more);
        image_profile = findViewById(R.id.image_profile);

        // private void setFireBaseProfileImage(String filename_GetUid) {
        intent.putExtra("post_publisher", post_publisher);
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference(); //스토리지를 참조한다
        storageRef.child("profile_images/" + post_publisher).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //성공시
                Glide.with(getApplicationContext()).load(uri).into(image_profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //실패시
                image_profile.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
        db.collection("users").document(post_publisher).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        userNick_publisher = document.getData().get("name").toString();
                        if (userNick_publisher != null)
                            publisher.setText(userNick_publisher);
                        else {
                            publisher.setText("NULL");
                        }

                        if (userNick_publisher != null)
                            username.setText(userNick_publisher);
                        else {
                            username.setText("NULL");
                        }
                    }
                });
        Glide.with(this).load(listImgURL2).error(R.drawable.ic_launcher_background).into(post_image);


        //글 설명
        if (post_description != null)
            description.setText(post_description);
        else {
            description.setText("NULL");
        }

        nrlikes = 0;

        isLiked(user.getUid(), like);
        nrLikes(likes);
        isSaved(user.getUid(), save);

        /*getCommetns(post.getPostid(), holder.comments);*/

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("post_publisher", post_publisher);
                startActivity(intent);
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("post_publisher", post_publisher);
                startActivity(intent);
            }
        });

        //Like
        CollectionReference likesRef = db.collection("board").document(post_document).collection("Likes");
        CollectionReference likesRef_user = db.collection("users").document(user.getUid()).collection("board_likes");
        //If click like button
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liked) {
                    nrlikes++;
                    nrLikes(likes);
                    likes.setText(String.valueOf(nrlikes) + "likes");
                    like.setImageResource(R.drawable.ic_liked);
                    Map_like.put("user", user.getUid());

                    likesRef.document(user.getUid())
                            .set(Map_like)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("like", "Document written success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Like", "Fail", e);
                                }
                            });
                    liked = false;

                    likesRef_user.document(post_document)
                            .set(Map_like)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("like_userlike", "Document written success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Like", "Fail", e);
                                }
                            });
                } else {
                    nrlikes--;
                    nrLikes(likes);
                    likes.setText(String.valueOf(nrlikes) + "likes");
                    like.setImageResource(R.drawable.ic_like);
                    likesRef.document(user.getUid())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Unlike", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Unlike", "Error deleting document", e);
                                }
                            });

                    likesRef_user.document(post_document)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Unlike", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Unlike", "Error deleting document", e);
                                }
                            });

                    liked = true;
                }

            }
        });

        CollectionReference savesRef = db.collection("board").document(post_document).collection("Saves");
        CollectionReference savesRef_user = db.collection("users").document(user.getUid()).collection("board_saves");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saved) {
                    save.setImageResource(R.drawable.ic_save_black);
                    Map_save.put("user", user.getUid());

                    savesRef.document(user.getUid())
                            .set(Map_save)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("save", "Document written success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("save", "Fail", e);
                                }
                            });

                    savesRef_user.document(post_document)
                            .set(Map_like)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("Save_userSave", "Document written success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Like", "Fail", e);
                                }
                            });
                    saved = false;
                } else {
                    save.setImageResource(R.drawable.ic_save);
                    savesRef.document(user.getUid())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Save cancel", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("save cancel", "Error deleting document", e);
                                }
                            });

                    savesRef_user.document(post_document)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Save cancel", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("save cancel", "Error deleting document", e);
                                }
                            });
                    saved = true;
                }

            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                intent.putExtra("post_document", post_document);
                startActivity(intent);
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                intent.putExtra("postid", user.getUid());
                intent.putExtra("post_document", post_document);
                startActivity(intent);
            }
        });


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference likesRef_user = db.collection("users").document(user.getUid()).collection("board_likes");
                CollectionReference savesRef_user = db.collection("users").document(user.getUid()).collection("board_saves");
                CollectionReference myboardRef_user = db.collection("users").document(user.getUid()).collection("Myboard");

                Log.d("click", "me success");
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.inflate(R.menu.post_menu);
                if (!post_publisher.equals(user.getUid())) {
                    Log.d("click", "me success");
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
//                            case R.id.edit:
//                                editPost(post.getPostid());
//                                return true;
                            case R.id.delete:

                                db.collection("board").document(post_document)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Board delete", "delete success");
                                        Intent intent = new Intent(getApplicationContext(), BoardFragment.class);
                                        intent.putExtra("refresh", "refresh");
                                        finish();

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Boarddelete", "delete failed");
                                            }
                                        });

                                likesRef_user.document(post_document)
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Board delete", "delete success");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Boarddelete", "delete failed");
                                            }
                                        });

                                savesRef_user.document(post_document)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                myboardRef_user.document(post_document)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            default:
                                return false;
                        }

                    }
                });

            }
        });
    }



    private void isSaved(String uid, ImageView save) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference savesRef = db.collection("board").document(post_document).collection("Saves");
        savesRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        save.setImageResource(R.drawable.ic_save_black);
                        saved = false;
                    } else {
                        save.setImageResource(R.drawable.ic_save);
                        saved = true;
                    }
                } else {
                    Log.d("isSaved", "Failed with: ", task.getException());
                }
            }
        });



    }

    private void nrLikes(TextView likes) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference likesRef = db.collection("board").document(post_document).collection("Likes");
        DocumentReference CountlikesRef = db.collection("board").document(post_document);
        DocumentReference MyboardlikesRef = db.collection("users").document(post_publisher).collection("Myboard").document(post_document);

        likesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            nrlikes = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                nrlikes++;
                            }
                        } else {
                            Log.d("nrlikes", "Error getting documents: ", task.getException());
                        }

                        System.out.println(nrlikes);
                        likes.setText(String.valueOf(nrlikes) + "likes");
                        Map<String, Object> data = new HashMap<>();
                        data.put("nrlikes", nrlikes);
                        CountlikesRef.set(data, SetOptions.merge());
                        MyboardlikesRef.set(data,SetOptions.merge());

                    }


                });


    }


    private void isLiked(String uid, ImageView like) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference likesRef = db.collection("board").document(post_document).collection("Likes");
        likesRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        like.setImageResource(R.drawable.ic_liked);
                        liked = false;
                    } else {
                        like.setImageResource(R.drawable.ic_like);
                        liked = true;
                    }
                } else {
                    Log.d("isliked", "Failed with: ", task.getException());
                }
            }
        });
    }
    // 중복제거
    private void Duplicate_Removal() {
        for(int i=0; i<list.size(); i++) {
            for(int k=i+1; k<list.size(); k++) {
                if(list.get(i).getTitle().equals(list.get(k).getTitle()))
                    list.remove(i);
            }
        }
    }
}
