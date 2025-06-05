package com.jummania.picasso;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;

import java.util.Random;

final class PicassoSampleAdapter extends BaseAdapter {

    private final LayoutInflater inflater;

    PicassoSampleAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Sample.values().length;
    }

    @Override
    public Sample getItem(int position) {
        return Sample.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) convertView;
        if (view == null) {
            view = (TextView) inflater.inflate(R.layout.picasso_sample_activity_item, parent, false);
        }

        view.setText(getItem(position).name);

        return view;
    }

    enum Sample {
        GRID_VIEW("Image Grid View", SampleGridViewActivity.class), GALLERY("Load from Gallery", SampleGalleryActivity.class), CONTACTS("Contact Photos", SampleContactsActivity.class), LIST_DETAIL("List / Detail View", SampleListDetailActivity.class),

        SHOW_NOTIFICATION("Sample Notification", null) {
            @Override
            public void launch(Activity activity) {
                final int NOTIFICATION_ID = 1;
                final String CHANNEL_ID = "picasso";

                RemoteViews remoteViews = new RemoteViews(activity.getPackageName(), R.layout.notification_view);

                Intent intent = new Intent(activity, SampleGridViewActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

                // Create notification channel if needed (Android 8+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Picasso Channel", NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Channel for sample Picasso notification");
                    notificationManager.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID).setSmallIcon(R.drawable.icon).setContentIntent(pendingIntent).setCustomContentView(remoteViews).setAutoCancel(true);

                Notification notification = builder.build();
                notificationManager.notify(NOTIFICATION_ID, notification);

                // Load image into notification using Picasso
                Picasso.get().load(Data.URLS[new Random().nextInt(Data.URLS.length)]).resizeDimen(R.dimen.notification_icon_width_height, R.dimen.notification_icon_width_height).into(remoteViews, R.id.photo, NOTIFICATION_ID, notification);
            }
        };

        private final Class<? extends Activity> activityClass;
        private final String name;

        Sample(String name, Class<? extends Activity> activityClass) {
            this.name = name;
            this.activityClass = activityClass;
        }

        public void launch(Activity activity) {
            if (activityClass != null) {
                Intent intent = new Intent(activity, activityClass);
                activity.startActivity(intent);
            }
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}
