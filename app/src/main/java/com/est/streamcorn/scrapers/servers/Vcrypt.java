package com.est.streamcorn.scrapers.servers;

import android.content.Context;
import android.util.Log;
import com.est.streamcorn.scrapers.utils.NetworkUtils;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class Vcrypt extends Server {

    private static final String TAG = "Vcrypt";

    @Override
    public Single<String> resolve(String url, Context context) {
        url = url.replaceFirst("shield", "opencryptz1")
                .replaceFirst("wss", "wss1");
        final String finalUrl = url;

        Log.d(TAG, "Resolving: " + url);
        return NetworkUtils.downloadPage(url)
                .observeOn(Schedulers.computation())
                .map(document -> {
                    String parsedUrl;
                    if (finalUrl.contains("opencryptz1")) {
                        parsedUrl = document.selectFirst("iframe[src]").attr("src");
                    } else {
                        Log.d(TAG, "base: " + document.baseUri());
                        parsedUrl = document.location().equals(finalUrl) ? null : document.location();
                    }

                    Log.d(TAG, "Resolved: " + parsedUrl);
                    return parsedUrl;
                });
    }

    @Override
    public boolean isVideo() {
        return false;
    }
}
