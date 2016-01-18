package edu.uw.fragmentdemo;

/**
 * A simple container for Movie info
 */
public class Movie {
    public String title;
    public int year;
    public String imdbId;
    public String posterUrl;


    public Movie(String title, int year, String imdbId, String posterUrl){
        this.title = title;
        this.year = year;
        this.imdbId = imdbId;
        this.posterUrl = posterUrl;
    }

    //default constructor; empty movie
    public Movie(){}

    public String toString(){
        return this.title + " ("+this.year+")";
    }
}
