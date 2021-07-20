package edu.uci.eecs40;

import java.util.*;
import java.lang.*;


public class Warrior {
    private String op;
    public int location;
    private int spl_num = 0;
    public String[] board;
    int death = 0;
    Vector<Warrior> queue = new Vector<>();
    Warrior main_warrior;


    //Scans string and perform the appropriate operation on the board;\
    public void read(String input, String[] new_board) {
        Warrior next_warrior = main_warrior.queue.firstElement();
        board = new_board.clone();
        if (isNullOrEmpty(input)) {
            death = 1;
        } else {
            String[] array = input.split(" ", 0);
            op = array[0];
            int[] flags = give_flags(input);
            int[] num_values = give_values(input, flags);
            switch (op) {
                case "ADD":
                    board = add(num_values, board, flags).clone();
                    location++;
                    break;
                case "SUB":
                    board = sub(num_values, board, flags).clone();
                    location++;
                    break;
                case "JMP":
                    location = jmp(num_values, board, flags);
                    break;
                case "JMZ":
                    location = jmz(num_values, board, flags);
                    break;
                case "DJN":
                    location = djn(num_values, board, flags);
                    break;
                case "CMP":
                    location = cmp(num_values, board, flags);
                    break;
                case "SPL":
                    location = spl(num_values, board, flags);
                    spl_num = 1;
                    break;
                case "MOV":
                    board = mov(num_values, board, flags).clone();
                    location++;
                    break;
                case "DAT":
                    death = dat();
                    break;
                case "null":
                    death = 1;
                    break;
                default:
                    break;
            }
        }
        if (location >= 8000) {
            location = location % 8000;
        }

        if (spl_num == 0) {
            main_warrior.queue.remove(0);
            main_warrior.queue.add(next_warrior);
        } else {
            spl_num = 0;
        }
    }

