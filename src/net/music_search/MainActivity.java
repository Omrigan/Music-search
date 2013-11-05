package net.music_search;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
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
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    Toast toast;
    List<List<String>> list;
    MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secound);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.places_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                list = readFileSD(position);
                search("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        list = readFileSD(0);
        search("");
        spinner = (Spinner) findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(this,R.array.atr_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        EditText txt = (EditText) findViewById(R.id.editText);
        txt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        ListView listV = (ListView) findViewById(R.id.listView);
            listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        });


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
        }
        return true;
    }
 public void search(String s){

        s = s.toLowerCase();
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        int LocId = (int) spin.getSelectedItemId();
        spin = (Spinner) findViewById(R.id.spinner2);
        int MetaId = (int) spin.getSelectedItemId();

        List<String> result = new ArrayList<String>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).get(MetaId+1).toLowerCase().contains(s)){
                result.add(list.get(i).get(1) + "# " +  list.get(i).get(2) + " Автор: " + list.get(i).get(3));
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, result);
        ListView lv = (ListView)  findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }
 public List<List<String>> readFileSD(int type) {
     List<List<String>> file = new ArrayList<List<String>>();
       if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            return getErrorList("SD-карта не доступна: " + Environment.getExternalStorageState());
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/MusicSearcher/db" + Integer.toString(type) + ".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdPath));
            String str = "";
            int i=0;
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d("My_logs ",str);
                List<String>  myList = new ArrayList<String>(Arrays.asList(str.split(";")));
                myList.add(0,Integer.toString(i));
                file.add(myList);

                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return getErrorList("Файл не найден: " +  e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return getErrorList("Ошибка файловой системы, скорее всего файл не найден: " +  e.getLocalizedMessage());
        }
     catch (Exception e){
         e.printStackTrace();
         return getErrorList("Непонятное исключение: " +  e.getLocalizedMessage());
     }

     return file;
    }
 public List<List<String>> getErrorList(String s){
     toast = Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT);
     toast.show();
     List<List<String>> file = new ArrayList<List<String>>();
     List<String> myList = new ArrayList<String>();
     myList.add("0");
     myList.add("Name");
     myList.add("Avtor");
     myList.add("Text");
     file.add(myList);
     return file;
 }

}
