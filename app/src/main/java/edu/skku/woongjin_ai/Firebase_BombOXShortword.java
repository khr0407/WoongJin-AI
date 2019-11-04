package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class Firebase_BombOXShortword {
    public String question;
    public String answer;
    public String type;
    public String solve;

    public Firebase_BombOXShortword() {

    }
    public Firebase_BombOXShortword(String question, String answer, String type, String solve) {
        this.question = question;
        this.answer = answer;
        this.type = type;
        this.solve = solve;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("question", question);
        result.put("answer", answer);
        result.put("type", type);
        result.put("solve", solve);
        return result;
    }
}
