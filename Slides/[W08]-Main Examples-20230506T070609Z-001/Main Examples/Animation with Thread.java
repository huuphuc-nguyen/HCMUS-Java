import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MyPanel extends JPanel implements Runnable {
  Thread mythread;
  public void run() { // Called by anim thread
    while (running) { // the animation loop
      x = x - 2;      // Control animation step size
      if (x < -150)  x = this.getWidth();
      repaint();
      try{
        Thread.sleep(10);    // Control animation speed
      }catch(InterruptedException e){ }
    }
  }
  
  private void startAnim(){   // startup animation thread
    running = true;
    if(mythread == null){
      mythread = new Thread(this);
      mythread.start();
    }
  }
  private void stopAnim(){
    running = false;    // flag animation to stop
    mythread = null;    // cleanup anim thread
  }
  
  
  
  
  int x=100;
  JButton jButton1 = new JButton();
  boolean running = false;
  
  public MyPanel(){
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    jButton1.setText("Start / Stop");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    this.add(jButton1, null);
  }
  
  void jButton1_actionPerformed(ActionEvent e) {
    if (running)    // toggle button
    stopAnim();
    else
    startAnim();
  }
  
  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    // my graphics here:
    g2.drawString("Hello World", x, 100);
    
  }
  
  public static void main(String[] args) {
    MyPanel myJPanel1 = new MyPanel();
    JFrame frame = new JFrame();
    frame.getContentPane().add(myJPanel1, BorderLayout.CENTER);
    frame.setSize(300,200);
    frame.setVisible(true);
  }
}

