package com.ronin.phonenews.util;

import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

/**
 * Created by donghailong on 2018/4/10.
 */

public class UriHelper {

    /**
     * @param ay
     * @param uri
     * @return
     */
    public static String uri2Path(Activity ay, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            cursor = ay.managedQuery(uri, projection, null, null, null);
        } else {
            CursorLoader cursorLoader = new CursorLoader(ay, uri,
                    projection, null, null, null);
            cursor = cursorLoader.loadInBackground();
        }
        int columnIndex = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
}
