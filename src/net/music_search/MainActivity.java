package net.music_search;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    final String LOG_TAG = "myLogs";

    Toast toast;

    MediaPlayer mediaPlayer = new MediaPlayer();
    SQLiteDatabase db;
    AdapterView.OnItemClickListener listMusListener;
    AdapterView.OnItemClickListener listIspListener;

    TextWatcher txtListener;
    int tek_b;     //Текущая эстрада
    String tek_author;//Текущий автор
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.secound);
        init();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array._b, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                  ListView isplst = (ListView) findViewById(R.id.listView1); //Лист исполнителей
                  isplst.setAdapter(searchInAuthors("",position));
                tek_b = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        db = openDB();


        ListView isplst = (ListView) findViewById(R.id.listView1); //Лист исполнителей
        isplst.setAdapter(searchInAuthors("", 0));
        isplst.setOnItemClickListener(listIspListener);


        EditText txt = (EditText) findViewById(R.id.editText);
        txt.addTextChangedListener(txtListener);

        ListView listV = (ListView) findViewById(R.id.listView);
        listV.setOnItemClickListener(listMusListener);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stop:
                mediaPlayer.stop();
                break;
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.button:
                EditText txt = (EditText) findViewById(R.id.editText);
                ListView isplst = (ListView) findViewById(R.id.listView1); //Лист исполнителей
                isplst.setAdapter(searchInAuthors(txt.getText().toString(), tek_b));
                break;
        }
    }
    public ArrayAdapter<String> searchInSongs(String s, String author){
        s = s.toLowerCase();
        List<String> result = new ArrayList<String>();

      Cursor authorsCursor = db.rawQuery("SELECT name FROM songs JOIN authors ON author=authors._id WHERE (author_name='"+ author + "') AND ((name LIKE '%" + s + "%') OR (name LIKE '%" + s + "%'))", null);
  authorsCursor.moveToFirst();
        if(!authorsCursor.isAfterLast()) {
            do
            {
                result.add(authorsCursor.getString(0));
            }
            while (authorsCursor.moveToNext());
        }
        authorsCursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, result);
        return adapter;
    }


    public ArrayAdapter<String> searchInAuthors(String s, int num_b){ //_b - суффикс для обозначения эстрады
        s = s.toLowerCase();
        List<String> result = new ArrayList<String>();

        Cursor authorsCursor = db.rawQuery("SELECT author_name FROM authors WHERE _b=" + Integer.toString(num_b)+" AND (author_name LIKE '%" + s + "%')", null);
        authorsCursor.moveToFirst();
        if(!authorsCursor.isAfterLast()) {
            do
            {
                result.add(authorsCursor.getString(0));
            }
            while (authorsCursor.moveToNext());
        }
        authorsCursor.close();
        return  new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, result);
    }
    public SQLiteDatabase openDB(){
        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(getApplicationContext(), "music.db");
        return dbOpenHelper.getDb();
    }
    public List<String> getErrorList(){
        List<String> myList = new ArrayList<String>();
        myList.add("0");
        myList.add("Name");
        myList.add("Avtor");
        myList.add("Text");
        return myList;
    }
    public void init(){
        listMusListener = new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parentView, View childView,int position, long id)
            {
                try{
                    mediaPlayer.stop();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/MusicSearcher/" + Integer.toString(position) + ".mp3");
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getApplicationContext(),  "Файл не найден: " +  e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    toast = Toast.makeText(getApplicationContext(), "Ошибка ввода/выводв: " +  e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch (Exception e){
                    toast = Toast.makeText(getApplicationContext(), "Непонятное исключение, скорее всего файл не найден: " +  Environment.getExternalStorageDirectory().getPath() + "/MusicSearcher/" + Integer.toString(position) + ".mp3", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

        };
        listIspListener = new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parentView, View childView,int position, long id)
            {
                ListView isplst = (ListView) findViewById(R.id.listView1); //Лист исполнителей
                tek_author = (String) isplst.getItemAtPosition(position);
                ListView muslst = (ListView) findViewById(R.id.listView); //Лист музыки
                muslst.setAdapter(searchInSongs("", tek_author));
            }

        };
        txtListener = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                ListView muslst = (ListView) findViewById(R.id.listView);
                muslst.setAdapter(searchInSongs(s.toString(),  tek_author));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

    }
}
