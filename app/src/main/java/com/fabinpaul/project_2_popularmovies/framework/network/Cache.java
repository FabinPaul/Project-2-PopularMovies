package com.fabinpaul.project_2_popularmovies.framework.network;

import java.util.HashMap;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/7/2016 12:32 PM.
 */

public enum Cache {
    INSTANCE;

    private final HashMap<Object, Object> mInMemoryCache = new HashMap<>();

    public void addToCache(Object pTag, Object pObject) {
        mInMemoryCache.put(pTag, pObject);
    }

    public Object getFromCache(Object pTag) {
        return mInMemoryCache.get(pTag);
    }

    public void removeAllMovieList(@MoviesServiceApi.MovieSortTypes String sortType) {
        int i = 1;
        while (mInMemoryCache.size() > 0) {
            if (mInMemoryCache.containsKey(sortType + i))
                mInMemoryCache.remove(sortType + i);
            else
                break;
            i++;
        }
    }
}