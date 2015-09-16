package es.iridiobis.popularmovies.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * POJO for movies. Only contains information required by the business logic.
 */
public class Movie {
    private int id;
    @SerializedName("original_title")
    private String originalTitle;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;

    private Movie(Builder builder) {
        id = builder.id;
        originalTitle = builder.originalTitle;
        overview = builder.overview;
        posterPath = builder.posterPath;
        releaseDate = builder.releaseDate;
        voteAverage = builder.voteAverage;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(final Movie copy) {
        Builder builder = new Builder();
        builder.id = copy.id;
        builder.originalTitle = copy.originalTitle;
        builder.overview = copy.overview;
        builder.posterPath = copy.posterPath;
        builder.releaseDate = copy.releaseDate;
        builder.voteAverage = copy.voteAverage;
        return builder;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    /**
     * {@code Movie} builder static inner class.
     */
    public static final class Builder {
        private int id;
        private String originalTitle;
        private String overview;
        private String posterPath;
        private String releaseDate;
        private double voteAverage;

        private Builder() {
        }

        /**
         * Sets the {@code id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder id(final int val) {
            id = val;
            return this;
        }

        /**
         * Sets the {@code originalTitle} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code originalTitle} to set
         * @return a reference to this Builder
         */
        public Builder originalTitle(final String val) {
            originalTitle = val;
            return this;
        }

        /**
         * Sets the {@code overview} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code overview} to set
         * @return a reference to this Builder
         */
        public Builder overview(final String val) {
            overview = val;
            return this;
        }

        /**
         * Sets the {@code posterPath} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code posterPath} to set
         * @return a reference to this Builder
         */
        public Builder posterPath(final String val) {
            posterPath = val;
            return this;
        }

        /**
         * Sets the {@code releaseDate} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code releaseDate} to set
         * @return a reference to this Builder
         */
        public Builder releaseDate(final String val) {
            releaseDate = val;
            return this;
        }

        /**
         * Sets the {@code voteAverage} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code voteAverage} to set
         * @return a reference to this Builder
         */
        public Builder voteAverage(final double val) {
            voteAverage = val;
            return this;
        }

        /**
         * Returns a {@code Movie} built from the parameters previously set.
         *
         * @return a {@code Movie} built with parameters of this {@code Movie.Builder}
         */
        public Movie build() {
            return new Movie(this);
        }
    }
}
