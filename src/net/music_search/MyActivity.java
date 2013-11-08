package net.music_search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyActivity extends Activity {
    SharedPreferences mSettings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSettings = getSharedPreferences("MSsettings", Context.MODE_PRIVATE);
        if (!mSettings.contains("password")) {

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("password", myUtils.md5("password"));
            editor.apply();
        }
    }
    public void onClick(View view){
        EditText txt = (EditText) findViewById(R.id.editText);
        String str1 = myUtils.md5(txt.getText().toString());
        String str2 = mSettings.getString("password", "password");
      if(str1.equals(str2)){

          Intent intent = new Intent(this, MainActivity.class);
          startActivity(intent);
          this.finish();

      }
        else{
           Toast toast = Toast.makeText(getApplicationContext(), "Неправильный пароль! " + mSettings.getString("password", "password"), Toast.LENGTH_SHORT);
            toast.show();

        }
    }
}
