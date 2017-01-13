package igear.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import igear.example.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testButton(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("igear.example", "data from first activity");
        startActivity(intent);
    }



}
