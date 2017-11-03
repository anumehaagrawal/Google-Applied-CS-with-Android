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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class TrieNode {

    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode temppoint=this;
        for(int i=0;i<s.length();i++) {
            char letter = s.charAt(i);
            if (temppoint.children.containsKey(letter)) {
                temppoint = temppoint.children.get(letter);
            } else {
                TrieNode temp = new TrieNode();
                temppoint.children.put(letter, temp);
                temppoint = temp;
            }

        }
        temppoint.isWord=true;
    }

    public boolean isWord(String s) {
        TrieNode temppoint = this;
        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            if (temppoint.children.containsKey(letter)) {
                temppoint = temppoint.children.get(letter);
            }
        }
        if (temppoint.isWord == true) {
            return true;
        }
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode temppoint=this;

        for(int i=0;i<s.length();i++){
            char letter = s.charAt(i);
            if (temppoint.children.containsKey(letter)) {
                temppoint = temppoint.children.get(letter);
            }
            else{
                return null;
            }
        }
        if(temppoint.isWord==true){
            return s;
        }
        String str=s;
        while (!temppoint.isWord){
            ArrayList<Character> keysarray = new ArrayList<>(temppoint.children.keySet());
            Random rand= new Random();
            Character c = keysarray.get(rand.nextInt(keysarray.size()));
            str=str.concat(Character.toString(c));
            temppoint=temppoint.children.get(c);
        }
        return str;
    }


    public String getGoodWordStartingWith(String s,Boolean firstTurn) {

       return null;

}}