package org.techtown.sns_project.Normal.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.MemberInfoClass;
import org.techtown.sns_project.Normal.NormalMemberInfoActivity;
import org.techtown.sns_project.R;

import java.util.HashMap;
import java.util.Map;

public class NormalMemberInfo extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    TextView name, address, phone, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_setting_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.MemberInfoChangeButton).setOnClickListener(onClickListener);
        DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MemberInfoClass memberInfo = documentSnapshot.toObject(MemberInfoClass.class);
                if(memberInfo != null) {
                    name = (TextView) findViewById(R.id.NameText);
                    address = (TextView) findViewById(R.id.AddressText);
                    phone = (TextView) findViewById(R.id.PhoneText);
                    date = (TextView) findViewById(R.id.DateText);

                    name.setText(memberInfo.getName());
                    address.setText(memberInfo.getAddress());
                    phone.setText(memberInfo.getPhone());
                    date.setText(memberInfo.getDate());
                }
                else
                    Log.d("temp", "onSuccess: null");
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 액티비티가 굉장히 복잡하게 얽힌다. 플래그를 활용해보자.
            // 일단 DB에서 값 지워주고 액티비티 들어가서 다시 DB에 값 넣어주자.
            // 로직상 현재 액티비티를 다시 띄우면 알아서 DB에 가서 값을 가져온다.
            // 따라서 새로 띄운 액티비티는 그냥 창을 종료하고 다시 이 창을 띄워주면 되겠다.
            db.collection("users").document(firebaseUser.getUid()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // 새로운 액티비티 띄우기
                            // 기존의 회원정보 activity를 다시 띄워도 될 것 같은데?

                            // document가 지워져도 하위 collection은 지워지지 않는다.
                            // 따라서 수동으로 지워주어야 한다.
//                            Map<String,Object> updates = new HashMap<>();
//                            updates.put("name", FieldValue.delete());
//                            updates.put("address", FieldValue.delete());
//                            updates.put("date", FieldValue.delete());
//                            updates.put("phone", FieldValue.delete());

                            StartActivity(NormalMemberInfoActivity.class);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // 실패 로직 처리하기
                    Log.d("memberInfo", "onFailure: fail");
                }
            });
        }
    };

    private void StartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
