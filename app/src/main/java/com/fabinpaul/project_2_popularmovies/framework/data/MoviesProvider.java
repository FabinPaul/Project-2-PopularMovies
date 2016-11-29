package com.fabinpaul.project_2_popularmovies.framework.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Fabin Paul on 11/25/2016 3:31 PM.
 */

public class MoviesProvider extends ContentProvider{
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query (Uri uri, String[] projection, String pSelection, String[] pSelectionArgs, String pSortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri pUri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri pUri, ContentValues pContentValues) {
        return null;
    }

    @Override
    public int delete(Uri pUri, String pSelection, String[] pSelectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri pUri, ContentValues pContentValues, String pSelection, String[] pSelectionArgs) {
        return 0;
    }
}
