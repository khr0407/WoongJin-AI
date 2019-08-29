package edu.skku.woongjin_ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordListAdapter extends BaseAdapter {

    private ArrayList<WordListItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }
    @Override
    public WordListItem getItem(int position) {
        return mItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_wordlist_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon) ;
        TextView word = (TextView) convertView.findViewById(R.id.word) ;
        ImageButton learn = (ImageButton) convertView.findViewById(R.id.learn) ;
        ImageButton script = (ImageButton) convertView.findViewById(R.id.script) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        WordListItem myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        icon.setImageDrawable(myItem.getIcon());
        word.setText(myItem.getWord());
        learn.setImageDrawable(myItem.getLearn());
        script.setImageDrawable(myItem.getScript());
        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(Drawable icon, String word, Drawable learn, Drawable script) {

        WordListItem mItem = new WordListItem();
        /* MyItem에 아이템을 setting한다. */
        mItem.setIcon(icon);
        mItem.setWord(word);
        mItem.setLearn(learn);
        mItem.setScript(script);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }
}
