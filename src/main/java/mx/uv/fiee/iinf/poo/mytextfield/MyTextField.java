package mx.uv.fiee.iinf.poo.mytextfield;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JTextField;

public class MyTextField extends JTextField implements KeyListener {
    
    public MyTextField () {
        this.addKeyListener (this);
    }
    
    @Override
    public void keyTyped (KeyEvent e) {
        if ( (!((e.getKeyChar () >= '0' && e.getKeyChar () <= '9') || e.getKeyChar () == '(' ||e.getKeyChar () == ')' || e.getKeyChar() == ' '))
                || (e.getKeyChar () == '(' && this.getText ().contains ("(")) ||
                (e.getKeyChar () == ')' && this.getText ().contains (")")) ||
                (this.getText().length() >= 13)) {
            e.consume ();
        }
    }

    @Override
    public void keyPressed (KeyEvent e) {
    }

    @Override
    public void keyReleased (KeyEvent e) {
        if(this.getText().isEmpty() || this.getText().matches("\\(\\d{3}\\)\\s\\d{7}")) {
            this.setBackground(Color.WHITE);
            this.setForeground(Color.BLACK);
        } else {
            this.setBackground(Color.RED);
            this.setForeground(Color.WHITE);
        }
    }

    @Override
    public void paint (Graphics g) {
        super.paint (g);
    }

}