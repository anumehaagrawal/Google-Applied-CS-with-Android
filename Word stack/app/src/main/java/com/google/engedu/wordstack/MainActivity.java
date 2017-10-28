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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private int word1length,word2length,stacksize;
    Queue<Character> queue1= new LinkedList<Character>();
    Stack<LetterTile> placedTiles= new Stack<>();
    Queue<Character> queue2=new LinkedList<>();
    Queue<Character> finalqueue=new LinkedList<>();
    private StackedLayout stackedLayout;
    private String word1, word2;
    TextView text;
    LinearLayout mword1,mword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if (word.length()==WORD_LENGTH){
                    words.add(word);
                }

            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);

                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
        stackedLayout.clear();
        mword1=(LinearLayout)findViewById(R.id.word1);
        mword2=(LinearLayout) findViewById(R.id.word2);
        mword1.removeAllViewsInLayout();
        mword2.removeAllViewsInLayout();


        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");

        word1=words.get(random.nextInt(words.size()));
        word2=words.get(random.nextInt(words.size()));

        while (word2.equals(word1)){
            word2=words.get(random.nextInt(words.size()));
        }

        word1length=word1.length();

        for(int i=0;i<word1length;i++){
            queue1.add(word1.charAt(i));
        }

        word2length=word2.length();

        for(int i=0;i<word2length;i++){
            queue2.add(word2.charAt(i));
        }

        for(int i=0;i<word1length+word2length;i++){
            int r=random.nextInt(2)+1;
            if(r==1 && queue1.size()>0){
                Character c=queue1.remove();
                finalqueue.add(c);
            }
            else if(r==1 && queue1.size()==0){
                Character c=queue2.remove();
                finalqueue.add(c);
            }
            else if(r==2 && queue2.size()>0){
                Character c=queue2.remove();
                finalqueue.add(c);
            }
            else if(r==2 && queue2.size()==0){
                Character c=queue1.remove();
                finalqueue.add(c);
            }

        }
        String scrambledword="";
        while(finalqueue.size()>0){
            Character letter=finalqueue.remove();
            scrambledword=scrambledword+letter;
        }
        text=(TextView)findViewById(R.id.message_box);
        text.setText(scrambledword);
        for(int j=scrambledword.length()-1;j>=0;j--){
            LetterTile tile=new LetterTile(this,scrambledword.charAt(j));
            stackedLayout.push(tile);
        }
        stacksize=word1length+word2length;

        return true;
    }

    public boolean onUndo(View view) {
        if(placedTiles.size()>0) {
            LetterTile undoletter = placedTiles.pop();
            undoletter.moveToViewGroup(stackedLayout);
        }
        else{
            Toast toast=Toast.makeText(this,"cant undo",Toast.LENGTH_SHORT);
            toast.show();
        }

        return true;
    }
}
