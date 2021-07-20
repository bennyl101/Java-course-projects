package edu.uci.eecs40;

import java.util.*;
import java.io.*;


public class Board {
    public String[] board;
     static String[] InitBoard(int x, int y) {
        String[] gameboard;
        String[] boardopcodesx;
        String[] boardopcodesy;
        gameboard = new String[8000];

        //copying the string array from main into strings for both warriors
        boardopcodesx = Main.opcodesx;
        boardopcodesy = Main.opcodesy;

        int i, k;
        int cnt = 1;

         //random number generator used to put the string based commands into a random location in the array every time program is started
        for( int p = 0; p < 8000; p ++){
            gameboard[p] = "DAT" + ' ' + 0;
        }
         for (i = x; i < 8100; i++) {
            if(cnt<boardopcodesx.length) {
                gameboard[Warrior.true_location(i)] = boardopcodesx[cnt];
            }
            cnt++;
        }

         cnt = 1;
        for (k = y; k < 8100; k++) {
            if(cnt < boardopcodesy.length) {
                gameboard[Warrior.true_location(k)] = boardopcodesy[cnt];
            }
            cnt++;
        }
    return gameboard;
    }


}
