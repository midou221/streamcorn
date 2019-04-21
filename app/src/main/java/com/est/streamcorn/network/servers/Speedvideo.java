package com.est.streamcorn.network.servers;

import android.content.Context;
import android.util.Log;
import com.est.streamcorn.network.utils.NetworkUtils;
import com.est.streamcorn.utils.RegexpUtils;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.util.regex.Pattern;

public class Speedvideo extends Server {

    private static final String TAG = "Speedvideo";

    private static final Pattern GET_SPEEDVIDEO_URL = Pattern.compile("https?:\\/\\/speedvideo\\.net\\/(.*)\\/");
    private static final Pattern GET_SPEEDVIDEO_FILE_URL = Pattern.compile("linkfile =\"([^\"]+)\"");

    @Override
    public Single<String> resolve(String url, Context context) {
        if (!url.contains("embed"))
            url = "https://speedvideo.net/embed-" + RegexpUtils.getFirstMatch(GET_SPEEDVIDEO_URL, url) + "-1x1.html";

        Log.d(TAG, "Resolving: " + url);
        return NetworkUtils.downloadPageHeadless(url, context)
                .observeOn(Schedulers.computation())
                .map(document -> {
                    String parsedUrl = RegexpUtils.getFirstMatch(GET_SPEEDVIDEO_FILE_URL, document.html());

                    Log.d(TAG, "Resolved: " + parsedUrl);
                    return parsedUrl;
                });
    }

    @Override
    public boolean isVideo() {
        return true;
    }
}