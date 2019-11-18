package edu.skku.woongjin_ai;
import java.util.HashMap;
import java.util.Map;

public class FirebasePost_list {
    public String roomname;
    public String user1;
    public String user2;
    public String user3;
    public String user4;
    public String user5;

    public String script;
    public String state;
    public FirebasePost_list(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost_list.class)
    }

    public FirebasePost_list(String roomname, String user1, String user2, String user3, String user4, String user5, String script, String state) {
        this.roomname = roomname;
        this.user1 = user1;
        this.user2 = user2;
        this.user3 = user3;
        this.user4 = user4;
        this.user5 = user5;
        this.script = script;
        this.state = state;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomname", roomname);
        result.put("user1", user1);
        result.put("user2", user2);
        result.put("user1", user3);
        result.put("user2", user4);
        result.put("user2", user5);
        result.put("script", script);
        result.put("state", state);
        return result;
    }
}