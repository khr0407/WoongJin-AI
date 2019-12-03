package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BucketListAdapter extends BaseAdapter {

    private ArrayList<BucketListItem> bucketListItems = new ArrayList<BucketListItem>();

    @Override
    public int getCount() {
        return bucketListItems.size();
    }

    @Override
    public BucketListItem getItem(int position) {
        return bucketListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listviewcustom_gamelist, parent, false);
        }

        TextView withwhom=(TextView)convertView.findViewById(R.id.withwhom);
        TextView roomname=(TextView)convertView.findViewById(R.id.gameroomname);
        TextView status=(TextView) convertView.findViewById(R.id.game_status);

        BucketListItem bucketListItem = getItem(position);

        withwhom.setText(bucketListItem.getWithwhom());
        roomname.setText(bucketListItem.getRoomname());
        String state=bucketListItem.getStatus();
        if(state.equals("end")){
            status.setTextColor(Color.parseColor("#696969"));
            status.setText("게임이\n끝났어요!");
        }else if(state.equals("newbomb")){
            status.setTextColor(Color.parseColor("#EB0000"));
            status.setText("문제가\n도착했어요!");
        }else if(state.equals("myturn")){
            status.setTextColor(Color.parseColor("#3399CC"));
            status.setText("내가 문제를\n낼 차례!");
        }else if(state.equals("elseturn")){
            status.setTextColor(Color.parseColor("#FF9900"));
            status.setText("친구가 폭탄\n만드는 중!");
        }

        return convertView;
    }

    public void addItem(String roomname, String withwhom, String status) {
        BucketListItem bucketListItem = new BucketListItem();
        bucketListItem.setRoomname(roomname);
        bucketListItem.setWithwhom(withwhom);
        bucketListItem.setStatus(status);
        bucketListItems.add(bucketListItem);
    }
}
