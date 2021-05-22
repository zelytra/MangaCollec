package fr.zelytra.mangacollec.api;

import java.util.List;

public class RestMovieResponse {
    private int page;
    private int totalPages;
    private int totalResults;
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
