package io.praveen.typenote;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anjlab.android.iab.v3.Constants;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import io.praveen.typenote.SQLite.ClickListener;
import io.praveen.typenote.SQLite.DatabaseHandler;
import io.praveen.typenote.SQLite.Note;
import io.praveen.typenote.SQLite.NoteAdapter;
import io.praveen.typenote.SQLite.RecyclerTouchListener;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int imp = 0;
    private InterstitialAd interstitialAd;
    private boolean isVisible;
    private List<Note> l;
    private NoteAdapter mAdapter;
    private MenuItem mi;
    private ScheduledExecutorService scheduler;
    private int ser = 0;

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void prepareAd() {
        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setAdUnitId("ca-app-pub-8429477298745270/2004640333");
        this.interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity
    public void onStop() {
        super.onStop();
        this.scheduler.shutdownNow();
        this.scheduler = null;
        this.isVisible = false;
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity
    public void onStart() {
        super.onStart();
        this.isVisible = true;
        prepareAd();
        int i = PreferenceManager.getDefaultSharedPreferences(this).getInt("premium", 0);
        if (this.scheduler == null) {
            this.scheduler = Executors.newSingleThreadScheduledExecutor();
            if (i != 1) {
                this.scheduler.scheduleAtFixedRate(new Runnable() {
                    /* class io.praveen.typenote.MainActivity.AnonymousClass1 */

                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            /* class io.praveen.typenote.MainActivity.AnonymousClass1.AnonymousClass1 */

                            public void run() {
                                if (!MainActivity.this.interstitialAd.isLoaded() || !MainActivity.this.isVisible) {
                                    Log.d("AD", " Interstitial Not Loaded");
                                } else {
                                    MainActivity.this.interstitialAd.show();
                                }
                                MainActivity.this.prepareAd();
                            }
                        });
                    }
                }, 5, 20, TimeUnit.SECONDS);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.SupportActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity
    @TargetApi(26)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Notes");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.fabView);
        populateData();
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            /* class io.praveen.typenote.MainActivity.AnonymousClass2 */

            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, NoteActivity.class));
                MainActivity.this.finish();
            }
        });
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("note")) {
            boolean z = getIntent().getExtras().getBoolean("new");
            boolean z2 = getIntent().getExtras().getBoolean("edit");
            boolean z3 = getIntent().getExtras().getBoolean("delete");
            boolean z4 = getIntent().getExtras().getBoolean("restore");
            if (z) {
                Snackbar.make(coordinatorLayout, "Note added successfully!", -1).show();
            }
            if (z2) {
                Snackbar.make(coordinatorLayout, "Note edited successfully!", -1).show();
            }
            if (z3) {
                Snackbar.make(coordinatorLayout, "Note deleted successfully!", -1).show();
            }
            if (z4) {
                Snackbar.make(coordinatorLayout, "Note restored successfully!", -1).show();
            }
            InterstitialAd interstitialAd2 = new InterstitialAd(this);
            interstitialAd2.setAdUnitId("ca-app-pub-6275597090094912/5536611682");
            interstitialAd2.loadAd(new AdRequest.Builder().build());
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("shortcut", true)) {
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            if (notificationManager != null) {
                notificationManager.cancelAll();
            }
        } else if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager2 = (NotificationManager) getApplicationContext().getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel("NOTES_ADD", "Notes Shortcuts", 3);
            if (notificationManager2 != null) {
                notificationChannel.setSound(null, null);
                notificationManager2.createNotificationChannel(notificationChannel);
            }
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra("IS_FROM_NOTIFICATION", true);
            PendingIntent activity = PendingIntent.getActivity(this, 1, intent, 2);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "NOTES_ADD");
            builder.setContentTitle("Tap to add a note");
            builder.setContentText("Note something productive today.");
            builder.setContentIntent(activity);
            builder.setTicker("Add Notes");
            builder.setChannelId("NOTES_ADD");
            builder.setOngoing(true);
            builder.setColor(getResources().getColor(R.color.colorPrimary));
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.notification_white);
            builder.setPriority(2);
            TaskStackBuilder create = TaskStackBuilder.create(getApplicationContext());
            create.addNextIntent(intent);
            builder.setContentIntent(create.getPendingIntent(0, 134217728));
            if (notificationManager2 != null) {
                notificationManager2.notify(1, builder.build());
            }
        } else {
            Intent intent2 = new Intent(this, NoteActivity.class);
            intent2.putExtra("IS_FROM_NOTIFICATION", true);
            PendingIntent activity2 = PendingIntent.getActivity(this, 1, intent2, 0);
            Notification.Builder builder2 = new Notification.Builder(getApplicationContext());
            builder2.setContentTitle("Tap to add a note");
            builder2.setContentText("Note something productive today.");
            builder2.setContentIntent(activity2);
            builder2.setTicker("Add Notes");
            builder2.setOngoing(true);
            builder2.setAutoCancel(true);
            builder2.setColor(getResources().getColor(R.color.colorPrimary));
            builder2.setSmallIcon(R.drawable.notification_white);
            builder2.setPriority(2);
            Notification build = builder2.build();
            NotificationManager notificationManager3 = (NotificationManager) getSystemService("notification");
            if (notificationManager3 != null) {
                notificationManager3.notify(1, build);
            }
        }
    }

    public void backup() {
        try {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyHHmmss", Locale.ENGLISH);
            String str = "BACKUP" + simpleDateFormat.format(date) + ".txt";
            File file = new File(Environment.getExternalStorageDirectory(), "Notes");
            String str2 = "Storage/Notes/" + str;
            if (!(!file.exists() ? file.mkdirs() : true)) {
                Toast.makeText(this, "Backup Failed", 0).show();
                return;
            }
            FileWriter fileWriter = new FileWriter(new File(file, str));
            for (int i = 0; i < this.l.size(); i++) {
                fileWriter.append((CharSequence) this.l.get(i).getTitle()).append((CharSequence) "\n\n").append((CharSequence) this.l.get(i).getNote()).append((CharSequence) "\n").append((CharSequence) this.l.get(i).getDate()).append((CharSequence) "\n__________\n\n");
            }
            fileWriter.flush();
            fileWriter.close();
            Toast.makeText(this, "Backup Successful!\nFind your notes at\n" + str2, 1).show();
        } catch (Exception unused) {
        }
    }

    public String backupString() {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.l.size(); i++) {
                sb.append(this.l.get(i).getTitle());
                sb.append("\n\n");
                sb.append(this.l.get(i).getNote());
                sb.append("\n");
                sb.append(this.l.get(i).getDate());
                sb.append("\n__________\n\n");
            }
            return sb.toString();
        } catch (Exception unused) {
            return "Notes Backup Error";
        }
    }

    public void populateData() {
        this.l = new DatabaseHandler(this).getAllNotes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.placeholder);
        if (this.l.isEmpty()) {
            recyclerView.setVisibility(8);
            relativeLayout.setVisibility(0);
        }
        this.mAdapter = new NoteAdapter(this.l);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setAdapter(this.mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), new ClickListener() {
            /* class io.praveen.typenote.MainActivity.AnonymousClass3 */

            @Override // io.praveen.typenote.SQLite.ClickListener
            public void onClick(View view, int i) {
                if (MainActivity.this.ser == 0) {
                    if (MainActivity.this.imp == 1) {
                        i = MainActivity.this.mAdapter.impPos(i);
                    }
                    Note note = (Note) MainActivity.this.l.get(i);
                    ClipboardManager clipboardManager = (ClipboardManager) MainActivity.this.getSystemService("clipboard");
                    ClipData newPlainText = ClipData.newPlainText("text", note.getNote());
                    if (clipboardManager != null) {
                        clipboardManager.setPrimaryClip(newPlainText);
                    }
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    intent.putExtra("note", note.getNote());
                    intent.putExtra("id", note.getID());
                    intent.putExtra("imp", note.getStar());
                    intent.putExtra("date", note.getDate());
                    intent.putExtra(Constants.RESPONSE_TITLE, note.getTitle());
                    intent.putExtra("pos", i);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        }));
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mi = menu.findItem(R.id.menu_imp);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        search(searchView);
        ((EditText) searchView.findViewById(R.id.search_src_text)).setHint(getString(R.string.search));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_imp) {
            if (this.imp == 0) {
                this.imp = 1;
                this.mAdapter.getFilter().filter("#IMP");
                menuItem.setIcon(R.drawable.ic_bookmark_white_24dp);
            } else {
                this.imp = 0;
                this.mAdapter.getFilter().filter("#ALL");
                menuItem.setIcon(R.drawable.ic_bookmark_border_white_24dp);
            }
        }
        return true;
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override // android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        } else if (menuItem.getItemId() == R.id.nav_notes) {
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
            return true;
        } else if (menuItem.getItemId() == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            finish();
        } else if (menuItem.getItemId() == R.id.nav_premium) {
            startActivity(new Intent(this, ProActivity.class));
            finish();
        } else if (menuItem.getItemId() == R.id.nav_backup) {
            if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 11);
            } else {
                backupStart();
            }
        } else if (menuItem.getItemId() == R.id.nav_bin) {
            startActivity(new Intent(this, BinActivity.class));
            finish();
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        return true;
    }

    @Override // android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback, android.support.v4.app.FragmentActivity
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 11) {
            if (iArr[0] != 0) {
                Toast.makeText(this, "Grant the required permissions to backup your notes.", 1).show();
            } else {
                backupStart();
            }
        }
    }

    public void backupStart() {
        if (!this.l.isEmpty()) {
            new MaterialStyledDialog.Builder(this).setIcon(Integer.valueOf((int) R.drawable.ic_unarchive)).setDescription("You can backup your notes via your phone memory or sending them by email!").setPositiveText("EMAIL").setHeaderColor(R.color.colorPrimary).setTitle("Where to backup?").withIconAnimation(false).withDivider(true).onNegative(new MaterialDialog.SingleButtonCallback() {
                /* class io.praveen.typenote.MainActivity.AnonymousClass5 */

                @Override // com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    MainActivity.this.backup();
                }
            }).setNegativeText("PHONE MEMORY").onPositive(new MaterialDialog.SingleButtonCallback() {
                /* class io.praveen.typenote.MainActivity.AnonymousClass4 */

                @Override // com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    String backupString = MainActivity.this.backupString();
                    Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:"));
                    intent.putExtra("android.intent.extra.SUBJECT", "Notes Backup");
                    intent.putExtra("android.intent.extra.TEXT", backupString);
                    MainActivity.this.startActivity(intent);
                }
            }).show();
        } else {
            Toast.makeText(this, "Notes are empty!", 0).show();
        }
    }

    private void search(@NonNull SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /* class io.praveen.typenote.MainActivity.AnonymousClass6 */

            @Override // android.support.v7.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            @Override // android.support.v7.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                MainActivity.this.mAdapter.getFilter().filter(str);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            /* class io.praveen.typenote.MainActivity.AnonymousClass7 */

            public void onFocusChange(View view, boolean z) {
                MainActivity.this.mAdapter.getFilter().filter("");
            }
        });
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            /* class io.praveen.typenote.MainActivity.AnonymousClass8 */

            public void onViewDetachedFromWindow(View view) {
                MainActivity.this.ser = 0;
                MainActivity.this.mi.setVisible(true);
                MainActivity.this.mi.setIcon(R.drawable.ic_bookmark_border_white_24dp);
                MainActivity.this.imp = 0;
            }

            public void onViewAttachedToWindow(View view) {
                MainActivity.this.ser = 1;
                MainActivity.this.mi.setVisible(false);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            /* class io.praveen.typenote.MainActivity.AnonymousClass9 */

            @Override // android.support.v7.widget.SearchView.OnCloseListener
            public boolean onClose() {
                MainActivity.this.mAdapter.getFilter().filter("");
                return true;
            }
        });
    }
}
