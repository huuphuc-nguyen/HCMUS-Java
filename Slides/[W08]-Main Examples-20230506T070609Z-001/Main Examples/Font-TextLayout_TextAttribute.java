import java.awt.*;
import java.awt.font.*;
import java.awt.image.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

class Main extends JFrame {
    public void paint(Graphics g){
        Map<TextAttribute, Object> attrs = new HashMap<>();
        attrs.put(TextAttribute.FAMILY, "Arial");
        attrs.put(TextAttribute.SIZE, 24.0);
        attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        attrs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        attrs.put(TextAttribute.FOREGROUND, Color.RED);
        attrs.put(TextAttribute.BACKGROUND, Color.YELLOW);
        
        AttributedString str = new AttributedString("Hello, world!", attrs);
        
        Graphics2D g2d = (Graphics2D) g;
        TextLayout textLayout = new TextLayout(str.getIterator(), g2d.getFontRenderContext());
            textLayout.draw(g2d, 10, 100);
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(400, 400);
        main.setVisible(true);
    }
}
