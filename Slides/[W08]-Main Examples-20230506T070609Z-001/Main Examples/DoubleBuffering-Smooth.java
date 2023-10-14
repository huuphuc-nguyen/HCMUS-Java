import java.awt.*;
import java.awt.event.*;

class SmoothMove extends Frame implements MouseMotionListener {
  public static void main(String[] args) {
    new SmoothMove();
  }
  
  private int mX, mY;
  private Image mImage;
  
  public SmoothMove() {
    super("Smooth v1.0");
    addMouseMotionListener(this);
    setVisible(true);
  }
  
  public void mouseMoved(MouseEvent me) {
    mX = (int)me.getPoint().getX();
    mY = (int)me.getPoint().getY();
    repaint(); }
  
  public void mouseDragged(MouseEvent me) { mouseMoved(me); }
  
  public void paint(Graphics g) {
    // Create an offscreen image.
    checkOffscreenImage();
    // Clear the offscreen image.
    Dimension d = getSize();
    Graphics offG = mImage.getGraphics();
    offG.setColor(getBackground());
    offG.fillRect(0, 0, d.width, d.height);
    // Draw into the offscreen image.
    paintOffscreen(mImage.getGraphics());
    // Put the offscreen image on the screen.
    g.drawImage(mImage, 0, 0, null);
  }
  
  private void checkOffscreenImage() {
    Dimension d = getSize();
    if (mImage == null ||
      mImage.getWidth(null) != d.width ||
      mImage.getHeight(null) != d.height) {
        mImage = createImage(d.width, d.height);
      } 
  }   
  public void paintOffscreen(Graphics g) {
    int s = 100;
    g.setColor(Color.blue);
    g.fillRect(mX - s / 2, mY - s / 2, s, s);
  }
}