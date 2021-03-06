package org.techtown.sns_project.fragment.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;

import org.techtown.sns_project.Board.BoardPostClickEvent;
import org.techtown.sns_project.Normal.NormalMainActivity;
import org.techtown.sns_project.Normal.Setting.NormalSettingActivity;
import org.techtown.sns_project.fragment.profile.Closet.ClosetMainActivity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.DataFormat;
import org.techtown.sns_project.fragment.profile.Bookmark.bookmark;
import org.techtown.sns_project.qr.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private View view;
    private String TAG = "profile ";
    private int alarmCount = 0;
    //??????????????????
    FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
    FirebaseFirestore db;

    //recyclerview
    private RecyclerView recyclerView;
    private profileAdapter profileAdapter;

    //????????? ?????? ??????
    TextView textView;
    String userNick;

    //?????? ?????? ??????
    static CircleImageView imageView;
    private Uri photoUri;
    private Boolean isPermission = true;
    final CharSequence[] selectOption = {"???????????? ?????? ??????", "?????? ???????????? ??????"};
    private HashMap<String, Object> List;

    //?????? ????????? ?????? ??????
    public static ArrayList<String> listImgUrl = new ArrayList<>();
    static ArrayList<String> listDescription = new ArrayList<>();
    static ArrayList<String> listPublisher = new ArrayList<>();
    public static ArrayList<String> listDocument = new ArrayList<>();
    static ArrayList<ArrayList<ProductInfo>> listOfList = new ArrayList<>();
    DataFormat df;

    // ?????? ?????? ?????? ?????? - ?????? ??????
    TextView alarm_textView;

    //?????????????????? ????????????
    //https://ddangeun.tistory.com/50 -> ??????????????? ???????????? ??????
    //??? ???????????? fragment is added ????????????.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Log.e(TAG, "onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG, "onCreate()");
    }

    //??? ????????? onCreateView ??? ??????.

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.e(TAG, "onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
        scatter();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //??? ???????????? fragment is active ????????????.

    @Override
    public void onPause() {
        super.onPause();
        //Log.e(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.e(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.e(TAG, "onDestroyView()");
    }
    // onDestroyView ???????????? onCreateView ??? ???????????? ????????? ??????.

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.e(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.e(TAG, "onDetach()");
    }
    //??? ???????????? fragment is destroyed ????????????.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        //????????? ?????? main activity??? ??????
        view.findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);

        //????????? ????????? main activity??? ??????
        view.findViewById(R.id.bookmarkButton).setOnClickListener(onClickListener);

        //????????? ?????? ???????????? ??????
        view.findViewById(R.id.Temp_Setting_Button).setOnClickListener(onClickListener);

        //??????
        imageView = view.findViewById(R.id.circle_img);

        //?????? ??????
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //recyclerview
        recyclerView = view.findViewById(R.id.recycler_mypost);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        profileAdapter = new profileAdapter();
        recyclerView.setAdapter(profileAdapter);

        //?????? ????????? ?????? ?????????
        profileAdapter.setOnItemClickListener(new profileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent intent = new Intent(getContext(), BoardPostClickEvent.class);
                intent.putExtra("position", position);
                intent.putExtra("listImgUrl", listImgUrl);
                intent.putExtra("listPublisher", listPublisher);
                intent.putExtra("listDescription", listDescription);
                intent.putExtra("listOfList", listOfList);
                intent.putExtra("listDocument", listDocument);

                System.out.println("Start activity :" + listImgUrl);

                startActivity(intent);
            }
        });


        //?????? ??? ???????????? ????????? ????????? ??????
        textView = (TextView) view.findViewById(R.id.userNickname);
        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        System.out.println("userID"+firebaseUser.getUid());
                        userNick = document.getData().get("name").toString();
                        view.findViewById(R.id.ClosetButton).setOnClickListener(onClickListener);
                        textView.setText(userNick);

                        System.out.println(userNick);
                    }
                });

        //storage?????? ?????? ????????????
        setFireBaseProfileImage(firebaseUser.getUid());

        //?????? ????????? ??????
        view.findViewById(R.id.circle_img).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //?????? ????????? ?????? ??????
                tedPermission();



                android.app.AlertDialog.Builder ad = new android.app.AlertDialog.Builder(view.getContext(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                ad.setTitle("????????? ?????? ??????")
                        .setIcon(R.drawable.ic_noun_selecting_1833829)
                        .setItems(selectOption, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //????????? ??????
                                        if (isPermission) goToAlbum();
                                        break;
                                    case 1:
                                        imageView.setImageResource(R.drawable.ic_baseline_android_24);
                                        //storage??? ?????? ?????? ??????
                                        delProfile(firebaseUser.getUid());
                                        break;
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });

        return view;
    }

    public void scatter(){
        //???????????? ?????? ???????????? ????????? ???????????? ????????????

        profileAdapter.clearList();
        profileAdapter.notifyDataSetChanged();
        db.collection("board").get().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //????????? ?????? ??????
                        listImgUrl.clear();
                        listDescription.clear();
                        listPublisher.clear();
                        listOfList.clear();
                        listDocument.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            List = (HashMap<String, Object>) document.getData();
                            String ismypost = (String) List.get("publisher");

                            if (ismypost.equals(firebaseUser.getUid())) {

                                df = document.toObject(DataFormat.class);
                                listImgUrl.add(df.getImageUrl());
                                listPublisher.add(df.getPublisher());
                                listDescription.add(df.getDescription());
                                listDocument.add(document.getId());
                                listOfList.add(df.getList());

                                MyProfile_info data = new MyProfile_info(
                                        (String) List.get("publisher"),
                                        (String) List.get("imageUrl"),
                                        (String) List.get("description"));
                                profileAdapter.addItem(data);
                            }
                        }
                        profileAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    /**
     * ????????? ???????????? crop??????
     */
    private void goToAlbum() {

        CropImage.activity()
                .start(getContext(), this);
    }

    //??????????????? ????????? ?????? ??????
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                photoUri = result.getUri();
                uploadFile();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    //???????????? uri??? ????????? ??????
    private void uploadFile() {

        if (photoUri != null) {
            //????????? ?????? Dialog ?????????
            final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setTitle("????????? ????????? ???...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //user id
            String filename_GetUid = firebaseUser.getUid();

            //storage ????????? ?????? ???????????? ????????? ??????.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://toyproject-sns.appspot.com/").child("profile_images/" + filename_GetUid);
            //???????????????...
            storageRef.putFile(photoUri)
                    //?????????
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //????????? ??????
                            setFireBaseProfileImage(firebaseUser.getUid());
                            progressDialog.dismiss(); //????????? ?????? Dialog ?????? ??????
                            Toast.makeText(view.getContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //?????????
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //?????????
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //?????? ?????? ?????? ???????????? ????????? ????????????. ??? ??????????
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog??? ???????????? ???????????? ????????? ??????
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(view.getContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
    }


    //storage?????? ????????? ?????? ????????? ???????????? img view??? ?????????
    private void setFireBaseProfileImage(String filename_GetUid) {

        FirebaseStorage storage = FirebaseStorage.getInstance(); //???????????? ??????????????? ?????????,
        //??????????????? ????????? ?????????.
        StorageReference storageRef = storage.getReference(); //??????????????? ????????????
        storageRef.child("profile_images/" + filename_GetUid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //?????????
                Glide.with(view.getContext()).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //?????????
                imageView.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
    }

    //storage ?????? ?????? ??????
    public static void delProfile(String filename_GetUid) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageFef = storage.getReference();

        StorageReference desertRef = storageFef.child("profile_images/" + filename_GetUid);
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                imageView.setImageResource(R.drawable.ic_baseline_android_24);
            }
        });
    }

    /**
     * ????????? ?????? ??????
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // ?????? ?????? ??????
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // ?????? ?????? ??????
                isPermission = false;
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    /*
     * ??????, ?????????, ?????? ?????? ?????????
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ClosetButton:
                    StartActivity(ClosetMainActivity.class);
                    break;

                case R.id.bookmarkButton:
                    StartActivity(bookmark.class);
                    break;

                case R.id.Temp_Setting_Button:
                    StartActivity(NormalSettingActivity.class);
                    break;
            }
        }
    };

    private void StartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }
}
