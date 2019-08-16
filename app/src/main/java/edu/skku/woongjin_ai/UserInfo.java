package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    public String id;
    public String name;
    public String pw;
    public String coin;
    public String birth;
    public String gender;
    public String school;
    public String nickname;

    public UserInfo() {

    }

    public UserInfo(String id, String name, String pw, String coin, String birth, String gender, String school, String nickname) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.coin = coin;
        this.birth = birth;
        this.gender = gender;
        this.school = school;
        this.nickname = nickname;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("pw", pw);
        result.put("coin", coin);
        result.put("birth", birth);
        result.put("gender", gender);
        result.put("school", school);
        result.put("nickname", nickname);
        return result;
    }
}
