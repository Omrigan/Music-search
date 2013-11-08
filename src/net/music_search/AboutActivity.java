package net.music_search;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
    public void onClick(View view){

        SharedPreferences mSettings = getSharedPreferences("MSsettings", Context.MODE_PRIVATE);
        EditText txt = (EditText) findViewById(R.id.editTextOldPass);
        String str1 = myUtils.md5(txt.getText().toString());
        String str2 = mSettings.getString("password", "password");
        if(str1.equals(str2)){
            txt = (EditText) findViewById(R.id.editTextNewPass);
            str1 = myUtils.md5(txt.getText().toString());
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("password", str1);
            editor.apply();
            Toast toast = Toast.makeText(getApplicationContext(),"Пароль успешно изменен!", Toast.LENGTH_SHORT);
            toast.show();

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Неправильно введен старый пароль!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}