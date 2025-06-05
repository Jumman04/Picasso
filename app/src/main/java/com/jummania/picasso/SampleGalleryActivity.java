package com.jummania.picasso;

import static com.squareup.picasso.interfaces.Callback.EmptyCallback;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

public class SampleGalleryActivity extends PicassoSampleActivity {
    private static final String KEY_IMAGE = "com.example.picasso:image";

    private ImageView imageView;
    private ViewAnimator animator;
    private String image;
    private final ActivityResultLauncher<PickVisualMediaRequest> resultLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        if (uri != null) {
            image = uri.toString();
        } else image = null;
        loadImage();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_gallery_activity);

        animator = findViewById(R.id.animator);
        imageView = findViewById(R.id.image);

        Button go = findViewById(R.id.go);

        go.setOnClickListener(view -> resultLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()));

        if (savedInstanceState != null) {
            image = savedInstanceState.getString(KEY_IMAGE);
            if (image != null) {
                loadImage();
            }
        }

        go.callOnClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            // Always cancel the request here, this is safe to call even if the image has been loaded.
            // This ensures that the anonymous callback we have does not prevent the activity from
            // being garbage collected. It also prevents our callback from getting invoked even after the
            // activity has finished.
            Picasso.get().cancelRequest(imageView);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_IMAGE, image);
    }

    private void loadImage() {
        // Index 1 is the progress bar. Show it while we're loading the image.
        animator.setDisplayedChild(1);

        Picasso.get().load(image).fit().centerInside().into(imageView, new EmptyCallback() {
            @Override
            public void onSuccess() {
                // Index 0 is the image view.
                animator.setDisplayedChild(0);
            }
        });
    }
}
