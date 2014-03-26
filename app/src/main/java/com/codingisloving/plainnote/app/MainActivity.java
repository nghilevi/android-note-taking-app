package com.codingisloving.plainnote.app;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import data.NoteItem;
import data.NotesDataSource;


public class MainActivity extends ListActivity {
    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    public static final int MENU_DELETE_ID = 1002; //ID of my context menu item
    /*There are going to be two methods. One to construct the context menu and one to handle the event
    that's fired when the user selects a menu item. You'll be able to detect which item in the list the
    user has selected when the context menu is constructed but not when the menu item is selected.
    So you have to save it persistently between the two operations. And that's what this new field is for.*/
    public static int currentNoteId;
    private NotesDataSource datasource;
    List<NoteItem> notesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*when the user touches and holds on an item for a moment, then releases it, it'll be seen as
        that context menu request. And it'll result in calling a method called on create context menu.
        Each time the method is called You can either load a menu that you define in XML, or you can do it all in Java code.*/
        registerForContextMenu(getListView());

        datasource = new NotesDataSource(this);

        refreshDisplay();
    }

    private void refreshDisplay() {
        //The array adapter class will be used to wrap around the data, and feed that data to the list display
        notesList = datasource.findAll();
        ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(this,R.layout.list_item_layout,notesList);
        setListAdapter(adapter); //Neu implement FragmentActivitt (de cho xuat hien menu on action bar, thi setListAdapter phai lam sao?
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //When the user selects an item in the Action Bar or in the Options menu, which we're not using right now,
    // it triggers a method called on options item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_create) {
            createNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        NoteItem note =NoteItem.getNew();
        Intent intent=new Intent(this,NoteEditorActivity.class);
        intent.putExtra("key",note.getKey());
        intent.putExtra("text",note.getText());
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
        //When you start the activity, you pass in the request code. When the activity is finished,
        // it passes back the request code. And when the first activity receives the request code it
        // can figure out which screen it came from. The request code integer can be anything
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        NoteItem note =notesList.get(position);
        Intent intent=new Intent(this,NoteEditorActivity.class);
        intent.putExtra("key",note.getKey());
        intent.putExtra("text",note.getText());
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==EDITOR_ACTIVITY_REQUEST && resultCode==RESULT_OK){
            NoteItem note = new NoteItem();
            note.setKey(data.getStringExtra("key"));
            note.setText(data.getStringExtra("text"));
            datasource.update(note);
            refreshDisplay();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // the menu info object has been cast as info, and that object can tell me which item of the list I'm talking about.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentNoteId=(int) info.id;
        menu.add(0, MENU_DELETE_ID,0,"Delete this item");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId()==MENU_DELETE_ID){
            NoteItem note=notesList.get(currentNoteId);
            datasource.remove(note);
            refreshDisplay();
        }
        return super.onContextItemSelected(item);
    }
}
