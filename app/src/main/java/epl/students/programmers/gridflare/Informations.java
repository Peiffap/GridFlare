package epl.students.programmers.gridflare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class Informations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informations);
        setTitle("Scanning");
        getSupportActionBar().setDisplayShowHomeEnabled(true);//Display the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Make it clickable

        TextView txt = findViewById(R.id.textView);
        txt.setText(R.string.ping);
    }

}
