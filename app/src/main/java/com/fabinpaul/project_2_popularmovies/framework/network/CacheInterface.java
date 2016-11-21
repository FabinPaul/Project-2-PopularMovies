package com.fabinpaul.project_1_popularmovies.framework.network;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/7/2016 2:12 PM.
 */

public interface CacheInterface {

    void addToCache(String pTag, Object pObject);

    Object getFromCache(String pTag);
}
