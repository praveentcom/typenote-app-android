package io.praveen.typenote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import io.praveen.typenote.SQLite.AboutAdapter;
import java.util.ArrayList;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LicensesActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_licenses);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Tools & Licenses");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("Android Support Libraries");
        arrayList.add("Calligraphy Custom Fonts");
        arrayList.add("Android In-App Billing v3");
        arrayList.add("Material Styled Dialogs");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.licensesRecyclerView);
        AboutAdapter aboutAdapter = new AboutAdapter(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setAdapter(aboutAdapter);
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onBackPressed() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override // android.support.v7.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, AboutActivity.class));
        finish();
        return true;
    }
}
