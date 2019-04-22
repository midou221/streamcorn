package com.est.streamcorn.network.channels;


import androidx.arch.core.util.Function;
import com.est.streamcorn.network.ChannelService;


/**
 * Container per tutte le proprietà di un Channel
 */
public class ChannelProperties {

    private @ChannelService.ChannelType
    String domain;
    private String parametricName;
    private int bannerDrawable;
    private boolean hasMovies, hasTvSeries, isSearchable;
    private Function<Integer, String> getMovieListUrlFunction, getTvSeriesListUrlFunction;
    private Function<String, Function<Integer, String>> getMovieSearchUrlFunction, getTvSeriesSearchUrlFunction;

    public ChannelProperties(@ChannelService.ChannelType String domain,
                             String parametricName,
                             int bannerDrawable,
                             boolean hasMovies,
                             boolean hasTvSeries,
                             boolean isSearchable,
                             Function<Integer, String> getMovieListUrlFunction,
                             Function<Integer, String> getTvSeriesListUrlFunction,
                             Function<String, Function<Integer, String>> getMovieSearchUrlFunction,
                             Function<String, Function<Integer, String>> getTvSeriesSearchUrlFunction) {
        this.domain = domain;
        this.parametricName = parametricName;
        this.bannerDrawable = bannerDrawable;
        this.hasMovies = hasMovies;
        this.hasTvSeries = hasTvSeries;
        this.isSearchable = isSearchable;
        this.getMovieListUrlFunction = getMovieListUrlFunction;
        this.getTvSeriesListUrlFunction = getTvSeriesListUrlFunction;
        this.getMovieSearchUrlFunction = getMovieSearchUrlFunction;
        this.getTvSeriesSearchUrlFunction = getTvSeriesSearchUrlFunction;
    }

    public @ChannelService.ChannelType
    String getDomain() {
        return domain;
    }

    public String getBaseUrl() {
        return "https://" + getDomain() + "/";
    }

    public String getParametricName() {
        return parametricName;
    }

    public int getBannerDrawable() {
        return bannerDrawable;
    }

    public boolean hasMovies() {
        return hasMovies;
    }

    public boolean hasTvSeries() {
        return hasTvSeries;
    }

    public boolean isSearchable() {
        return isSearchable;
    }

    public String getMovieListUrl(int page) {
        return getBaseUrl() + getMovieListUrlFunction.apply(page);
    }

    public String getTvSeriesListUrl(int page) {
        return getBaseUrl() + getTvSeriesListUrlFunction.apply(page);
    }

    public String getMovieSearchUrl(String query, int page) {
        return getBaseUrl() + getMovieSearchUrlFunction.apply(query).apply(page);
    }

    public String getTvSeriesSearchUrl(String query, int page) {
        return getBaseUrl() + getTvSeriesSearchUrlFunction.apply(query).apply(page);
    }
}