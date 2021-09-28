package io.praveen.typenote;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.RelativeLayout;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import io.praveen.typenote.SQLite.BinDatabaseHandler;
import io.praveen.typenote.SQLite.ClickListener;
import io.praveen.typenote.SQLite.DatabaseHandler;
import io.praveen.typenote.SQLite.Note;
import io.praveen.typenote.SQLite.NoteAdapter;
import io.praveen.typenote.SQLite.RecyclerTouchListener;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BinActivity extends AppCompatActivity {
    private Intent i;
    private InterstitialAd interstitialAd;
    private List<Note> l;
    private int premium = 0;

    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    @TargetApi(26)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_bin);
        this.premium = PreferenceManager.getDefaultSharedPreferences(this).getInt("premium", 0);
        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setAdUnitId("ca-app-pub-8429477298745270/2004640333");
        this.interstitialAd.loadAd(new AdRequest.Builder().build());
        this.i = new Intent(this, MainActivity.class);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Trash");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.l = new BinDatabaseHandler(this).getAllNotes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.binRecyclerView);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.binPlaceholder);
        if (this.l.isEmpty()) {
            recyclerView.setVisibility(8);
            relativeLayout.setVisibility(0);
        }
        NoteAdapter noteAdapter = new NoteAdapter(this.l);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setAdapter(noteAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), new ClickListener() {
            /* class io.praveen.typenote.BinActivity.AnonymousClass1 */

            @Override // io.praveen.typenote.SQLite.ClickListener
            public void onClick(View view, final int i) {
                Note note = (Note) BinActivity.this.l.get(i);
                final String note2 = note.getNote();
                final int star = note.getStar();
                final String date = note.getDate();
                final String title = note.getTitle();
                new MaterialStyledDialog.Builder(BinActivity.this).setIcon(Integer.valueOf((int) R.drawable.ic_settings_backup_restore)).setDescription("You may choose to restore your note or delete it permanently!").setPositiveText("DELETE").onPositive(new MaterialDialog.SingleButtonCallback() {
                    /* class io.praveen.typenote.BinActivity.AnonymousClass1.AnonymousClass3 */

                    @Override // com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        BinDatabaseHandler binDatabaseHandler = new BinDatabaseHandler(BinActivity.this);
                        binDatabaseHandler.deleteNote(binDatabaseHandler.getAllNotes().get(i));
                        BinActivity.this.i.putExtra("note", true);
                        BinActivity.this.i.putExtra("delete", true);
                        if (!BinActivity.this.interstitialAd.isLoaded() || BinActivity.this.premium == 1) {
                            BinActivity.this.startActivity(BinActivity.this.i);
                            BinActivity.this.finish();
                            return;
                        }
                        BinActivity.this.interstitialAd.show();
                        BinActivity.this.interstitialAd.setAdListener(new AdListener() {
                            /* class io.praveen.typenote.BinActivity.AnonymousClass1.AnonymousClass3.AnonymousClass1 */

                            @Override // com.google.android.gms.ads.AdListener
                            public void onAdClosed() {
                                BinActivity.this.startActivity(BinActivity.this.i);
                                BinActivity.this.finish();
                            }
                        });
                    }
                }).setNegativeText("RESTORE").onNegative(new MaterialDialog.SingleButtonCallback() {
                    /* class io.praveen.typenote.BinActivity.AnonymousClass1.AnonymousClass2 */

                    @Override // com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        BinDatabaseHandler binDatabaseHandler = new BinDatabaseHandler(BinActivity.this);
                        new DatabaseHandler(BinActivity.this).addNote(new Note(note2, date, star, title));
                        binDatabaseHandler.deleteNote(binDatabaseHandler.getAllNotes().get(i));
                        BinActivity.this.i.putExtra("note", true);
                        BinActivity.this.i.putExtra("restore", true);
                        if (!BinActivity.this.interstitialAd.isLoaded() || BinActivity.this.premium == 1) {
                            BinActivity.this.startActivity(BinActivity.this.i);
                            BinActivity.this.finish();
                            return;
                        }
                        BinActivity.this.interstitialAd.show();
                        BinActivity.this.interstitialAd.setAdListener(new AdListener() {
                            /* class io.praveen.typenote.BinActivity.AnonymousClass1.AnonymousClass2.AnonymousClass1 */

                            @Override // com.google.android.gms.ads.AdListener
                            public void onAdClosed() {
                                BinActivity.this.startActivity(BinActivity.this.i);
                                BinActivity.this.finish();
                            }
                        });
                    }
                }).setNeutralText("DISMISS").onNeutral(new MaterialDialog.SingleButtonCallback() {
                    /* class io.praveen.typenote.BinActivity.AnonymousClass1.AnonymousClass1 */

                    @Override // com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    }
                }).setHeaderColor(R.color.colorPrimary).withIconAnimation(false).withDivider(true).show();
            }
        }));
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onBackPressed() {
        startActivity(this.i);
        finish();
    }

    @Override // android.support.v7.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        startActivity(this.i);
        finish();
        return true;
    }
}
