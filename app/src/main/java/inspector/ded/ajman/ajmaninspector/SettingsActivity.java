package inspector.ded.ajman.ajmaninspector;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_url_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_username_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_password_key)));
    }

    // Registers a shared preference change listener that gets notified when preferences change
    @Override
    protected void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    // Unregisters a shared preference change listener
    @Override
    protected void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        setPreferenceSummary(preference, o);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if ( s.equals(getString(R.string.pref_url_key)) ) {
//            SunshineSyncAdapter.syncImmediately(this);
            Toast.makeText(SettingsActivity.this, sharedPreferences.getString(getString(R.string.pref_url_key), ""), Toast.LENGTH_SHORT).show();
        } else if ( s.equals(getString(R.string.pref_username_key)) ) {
            // units have changed. update lists of weather entries accordingly
//            getContentResolver().notifyChange(WeatherContract.WeatherEntry.CONTENT_URI, null);
            Toast.makeText(SettingsActivity.this, sharedPreferences.getString(getString(R.string.pref_username_key), ""), Toast.LENGTH_SHORT).show();
        } else if ( s.equals(getString(R.string.pref_password_key)) ) {
            // units have changed. update lists of weather entries accordingly
//            getContentResolver().notifyChange(WeatherContract.WeatherEntry.CONTENT_URI, null);
            Toast.makeText(SettingsActivity.this, sharedPreferences.getString(getString(R.string.pref_password_key), ""), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Set the preference summaries
        setPreferenceSummary(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        String key = preference.getKey();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
    }
}
