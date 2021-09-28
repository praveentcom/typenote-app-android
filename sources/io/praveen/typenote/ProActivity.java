package io.praveen.typenote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    private BillingProcessor bp;
    private SharedPreferences preferences;

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.SupportActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity
    @SuppressLint({"SetTextI18n"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_pro);
        this.bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApcobfuZFov1KIJgKEKrzp9PP2n1EbBpV/xf9AyhWYN47QY8/rWGPuKht/7b4DmCVnpd6PrnYJqLt/rqR5c+lifLY5XuUH1VGqnkWA33TkPXm4UkGk3q/jvVIbM5xbcdPLqNkLiEoEuBlmAYNxM6K3lf5Kz+ff1HUH1ljYjDE9M38xS0TiLnQIRPm9cfehNxaKWOF81sx5Q9K3vNB1JoNuMyaMfBFQjfMRL6llsMRF42NEf6W/4/2c5Guxvg2qLo14/gGVRLS5H0ZVwqThNZVYTtLRWWNIrgFIwMnCjcbntFkEBK/B987poGN6miDI2r1m6XALRAgLEzM/IUaPnwnWwIDAQAB", this);
        this.bp.loadOwnedPurchasesFromGoogle();
        TextView textView = (TextView) findViewById(R.id.pro_head);
        TextView textView2 = (TextView) findViewById(R.id.pro_text);
        Button button = (Button) findViewById(R.id.pro_upgrade);
        Button button2 = (Button) findViewById(R.id.pro_redeem);
        Button button3 = (Button) findViewById(R.id.pro_restore);
        TextView textView3 = (TextView) findViewById(R.id.pro_help);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (this.preferences.getInt("premium", 0) == 1) {
            textView.setText("You're Premium!");
            textView2.setText("Thanks for upgrading, you'll continue to receive premium features until your lifetime!");
            button.setVisibility(8);
            button2.setVisibility(8);
            button3.setVisibility(8);
            ((LinearLayout) findViewById(R.id.pro_ll)).setVisibility(8);
            textView3.setText("For any queries,\nDon't hesitate to contact at\nhello@praveen.io or @HelloPraveenIO");
        }
        button.setOnClickListener(new View.OnClickListener() {
            /* class io.praveen.typenote.ProActivity.AnonymousClass1 */

            public void onClick(View view) {
                ProActivity.this.bp.purchase(ProActivity.this, "notes_pro");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            /* class io.praveen.typenote.ProActivity.AnonymousClass2 */

            public void onClick(View view) {
                ProActivity.this.bp.purchase(ProActivity.this, "notes_pro");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            /* class io.praveen.typenote.ProActivity.AnonymousClass3 */

            public void onClick(View view) {
                ProActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/redeem")));
            }
        });
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/whitney.ttf").setFontAttrId(R.attr.fontPath).build());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/whitney.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Premium");
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", createFromAsset), 0, spannableStringBuilder.length(), 34);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(spannableStringBuilder);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
    public void onBillingInitialized() {
        Log.v("BILLING", "Initialized");
    }

    @Override // com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
    public void onProductPurchased(@NonNull String str, TransactionDetails transactionDetails) {
        if ("notes_pro".equals(str)) {
            this.preferences.edit().putInt("premium", 1).apply();
            startActivity(new Intent(this, ProActivity.class));
            finish();
        }
    }

    @Override // com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
    public void onPurchaseHistoryRestored() {
        Log.v("BILLING", "Restored");
    }

    @Override // com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
    public void onBillingError(int i, @Nullable Throwable th) {
        Toast.makeText(this, "Error in purchase!\nPlease try again or contact us for queries.", 1).show();
    }

    /* access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (!this.bp.handleActivityResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity
    public void onDestroy() {
        if (this.bp != null) {
            this.bp.release();
        }
        super.onDestroy();
    }
}
