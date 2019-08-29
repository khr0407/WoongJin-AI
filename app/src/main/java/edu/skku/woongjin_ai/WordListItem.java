package edu.skku.woongjin_ai;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

public class WordListItem{

    private Drawable icon;
    private String word;
    private Drawable learn;
    private Drawable script;

    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

    public Drawable getLearn() {
        return learn;
    }
    public void setLearn(Drawable learn) {
        this.learn = learn;
    }

    public Drawable getScript() {
        return script;
    }
    public void setScript(Drawable script) {
        this.script = script;
    }
}
