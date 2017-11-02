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

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    private static String TAG = "anu";

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random rand = new Random();
        String word = null;
        if (prefix == null) {
            word = words.get(rand.nextInt(words.size()));

        } else {
            int first = 0;
            int last = words.size() - 1;
            int mid = 0;
            int finalmid = 0;
            while (first <= last) {
                mid = (first + last) / 2;
                String s = words.get(mid);
                int c = s.startsWith(prefix) ? 0 : prefix.compareTo(s);
                if (c > 0) {
                    first = mid + 1;
                } else if (c == 0) {
                    finalmid = mid;
                    break;
                } else {
                    last = mid - 1;
                }
            }
            if (finalmid != 0)
                word = words.get(finalmid);
            else
                word = null;

        }


        return word;
    }

    @Override
    public String getGoodWordStartingWith(String prefix,Boolean userturn) {
        Random rand = new Random();
        ArrayList<Integer> midlist = new ArrayList<>();
        String word = null;
        if (prefix == null) {
            word = words.get(rand.nextInt(words.size()));

        } else {
            int first = 0;
            int last = words.size() - 1;
            int mid = 0;
            int finalmid = 0;
            while (first <= last) {
                mid = (first + last) / 2;
                String s = words.get(mid);
                int c = s.startsWith(prefix) ? 0 : prefix.compareTo(s);
                if (c > 0) {
                    first = mid + 1;
                } else if (c == 0) {
                    midlist.add(mid);
                } else {
                    last = mid - 1;
                }
            }
        }
            Log.v(TAG, words.get(midlist.get(1)));
        for (int i = 0; i < midlist.size(); i++) {
            String mWord = words.get(midlist.get(i));
            if(userturn){

                if(mWord.length()%2!=0){
                    word=mWord;
                    break;
                }
            }
            else{
                if(mWord.length()%2==0){
                    word=mWord;
                    break;
                }
            }

            }


            return word;

    }
}



