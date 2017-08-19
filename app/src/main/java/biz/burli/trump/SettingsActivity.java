package biz.burli.trump;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Matteo on 19.08.2017.
 */

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        Preference second = findPreference("second");
        Preference mute = findPreference("mute");
        Preference darkmode = findPreference("darkmode");
        second.setOnPreferenceChangeListener(this);
        darkmode.setOnPreferenceChangeListener(this);
        mute.setOnPreferenceChangeListener(this);

// onPreferenceChange sofort aufrufen mit der in SharedPreferences gespeicherten Aktienliste
        SharedPreferences sharedPrefs = getSharedPreferences("MyPref", 0);
        String secondSaved = sharedPrefs.getString(second.getKey(), "");
        onPreferenceChange(second, secondSaved);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        preference.setSummary(value.toString());
        return false;
    }
}
