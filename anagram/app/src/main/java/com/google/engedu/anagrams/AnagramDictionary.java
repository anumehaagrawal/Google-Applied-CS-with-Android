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

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import android.view.View;
import java.util.Iterator;
import android.util.Log;
import android.widget.EditText;

import java.util.Random;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private int wordLength=DEFAULT_WORD_LENGTH;
    private static final int MAX_WORD_LENGTH = 7;
    EditText edit;
    private static final String TAG="Correct";
    Random randomizer = new Random();
    private Random random = new Random();
    public ArrayList<String> wordlist=new ArrayList<String>();
    public ArrayList<String> startwordlist=new ArrayList<String>();
    public HashSet<String> wordSet=new HashSet<String>();
    public HashMap<String,ArrayList<String>> lettersToWord;
    public HashMap<Integer,ArrayList<String>> lengthlist= new HashMap<Integer,ArrayList<String>>();
    private String key;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        lettersToWord = new HashMap();
        while((line = in.readLine()) != null) {
            String word = line.trim();

            int length;
            length=word.length();
            if(!lengthlist.containsKey(length)){
                ArrayList<String> wordlen=new ArrayList<String>();
                wordlen.add(word);
                lengthlist.put(length,wordlen);
            }
            else{
                lengthlist.get(length).add(word);


            }
            wordSet.add(word);
            wordlist.add(word);

            key = sortLetters(word);
            if(!lettersToWord.containsKey(key)){
                ArrayList al = new ArrayList();
                al.add(word);
                lettersToWord.put(key,al);
            }
            else{
                lettersToWord.get(key).add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word)){
            if(word.contains(base)){
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return false;
        }

    }

    public ArrayList<String> getAnagrams(String targetWord) {
        String targetSort;
        targetSort=sortLetters(targetWord);
        ArrayList<String> result = new ArrayList<String>();
        for(String word:wordlist){
            String check=sortLetters(word);
            if(check.equals(targetSort)){
                result.add(word);
                Log.v(TAG,"matched");
            }
        }

        return result;
    }
    public String sortLetters(String word){
        char[] neword=word.toCharArray();
        Arrays.sort(neword);
        String sorted=new String((neword));
        return sorted;

    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> extralist=new ArrayList<String>();
        for(char alphabet = 'a'; alphabet <='z'; alphabet++ ){
            String newword;
            newword=word+alphabet;
            extralist=getAnagrams(newword);
            for(String item : extralist){
                result.add(item);
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {


        while (true) {

            ArrayList<String> tempList = lengthlist.get(wordLength);

            Random random = new Random();
            int num = random.nextInt(tempList.size());

            String randomWord = tempList.get(num);
            ArrayList<String> arrayList = getAnagramsWithOneMoreLetter(randomWord);

            if ((randomWord.length() == wordLength) && arrayList.size() > MIN_NUM_ANAGRAMS) {

                if (wordLength < MAX_WORD_LENGTH) wordLength++;
                return randomWord;
            }
        }
}}
