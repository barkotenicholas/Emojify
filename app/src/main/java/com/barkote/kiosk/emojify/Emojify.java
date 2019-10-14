package com.barkote.kiosk.emojify;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barkote.kiosk.emojify.retrofit.Interface.NetworkCalls;
import com.barkote.kiosk.emojify.retrofit.instance.Emo;
import com.barkote.kiosk.emojify.retrofit.model.Emoji;
import com.barkote.kiosk.emojify.retrofit.model.Result;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.jetbrains.annotations.NotNull;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.barkote.kiosk.emojify.R.id.button;

public class Emojify extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    public static NetworkCalls networkCalls;
    public Call<Result> result;
    public View view;
    public EmojIconActions emojIcon;
    public EmojiconTextView emojiconEditText;
    public TextView emojiconEditText1;
    public CharSequence aaaa;
    public String aa;
    public Result r;
    public Result m;
    public String a;
    public ProgressBar progressBar;
    public ImageView imageView;
    public ProgressDialog progressDialog;
    private ImageView image;
    private EmojiconEditText editText;
    private AdView mAdView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_emojify);

        view = findViewById(R.id.rootview);
        image = findViewById(button);
        editText = findViewById(R.id.editText);
        networkCalls = Emo.getApiClient();
        r = new Result("ex", "ex");

       /* mAdView =  findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);*/
        setUpProgressDialog();
        emojIcon = new EmojIconActions(this, view, editText, image);
        View.OnTouchListener otl = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true; // the listener has consumed the event
            }
        };
        new TapTargetSequence(Emojify.this)
                .targets(


                        TapTarget.forView(findViewById(button), "Click", "click here to choose your emoji").cancelable(true)

                ).listener(new TapTargetSequence.Listener() {
            // This listener will tell us when interesting(tm) events happen in regards
            // to the sequence
            @Override
            public void onSequenceFinish() {

            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                // Boo
            }
        }).start();


        editText.setOnTouchListener(otl);
        editText.setEmojiconSize(80);
        emojIcon.setIconsIds(R.drawable.icon, R.drawable.icon);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                image.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onKeyboardClose() {
                image.setVisibility(View.VISIBLE);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.length() == 0) {
                    Toast.makeText(Emojify.this, "Please enter an emoji", Toast.LENGTH_SHORT).show();

                } else {
                    if (isNetworkAvailable()) {
                        progressDialog.show();
                        emojify(s);

                    } else {
                        close();


                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(Emojify.this);

        // Set horizontal progress bar style.
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Set progress dialog icon.
        progressDialog.setIcon(R.drawable.icon);
        // Set progress dialog title.
        progressDialog.setTitle("Waiting...");
        // The maxima progress value.
        // Whether progress dialog can be canceled or not.
        progressDialog.setCancelable(false);
        // When user touch area outside progress dialog whether the progress dialog will be canceled or not.
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void close() {
        editText.getText().clear();
        emojIcon.closeEmojIcon();
        final AlertDialog alertDialog = new AlertDialog.Builder(Emojify.this).create();
        alertDialog.setTitle("Info");
        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
        alertDialog.setIcon(R.drawable.frown);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void emojify(CharSequence s) {
        aaaa = s;
        aa = " " + s;
        r.setA(aa);

        final Emoji a = new Emoji(aa);

        result = Emojify.networkCalls.sendMessage(a);


        result.enqueue(new Callback<Result>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
                if (response.isSuccessful()) {
                    r = response.body();

                    assert r != null;

                    show(r);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result> call, @NotNull Throwable t) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void show(Result r) {


        final Dialog dialog = new Dialog(Emojify.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog);

        assert r != null;
        m = r;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        emojiconEditText = dialog.findViewById(R.id.emo);
        emojiconEditText1 = dialog.findViewById(R.id.mean);
        imageView = dialog.findViewById(R.id.img_close);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                editText.getText().clear();
                loadInterstitialAd();

            }
        });


        String ans = r.getAns();
        if (ans.equals("")) {

            Toast.makeText(Emojify.this, "please emter an emoji", Toast.LENGTH_SHORT).show();


        } else {
            String withoutFirstCharacter = ans.substring(2);
            String withoutLastCharacter = withoutFirstCharacter.substring(0, withoutFirstCharacter.length() - 1);


            dialog.show();
            dialog.getWindow().setAttributes(lp);
            a = m.getA();
            emojiconEditText.setText(aa);
            emojiconEditText1.setText(withoutLastCharacter);
            loadInterstitialAd();
            progressDialog.dismiss();

        }


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(Emojify.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(Emojify.this, "onAdFailedToLoad()" +i, Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

    }
}
