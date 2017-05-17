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
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("popularity")
    private double popularity;

    private Movie(final Builder builder) {
        id = builder.id;
        originalTitle = builder.originalTitle;
        overview = builder.overview;
        posterPath = builder.posterPath;
        backdropPath = builder.backdropPath;
        releaseDate = builder.releaseDate;
        voteAverage = builder.voteAverage;
        popularity = builder.popularity;
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
        builder.backdropPath = copy.backdropPath;
        builder.releaseDate = copy.releaseDate;
        builder.voteAverage = copy.voteAverage;
        builder.popularity = copy.popularity;
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

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    /**
     * {@code Movie} builder static inner class.
     */
    public static final class Builder {
        private int id;
        private String originalTitle;
        private String overview;
        private String posterPath;
        private String backdropPath;
        private String releaseDate;
        private double voteAverage;
        private double popularity;

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
         * Sets the {@code backdropPath} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code backdropPath} to set
         * @return a reference to this Builder
         */
        public Builder backdropPath(String val) {
            backdropPath = val;
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

        /**
         * Sets the {@code id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param id the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder withId(final int id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the {@code originalTitle} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param originalTitle the {@code originalTitle} to set
         * @return a reference to this Builder
         */
        public Builder withOriginalTitle(final String originalTitle) {
            this.originalTitle = originalTitle;
            return this;
        }

        /**
         * Sets the {@code overview} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param overview the {@code overview} to set
         * @return a reference to this Builder
         */
        public Builder withOverview(final String overview) {
            this.overview = overview;
            return this;
        }

        /**
         * Sets the {@code posterPath} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param posterPath the {@code posterPath} to set
         * @return a reference to this Builder
         */
        public Builder withPosterPath(final String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        /**
         * Sets the {@code backdropPath} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param backdropPath the {@code backdropPath} to set
         * @return a reference to this Builder
         */
        public Builder withBackdropPath(final String backdropPath) {
            this.backdropPath = backdropPath;
            return this;
        }

        /**
         * Sets the {@code releaseDate} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param releaseDate the {@code releaseDate} to set
         * @return a reference to this Builder
         */
        public Builder withReleaseDate(final String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        /**
         * Sets the {@code voteAverage} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param voteAverage the {@code voteAverage} to set
         * @return a reference to this Builder
         */
        public Builder withVoteAverage(final double voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        /**
         * Sets the {@code popularity} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param popularity the {@code popularity} to set
         * @return a reference to this Builder
         */
        public Builder withPopularity(final double popularity) {
            this.popularity = popularity;
            return this;
        }
    }
}
