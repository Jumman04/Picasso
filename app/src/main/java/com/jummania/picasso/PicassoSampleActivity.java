package com.jummania.picasso;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentActivity;

import com.squareup.picasso.Picasso;

abstract class PicassoSampleActivity extends FragmentActivity {
    private ToggleButton showHide;
    private FrameLayout sampleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.picasso_sample_activity);
        sampleContent = findViewById(R.id.sample_content);

        final ListView activityList = findViewById(R.id.activity_list);
        final PicassoSampleAdapter adapter = new PicassoSampleAdapter(this);
        activityList.setAdapter(adapter);
        activityList.setOnItemClickListener((adapterView, view, position, id) -> adapter.getItem(position).launch(PicassoSampleActivity.this));

        showHide = findViewById(R.id.faux_action_bar_control);
        showHide.setOnCheckedChangeListener((compoundButton, checked) -> activityList.setVisibility(checked ? VISIBLE : GONE));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (showHide.isChecked()) {
                    showHide.setChecked(false);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.get().cancelTag(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, sampleContent);
    }

    @Override
    public void setContentView(View view) {
        sampleContent.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        sampleContent.addView(view, params);
    }
}
