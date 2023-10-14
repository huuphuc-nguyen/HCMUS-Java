

import java.awt.*;
import javax.swing.*;

class Surface extends JPanel {
   private void doDrawing(Graphics g) {

       Graphics2D g2d = (Graphics2D) g.create();
       g2d.setPaint(Color.blue);

       for (int i = 1; i <= 10; i++) {

           float alpha = i * 0.1f;
           AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
           g2d.setComposite(alcom);
           g2d.fillRect(50 * i, 20, 40, 40);
       }
       g2d.dispose();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}

class TransparencyEx extends JFrame {

    public TransparencyEx() {
        initUI();
    }

    private void initUI() {
        final Surface surface = new Surface();
        add(surface);

        setTitle("Texture Examples");
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
class Example11 {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TransparencyEx ex = new TransparencyEx();
                ex.setVisible(true);
            }
        });
    }
}

