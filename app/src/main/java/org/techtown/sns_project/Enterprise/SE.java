package org.techtown.sns_project.Enterprise;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.techtown.sns_project.R;

public class SE extends PreferenceFragmentCompat {

    Preference keyword = null;
    public SE() {

    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.layout.member_setting_activity, rootKey);
        if(rootKey == null){
            keyword = findPreference("editTextPersonName");
        }
    }


}

