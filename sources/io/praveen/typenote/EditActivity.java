package io.praveen.typenote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.anjlab.android.iab.v3.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import io.praveen.typenote.SQLite.DatabaseHandler;
import io.praveen.typenote.SQLite.Note;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditActivity extends AppCompatActivity {
    private String Title = "";
    private int imp = 0;
    private Intent intent;
    private InterstitialAd interstitialAd;
    private int premium = 0;
    private TextInputEditText text;
    private TextInputEditText title;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_main, menu);
        if (getIntent().getExtras() != null) {
            this.imp = getIntent().getExtras().getInt("imp");
        }
        if (this.imp == 1) {
            menu.findItem(R.id.menu_important).setIcon(R.drawable.ic_bookmark_white_24dp);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit);
        int i = 0;
        this.premium = PreferenceManager.getDefaultSharedPreferences(this).getInt("premium", 0);
        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setAdUnitId("ca-app-pub-8429477298745270/2004640333");
        this.interstitialAd.loadAd(new AdRequest.Builder().build());
        this.intent = new Intent(this, MainActivity.class);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Edit Note");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String str = "";
        String str2 = "";
        if (getIntent().getExtras() != null) {
            str = getIntent().getExtras().getString("note");
            str2 = getIntent().getExtras().getString(Constants.RESPONSE_TITLE);
        }
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.edit_fab);
        this.text = (TextInputEditText) findViewById(R.id.edit_text);
        this.title = (TextInputEditText) findViewById(R.id.edit_title);
        this.text.setText(str);
        this.title.setText(str2);
        TextInputEditText textInputEditText = this.text;
        if (str != null) {
            i = str.length();
        }
        textInputEditText.setSelection(i);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            /* class io.praveen.typenote.EditActivity.AnonymousClass1 */

            public void onClick(@NonNull View view) {
                String obj = EditActivity.this.text.getText().toString();
                EditActivity.this.Title = EditActivity.this.title.getText().toString();
                if (obj.length() > 0) {
                    String format = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime());
                    int i = 0;
                    if (EditActivity.this.getIntent().getExtras() != null) {
                        i = EditActivity.this.getIntent().getExtras().getInt("id");
                    }
                    new DatabaseHandler(EditActivity.this).updateNote(new Note(i, obj, format, EditActivity.this.imp, EditActivity.this.Title));
                    EditActivity.this.intent.setFlags(268468224);
                    EditActivity.this.intent.putExtra("edit", true);
                    EditActivity.this.intent.putExtra("note", true);
                    if (!EditActivity.this.interstitialAd.isLoaded() || EditActivity.this.premium == 1) {
                        EditActivity.this.startActivity(EditActivity.this.intent);
                        EditActivity.this.finish();
                        return;
                    }
                    EditActivity.this.interstitialAd.show();
                    EditActivity.this.interstitialAd.setAdListener(new AdListener() {
                        /* class io.praveen.typenote.EditActivity.AnonymousClass1.AnonymousClass1 */

                        @Override // com.google.android.gms.ads.AdListener
                        public void onAdClosed() {
                            EditActivity.this.startActivity(EditActivity.this.intent);
                            EditActivity.this.finish();
                        }
                    });
                    return;
                }
                Snackbar.make(view, "Note is empty!", -1).show();
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_important) {
            if (this.imp == 0) {
                this.imp = 1;
                menuItem.setIcon(R.drawable.ic_bookmark_white_24dp);
            } else {
                this.imp = 0;
                menuItem.setIcon(R.drawable.ic_bookmark_border_white_24dp);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onBackPressed() {
        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.setFlags(268468224);
        startActivity(intent2);
        finish();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override // android.support.v7.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
