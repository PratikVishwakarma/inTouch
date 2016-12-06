package com.example.pratik.intouch_v_01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageButton;

public class SettingActivity extends AppCompatActivity {

    ImageButton imageButton_international;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //setupWindowAnimations();

        imageButton_international = (ImageButton) findViewById(R.id.image_button_setting_international);

        imageButton_international.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                imageButton_international.getResources();
                imageButton_international.setImageResource(R.drawable.ic_business_64_active);
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


}
