/*
 * Clase para los botones
 * Autor: Diego León
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.FontMetrics;
import javax.swing.JButton;

public class ColoredButton extends JButton{
    public Color unabledColor = Color.BLACK;
    public ColoredButton(String title){
        super(title);
    }
    public void mark(String text, Color c){
        setText(text);
        unabledColor = c;
        setEnabled(false);
    }
    @Override
    protected void paintComponent(Graphics g){
        if (!isEnabled()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(unabledColor);
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 4;
            g.drawString(getText(), x, y);
        } else {
            super.paintComponent(g);
        }
    }
}
