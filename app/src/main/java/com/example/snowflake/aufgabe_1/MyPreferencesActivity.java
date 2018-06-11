package com.example.snowflake.aufgabe_1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by mpwad on 17/10/2017.
 * class is a activity, it chooses the right ringtone with the preferences
 * It is used by clicking on the setting menu
 */

public class MyPreferencesActivity extends PreferenceActivity {

    private ListPreference.OnPreferenceChangeListener prefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }

    /**
     * this class is a fragment to edit the preference
     */
    public static class MyPreferenceFragment extends PreferenceFragment {
        //Schnittstellendefinition für einen Rückruf, der aufgerufen werden soll,
        // wenn eine gemeinsame Einstellung geändert wird.
        private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Preference Hinzufügen aus der XML_Datei
            addPreferencesFromResource(R.xml.preferences);

            SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                    ListPreference listPreference = (ListPreference) findPreference("selectType");
                    CharSequence currText = listPreference.getEntry();
                    Log.d("DEBUGLOG: MPA: ", "CurrText: " + currText.toString());
                    MediaAdapter.setRingtone(currText.toString());
                }
            };
            sPref.registerOnSharedPreferenceChangeListener(prefListener);
        }

    }


    public String getSelectedEntry() {
        return "";
    }


}
