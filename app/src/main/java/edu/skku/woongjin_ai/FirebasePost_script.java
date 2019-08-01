package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost_script {
    public String text;

    public FirebasePost_script(){}

    public FirebasePost_script(String text){
        this.text = text;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        return result;
    }
}