    //add function: use for ADD operation in MARS
    private String[] add(int[] num_values, String[] board, int[] flags) {
        String[] new_board = board.clone();
        String[] temp_board = board.clone();
        if (flags[1] == 0) {
            if (flags[0] == 2) {
                //A is immediate, B is relative
                String newcommandtemp;
                String newcommand;
                newcommandtemp = temp_board[true_location(location + num_values[1])];
                int[][] values = read_indirect(newcommandtemp);

                String lastcharacter;
                String[] stringsplit = newcommandtemp.split(" ", 0);
                lastcharacter = stringsplit[stringsplit.length - 1];

                int b_arg = values[1][1];
                int new_b_arg = b_arg + num_values[0];
                String newlastcharacter = String.valueOf(new_b_arg);
                if (values[0][0] == 3) {
                    if (values[0][1] == 0) {
                        newcommand = stringsplit[0] + ' ' + newlastcharacter;
                        new_board[true_location(location + num_values[1])] = newcommand;
                    } else {
                        char[] char_array = lastcharacter.toCharArray();
                        newcommand = stringsplit[0] + ' ' + char_array[0] + newlastcharacter;
                        new_board[true_location(location + num_values[1])] = newcommand;
                    }
                } else if (values[0][1] == 3) {
                    newcommand = stringsplit[0] + ' ' + stringsplit[1] + ',' + ' ' + newlastcharacter;
                    new_board[true_location(location + num_values[1])] = newcommand;
                } else if (values[0][1] == 4) {
                    new_board[true_location(location + num_values[1])] = "DAT" + ' ' + newlastcharacter;
                } else {
                    if (values[0][1] == 0) {
                        newcommand = stringsplit[0] + ' ' + stringsplit[1] + ' ' + newlastcharacter;
                        new_board[true_location(location + num_values[1])] = newcommand;
                    } else {
                        char[] char_array = lastcharacter.toCharArray();
                        newcommand = stringsplit[0] + ' ' + stringsplit[1] + char_array[0] + newlastcharacter;
                        new_board[true_location(location + num_values[1])] = newcommand;
                    }
                }
            } else if (flags[0] == 0) {
                //A is relative, B is relative
                //splitting string a numbers down and adding a arg from initial location to a arg of final location
                int[][] a_relative = read_indirect(temp_board[true_location(location + num_values[0])]);
                int[][] b_relative = read_indirect(temp_board[true_location(location + num_values[1])]);

                String new_b = String.valueOf(a_relative[1][1] + b_relative[1][1]);
                String new_a = String.valueOf(a_relative[1][0] + b_relative[1][0]);
                String[] stringsplit = temp_board[true_location(location + num_values[1])].split(" ", 0);
                if (b_relative[0][1] == 3) {
                    String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a);
                    new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_a_arg;
                } else if (b_relative[0][0] == 3) {
                    String final_b_arg = stringsplit[1].replaceAll("[0-9]", "") + (new_b);
                    new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_b_arg;
                } else if (b_relative[0][1] == 4) {
                    new_board[true_location(location + num_values[1])] = "DAT" + ' ' + new_b;
                } else {
                    String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a) + ',';

                    String final_b_arg = stringsplit[2].replaceAll("[0-9]", "") + (new_b);

                    new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_a_arg + ' ' + final_b_arg;
                }
            } else {
                //A is indirect, B is relative
                int[][] indirect_a = read_indirect(temp_board[true_location(location + num_values[0])]);
                int[][] b_relative = read_indirect(temp_board[true_location(location + num_values[1])]);
                int[][] final_a = read_indirect(temp_board[true_location(location + num_values[0] + indirect_a[1][0])]);

                String new_b = String.valueOf(final_a[1][1] + b_relative[1][1]);
                String new_a = String.valueOf(final_a[1][0] + b_relative[1][0]);
                String[] stringsplit = temp_board[true_location(location + num_values[1])].split(" ", 0);
                if (b_relative[0][1] == 3) {
                    String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a);
                    new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_a_arg;
                } else if (b_relative[0][0] == 3) {
                    String final_b_arg = stringsplit[1].replaceAll("[0-9]", "") + (new_b);
                    new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_b_arg;
                } else if (b_relative[0][1] == 4) {
                    new_board[true_location(location + num_values[1])] = "DAT" + ' ' + new_b;
                } else {
                    String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a) + ',';
                    String final_b_arg = stringsplit[2].replaceAll("[0-9]", "") + (new_b);
                    new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_a_arg + ' ' + final_b_arg;
                }
            }
        } else {
            //B is indirect
            int[][] b_indirect = read_indirect(temp_board[true_location(location + num_values[1])]);
            int[][] final_b = read_indirect(temp_board[true_location(location + num_values[1] + b_indirect[1][1])]);
            String[] stringsplit = temp_board[true_location(location + num_values[1] + b_indirect[1][1])].split(" ", 0);

            String new_a;
            String new_b;

            if (flags[0] == 2) {//A is immediate
                new_a = "";
                new_b = String.valueOf(final_b[1][0] + num_values[0]);
            } else if (flags[0] == 0) {//A is relative
                int[][] a_relative = read_indirect(temp_board[true_location(location + num_values[0])]);
                new_a = String.valueOf(final_b[1][0] + a_relative[1][0]);
                new_b = String.valueOf(final_b[1][1] + a_relative[1][1]);
            } else {//A is indirect
                int[][] a_relative = read_indirect(temp_board[true_location(location + num_values[0])]);
                int[][] final_a = read_indirect(temp_board[true_location(location + num_values[0] + a_relative[1][0])]);
                new_a = String.valueOf(final_b[1][0] + final_a[1][0]);
                new_b = String.valueOf(final_b[1][1] + final_a[1][1]);
            }
            if (final_b[0][1] == 3) {
                String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a);
                new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_a_arg;
            } else if (final_b[0][0] == 3) {
                String final_b_arg = stringsplit[1].replaceAll("[0-9]", "") + (new_b);
                new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_b_arg;
            } else if (final_b[0][1] == 4) {
                new_board[true_location(location + num_values[1])] = "DAT" + ' ' + new_b;
            } else {
                String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a) + ',';

                String final_b_arg = stringsplit[2].replaceAll("[0-9]", "") + (new_b);

                new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_a_arg + ' ' + final_b_arg;
            }

        }
        return new_board;
    }

    //sub function: use for SUB operation in MARS
    private String[] sub(int[] num_values, String[] board, int[] flags) {
        String[] new_board = board.clone();
        String[] temp_board = board.clone();

        int[][] b_relative = read_indirect(temp_board[true_location(location + num_values[1])]);
        int[][] b_indirect = read_indirect(temp_board[true_location(location + num_values[1] + b_relative[1][1])]);
        String[] stringsplit;

        int[][] a_relative = read_indirect(temp_board[true_location(location + num_values[0])]);
        int[][] a_indirect = read_indirect(temp_board[true_location(location + num_values[0] + a_relative[1][0])]);


        String new_a;
        String new_b;

        if (flags[1] == 1) {// B is indirect
            stringsplit = temp_board[true_location(location + num_values[1] + b_relative[1][1])].split(" ", 0);
            if (flags[0] == 2) {//A is immediate
                new_a = "";
                new_b = String.valueOf(b_indirect[1][0] - num_values[0]);
            } else if (flags[0] == 0) {//A is relative
                new_a = String.valueOf(b_indirect[1][0] - a_relative[1][0]);
                new_b = String.valueOf(b_indirect[1][1] - a_relative[1][1]);
            } else {//A is indirect
                new_a = String.valueOf(b_indirect[1][0] - a_indirect[1][0]);
                new_b = String.valueOf(b_indirect[1][1] - a_indirect[1][1]);
            }
        } else {//B is relative
            stringsplit = temp_board[true_location(location + num_values[1])].split(" ", 0);
            if (flags[0] == 2) {//A is immediate
                new_a = "";
                new_b = String.valueOf(b_relative[1][0] - num_values[0]);
            } else if (flags[0] == 0) {//A is relative
                new_a = String.valueOf(b_relative[1][0] - a_relative[1][0]);
                new_b = String.valueOf(b_relative[1][1] - a_relative[1][1]);
            } else {//A is indirect
                new_a = String.valueOf(b_relative[1][0] - a_indirect[1][0]);
                new_b = String.valueOf(b_relative[1][1] - a_indirect[1][1]);
            }
        }
        if (b_indirect[0][1] == 3) {
            String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a);
            new_board[true_location(location + num_values[1])] = stringsplit[0] + final_a_arg;
        } else if (b_indirect[0][0] == 3) {
            String final_b_arg = stringsplit[1].replaceAll("[0-9]", "") + (new_b);
            new_board[true_location(location + num_values[1])] = stringsplit[0] + final_b_arg;
        } else if (b_indirect[0][1] == 4) {
            new_board[true_location(location + num_values[1])] = "DAT" + ' ' + new_b;
        } else {
            String final_a_arg = stringsplit[1].replaceAll("[0-9],", "") + (new_a) + ',';

            String final_b_arg = stringsplit[2].replaceAll("[0-9]", "") + (new_b);

            new_board[true_location(location + num_values[1])] = stringsplit[0] + ' ' + final_a_arg + ' ' + final_b_arg;
        }
        return new_board;
    }


    //jmp function: use for JMP operation in MARS
    private int jmp(int[] num_values, String[] board, int[] flags) {
        int new_location;
        if (flags[0] == 1) {//A is indirect
            int[][] new_values = read_indirect(board[true_location(location + num_values[1])]);
            new_location = true_location(location + num_values[0] + new_values[1][0]);
        } else {//A is relative
            new_location = true_location(location + num_values[0]);
        }
        return new_location;
    }

    //jmz function: use for JMZ operation in MARS
    private int jmz(int[] num_values, String[] board, int[] flags) {
        int new_location;
        int b_check = 0;
        switch (flags[1]) {// Sets b_check flag to 1 if B-field pointed by B-argument is 0
            case 0://B is relative
                int[][] check_values = read_indirect(board[true_location(location + num_values[1])]);
                if (check_values[1][1] == 0) {
                    b_check = 1;
                }
                break;
            case 1:// B is indirect
                int[][] first_values = read_indirect(board[true_location(location + num_values[1])]);
                if (first_values[1][1] == 0) {
                    b_check = 1;
                } else {
                    int[][] second_values = read_indirect(board[true_location(location + num_values[1] + first_values[1][1])]);
                    if (second_values[1][1] == 0) {
                        b_check = 1;
                    }
                }
                break;
            case 2://B is immediate
                if (num_values[1] == 0) {
                    b_check = 1;
                }
                break;
            default:
                break;
        }
        if (flags[0] == 1) {//A is indirect
            int[][] new_values = read_indirect(board[true_location(location + num_values[1])]);
            new_location = true_location(location + num_values[0] + new_values[1][0]);
        } else {//A is relative
            new_location = true_location(location + num_values[0]);
        }
        if (b_check == 1) {
            return new_location;
        } else {
            location++;
            return location;
        }
    }

    //djn function: use for DJN operation in MARS
    private int djn(int[] num_values, String[] board, int[] flags) {
        int new_location;
        int b_check = 0;
        String[] new_board = board.clone();
        switch (flags[1]) {// Sets b_check flag to 1 if B-field pointed by B-argument is 0
            case 0://B is relative
                int[][] check_values = read_indirect(board[true_location(location + num_values[1])]);
                if (check_values[1][1] - 1 != 0) {
                    b_check = 1;
                }
                if (check_values[0][1] == 3) {
                    String copy_op = new_board[true_location(location + num_values[1])];
                    new_board[true_location(location + num_values[1])] = copy_op + ", " + (check_values[1][1] - 1);
                    board = new_board.clone();
                } else if (check_values[0][0] == 3) {
                    String copy_op = new_board[true_location(location + num_values[1])];
                    String[] split_op = copy_op.split(" ", 0);
                    String new_string = split_op[1].replaceAll("[0-9]", "") + (check_values[1][1] - 1);
                    copy_op = split_op[0] + ' ' + new_string;
                    new_board[true_location(location + num_values[1])] = copy_op;
                    board = new_board.clone();
                } else {
                    String copy_op = new_board[true_location(location + num_values[1])];
                    String[] split_op = copy_op.split(" ", 0);
                    int[][] line_values = read_indirect(copy_op);
                    String new_string = split_op[2].replaceAll("[0-9]", "") + (line_values[1][1] - 1);
                    copy_op = split_op[0] + ' ' + split_op[1] + ' ' + new_string;
                    new_board[true_location(location + num_values[1])] = copy_op;
                    board = new_board.clone();
                }
                break;
            case 1:// B is indirect
                int[][] first_values = read_indirect(board[true_location(location + num_values[1])]);
                int[][] second_values = read_indirect(board[true_location(location + num_values[1] + first_values[1][1])]);
                if (first_values[1][1] == 0) {
                    b_check = 1;
                } else {
                    if (second_values[1][1] - 1 != 0) {
                        b_check = 1;
                    }
                }
                if (second_values[0][1] == 3) {
                    String copy_op = new_board[true_location(location + num_values[1] + first_values[1][1])];
                    new_board[true_location(location + num_values[1] + first_values[1][1])] = copy_op + ", " + (second_values[1][1] - 1);
                    board = new_board.clone();
                } else if (second_values[0][0] == 3) {
                    String copy_op = new_board[true_location(location + first_values[1][0] + num_values[1])];
                    String[] split_op = copy_op.split(" ", 0);
                    String new_string = split_op[1].replaceAll("[0-9]", "") + (second_values[1][1] - 1);
                    copy_op = split_op[0] + " " + new_string;
                    new_board[true_location(location + num_values[1] + first_values[1][0])] = copy_op;
                    board = new_board.clone();
                } else {
                    String copy_op = new_board[true_location(location + num_values[1] + first_values[1][1])];
                    String[] split_op = copy_op.split(" ", 0);
                    int[][] line_values = read_indirect(copy_op);
                    String new_string = split_op[2].replaceAll("[0-9]", "") + (line_values[1][1] - 1);
                    copy_op = split_op[0] + ' ' + split_op[1] + ' ' + new_string;
                    new_board[true_location(location + num_values[1])] = copy_op;
                    board = new_board.clone();
                }
                break;
            case 2://B is immediate
                if (num_values[1] - 1 != 0) {
                    b_check = 1;
                }
                String copy_op = new_board[true_location(location)];
                String[] split_op = copy_op.split(" ", 0);
                int[][] line_values = read_indirect(copy_op);
                String new_string = split_op[2].replaceAll("[0-9]", "") + (line_values[1][1] - 1);
                copy_op = split_op[0] + ' ' + split_op[1] + ' ' + new_string;
                new_board[true_location(location + num_values[1])] = copy_op;
                board = new_board.clone();
                break;
            default:
                break;
        }
        if (flags[0] == 1) {//A is indirect
            int[][] new_values = read_indirect(board[true_location(location + num_values[1])]);
            new_location = true_location(location + num_values[0] + new_values[1][0]);
        } else {//A is relative
            new_location = true_location(location + num_values[0]);
        }
        if (b_check == 1) {
            return new_location;
        } else {
            int x = true_location(location + 1);
            return x;
        }
    }

    //cmp function: use for cmp operation in MARS
    private int cmp(int[] num_values, String[] board, int[] flags) {
        String[] new_board = board.clone();
        String A;
        String B;
        int cmp_check = 0;
        int new_location = 0;
        if (flags[0] == 0) {
            A = new_board[true_location(location + num_values[0])];
        } else {
            int[][] indirect_A = read_indirect(new_board[true_location(location + num_values[0])]);
            A = new_board[true_location(location + num_values[0] + indirect_A[1][0])];
        }
        if (flags[1] == 0) {
            B = new_board[true_location(location + num_values[1])];
        } else {
            int[][] indirect_B = read_indirect(new_board[true_location(location + num_values[1])]);
            B = new_board[true_location(location + num_values[1] + indirect_B[1][1])];
        }
        if (flags[0] == 2) {
            if (flags[1] == 0) {
                B = new_board[true_location(location + num_values[1])];
                int[][] B_values = read_indirect(B);
                if (num_values[0] == B_values[1][1]) {
                    cmp_check = 1;
                }
            } else {
                int[][] indirect_B = read_indirect(new_board[true_location(location + num_values[1])]);
                B = new_board[true_location(location + num_values[1] + indirect_B[1][1])];
                int[][] B_values = read_indirect(B);
                if (num_values[0] == B_values[1][1]) {
                    cmp_check = 1;
                }
            }
        } else {
            if (A.equals(B))
                cmp_check = 1;
        }
        if (cmp_check == 1) {
            new_location = true_location(location + 2);
            return new_location;
        } else {
            new_location = true_location(location + 1);
            return new_location;
        }
    }

    //SPL function: use for SPL operation in MARS
    private int spl(int[] num_values, String[] board, int[] flags) {
        int[][] a_relative = read_indirect(board[true_location(location + num_values[0])]);

        Warrior next_warrior = main_warrior.queue.firstElement();
        main_warrior.queue.add(next_warrior);
        main_warrior.queue.remove(0);

        Warrior new_warrior = new Warrior();
        new_warrior.board = board.clone();
        new_warrior.main_warrior = main_warrior.main_warrior;
        if (flags[0] == 0) {
            //A is relative
            new_warrior.location = true_location(location + num_values[0]);
        } else {
            //A is indirect
            new_warrior.location = true_location(location + num_values[0] + a_relative[1][0]);
        }
        main_warrior.queue.add(new_warrior);
        return true_location(location + 1);
    }


    //mov function: use for MOV operation in MARS
    private String[] mov(int[] num_values, String[] board, int[] flags) {
        String[] new_board = board.clone();
        if (flags[1] == 1) {//B is indirect
            int[][] new_values = read_indirect(new_board[true_location(location + num_values[1])]);
            if (flags[0] == 2) {//A is an immediate value
                if (new_values[0][0] == 3) {
                    String copy_op = new_board[true_location(location + new_values[1][0] + num_values[1])];
                    String[] split_op = copy_op.split(" ", 0);
                    String new_string = split_op[1].replaceAll("[0-9]", "") + num_values[0];
                    copy_op = split_op[0] + ' ' + new_string;
                    new_board[true_location(location + num_values[1] + new_values[1][0])] = copy_op;
                } else if (new_values[0][1] == 3) {
                    String copy_op = new_board[true_location(location + new_values[1][0] + num_values[1])] + ", " + num_values[0];
                    new_board[true_location(location + new_values[1][0] + num_values[1])] = copy_op;
                } else {
                    String copy_op = new_board[true_location(location + new_values[1][0] + num_values[1])];
                    String[] split_op = copy_op.split(" ", 0);
                    String new_string = split_op[2].replaceAll("[0-9]", "") + num_values[0];
                    copy_op = split_op[0] + ' ' + split_op[1] + ' ' + new_string;
                    new_board[true_location(location + num_values[1])] = copy_op;
                }
            } else if (flags[0] == 0) {// A is relative
                String new_string = new_board[true_location(location + num_values[0])];
                new_board[true_location(location + new_values[1][0] + num_values[1])] = new_string;
            } else {
                int[][] indir_a = read_indirect(new_board[true_location(location + num_values[0])]);
                String new_string = new_board[true_location(location + num_values[0] + indir_a[1][0])];
                new_board[true_location(location + num_values[1] + new_values[1][0])] = new_string;
            }
        } else {// B is relative
            String copy_op = new_board[true_location(location + num_values[1])];
            int[][] new_values = read_indirect(copy_op);
            if (flags[0] == 2) {//A is an immediate value
                if (new_values[0][0] == 3) {
                    String[] split_op = copy_op.split(" ", 0);
                    String new_string = split_op[1].replaceAll("[0-9]", "") + num_values[0];
                    copy_op = split_op[0] + ' ' + new_string;
                    new_board[true_location(location + num_values[1])] = copy_op;
                } else if (new_values[0][1] == 3) {
                    new_board[true_location(location + num_values[1])] = copy_op + ", " + num_values[0];
                } else {
                    String[] split_op = copy_op.split(" ", 0);
                    String new_string = split_op[2].replaceAll("[0-9]", "") + num_values[0];
                    copy_op = split_op[0] + ' ' + split_op[1] + ' ' + new_string;
                    new_board[true_location(location + num_values[1])] = copy_op;
                }
            } else if (flags[0] == 0) {// A is relative
                String new_string = new_board[true_location(location + num_values[0])];
                new_board[true_location(location + num_values[1])] = new_string;
            } else {//A is indirect
                int[][] indir_a = read_indirect(new_board[true_location(location + num_values[0])]);
                String new_string = new_board[true_location(location + num_values[0] + indir_a[1][0])];
                new_board[true_location(location + num_values[1])] = new_string;
            }

        }
        return new_board;
    }


    //dat function: used for DAT operation in MARS
    private int dat() {

        /*
        kill code
         */

        return 1;
    }

    //Scans strings for #, *, and @: returns num array of size 2 that contains appropriate flags when found
