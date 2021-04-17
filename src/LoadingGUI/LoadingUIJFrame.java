package LoadingGUI;

import FrameTools.jFrameTools;

import javax.swing.*;
import java.awt.*;

public class LoadingUIJFrame extends JFrame{
    private JPanel MainPanel;

    public LoadingUIJFrame(){
        super("Loading");
        this.setUndecorated(true);
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(800, 670);
        this.setResizable(false);
        this.setIconImage(takeImage("/image/icon.png"));
        jFrameTools.setDefaultLocation(this);
    }

    public void waitToDispose(int time){
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i <= time; i++) {
                    try {
                        java.lang.Thread.sleep(40);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                dispose();
            }
        }).start();
    }

    public Image takeImage(String source){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(getClass().getResource(source));
        return image;
    }
}
