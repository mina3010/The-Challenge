package com.example.sharara.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResultScore implements Comparable<ResultScore> {
    private int category_id;
    private String category_name;
    private int level;
    private int score;
    private int score_len;
    public List<Comparable>sortScore=new ArrayList<>();



    public ResultScore(int category_id, String category_name, int level, int score, int score_len) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.level = level;
        this.score = score;
        this.score_len = score_len;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getScore_len() {
        return score_len;
    }

    public void setScore_len(int score_len) {
        this.score_len = score_len;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Comparable> getSortScore() {
        return sortScore;
    }

    public void setSortScore(List<Comparable> sortScore) {
        this.sortScore = sortScore;
    }


    @Override
    public int compareTo(ResultScore o) {
        return o.getScore() - this.score ;
    }
}
