package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class UserInfo { ;
    public String id;
    public String pw;
    public String name;
    public String nickname;
    public String address;
    public String school;
    public String gender;
    public String birth;
    public String coin;

    public UserInfo() {

    }

    public UserInfo(String id, String pw, String name, String nickname, String address, String school, String gender, String birth, String coin) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.school = school;
        this.gender = gender;
        this.birth = birth;
        this.coin = coin;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("name",name);
        result.put("nickname", nickname);
        result.put("address", address);
        result.put("school", school);
        result.put("gender", gender);
        result.put("birth", birth);
        result.put("coin", coin);
        return result;
    }
}
