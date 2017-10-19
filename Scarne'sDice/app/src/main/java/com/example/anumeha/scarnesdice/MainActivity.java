package com.example.anumeha.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public int useroverall,userturn,compoverall,compturn;
    ImageView dice;
    Button roll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rolldice();
            }
        });

    }
    public void Rolldice(){
        int[] images={R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
        Random rand =new Random();
        int roll=rand.nextInt(7-1)+1;
        dice=(ImageView)findViewById(R.id.dice);
        dice.setBackgroundResource(images[roll-1]);

    }
}
