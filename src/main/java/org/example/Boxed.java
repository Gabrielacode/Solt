package org.example;

import javax.swing.*;
import java.awt.*;

public class Boxed extends JFrame {
   Button button1, button2 ;

    public Boxed() {
setSize(500,500);
       setLayout(new FlowLayout());
       setTitle("Buttons");
        setVisible(true);
        String ae = "<html><body><p>Hello</p><img src = \"C:\\Users\\gabri\\IdeaProjectsSA\\Farnd\\src\\main\\resources\\send (1).png\" alt = \"An image\"></body></html>";
      JLabel happy = new JLabel(ae);
      this.add(happy);



    }

    public static void main(String[] args) {
       new Boxed();
    }
}
