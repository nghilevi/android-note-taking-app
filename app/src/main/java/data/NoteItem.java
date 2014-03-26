package data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Le on 3/25/14.
 *  This java class is to model a single instance of a note
 */
public class NoteItem {
    private String key;
    private String text;

    public String getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static NoteItem getNew(){
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);

        String pattern ="yy-MM-dd HH:mm:ss Z";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String key=formatter.format(new Date());

        NoteItem note=new NoteItem();
        note.setKey(key);
        note.setText("");
        return note;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