//if there is only one argument, one of the elements is set to 3, depending on which argument is missing
    /*
        flag = 0 for relative;
        flag = 1 for indirect;
        flag = 2 for immediate;
        flag = 3 for non-existent
     */
    private int[] give_flags(String input_op) {
        int[] flags = new int[2];
        int one_arg = 0;
        if (isNullOrEmpty(input_op)) {
            flags[0] = 4;
            flags[1] = 4;
            return flags;
        }
        if (input_op.lastIndexOf(',') == -1) {
            one_arg = 1;
        }
        String[] array = input_op.split(" ", 0);
        if (one_arg == 0) {
            array[1] = array[1].replace(",", "");
            char[] a_char = array[1].toCharArray();
            switch (a_char[0]) {
                case '*':
                    flags[0] = 1;
                    break;
                case '#':
                    flags[0] = 2;
                    break;
                default:
                    break;
            }
            char[] b_char = array[2].toCharArray();
            switch (b_char[0]) {
                case '@':
                    flags[1] = 1;
                    break;
                case '#':
                    flags[1] = 2;
                    break;
                default:
                    break;
            }
        } else {
            if (array[0].equals("DAT")) {
                flags[0] = 3;
                char[] b_char = array[1].toCharArray();
                if (b_char[0] == '@' || b_char[0] == '#') {
                    switch (b_char[0]) {
                        case '@':
                            flags[1] = 1;
                            break;
                        case '#':
                            flags[1] = 2;
                            break;
                    }
                }
            } else {
                flags[1] = 3;
                array[1] = array[1].replace(",", "");
                char[] a_char = array[1].toCharArray();
                if (a_char[0] == '*' || a_char[0] == '#') {
                    switch (a_char[0]) {
                        case '*':
                            flags[0] = 1;
                            break;
                        case '#':
                            flags[0] = 2;
                            break;
                    }
                }
            }
        }
        return flags;
    }

    //Scans strings and returns the appropriate values for A and B in a num array
