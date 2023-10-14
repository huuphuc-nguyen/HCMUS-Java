import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;

class Shape extends JFrame {
    public Shape() {
        super("Shape");
        setSize(400, 400);
        setVisible(true);
    }

    public void paint(Graphics g) {
      super.paint(g);
      //Get 2d graphics
        Graphics2D g2d = (Graphics2D) g;
        //draw 2d ellipse filled with a blue-yellow gradient
        g2d.setPaint(new GradientPaint(5, 30, Color.blue, 35, 100, Color.yellow, true));
        g2d.fill(new Ellipse2D.Double(5, 30, 65, 100));
       
        //draw 2d rectangle filled with a red color
        g2d.setPaint(Color.red);
        g2d.setStroke(new BasicStroke(10.0f));
        g2d.draw(new Rectangle2D.Double(80, 30, 65, 100));

        //draw 2d round rectangle with buffered background
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D gbi = bufferedImage.createGraphics();
        gbi.setColor(Color.yellow);
        gbi.fillRect(0, 0, 10, 10);
        gbi.setColor(Color.black);
        gbi.drawRect(1, 1, 6, 6);
        gbi.setColor(Color.blue);
        gbi.fillRect(1, 1, 3, 3);
        gbi.setColor(Color.red);
        gbi.fillRect(4, 4, 3, 3);
        g2d.setPaint(new TexturePaint(bufferedImage, new Rectangle(10, 10)));
        g2d.fill(new RoundRectangle2D.Double(155, 30, 75, 100, 50, 50));

        //draw 2d pie shape filled with a while color
        g2d.setPaint(Color.white);
        g2d.setStroke(new BasicStroke(6.0f));
        g2d.fill(new Arc2D.Double(240, 30, 75, 100, 0, 270, Arc2D.PIE));

        //draw 2d lines in green and yellow colors
        g2d.setPaint(Color.green);
        g2d.setStroke(new BasicStroke(6.0f));
        g2d.draw(new Line2D.Double(5, 150, 380, 150));
        
        float dashes[] = {10.0f};
        g2d.setPaint(Color.yellow);
        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dashes, 0.0f));
        g2d.draw(new Line2D.Double(5, 170, 380, 170));
   }

   
   public static void main(String args[]) {
      JFrame test = new Shape();
      test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
}