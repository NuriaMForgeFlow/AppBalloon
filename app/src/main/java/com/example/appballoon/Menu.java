package com.example.appballoon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import com.itextpdf.text.Image;
import java.io.File;



public class Menu extends AppCompatActivity {
    Button op1,op4,btn_saveall;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        op1 =findViewById(R.id.op1);
        op4=findViewById(R.id.op4);
        btn_saveall=findViewById(R.id.btn_saveall);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");



        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Menu.this,WeatherActivity.class);
                Menu.this.startActivity(intent1);
            }
        });



        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(Menu.this,ViewActivity.class);
                Menu.this.startActivity(intent4);
            }
        });

    }


}
