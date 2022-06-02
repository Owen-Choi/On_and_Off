package org.techtown.sns_project.Normal.Setting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.techtown.sns_project.CommonSignInActivity;
import org.techtown.sns_project.Normal.Setting.NormalMemberInfo;
import org.techtown.sns_project.Password_Init_Activity;
import org.techtown.sns_project.R;
import org.techtown.sns_project.fragment.profile.ProfileFragment;


public class SettingsFragment extends PreferenceFragmentCompat {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public SettingsFragment() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Intent git = new Intent(Intent.ACTION_VIEW);
        git.setData(Uri.parse("https://github.com/Owen-Choi/ToyProject_SNS"));
        Intent notion = new Intent(Intent.ACTION_VIEW);
        notion.setData(Uri.parse("https://www.notion.so/896c29e9803945ef839d00831843d615"));
        Preference mypref = (Preference)findPreference("contact_preference");
        Preference notice = (Preference)findPreference("notice");
        Preference memberInfo = (Preference)findPreference("memberInfo");
        Preference PWChange = (Preference)findPreference("change_pw");
        Preference withdrawal = (Preference)findPreference("withdrawal");
        Preference SignOut = (Preference)findPreference("sign_out");
//        SeekBarPreference sp = findPreference("volume_notifications");
//        sp.getValue();
        mypref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(git);
                return true;
            }
        });

        notice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(notion);
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
                        //파베 storage에서 프사 정보 삭제 - 인범 추가
                        ProfileFragment.delProfile(firebaseAuth.getCurrentUser().getUid());
                        StartActivity(CommonSignInActivity.class);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });

        SignOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                firebaseAuth.signOut();
                StartActivity(CommonSignInActivity.class);
                return true;
            }
        });
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}