package com.barkote.kiosk.emojify;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.jetbrains.annotations.NotNull;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Emojify extends AppCompatActivity {


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
    private ImageView image;
    private EmojiconEditText editText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emojify);


        view = findViewById(R.id.rootview);
        image = findViewById(R.id.button);
        editText = findViewById(R.id.editText);

        networkCalls = Emo.getApiClient();
        r = new Result("ex", "ex");

        progressBar = findViewById(R.id.progress);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        emojIcon = new EmojIconActions(this, view, editText, image);
        View.OnTouchListener otl = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true; // the listener has consumed the event
            }
        };

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

                if (isNetworkAvailable()) {
                    progressBar.setVisibility(View.VISIBLE);
                    emojify(s);

                } else {
                    if (editText.getText() != null) {
                        close();
                    } else {
                        Toast.makeText(Emojify.this, "Please enter an emoji", Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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


        result.clone().enqueue(new Callback<Result>() {
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

    @SuppressLint("SetTextI18n")
    private void show(Result r) {

        progressBar.setVisibility(View.INVISIBLE);

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


            }
        });


        String ans = r.getAns();

        String withoutFirstCharacter = ans.substring(2);
        String withoutLastCharacter = withoutFirstCharacter.substring(0, withoutFirstCharacter.length() - 1);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        a = m.getA();
        emojiconEditText.setText(aa);
        emojiconEditText1.setText(withoutLastCharacter);


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
