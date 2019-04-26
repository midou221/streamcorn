package com.est.streamcorn.scrapers.servers;

import android.content.Context;
import android.util.Log;
import com.est.streamcorn.scrapers.ServerService;
import com.est.streamcorn.scrapers.utils.NetworkUtils;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class FilmSenzaLimiti extends Server {
    private static final String TAG = "FilmSenzaLimiti";

    @Override
    public Single<String> resolve(String url, Context context) {
        Log.d(TAG, "Resolving: " + url);
        //  Necessario il referrer per ottenere il redirect dal server
        return NetworkUtils.downloadPage(url, "https://" + ServerService.ServerType.FILMSENZALIMITI + "/title/")
                .observeOn(Schedulers.computation())
                .map(document -> {
                    String parsedUrl = document.location();
                    Log.d(TAG, "Resolved: " + parsedUrl);
                    return parsedUrl;
                });
    }

    @Override
    public boolean isVideo() {
        return false;
    }
}
