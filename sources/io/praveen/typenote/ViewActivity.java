package io.praveen.typenote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.anjlab.android.iab.v3.Constants;
import io.praveen.typenote.SQLite.BinDatabaseHandler;
import io.praveen.typenote.SQLite.DatabaseHandler;
import io.praveen.typenote.SQLite.Note;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewActivity extends AppCompatActivity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    @Nullable
    private String date;
    private int id;
    private int imp;
    @Nullable
    private String noteText;
    @Nullable
    private String noteTitle;
    private int position;
    private TextView tv;

    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    @SuppressLint({"SetTextI18n"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_view);
        if (getIntent().getExtras() != null) {
            this.noteText = getIntent().getExtras().getString("note");
            this.noteTitle = getIntent().getExtras().getString(Constants.RESPONSE_TITLE);
            this.imp = getIntent().getExtras().getInt("imp");
            this.date = getIntent().getExtras().getString("date");
            this.position = getIntent().getExtras().getInt("pos");
        }
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.tv = (TextView) findViewById(R.id.view_text);
        TextView textView = (TextView) findViewById(R.id.view_date);
        TextView textView2 = (TextView) findViewById(R.id.view_important);
        TextView textView3 = (TextView) findViewById(R.id.view_title);
        if (this.imp == 1) {
            textView2.setVisibility(0);
        }
        textView3.setText(this.noteTitle);
        if (this.noteTitle.length() == 0) {
            textView3.setText("Untitled Note");
        }
        textView.setText(this.date);
        this.id = getIntent().getExtras().getInt("id");
        this.tv.setText(this.noteText);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.copy) {
            Snackbar.make(this.tv, "Copied!", -1).show();
        } else if (menuItem.getItemId() == R.id.edit) {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("note", this.noteText);
            intent.putExtra("id", this.id);
            intent.putExtra(Constants.RESPONSE_TITLE, this.noteTitle);
            intent.putExtra("imp", this.imp);
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.share) {
            Intent intent2 = new Intent();
            intent2.setAction("android.intent.action.SEND");
            intent2.putExtra("android.intent.extra.TEXT", this.noteTitle + "\n\n" + this.noteText);
            intent2.setType("text/plain");
            startActivity(intent2);
        } else if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        } else if (menuItem.getItemId() == R.id.delete) {
            DatabaseHandler databaseHandler = new DatabaseHandler(this);
            Note note = databaseHandler.getAllNotes().get(this.position);
            databaseHandler.deleteNote(note);
            new BinDatabaseHandler(this).addNote(new Note(note.getNote(), note.getDate(), note.getStar(), note.getTitle()));
            Intent intent3 = new Intent(this, MainActivity.class);
            intent3.putExtra("delete", true);
            intent3.putExtra("note", true);
            startActivity(intent3);
            finish();
        }
        return true;
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
