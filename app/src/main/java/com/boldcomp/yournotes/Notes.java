package com.boldcomp.yournotes;

public class Notes {
    private int id;
    private String n_title;
    private String n_content;

    public Notes(String title, String content){
        this.n_title = title;
        this.n_content = content;
    }
    public Notes(int id, String title, String content){
        this.id = id;
        this.n_title = title;
        this.n_content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getN_title() {
        return n_title;
    }

    public void setN_title(String n_title) {
        this.n_title = n_title;
    }

    public String getN_content() {
        return n_content;
    }

    public void setN_content(String n_content) {
        this.n_content = n_content;
    }
}
