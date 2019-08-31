package com.example.ayush.theguardiannews;

public class News {

    /**
     * Title of Article.
     */
    private String mTitle;

    /**
     * Name of Section.
     */
    private String mSection;

    /**
     * Article publication Date & Time.
     */
    private String mPublicationDate;

    /**
     * URL of the article.
     */
    private String mUrl;

    /**
     * Author of the article.
     */
    private String mAuthor;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title           is the title of the news article
     * @param section         is the section name of article
     * @param publicationDate is the date & time of article published
     * @param url             is the website url to find more details about article
     * @param author          is the author name of article
     */
    public News(String title, String section, String publicationDate, String url, String author) {
        mTitle = title;
        mSection = section;
        mPublicationDate = publicationDate;
        mUrl = url;
        mAuthor = author;
    }

    /**
     * Returns the Title of the article.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the Section name of article.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the Date & Time of article published.
     */
    public String getPublicationDate() {
        return mPublicationDate;
    }

    /**
     * Returns the website Url of the article.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Returns the author name of article.
     */
    public String getAuthor() {
        return mAuthor;
    }

}
