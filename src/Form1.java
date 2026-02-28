/*
 * Formulario principal
 * Autor: Diego León
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.ImageIcon;
import java.util.Objects;

public class Form1 extends JFrame {
    final Border border_def = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xB3000000, true),10),
            BorderFactory.createEmptyBorder(10,15,10,15));
    final Font fh = new Font("JetBrains Mono", Font.BOLD, 13);
    final Font f1 = new Font("JetBrains Mono", Font.PLAIN, 13);
    Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
    JButton bt_run;
    JLabel lb_points, lb_error;
    JRadioButton[] rbt_dif = new JRadioButton[3];
    TicTacToe game;

    public Form1(String title) throws HeadlessException {
        super(title);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon.png")));
        setIconImage(icon.getImage());
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation((s.width - 500)/2, (s.height - 500)/2 );
        setMinimumSize(new Dimension(500,500));
        //Panels
        JPanel p_main = new JPanel();
        setPanel(p_main, (JPanel) getContentPane(),Color.lightGray, new BoxLayout(p_main, BoxLayout.Y_AXIS));
        JPanel p_game = new JPanel();
        setPanel(p_game, p_main, new Color(8817853),new GridLayout(3,3,10,10));
        p_game.setPreferredSize(new Dimension(500,4000));
        p_game.setBorder(border_def);
        JPanel p_input = new JPanel();
        setPanel(p_input,p_main,Color.LIGHT_GRAY,new BoxLayout(p_input,BoxLayout.X_AXIS));
        p_input.setBorder(border_def);
        //Form
        JPanel p_form = new JPanel();
        setPanel(p_form,p_input,Color.LIGHT_GRAY,new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Los pone uno debajo del otro
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre elementos (arriba, izq, abajo, der)
        gbc.fill = GridBagConstraints.NONE;      // EVITA que se estiren
        gbc.anchor = GridBagConstraints.CENTER;  // Los mantiene centrados

        p_form.add(new JLabel("<html><h1>TIC TAC TOE</h1></html>") {{setFont(fh); }} , gbc);
        bt_run = setButton("Jugar",p_form,this::gameState, gbc);
        JFrame self = this;
        setButton("Info", p_form, () -> showInfo(self), gbc);
        lb_error = new JLabel(" "){{setFont(f1); setForeground(new Color(4544926));}};
        p_form.add(lb_error, gbc);

        JPanel p_form2 = new JPanel();
        setPanel(p_form2,p_input,Color.LIGHT_GRAY,new GridBagLayout());
        String[] dif = {"Fácil", "Medio", "Difícil"};
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 3; i++){
            rbt_dif[i] = i != 0 ? new JRadioButton(dif[i]) : new JRadioButton(dif[i],true);
            rbt_dif[i].setActionCommand(dif[i]);
            rbt_dif[i].setBackground(Color.LIGHT_GRAY);
            group.add(rbt_dif[i]);
            p_form2.add(rbt_dif[i],gbc);
        }
        lb_points = new JLabel("Puntaje: 0") {{setFont(f1);}};
        p_form2.add(lb_points,gbc);
        //Game
        game = new TicTacToe(p_game,group, lb_error, lb_points, bt_run, rbt_dif);
    }

    protected void showInfo(JFrame parent) {
        JDialog info = new JDialog(parent, "Información", true);
        info.setSize(800,300);
        info.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        info.setLocation((s.width - 800) / 2,(s.height - 300) / 2);
        info.setMinimumSize(new Dimension(800, 300));

        JPanel p_main = new JPanel();
        setPanel(p_main, (JPanel) info.getContentPane(), Color.LIGHT_GRAY, new GridLayout(5, 1, 5, 5));
        p_main.setBorder(border_def);

        String[] tx = {"Este programa permite jugar al clasico juego de Tic Tac Toe.",
                "Fácil: Selección aleatoria de posición. (10p x turno)",
                "Medio: Depth-First Search y Branch-and-bound. (50p x turno)",
                "Difícil: Minimax personalizado. (100p x turno)",
                "<html><i>Desarrollado por <b>Diego León</b> © 2026</i></html>"};

        for (String t : tx) {
            JLabel j = new JLabel(t);
            j.setFont(f1);
            p_main.add(j);
        }

        info.setVisible(true);
    }
    protected void setPanel(JPanel panel, JPanel parent, Color bg, LayoutManager layout){
        parent.add(panel);
        panel.setBackground(bg);
        panel.setLayout(layout);
    }
    protected JButton setButton(String title,JPanel parent,Runnable func, Object constraints){
        JButton b = new JButton(title);
        parent.add(b, constraints);
        b.setFont(fh);
        b.addActionListener(e -> func.run());
        return b;
    }

    protected void gameState(){
        game.gameState();
    }

}
