package org.techtown.sns_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;


public class SettingsFragmentForEnterprise extends PreferenceFragmentCompat {

    public SettingsFragmentForEnterprise() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://github.com/Owen-Choi/ToyProject_SNS"));
        Preference mypref = (Preference)findPreference("contact_preference");
        Preference memberInfo = (Preference)findPreference("memberInfo");
//        SeekBarPreference sp = findPreference("volume_notifications");
//        sp.getValue();
        mypref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(i);
                return true;
            }
        });

        memberInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LayoutInflater inflater = getLayoutInflater();
                View v1 = inflater.inflate(R.layout.member_setting_fragment, null);
                return true;
            }
        });
    }




}