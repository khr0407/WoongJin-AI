package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class QuizOXShortwordTypeInfo {
    public String uid;
    public String question;
    public String answer;
    public String star;
    public String desc;
    public String like;

    public QuizOXShortwordTypeInfo() {

    }

    public QuizOXShortwordTypeInfo(String uid, String question, String answer, String star, String desc, String like) {
        this.uid = uid;
        this.question = question;
        this.answer = answer;
        this.star = star;
        this.desc = desc;
        this.like = like;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("question", question);
        result.put("answer", answer);
        result.put("star", star);
        result.put("desc", desc);
        result.put("like", like);
        return result;
    }
}
