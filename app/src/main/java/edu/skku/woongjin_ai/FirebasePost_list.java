package edu.skku.woongjin_ai;
import java.util.HashMap;
import java.util.Map;

public class FirebasePost_list {
    public String roomname;
    public FirebasePost_list(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost_list.class)
    }
    public FirebasePost_list(String roomname) {
        this.roomname = roomname;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomname", roomname);
        return result;
    }
}