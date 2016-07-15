package com.example.framgia.alarmclock.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.framgia.alarmclock.R;
import com.example.framgia.alarmclock.data.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextClock mTextClockHour, mTextClockSecond;
    private TextView mTextViewBattery, mTextViewHideHour, mTextViewHideSecond;
    private ImageView mImageViewWeather, mImageViewSettings, mImageViewHelp, mImageViewAlarm;
    private ImageView mImageViewTimer;
    private GestureDetector mGestureDetector;
    private float mAlpha;
    private boolean mIscreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideStatusBar();
        initViews();
        showAdvanced();
        onSimpleOnGestureListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIscreated = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIscreated) {
            showAdvanced();
        }
    }

    private void onSimpleOnGestureListener() {
        GestureDetector.SimpleOnGestureListener simpleOnGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float y1 = e1.getY();
                    float y2 = e2.getY();
                    if (y1 > y2) {
                        onFadeInChangeBrightness(Constants.UP);
                    } else if (y1 < y2) {
                        onFadeInChangeBrightness(Constants.DOWN);
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            };
        mGestureDetector = new GestureDetector(this, simpleOnGestureListener);
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initViews() {
        Typeface face = Typeface.createFromAsset(getAssets(), Constants.FONT_TIME);
        // text clock
        mTextClockHour = (TextClock) findViewById(R.id.text_clock_hour);
        mTextClockSecond = (TextClock) findViewById(R.id.text_clock_second);
        mTextClockHour.setTypeface(face);
        mTextClockSecond.setTypeface(face);
        // hide text
        mTextViewHideHour = (TextView) findViewById(R.id.text_hide_hour);
        mTextViewHideSecond = (TextView) findViewById(R.id.text_hide_second);
        mTextViewHideHour.setTypeface(face);
        mTextViewHideSecond.setTypeface(face);
        // text battery
        mTextViewBattery = (TextView) findViewById(R.id.text_battery);
        // image icon
        mImageViewWeather = (ImageView) findViewById(R.id.image_weather);
        mImageViewSettings = (ImageView) findViewById(R.id.image_settings);
        mImageViewHelp = (ImageView) findViewById(R.id.image_help);
        mImageViewAlarm = (ImageView) findViewById(R.id.image_alarm);
        mImageViewTimer = (ImageView) findViewById(R.id.image_timer);
        mImageViewWeather.setOnClickListener(this);
        mImageViewSettings.setOnClickListener(this);
        mImageViewHelp.setOnClickListener(this);
        mImageViewAlarm.setOnClickListener(this);
        mImageViewTimer.setOnClickListener(this);
        mAlpha = Constants.ALPHA_MAX;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_weather:
                // TODO: 12/07/2016  
                break;
            case R.id.image_settings:
                openActivity(SettingActivity.class);
                break;
            case R.id.image_help:
                // TODO: 12/07/2016  
                break;
            case R.id.image_alarm:
                // TODO: 12/07/2016
                break;
            case R.id.image_timer:
                // TODO: 12/07/2016
                break;
        }
    }

    private void openActivity(Class myClass) {
        Intent intent = new Intent(this, myClass);
        startActivity(intent);
    }

    private void showAdvanced() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCES,
            Context.MODE_PRIVATE);
        int typeClock =
            sharedPreferences.getInt(Constants.TYPE_CLOCKS, Constants.TYPE_CLOCKS_WHITE);
        switch (typeClock) {
            case Constants.TYPE_CLOCKS_WHITE:
                showColorViews(Color.WHITE, R.color.colorWhite);
                break;
            case Constants.TYPE_CLOCKS_BLUE:
                showColorViews(Color.BLUE, R.color.colorBlue);
                break;
            case Constants.TYPE_CLOCKS_GREEN:
                showColorViews(Color.GREEN, R.color.colorGreen);
                break;
            case Constants.TYPE_CLOCKS_RED:
                showColorViews(Color.RED, R.color.colorRed);
                break;
            case Constants.TYPE_CLOCKS_YELLOW:
                showColorViews(Color.YELLOW, R.color.colorYellow);
                break;
            case Constants.TYPE_CLOCKS_ANALOG:
                // TODO: 14/07/2016
                break;
        }
        mTextViewBattery.setVisibility(View.INVISIBLE);
        mTextClockHour.setFormat12Hour(null);
        mTextClockSecond.setFormat12Hour(null);
        mTextClockSecond.setFormat24Hour(Constants.FOMAT_TIME24);
    }

    private void showColorViews(int color, int colorHide) {
        mTextClockHour.setTextColor(color);
        mTextClockSecond.setTextColor(color);
        mTextViewHideHour.setTextColor(ContextCompat.getColor(this, colorHide));
        mTextViewHideSecond.setTextColor(ContextCompat.getColor(this, colorHide));
        mImageViewAlarm.setColorFilter(color);
    }

    private void onFadeInChangeBrightness(int type) {
        switch (type) {
            case Constants.DOWN:
                onDownBrightness();
                break;
            case Constants.UP:
                onUpBrightness();
                break;
        }
    }

    private void onDownBrightness() {
        if (mAlpha > Constants.ALPHA_MIN) {
            mAlpha -= Constants.ALPHA_DELTA;
            setAlpha();
        }
    }

    private void setAlpha() {
        mTextClockHour.setAlpha(mAlpha);
        mTextClockSecond.setAlpha(mAlpha);
        mImageViewWeather.setAlpha(mAlpha);
        mImageViewSettings.setAlpha(mAlpha);
        mImageViewHelp.setAlpha(mAlpha);
        mImageViewAlarm.setAlpha(mAlpha);
        mImageViewTimer.setAlpha(mAlpha);
    }

    private void onUpBrightness() {
        if (mAlpha < Constants.ALPHA_MAX) {
            mAlpha += Constants.ALPHA_DELTA;
            setAlpha();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
