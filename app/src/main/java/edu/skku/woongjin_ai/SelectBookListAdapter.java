package edu.skku.woongjin_ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectBookListAdapter extends BaseAdapter {

    private ArrayList<SelectBookListItem> selectBookListItems = new ArrayList<SelectBookListItem>();

    @Override
    public int getCount() {
        return selectBookListItems.size();
    }

    @Override
    public SelectBookListItem getItem(int position) {
        return selectBookListItems.get(position);
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
            convertView = inflater.inflate(R.layout.listviewcustom_selectbooklist, parent, false);
        }

        TextView textViewTitle = (TextView) convertView.findViewById(R.id.titleSelectBookList);

        SelectBookListItem selectBookListItem = getItem(position);

        textViewTitle.setText(selectBookListItem.getTitle());

        return convertView;
    }

    public void addItem(String title) {
        SelectBookListItem selectBookListItem = new SelectBookListItem();
        selectBookListItem.setTitle(title);
        selectBookListItems.add(selectBookListItem);
    }
}
