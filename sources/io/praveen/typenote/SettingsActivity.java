package io.praveen.typenote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatPreferenceActivity {
    static boolean b;
    static SharedPreferences preferences;
    @NonNull
    private static Preference.OnPreferenceClickListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceClickListener() {
        /* class io.praveen.typenote.SettingsActivity.AnonymousClass1 */

        @SuppressLint({"ApplySharedPref"})
        public boolean onPreferenceClick(Preference preference) {
            if ((preference instanceof SwitchPreference) && preference.getKey().equals("notification")) {
                SettingsActivity.b = ((SwitchPreference) preference).isChecked();
                SharedPreferences.Editor edit = SettingsActivity.preferences.edit();
                edit.remove("shortcut");
                edit.apply();
                if (SettingsActivity.b) {
                    edit.putBoolean("shortcut", false);
                } else {
                    edit.putBoolean("shortcut", true);
                }
                edit.apply();
            }
            return true;
        }
    };

    /* access modifiers changed from: private */
    public static void bindPreferenceSummaryToValue(@NonNull Preference preference) {
        preference.setOnPreferenceClickListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceClick(preference);
    }

    /* access modifiers changed from: protected */
    @Override // io.praveen.typenote.AppCompatPreferenceActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Settings");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        getSupportActionBar().setTitle(spannableStringBuilder);
        getFragmentManager().beginTransaction().replace(16908290, new MainPreferenceFragment()).commit();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(R.xml.pref_notification);
            SettingsActivity.bindPreferenceSummaryToValue(findPreference("notification"));
        }
    }
}
