import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


class Shape2 extends JFrame {
  public Shape2() {
    super("Shape2");
    getContentPane().setBackground(Color.white);
    setSize(400, 400);
    setVisible(true);
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    int xPoints[] = { 55, 67, 109, 73, 83, 55, 27, 37, 1, 43 };
    int yPoints[] = { 0, 36, 36, 54, 96, 72, 96, 54, 36, 36 }; 
    
    Graphics2D g2d = (Graphics2D) g;
    GeneralPath star = new GeneralPath();
    star.moveTo(xPoints[0], yPoints[0]);   
    for (int k = 1; k < xPoints.length; k++) {
      star.lineTo(xPoints[k], yPoints[k]);
    }
    
    star.closePath();
    g2d.translate(200, 200);
    
    for (int j = 1; j <= 20; j++) {
      g2d.rotate(Math.PI / 10.0);
      g2d.setColor( new Color( ( int ) ( Math.random() * 256 ), ( int ) ( Math.random() * 256 ), ( int ) ( Math.random() * 256 ) ) );
      
      g2d.fill(star);
    }
  }
  
  public static void main(String args[]) {
    JFrame test = new JFrame();
    test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    test.setSize(400, 400);
    test.setVisible(true);
    test.add(new Shape2());
    
  }
}