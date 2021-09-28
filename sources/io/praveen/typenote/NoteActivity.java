package io.praveen.typenote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import io.praveen.typenote.SQLite.DatabaseHandler;
import io.praveen.typenote.SQLite.Note;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NoteActivity extends AppCompatActivity {
    private String Title = "";
    private int imp = 0;
    private Intent intent2;
    private InterstitialAd interstitialAd;
    private int premium = 0;
    private TextInputEditText text;
    private TextInputEditText title;

    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_note);
        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setAdUnitId("ca-app-pub-8429477298745270/2004640333");
        this.interstitialAd.loadAd(new AdRequest.Builder().build());
        this.intent2 = new Intent(this, MainActivity.class);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Add Note");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        boolean z = defaultSharedPreferences.getBoolean("shortcut", true);
        this.premium = defaultSharedPreferences.getInt("premium", 0);
        if (!z) {
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService("notification");
                NotificationChannel notificationChannel = new NotificationChannel("TN_1", "Type Note Shortcuts", 3);
                if (notificationManager != null) {
                    notificationChannel.setSound(null, null);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                Intent intent = new Intent(this, NoteActivity.class);
                intent.putExtra("IS_FROM_NOTIFICATION", true);
                PendingIntent activity = PendingIntent.getActivity(this, 1, intent, 2);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                builder.setContentTitle("Tap to add a note");
                builder.setContentText("Note something productive today.");
                builder.setContentIntent(activity);
                builder.setTicker("Add Notes");
                builder.setChannelId("TN_1");
                builder.setColor(getResources().getColor(R.color.colorPrimary));
                builder.setOngoing(true);
                builder.setAutoCancel(true);
                builder.setSmallIcon(R.drawable.notification_white);
                builder.setPriority(2);
                TaskStackBuilder create = TaskStackBuilder.create(getApplicationContext());
                create.addNextIntent(intent);
                builder.setContentIntent(create.getPendingIntent(0, 134217728));
                if (notificationManager != null) {
                    notificationManager.notify(1, builder.build());
                }
            } else {
                Intent intent3 = new Intent(this, NoteActivity.class);
                intent3.putExtra("IS_FROM_NOTIFICATION", true);
                PendingIntent activity2 = PendingIntent.getActivity(this, 1, intent3, 0);
                Notification.Builder builder2 = new Notification.Builder(getApplicationContext());
                builder2.setContentTitle("Tap to add a note");
                builder2.setContentText("Note something productive today.");
                builder2.setContentIntent(activity2);
                builder2.setTicker("Add Notes");
                builder2.setOngoing(true);
                builder2.setColor(getResources().getColor(R.color.colorPrimary));
                builder2.setAutoCancel(true);
                builder2.setSmallIcon(R.drawable.notification_white);
                builder2.setPriority(2);
                Notification build = builder2.build();
                NotificationManager notificationManager2 = (NotificationManager) getSystemService("notification");
                if (notificationManager2 != null) {
                    notificationManager2.notify(1, build);
                }
            }
        }
        this.text = (TextInputEditText) findViewById(R.id.add_text);
        this.title = (TextInputEditText) findViewById(R.id.add_title);
        ((FloatingActionButton) findViewById(R.id.add_fab)).setOnClickListener(new View.OnClickListener() {
            /* class io.praveen.typenote.NoteActivity.AnonymousClass1 */

            public void onClick(@NonNull View view) {
                String obj = NoteActivity.this.text.getText().toString();
                NoteActivity.this.Title = NoteActivity.this.title.getText().toString();
                if (obj.length() > 0) {
                    new DatabaseHandler(NoteActivity.this).addNote(new Note(obj, new SimpleDateFormat("HH:mm dd/MM/yyyy").format(Calendar.getInstance().getTime()), NoteActivity.this.imp, NoteActivity.this.Title));
                    NoteActivity.this.intent2.putExtra("note", true);
                    NoteActivity.this.intent2.putExtra("new", true);
                    if (!NoteActivity.this.interstitialAd.isLoaded() || NoteActivity.this.premium == 1) {
                        NoteActivity.this.startActivity(NoteActivity.this.intent2);
                        NoteActivity.this.finish();
                        return;
                    }
                    NoteActivity.this.interstitialAd.show();
                    NoteActivity.this.interstitialAd.setAdListener(new AdListener() {
                        /* class io.praveen.typenote.NoteActivity.AnonymousClass1.AnonymousClass1 */

                        @Override // com.google.android.gms.ads.AdListener
                        public void onAdClosed() {
                            NoteActivity.this.startActivity(NoteActivity.this.intent2);
                            NoteActivity.this.finish();
                        }
                    });
                    return;
                }
                Snackbar.make(view, "Note is empty!", -1).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.add_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.menu_important) {
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
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override // android.support.v7.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return true;
    }
}
