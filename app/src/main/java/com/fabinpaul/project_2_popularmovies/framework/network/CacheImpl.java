package com.fabinpaul.project_1_popularmovies.framework.network;

import java.util.HashMap;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/7/2016 12:32 PM.
 */

public enum CacheImpl implements CacheInterface {
    INSTANCE;

    private HashMap<String, Object> mInMemoryCache = new HashMap<>();

    @Override
    public void addToCache(String pTag, Object pObject) {
        mInMemoryCache.put(pTag, pObject);
    }

    @Override
    public Object getFromCache(String pTag) {
        return mInMemoryCache.get(pTag);
    }
}
