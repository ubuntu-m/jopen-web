package io.jopen.web.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 */
@Document(collection = "ubuntu")
public class MongoDBModel implements Serializable {


    @Id
    private String id;


    private String title;

    private String description;


    private Long subscribe;


    private String keys;


    private Date publishDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Long subscribe) {
        this.subscribe = subscribe;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
