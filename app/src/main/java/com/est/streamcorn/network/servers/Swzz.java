package com.est.streamcorn.network.servers;

import android.content.Context;
import android.util.Log;
import com.est.streamcorn.network.utils.NetworkUtils;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.jsoup.nodes.Element;

public class Swzz extends Server {

    private static final String TAG = "Swzz";

    @Override
    public Single<String> resolve(String url, Context context) {
        if (!url.contains("embed")) url += "embed/552x352/";

        Log.d(TAG, "Resolving: " + url);
        return NetworkUtils.downloadPageHeadless(url, context)
                .observeOn(Schedulers.computation())
                .map(document -> {
                    Element element = document.selectFirst("a[href].btn-wrapper.link");
                    String parsedUrl = element.attr("href");
                    if (!parsedUrl.startsWith("http")) parsedUrl = "https:" + parsedUrl;

                    Log.d(TAG, "Resolved: " + parsedUrl);
                    return parsedUrl;
                });
    }

    @Override
    public boolean isVideo() {
        return false;
    }
}