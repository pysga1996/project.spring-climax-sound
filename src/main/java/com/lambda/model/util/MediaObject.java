package com.lambda.model.util;

import com.lambda.model.entity.*;

import java.util.Collection;
import java.util.Date;

public interface MediaObject {
    String getName();

    void setName(String name);

    Date getReleaseDate();

    void setReleaseDate(Date releaseDate);

    Collection<Genre> getGenres();

    void setGenres(Collection<Genre> genres);

    Collection<Artist> getArtists();

    void setArtists(Collection<Artist> artists);

    Collection<Tag> getTags();

    void setTags(Collection<Tag> tags);

    Mood getMood();

    void setMood(Mood mood);

    Activity getActivity();

    void setActivity(Activity activity);
}
