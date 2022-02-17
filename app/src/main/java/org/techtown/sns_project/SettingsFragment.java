package org.techtown.sns_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.firestore.core.View;

import org.techtown.sns_project.Normal.NormalMainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
          setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Preference mypref = (Preference)findPreference("contact_preference");
        mypref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getContext(), NormalMainActivity.class));
                return true;
            }
        });
    }

}