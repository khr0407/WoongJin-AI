package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;
/*
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
*/
public class FirebasePost_script {
    public String id;
    public String name;
    public Long age;
    public String gender;
    public FirebasePost_script(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public FirebasePost_script(String id, String name, Long age, String gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("age", age);
        result.put("gender", gender);
        return result;
    }
}