// if there is only one argument in the operation string, then the num is size of 1
    private int[] give_values(String input_op, int[] flags) {
        int[] num_values = new int[2];
        if (flags[0] == 4) {
            num_values[0] = 0;
            num_values[1] = 0;
            return num_values;
        }
        String[] array = input_op.split(" ", 0);


        if (flags[0] == 3) {
            StringBuilder b_new = new StringBuilder(array[1]);
            switch (flags[1]) {
                case 0:
                    num_values[1] = Integer.valueOf(b_new.toString());
                    break;
                case 1:
                case 2:
                    b_new.deleteCharAt(0);
                    num_values[1] = Integer.valueOf(b_new.toString());
                    break;
                default:
                    System.out.println("ERROR!");
                    break;
            }
        } else if (flags[1] == 3) {
            StringBuilder a_new = new StringBuilder(array[1]);
            switch (flags[0]) {
                case 0:
                    num_values[0] = Integer.valueOf(a_new.toString());
                    break;
                case 1:
                case 2:
                    a_new.deleteCharAt(0);
                    num_values[0] = Integer.valueOf(a_new.toString());
                    break;
                default:
                    System.out.println("ERROR!");
                    break;
            }
        } else {
            array[1] = array[1].replace(",", "");
            StringBuilder a_new = new StringBuilder(array[1]);
            StringBuilder b_new = new StringBuilder(array[2]);
            switch (flags[0]) {
                case 0:
                    num_values[0] = Integer.valueOf(array[1]);
                    break;
                case 1:
                case 2:
                    a_new.deleteCharAt(0);
                    num_values[0] = Integer.valueOf(a_new.toString());
                    break;
                default:
                    System.out.println("ERROR!");
                    break;
            }
            switch (flags[1]) {
                case 0:
                    num_values[1] = Integer.valueOf(b_new.toString());
                    break;
                case 1:
                case 2:
                    b_new.deleteCharAt(0);
                    num_values[1] = Integer.valueOf(b_new.toString());
                    break;
                default:
                    System.out.println("ERROR!");
                    break;
            }
        }

        return num_values;

    }

    //Scans strings for values used when indirect operations are perform
//Scans the string specified by the indirect marker, then returns an array of int arrays
    private int[][] read_indirect(String input) {
        int[][] values = new int[2][];
        values[0] = give_flags(input);
        values[1] = give_values(input, values[0]);
        return values;
    }


    private static boolean isNullOrEmpty(String str) {
        if (str != null && !str.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public static int true_location(int x) {
        int y = x % 8000;
        return y;
    }

}
