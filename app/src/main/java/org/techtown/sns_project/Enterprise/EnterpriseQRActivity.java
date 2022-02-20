package org.techtown.sns_project.Enterprise;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.R;
import org.techtown.sns_project.SignUpActivity;
import org.techtown.sns_project.qr.New_Parser;
import org.techtown.sns_project.qr.Parser;

import java.io.IOException;

public class EnterpriseQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_qr_generate);
        findViewById(R.id.URLButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.URLButton){
                String URL = ((EditText)findViewById(R.id.URLTextBox)).getText().toString();
                if(URL.equals(""))
                    StartToast("please fill the blank");
                else {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                        New_Parser new_parser = new New_Parser(firebaseAuth, firebaseUser, db, URL);
                }
            }
        }
    };

    private void StartToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
