package net.music_search;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class MainActivity extends Activity {

    Toast toast;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secound);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.places_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner = (Spinner) findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(this,R.array.atr_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        EditText txt = (EditText) findViewById(R.id.editText);
        txt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String str = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });



    }
 public String[][] readFileSD() {
        String[][] file = new String[0][];
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            toast = Toast.makeText(getApplicationContext(),"SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_SHORT);
            toast.show();

            return file;
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/MusicSearcher/db.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdPath));
            String str = "";
            int i=0;
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d("My_logs ",str);
                file[i] = str.split(";");
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
     return file;
    }
}
