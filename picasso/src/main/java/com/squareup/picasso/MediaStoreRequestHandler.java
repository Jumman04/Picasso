/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.picasso;

import static android.content.ContentResolver.SCHEME_CONTENT;
import static android.content.ContentUris.parseId;
import static android.provider.MediaStore.Images;
import static android.provider.MediaStore.Video;
import static com.squareup.picasso.Picasso.LoadedFrom.DISK;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Size;

import java.io.IOException;

import okio.Okio;
import okio.Source;

class MediaStoreRequestHandler extends ContentStreamRequestHandler {
    private static final String[] CONTENT_ORIENTATION = new String[]{Images.ImageColumns.ORIENTATION};

    MediaStoreRequestHandler(Context context) {
        super(context);
    }

    static PicassoKind getPicassoKind(int targetWidth, int targetHeight) {
        if (targetWidth <= PicassoKind.MICRO.width && targetHeight <= PicassoKind.MICRO.height) {
            return PicassoKind.MICRO;
        } else if (targetWidth <= PicassoKind.MINI.width && targetHeight <= PicassoKind.MINI.height) {
            return PicassoKind.MINI;
        }
        return PicassoKind.FULL;
    }

    static int getExifOrientation(ContentResolver contentResolver, Uri uri) {
        try (Cursor cursor = contentResolver.query(uri, CONTENT_ORIENTATION, null, null, null)) {
            if (cursor == null || !cursor.moveToFirst()) {
                return 0;
            }
            return cursor.getInt(0);
        } catch (RuntimeException ignored) {
            return 0;
        }
    }

    @Override
    public boolean canHandleRequest(Request data) {
        final Uri uri = data.uri;
        return SCHEME_CONTENT.equals(uri.getScheme()) && MediaStore.AUTHORITY.equals(uri.getAuthority());
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        int exifOrientation = getExifOrientation(contentResolver, request.uri);

        String mimeType = contentResolver.getType(request.uri);
        boolean isVideo = mimeType != null && mimeType.startsWith("video/");

        if (request.hasSize()) {
            PicassoKind kind = getPicassoKind(request.targetWidth, request.targetHeight);

            Bitmap bitmap = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    bitmap = contentResolver.loadThumbnail(request.uri, new Size(kind.width, kind.height), null);
                } catch (IOException ignored) {
                }
            } else {
                // For older Android versions, fall back to deprecated methods
                long id = parseId(request.uri);
                BitmapFactory.Options options = createBitmapOptions(request);
                options.inJustDecodeBounds = true;

                calculateInSampleSize(request.targetWidth, request.targetHeight, kind.width, kind.height, options, request);

                if (isVideo) {
                    int legacyKind = (kind == PicassoKind.FULL) ? Video.Thumbnails.MINI_KIND : kind.legacyKind;
                    bitmap = Video.Thumbnails.getThumbnail(contentResolver, id, legacyKind, options);
                } else {
                    int legacyKind = (kind == PicassoKind.FULL) ? Images.Thumbnails.MINI_KIND : kind.legacyKind;
                    bitmap = Images.Thumbnails.getThumbnail(contentResolver, id, legacyKind, options);
                }
            }

            if (bitmap != null) {
                return new Result(bitmap, null, DISK, exifOrientation);
            }
        }

        Source source = Okio.source(getInputStream(request));
        return new Result(null, source, DISK, exifOrientation);
    }

    enum PicassoKind {
        MICRO(96, 96, Images.Thumbnails.MICRO_KIND), MINI(512, 384, Images.Thumbnails.MINI_KIND), FULL(-1, -1, Images.Thumbnails.MINI_KIND); // Use MINI_KIND as fallback for FULL

        final int width;
        final int height;
        final int legacyKind;

        PicassoKind(int width, int height, int legacyKind) {
            this.width = width;
            this.height = height;
            this.legacyKind = legacyKind;
        }
    }
}

