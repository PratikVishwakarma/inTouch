package com.example.pratik.intouch_v_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.telecom.Call;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    ImageButton imageButton_international, imageButton_politics, imageButton_science, imageButton_sports;
    ImageButton imageButton_business, imageButton_technology;
    SwitchCompat switchCompat_show_image;

    private static SharedPreferences sp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //setupWindowAnimations();

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        initializeScreen();

        imageButton_international.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(getResources().getString(R.string.key_get_news_category),
                        getResources().getString(R.string.key_news_category_international)).apply();
                set_button_active();
                makeToast(getResources().getString(R.string.news_category_international));
                onBackPressed();
            }
        });
        imageButton_science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(getResources().getString(R.string.key_get_news_category),
                        getResources().getString(R.string.key_news_category_science)).apply();
                set_button_active();
                makeToast(getResources().getString(R.string.news_category_science));
                onBackPressed();
            }
        });
        imageButton_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(getResources().getString(R.string.key_get_news_category),
                        getResources().getString(R.string.key_news_category_sports)).apply();
                set_button_active();
                makeToast(getResources().getString(R.string.news_category_sports));
                onBackPressed();
            }
        });
        imageButton_politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(getResources().getString(R.string.key_get_news_category),
                        getResources().getString(R.string.key_news_category_politics)).apply();
                set_button_active();
                makeToast(getResources().getString(R.string.news_category_politics));
                onBackPressed();
            }
        });
        imageButton_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(getResources().getString(R.string.key_get_news_category),
                        getResources().getString(R.string.key_news_category_business)).apply();
                set_button_active();
                makeToast(getResources().getString(R.string.news_category_business));
                onBackPressed();
            }
        });
        imageButton_technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(getResources().getString(R.string.key_get_news_category),
                        getResources().getString(R.string.key_news_category_technology)).apply();
                set_button_active();
                makeToast(getResources().getString(R.string.news_category_technology));
                onBackPressed();
            }
        });
        switchCompat_show_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat_show_image.isChecked()){
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putBoolean(getResources().getString(R.string.key_show_news_image), Boolean.TRUE).apply();
                    makeToast(getResources().getString(R.string.toast_message_show_image));
                    onBackPressed();
                } else{
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putBoolean(getResources().getString(R.string.key_show_news_image), Boolean.FALSE).apply();
                    makeToast(getResources().getString(R.string.toast_message_dont_show_image));
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }

    private void setupWindowAnimations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(fade);

//        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
//        getWindow().setExitTransition(slide);
    }

    public void initializeScreen(){
        imageButton_international = (ImageButton) findViewById(R.id.image_button_setting_international);
        imageButton_science = (ImageButton) findViewById(R.id.image_button_setting_science);
        imageButton_politics = (ImageButton) findViewById(R.id.image_button_setting_politics);
        imageButton_sports = (ImageButton) findViewById(R.id.image_button_setting_sports);
        imageButton_business = (ImageButton) findViewById(R.id.image_button_setting_business);
        imageButton_technology = (ImageButton) findViewById(R.id.image_button_setting_technology);

        switchCompat_show_image = (SwitchCompat) findViewById(R.id.switch_show_image_option);

        boolean showImageBoolean = sp.getBoolean(getResources().getString(R.string.key_show_news_image), Boolean.TRUE);
        if(showImageBoolean){
            SharedPreferences.Editor spe = sp.edit();
            spe.putBoolean(getResources().getString(R.string.key_show_news_image), Boolean.TRUE).apply();
            switchCompat_show_image.setChecked(Boolean.TRUE);
        }else{
            SharedPreferences.Editor spe = sp.edit();
            spe.putBoolean(getResources().getString(R.string.key_show_news_image), Boolean.FALSE).apply();
            switchCompat_show_image.setChecked(Boolean.FALSE);
        }
        set_button_active();

    }

    public void set_button_active(){

        imageButton_international.setImageResource(R.drawable.ic_international_64_inactive);
        imageButton_science.setImageResource(R.drawable.ic_science_64_inactive);
        imageButton_sports.setImageResource(R.drawable.ic_sports_64_inactive);
        imageButton_business.setImageResource(R.drawable.ic_business_64_inactive);
        imageButton_politics.setImageResource(R.drawable.ic_international_64_inactive);
        imageButton_technology.setImageResource(R.drawable.ic_technology_64_inactive);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String newsCategory = sp.getString(getResources().getString(R.string.key_get_news_category),
                getResources().getString(R.string.key_news_category_allNews));

        Log.e("SettingsActivity ", "Data category is "+newsCategory+"");

        if(newsCategory.equals(getResources().getString(R.string.key_news_category_international))){
            imageButton_international.setImageResource(R.drawable.ic_international_64_active);
        } else if(newsCategory.equals(getResources().getString(R.string.key_news_category_science))){
            imageButton_science.setImageResource(R.drawable.ic_science_64_active);
        } else if(newsCategory.equals(getResources().getString(R.string.key_news_category_technology))){
            imageButton_technology.setImageResource(R.drawable.ic_technology_64_active);
        } else if(newsCategory.equals(getResources().getString(R.string.key_news_category_sports))){
            imageButton_sports.setImageResource(R.drawable.ic_sports_64_active);
        } else if(newsCategory.equals(getResources().getString(R.string.key_news_category_politics))){
            imageButton_politics.setImageResource(R.drawable.ic_international_64_active);
        } else if(newsCategory.equals(getResources().getString(R.string.key_news_category_business))){
            imageButton_business.setImageResource(R.drawable.ic_business_64_active);
        } else if(newsCategory.equals(getResources().getString(R.string.key_news_category_allNews))){

        }
    }

    public void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
