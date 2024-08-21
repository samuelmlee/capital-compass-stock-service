package org.capitalcompass.stockservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TickerNewsResult {

    private TickerNewsPublisher publisher;
    private String title;
    private String author;
    private Date publishedUtc;
    private String articleUrl;
    private List<String> tickers;
    private String imageUrl;
    private String description;
    private ArrayList<String> keywords;

    @JsonProperty("publishedUtc")
    public Date getPublishedUtc() {
        return publishedUtc;
    }

    @JsonProperty("published_utc")
    public void setPublishedUtc(Date publishedUtc) {
        this.publishedUtc = publishedUtc;
    }

    @JsonProperty("articleUrl")
    public String getArticleUrl() {
        return articleUrl;
    }

    @JsonProperty("article_url")
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
