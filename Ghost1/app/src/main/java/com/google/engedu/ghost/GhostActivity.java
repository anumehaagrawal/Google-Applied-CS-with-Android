/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    public boolean userTurn = false;
    private Random random = new Random();
    Button challengeb,resetb;
    InputStream inputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();


        try {
            inputStream = assetManager.open("words.txt");
            dictionary=new SimpleDictionary(inputStream);
        }
        catch (IOException e){}

        onStart(null);
        challengeb=(Button)findViewById(R.id.challenge);
        resetb=(Button)findViewById(R.id.reset);
        challengeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView ghostText=(TextView)findViewById(R.id.ghostText);
                TextView label=(TextView)findViewById(R.id.gameStatus);

                String ghostword=ghostText.getText().toString();
                if(ghostword.length()>=4 && dictionary.isWord(ghostword)){
                    label.setText("User Won");

                }
                String word=dictionary.getAnyWordStartingWith(ghostword);
                if(word!=null){
                    label.setText("Computer won"+ " "+word);
                }
                else{
                    label.setText("User won");
                }

            }
        });
        resetb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView ghostText=(TextView)findViewById(R.id.ghostText);
                ghostText.setText(" ");

                onStart(null);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }



    private void computerTurn() {
        TextView ghostText = (TextView) findViewById(R.id.ghostText);
        String ghostword = ghostText.getText().toString();
        TextView label = (TextView) findViewById(R.id.gameStatus);

        if (ghostword.length() >= 4 && dictionary.isWord(ghostword)) {

            Toast.makeText(this, ghostword, Toast.LENGTH_LONG).show();
            label.setText("Computer Won");
            return;
        }

        String word = dictionary.getAnyWordStartingWith(ghostword);
        if (word == null) {
            Toast.makeText(this, "Cheaterr", Toast.LENGTH_SHORT).show();
            label.setText("Computer Won");
            return;
        }

        ghostword = ghostword + word.charAt(ghostword.length());

        ghostText.setText(ghostword);
        if (dictionary.isWord(ghostword)) {
            label.setText("User won");
        } else {


            userTurn = true;
            label.setText(USER_TURN);
        }
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        TextView ghostText=(TextView) findViewById(R.id.ghostText);
        String wordghost=ghostText.getText().toString();
        if(keyCode>KeyEvent.KEYCODE_A && keyCode <KeyEvent.KEYCODE_Z){
            char letter=(char)(keyCode-KeyEvent.KEYCODE_A+'a');
            wordghost=wordghost.concat(Character.toString(letter));
        }
        ghostText.setText(wordghost);
        computerTurn();
        return super.onKeyUp(keyCode, event);
    }
}
