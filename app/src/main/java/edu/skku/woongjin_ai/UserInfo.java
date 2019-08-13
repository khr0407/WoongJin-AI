package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    public String id;
    public String name;
    public String pw;
    public String coin;
    public String gender;
    public String birth;
    public UserInfo() {

    }

    public UserInfo(String id, String name, String pw, String coin) { //String gender, String birth) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.coin = coin;
        //this.gender = gender;
        //this.birth = birth;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("pw", pw);
        result.put("coin", coin);
        //result.put("gender", gender);
        //result.put("birth", birth);
        return result;
    }
}
