package edu.skku.woongjin_ai;
import java.util.HashMap;
import java.util.Map;

public class FirebasePost_list {
    public String roomname;
    public String user1;
    public String user2;
    public FirebasePost_list(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost_list.class)
    }
    public FirebasePost_list(String roomname, String user1, String user2) {
        this.roomname = roomname;
        this.user1 = user1;
        this.user2 = user2;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomname", roomname);
        result.put("user1", user1);
        result.put("user2", user2);
        return result;
    }
}