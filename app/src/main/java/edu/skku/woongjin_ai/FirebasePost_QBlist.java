package edu.skku.woongjin_ai;
import java.util.HashMap;
import java.util.Map;

public class FirebasePost_QBlist {
    public String roomname;
    public String user1;
    public String user2;
    public String user3;
    public String user4;
    public String user5;
    public String bucketcnt;
    public String script;
    public String state;

    public FirebasePost_QBlist(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost_QBlist.class)
    }
    public FirebasePost_QBlist(String roomname, String user1, String user2, String user3, String user4, String user5, String bucketcnt, String script, String state) {
        this.roomname = roomname;
        this.user1 = user1;
        this.user2 = user2;
        this.user3 = user3;
        this.user4 = user4;
        this.user5 = user5;
        this.bucketcnt = bucketcnt;
        this.script = script;
        this.state = state;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomname", roomname);
        result.put("user1", user1);
        result.put("user2", user2);
        result.put("user3", user3);
        result.put("user4", user4);
        result.put("user5", user5);
        result.put("bucketcnt", bucketcnt);
        result.put("script", script);
        result.put("state", state);
        return result;
    }
}