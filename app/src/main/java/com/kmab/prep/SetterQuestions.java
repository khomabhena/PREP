package com.kmab.prep;

public class SetterQuestions {

    public SetterQuestions() {
    }

    private String key, parentKey, name, link;
    private boolean question;

    public SetterQuestions(String key, String parentKey, String name, String link, boolean question) {
        this.key = key;
        this.parentKey = parentKey;
        this.name = name;
        this.link = link;
        this.question = question;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isQuestion() {
        return question;
    }

    public void setQuestion(boolean question) {
        this.question = question;
    }

}
