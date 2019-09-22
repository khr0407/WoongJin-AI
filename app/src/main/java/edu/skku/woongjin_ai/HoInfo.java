package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class HoInfo {
    public int correct;
    public float level;
    public int like;
    public int cnt;

    public HoInfo() {

    }

    public HoInfo(int correct, float level, int like, int cnt) {
        this.correct = correct;
        this.level = level;
        this.like = like;
        this.cnt = cnt;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("correct", correct);
        result.put("level", level);
        result.put("like", like);
        result.put("cnt", cnt);
        return result;
    }
}
