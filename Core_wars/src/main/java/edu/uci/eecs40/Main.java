package edu.uci.eecs40;

import java.io.*;
import javax.swing.*;
import java.util.*;

public class Main {
    static String[] opcodesx;
    static String[] opcodesy;
    private static int randnum1 = randnumgenerator(0, 8000);
    private static int randnum2 = randnumgenerator(0, 8000);

    private static File[] openFile() {
        JFileChooser fileWindow = new JFileChooser();
        fileWindow.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileWindow.setMultiSelectionEnabled(true);
        fileWindow.showOpenDialog(null);
        return fileWindow.getSelectedFiles();
    }

    private static void printFileContents(File fileIN) {
        BufferedReader reader;
        BufferedReader commandreader;
        try {
            reader = new BufferedReader(new FileReader(fileIN.getAbsolutePath()));
            commandreader = new BufferedReader(new FileReader(fileIN));
            String stringLine;
            List<String> commandsx = new ArrayList<>();
            List<String> commandsy = new ArrayList<>();
            String s;
            //reading commands from text file into a list of strings
            while ((s = commandreader.readLine()) != null) {
                if (fileIN.getName().equals("program_x.txt")) {
                    commandsx.add(s);
                }
                if (fileIN.getName().equals("program_y.txt")) {
                    commandsy.add(s);
                }
            }
            //converting the list of strings into an array of strings
            if (fileIN.getName().equals("program_x.txt")) {
                opcodesx = commandsx.toArray(new String[]{});
            }
            if (fileIN.getName().equals("program_y.txt")) {
                opcodesy = commandsy.toArray(new String[]{});
            }/*
            while (reader.ready()) {
                stringLine = reader.readLine();
                System.out.println
                        (stringLine);
            }
            */
            reader.close();
        } catch (IOException err) {
            System.out.println("Couldn't open file.\n Reason being" + err.getMessage());
            err.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("PLEASE SELECT BOTH FILES AND CLICK OK");
        File[] warriors = openFile();
        printFileContents(warriors[0]);
        printFileContents(warriors[1]);

        Board game_board = new Board();
        game_board.board = Board.InitBoard(randnum1, randnum2).clone();


        Warrior warrior_x = new Warrior();
        Warrior warrior_y = new Warrior();

        warrior_x.location = randnum1;
        warrior_y.location = randnum2;

        warrior_x.board = game_board.board.clone();
        warrior_y.board = game_board.board.clone();

        warrior_x.main_warrior = warrior_x;
        warrior_y.main_warrior = warrior_y;
        warrior_x.queue.add(warrior_x);
        warrior_y.queue.add(warrior_y);

        int i = 0;
        int draw = 5;
        Warrior current_warrior;
        String[][] movelog = new String[2][11];

        String A;
        String B;
        int x_location;
        int y_location;
        while (i < 8000) {

            current_warrior = warrior_x.queue.firstElement();
            A = String.valueOf(game_board.board[current_warrior.location]);
            x_location = current_warrior.location;
            current_warrior.read(game_board.board[current_warrior.location], game_board.board);
            warrior_y.board = current_warrior.board.clone();
            if (current_warrior.death == 1) {
                System.out.println("Program Y wins!");
                draw = 0;
                break;
            }
            game_board.board = current_warrior.board.clone();

            current_warrior = warrior_y.queue.firstElement();
            B = String.valueOf(game_board.board[current_warrior.location]);
            y_location = current_warrior.location;
            current_warrior.read(game_board.board[current_warrior.location], game_board.board);
            warrior_x.board = current_warrior.board.clone();
            if (current_warrior.death == 1) {
                System.out.println("Program X wins!");
                draw = 0;
                break;
            }
            game_board.board = current_warrior.board.clone();
            i++;

            movelog = PrintMoves(LoadMoves(A, B, movelog, x_location, y_location));
            System.out.println("******Program X******||********Program Y********");
        }
        if (draw == 5) {
            System.out.println("DRAW! Neither Program wins!");
        }
    }

    private static int randnumgenerator(int min, int max) {
        Random num = new Random();
        return num.nextInt((max - min) + 1) + min;
    }

    private static String[][] PrintMoves(String[][] movelog) {

        for (int x = 0; x < 10; x++) {
            System.out.print(movelog[0][x] + "        ");
            System.out.print(movelog[1][x] + " \n");
        }
        return movelog;
    }

    private static String[][] LoadMoves(String x, String y, String[][] movelog, int x_location, int y_location) {
        String[][] temp_movelog = new String[2][11];
        for (int j = 0; j < 10; j++) {
            temp_movelog[0][j] = movelog[0][j + 1];
            temp_movelog[1][j] = movelog[1][j + 1];
        }
        String[] A = x.split(" ", 0);
        String[] B = y.split(" ", 0);
        if(A.length == 2) {
            temp_movelog[0][10] = x_location + ": " + A[0] + "       " + A[1];
        }else{
            temp_movelog[0][10] = x_location + ": " + A[0] + "  " + A[1] + "   " + A[2];
        }
        if(B.length == 2){
            temp_movelog[1][10] = y_location + ": " + B[0] + "       " + B[1];

        }else{
            temp_movelog[1][10] = y_location + ": " + B[0] + "  " + B[1] + "   " + B[2];

        }
        return temp_movelog;
    }
}










