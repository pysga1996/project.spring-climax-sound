package com.lambda.model.form;

import java.util.Date;

public interface MediaForm {
    String getName();

    void setName(String name);

    Date getReleaseDate();

    void setReleaseDate(Date releaseDate);

    String getArtists();

    void setArtists(String artists);

    String getGenres();

    void setGenres(String genres);

    String getTags();

    void setTags(String tags);

    String getCountry();

    void setCountry(String country);

    String getTheme();

    void setTheme(String theme);
}