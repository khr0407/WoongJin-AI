package edu.skku.woongjin_ai;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    public String id;
    public String name;
    public String pw;
    public int coin;
    public UserInfo() {

    }

    public UserInfo(String id, String name, String pw, int coin) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.coin = coin;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("pw", pw);
        result.put("coin", coin);
        return result;
    }
}
