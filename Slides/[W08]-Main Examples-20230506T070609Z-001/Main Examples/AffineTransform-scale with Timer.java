import java.awt.*;
import java.awt.Shape;
import java.awt.geom.*;
import javax.swing.*;

class Main extends JFrame {
    private Rectangle rect;
    private AffineTransform transform;
    private boolean shouldScale;
    
    Timer timer;
    double scaleF = 1.0;
    
    public Main() {
        super("Scaled Rectangle");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        rect = new Rectangle(50, 50, 100, 50);
        transform = new AffineTransform();
        shouldScale = false;
        
        timer = new Timer(100, e -> {
            shouldScale = true;
            transform.scale(scaleF, scaleF);
            scaleF += 0.01;
            if (scaleF >= 1.1) {
                timer.stop();
            }
            repaint();    
        });
        timer.setRepeats(true);
        timer.start();
    }
    
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        if (shouldScale) {
            Shape scaledRect = transform.createTransformedShape(rect);
            g2d.setColor(g2d.getBackground());
            g2d.fillRect(0, 0, this.getContentPane().getSize().width, this.getContentPane().getSize().height);
            g2d.setColor(Color.RED);
            g2d.draw(scaledRect);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.draw(rect);
        }
    }
    
    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
}
