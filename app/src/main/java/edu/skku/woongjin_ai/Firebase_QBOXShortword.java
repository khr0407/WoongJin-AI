package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class Firebase_QBOXShortword {
    public String question;
    public String diff;
    public String answer;
    public String type;
    public String solve;
    public String last;

    public Firebase_QBOXShortword() {

    }
    public Firebase_QBOXShortword(String question, String diff, String answer, String type, String solve, String last) {
        this.question = question;
        this.diff = diff;
        this.answer = answer;
        this.type = type;
        this.solve = solve;
        this.last = last;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("question", question);
        result.put("diff", diff);
        result.put("answer", answer);
        result.put("type", type);
        result.put("solve", solve);
        result.put("last", last);
        return result;
    }
}
