import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Font;
public class TicTacToe {
    int score = 0, new_score = 0, mount = 0;
    ColoredButton[] btns = new ColoredButton[9];
    JButton bt_run;
    JLabel info, points;
    ButtonGroup diff;
    JRadioButton[] rbt_dif;
    Map<String,Double> boards = new HashMap<>();
    Map<String, Integer> indexes = new HashMap<>();
    char[][] board = new char[3][3];
    private boolean is_running = false, can_play;

    public TicTacToe(JPanel par, ButtonGroup group, JLabel inf, JLabel poin, JButton brun, JRadioButton[] dif){
        for (int i = 0; i < 9; i++){
            btns[i] = new ColoredButton("-");
            btns[i].setFont(new Font("Berlin Sans FB Demi",Font.PLAIN,26));
            int finalI = i;
            btns[i].addActionListener(e -> mark(true, finalI));
            btns[i].setEnabled(is_running);
            par.add(btns[i]);
            bt_run = brun;
            rbt_dif = dif;
        }
        diff = group;
        info = inf;
        points = poin;
        can_play = Math.random() < 0.5;
    }

    public String getTextBoard(){
        StringBuilder txt = new StringBuilder();
        for(int i = 0; i< 9; i++){
            txt.append(btns[i].getText());
        }
        return txt.toString();
    }
    public String getLowerBoard(String current){
        ArrayList<String> list = new ArrayList<>();
        list.add(current);
        list.add(reflectBoard(current));
        for (int i = 0; i < 3; i++){
            current = rotateBoard(current);
            list.add(current);
            list.add(reflectBoard(current));
        }
        return list.stream().min(String::compareTo).orElse(null);
    }
    public String reflectBoard(String current){
        char[] b = current.toCharArray();
        return "" + b[2] + b[1] + b[0] + b[5] + b[4] + b[3] + b[8] + b[7] + b[6];
    }
    public String rotateBoard(String current){
        char[] b = current.toCharArray();
        return "" + b[6] + b[3] + b[0] + b[7] + b[4] + b[1] + b[8] + b[5] + b[2];
    }
    public ArrayList<Integer> getEmptyList(String current){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 9 ; i++){
            if (current.charAt(i) == '-') list.add(i);
        }
        return list;
    }

    public void addInHash(String current, double qant, int index){
        String b = getLowerBoard(current);
        boards.putIfAbsent(b,qant);
        indexes.putIfAbsent(b,index);
    }

    public boolean isFull(){
        return !getTextBoard().contains("-");
    }
    public boolean hasWon(boolean player, String current){
        char val = player ? 'X' : 'O';
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = current.charAt(i);
        }
        for (int j = 0; j < 3; j++) {
            if (board[j][0] == val && board[j][1] == val && board[j][2] == val) return true;
            if (board[0][j] == val && board[1][j] == val && board[2][j] == val) return true;
        }
        if (board[0][0] == val && board[1][1] == val && board[2][2] == val) return true;
        return board[2][0] == val && board[1][1] == val && board[0][2] == val;
    }
    public boolean canWin(){
        String current = getTextBoard();
        ArrayList<Integer> list = getEmptyList(current);
        for (int i : list){
            char[] ar = current.toCharArray();
            ar[i] = 'O';
            if (hasWon(false,new String(ar))) {
                mark(false,i);
                return true;
            }
        }
        return false;
    }


    public void mark(boolean player, int btn){
        int scr = 0;
        if (btns[btn].getText().equals("-")) {
            String val = player ? "X" : "O";
            btns[btn].mark(val,player? Color.BLUE : Color.RED);
        }
        can_play = !can_play;
        if (hasWon(player,getTextBoard())) {
            scr = mount * 10;
            end(player ? 1 : -1);
        } else if (isFull()) {
            end(0);
        } else {
            scr = mount;
            turn();
        }
        new_score += scr;
    }
    public void turn(){
        for( JButton bt : btns){
            if (bt.getText().equals("-")) {
                bt.setEnabled(can_play);
            }
        }
        if (can_play){
            info.setForeground(new Color(4544926));
            info.setText("Tu turno");
        } else {
            info.setForeground(new Color(0x459E72));
            info.setText("Turno de la PC");
            bt_run.setEnabled(false);
            Timer timer = new Timer(1000, e -> play());
            timer.setRepeats(false);
            timer.start();
        }

    }
    public void play(){
        if (isFull()) {
            end(0);
        } else {
            switch (diff.getSelection().getActionCommand()){
                case "Fácil": easy(); break;
                case "Medio": medium(); break;
                case "Difícil": hard(); break;
            }
            bt_run.setEnabled(true);
        }
    }

    public void easy(){
        ArrayList<Integer> list = getEmptyList(getTextBoard());
        int index = (int) (Math.random() * list.size());
        mark(false,list.get(index));
    }
    public void medium(){
        if (!canWin()){
            boards.clear();
            indexes.clear();
            System.out.println("current: " + getTextBoard());
            seekVictory(getTextBoard(),0,-1);

            //System.out.println("boards: " + boards.toString());
            String[] keys = boards.keySet().toArray(new String[0]);
            //System.out.println("indexes: " + indexes.toString());
            if (keys.length == 0){
                easy();
            } else {
                //System.out.println("keys: " + Arrays.toString(keys));
                String randkey = keys[(int) (Math.random() * keys.length)];
                mark(false,indexes.get(randkey));
            }
        }
    }
    public void hard(){

    }
    public void seekVictory(String current, double qant, int index){
        if (boards.values().stream().anyMatch(val -> val < qant)) {
            return;
        }
        if (hasWon(true,current)){
            if (boards.values().stream().anyMatch(val -> val > qant)) {
                boards.clear();
                indexes.clear();
            }
            addInHash(current,qant,index);
            System.out.println("win in: " + current);
        } else {
            ArrayList<Integer> list = getEmptyList(current);
            if (boards.values().stream().noneMatch(val -> val < qant)){
                for (int v : list){
                    char[] ar = current.toCharArray();
                    ar[v] = 'X';
                    String newcurrent = new String(ar);
                    int newindex = index == -1? v : index;
                    //System.out.println("b" + i + ": " + newcurrent);
                    seekVictory(newcurrent,qant + 1, newindex);
                }
            }
        }
    }


    public void gameState(){
        is_running = !is_running;
        if (is_running){
            for (ColoredButton b : btns){
                b.setText("-");
                b.unabledColor = Color.BLACK;
            }
            turn();
            for(JRadioButton rb : rbt_dif){
                rb.setEnabled(false);
            }
            new_score = 0;
            mount = switch(diff.getSelection().getActionCommand()){
                case "Medio" -> 50;
                case "Difícil" -> 100;
                default -> 10;
            };
        } else {
            end(0);
        }
        bt_run.setText(is_running ? "Detener" : "Jugar");
    }
    public void end(int winner ){
        info.setForeground(new Color(0xB723F3));
        String text = switch(winner){
            case -1 -> "Perdiste!";
            case 1 -> "Ganaste!";
            default -> "Fin del juego!";
        };
        info.setText(text);
        is_running = false;
        bt_run.setText("Jugar");
        for (JButton b : btns){
            b.setEnabled(false);
        }
        for(JRadioButton rb : rbt_dif){
            rb.setEnabled(true);
        }
        score += winner * new_score;
        points.setText("Puntaje: " + score);
    }
}
