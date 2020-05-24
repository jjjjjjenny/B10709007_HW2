package com.example.b10709007_hw2;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{//implement是用來處理summary
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {//起出設定時
        addPreferencesFromResource(R.xml.pref_visualizer);

        PreferenceScreen prefScreen = getPreferenceScreen();//幾個
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();//取得設定

        int count = prefScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {//設定summary
            Preference p = prefScreen.getPreference(i);
            if ((p instanceof ListPreference)) {//只設定list 一般設定的時候會跳過check因為他們本來就有

                String value = sharedPreferences.getString(p.getKey(), "");//取得 如果沒有就預設''
                setPreferenceSummary(p, value);//設定summary
            }
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {//觸發時 目的是跟著使用者的選擇summary會更動
        Preference preference = findPreference(key);
        if (null != preference) {
            if (preference instanceof ListPreference) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    private void setPreferenceSummary(Preference p, String value) {
        if (p instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) p;
            int prefIndex = listPreference.findIndexOfValue(value);//找設定的是哪個index

            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);//然後取得值
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
