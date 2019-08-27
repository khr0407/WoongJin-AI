package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    public String id;
    public String name;
    public String pw;
    public String nickname;
    public String school;
    public String gender;
    public String grade;
    public String coin;

    public UserInfo(){

    }

    public UserInfo(String id, String pw, String name, String nickname, String school, String gender, String grade, String coin) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.name = name;
        this.nickname = nickname;
        this.school = school;
        this.gender = gender;
        this.grade = grade;
        this.coin = coin;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("name",name);
        result.put("nickname", nickname);
        //result.put("address", address);
        result.put("school", school);
        result.put("gender", gender);
        result.put("grade", grade);
        result.put("coin", coin);
        return result;
    }
}
