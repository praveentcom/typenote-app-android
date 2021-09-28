package io.praveen.typenote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.LinearLayout;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import io.praveen.typenote.SQLite.AboutAdapter;
import io.praveen.typenote.SQLite.ClickListener;
import io.praveen.typenote.SQLite.RecyclerTouchListener;
import java.util.ArrayList;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    private BillingProcessor bp;
    private LinearLayout sv;

    @Override // com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
    public void onBillingInitialized() {
    }

    @Override // com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
    public void onPurchaseHistoryRestored() {
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.SupportActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_about);
        this.bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApcobfuZFov1KIJgKEKrzp9PP2n1EbBpV/xf9AyhWYN47QY8/rWGPuKht/7b4DmCVnpd6PrnYJqLt/rqR5c+lifLY5XuUH1VGqnkWA33TkPXm4UkGk3q/jvVIbM5xbcdPLqNkLiEoEuBlmAYNxM6K3lf5Kz+ff1HUH1ljYjDE9M38xS0TiLnQIRPm9cfehNxaKWOF81sx5Q9K3vNB1JoNuMyaMfBFQjfMRL6llsMRF42NEf6W/4/2c5Guxvg2qLo14/gGVRLS5H0ZVwqThNZVYTtLRWWNIrgFIwMnCjcbntFkEBK/B987poGN6miDI2r1m6XALRAgLEzM/IUaPnwnWwIDAQAB", this);
        this.sv = (LinearLayout) findViewById(R.id.about_scroll);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("About");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("Project Contributors");
        arrayList.add("Tools & Licenses Used");
        arrayList.add("Donate and Support");
        arrayList.add("Rate the Application");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.aboutRecyclerView);
        AboutAdapter aboutAdapter = new AboutAdapter(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setAdapter(aboutAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), new ClickListener() {
            /* class io.praveen.typenote.AboutActivity.AnonymousClass1 */

            @Override // io.praveen.typenote.SQLite.ClickListener
            public void onClick(View view, int i) {
                if (i == 0) {
                    AboutActivity.this.startActivity(new Intent(AboutActivity.this, ContributorsActivity.class));
                } else if (i == 1) {
                    AboutActivity.this.startActivity(new Intent(AboutActivity.this, LicensesActivity.class));
                } else if (i == 2) {
                    AboutActivity.this.bp.purchase(AboutActivity.this, "typenote_donate");
                } else if (i == 3) {
                    AboutActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=io.praveen.typenote")));
                }
            }
        }));
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

    @Override // com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
    public void onProductPurchased(@NonNull String str, TransactionDetails transactionDetails) {
        Snackbar.make(this.sv, "Thanks for your donation, we'll strive to provide you the best of us!", -2).setAction("OK", new View.OnClickListener() {
            /* class io.praveen.typenote.AboutActivity.AnonymousClass2 */

            public void onClick(View view) {
            }
        }).show();
        this.bp.consumePurchase("typenote_donate");
    }

    @Override // com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
    public void onBillingError(int i, Throwable th) {
        Snackbar.make(this.sv, "There was an error in completing the Donation process!", 0).show();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (!this.bp.handleActivityResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }
}
