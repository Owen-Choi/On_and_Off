package org.techtown.sns_project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.Enterprise.Setting.EnterpriseMemberInfo;
import org.techtown.sns_project.Enterprise.Setting.EnterpriseSettingActivity;
import org.techtown.sns_project.Normal.Setting.NormalMemberInfo;


public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://github.com/Owen-Choi/ToyProject_SNS"));
        Preference mypref = (Preference)findPreference("contact_preference");
        Preference memberInfo = (Preference)findPreference("memberInfo");
        Preference PWChange = (Preference)findPreference("change_pw");
        Preference withdrawal = (Preference)findPreference("withdrawal");
//        SeekBarPreference sp = findPreference("volume_notifications");
//        sp.getValue();
        mypref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(i);
                return true;
            }
        });

        // 두가지 방법이 있다. 일반 회원인지 기업 회원인지 판단한 후 화면을 다르게 띄워주거나
        // 아예 클래스를 분리해서 기업용 클래스를 하나 더 만들거나.
        memberInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                StartActivity(NormalMemberInfo.class);
                return true;
            }
        });

        PWChange.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                StartActivity(Password_Init_Activity.class);
                return true;
            }
        });

        withdrawal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("정말 탈퇴하시겠습니까?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.getCurrentUser().delete();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                                .delete();
                        // 추가적으로 db의 정보들도 삭제해야함.
//                        builder.setMessage("정상적으로 탈퇴되었습니다.");
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // 이 부분은 약간의 수정여지가 있다.
//                                System.exit(0);
//                            }
//                        });
//                        builder.show();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }


}