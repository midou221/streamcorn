package com.est.streamcorn.network.channels;

import com.est.streamcorn.models.Media;
import com.est.streamcorn.models.Movie;
import com.est.streamcorn.models.TvSeries;
import com.est.streamcorn.network.utils.NetworkUtils;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Base class per tutti i canali
 */
public abstract class Channel {

    public Single<ArrayList<Media>> getMovieList(int page) {
        return NetworkUtils.downloadPage(getProperties().getMovieListUrl(page))
                .observeOn(Schedulers.computation())
                .map(this::parseMovieList)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Media>> getTvSeriesList(int page) {
        return NetworkUtils.downloadPage(getProperties().getTvSeriesListUrl(page))
                .observeOn(Schedulers.computation())
                .map(this::parseTvSeriesList)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Media>> searchMovie(String query, int page) {
        return NetworkUtils.downloadPage(getProperties().getMovieSearchUrl(query, page))
                .observeOn(Schedulers.computation())
                .map(this::parseSearchedMovieList)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Media>> searchTvSeries(String query, int page) {
        return NetworkUtils.downloadPage(getProperties().getTvSeriesSearchUrl(query, page))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::parseSearchedTvSeriesList)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Movie> getMovie(String url) {
        return NetworkUtils.downloadPage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::parseMovieDetail)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<TvSeries> getTvSeries(String url) {
        return NetworkUtils.downloadPage(url)
                .observeOn(Schedulers.computation())
                .map(this::parseTvSeriesDetail)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single getMedia(String url, @Media.MediaType int mediaType) {
        switch (mediaType) {
            case Media.MOVIE:
                return getMovie(url);
            case Media.TV_SERIES:
                return getTvSeries(url);
            case Media.UNKNOWN:
            default:
                //  Nel caso il tipo di media aperto nel channel sia sconosciuto, cerco di capire che tipo è
                return NetworkUtils.downloadPage(url)
                        .observeOn(Schedulers.computation())
                        .map(document -> {
                            Movie m = parseMovieDetail(document);
                            if (m.getUrls().size() == 0)  //Try to parse as movie. If fail, parse as Tv series
                                return parseTvSeriesDetail(document);
                            else
                                return m;
                        })
                        .observeOn(AndroidSchedulers.mainThread());
        }
    }

    protected abstract ArrayList<Media> parseMovieList(Document document) throws Exception;

    protected abstract ArrayList<Media> parseTvSeriesList(Document document) throws Exception;

    protected abstract ArrayList<Media> parseSearchedMovieList(Document document) throws Exception;

    protected abstract ArrayList<Media> parseSearchedTvSeriesList(Document document) throws Exception;

    protected abstract Movie parseMovieDetail(Document document) throws Exception;

    protected abstract TvSeries parseTvSeriesDetail(Document document) throws Exception;

    public abstract ChannelProperties getProperties();
}
