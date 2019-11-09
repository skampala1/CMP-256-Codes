/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab6c;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @version 1.0 built 520101921
 * @author Asif Rasheed
 */
public class lab6c {
    public static void main(String[] args){
        /*<-----Setting Up the Frame----->*/
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0, 0, screen.width/3, screen.height/3);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.BLACK); //Frame Background is Black
        
        /*<-----WatchFace----->*/
        frame.add(new JComponent(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                
                //Getting Time
                int hours = LocalTime.now().getHour();
                int minutes = LocalTime.now().getMinute();
                int seconds = LocalTime.now().getSecond();
                int nano = LocalTime.now().getNano();
                
                DateTimeFormatter format = DateTimeFormatter.ofPattern("hh:mm:ss a");
                
                //Frame Title
                frame.setTitle(LocalTime.now().format(format));
                
                //Dial Ring
                drawDialRing(g2);
                
                //Date
                drawDate(g2);

                //Draw and Rotate Hours needle
                g2.rotate(Math.toRadians(hours * 30 + minutes * 30.0 / 60 +seconds*30.0/3600), frame.getWidth()/2, frame.getHeight()/2);
                drawHourNeedle(g2);
                g2.rotate(-Math.toRadians(hours * 30 + minutes * 30.0 / 60 +seconds*30.0/3600), frame.getWidth()/2, frame.getHeight()/2);
                
                //Draw and Rotate Minutes needle
                g2.rotate(Math.toRadians(minutes * 6.0 + seconds * 6.0 / 60), frame.getWidth()/2, frame.getHeight()/2);
                drawMinuteNeedle(g2);
                g2.rotate(-Math.toRadians(minutes * 6.0 + seconds * 6.0 / 60), frame.getWidth()/2, frame.getHeight()/2);
                
                //Draw and Rotate Seconds needle
                g2.rotate(Math.toRadians(seconds * 6.0 + nano * 6.0 / 1000000000), frame.getWidth()/2, frame.getHeight()/2);
                drawSecondNeedle(g2);
                g2.rotate(-Math.toRadians(seconds * 6.0 + nano * 6.0 / 1000000000), frame.getWidth()/2, frame.getHeight()/2);
                
            }
            
            private void drawDialRing(Graphics2D g2D){
                /*<-----Monogram----->*/
                g2D.setFont(new Font("Brush Script MT",Font.BOLD,14));
                String monogram = "ARRT";
                g2D.drawString(monogram,frame.getWidth()/2-(monogram.length()*5),frame.getHeight()/2-40);
                /*<-----Minutes----->*/
                g2D.setColor(Color.GRAY);
                for(int i=1;i<31;i++)
                {
                    g2D.rotate(Math.toRadians(i * 6.0), frame.getWidth()/2, frame.getHeight()/2);
                    g2D.fillRoundRect(frame.getWidth()/2-2,frame.getHeight()/2-(7*frame.getHeight()/18)-2,4,10,10,5);
                    g2D.fillRoundRect(frame.getWidth()/2-2,frame.getHeight()/2+(7*frame.getHeight()/18)-8,4,10,10,5);
                    g2D.rotate(-Math.toRadians(i * 6.0), frame.getWidth()/2, frame.getHeight()/2);
                }
                g2D.setColor(Color.WHITE);
                /*<-----Hours----->*/
                for(int i=1;i<7;i++)
                {
                    g2D.rotate(Math.toRadians(i * 30), frame.getWidth()/2, frame.getHeight()/2);
                    g2D.fillRoundRect(frame.getWidth()/2-4,frame.getHeight()/2-(7*frame.getHeight()/18)-2,8,25,10,5);
                    g2D.fillRoundRect(frame.getWidth()/2-4,frame.getHeight()/2+(7*frame.getHeight()/18)-23,8,25,10,5);
                    g2D.rotate(-Math.toRadians(i * 30), frame.getWidth()/2, frame.getHeight()/2);
                }
            }
            
            private void drawDate(Graphics2D g2D){
                //California WatchFace Style Date
                g2D.drawRoundRect(frame.getWidth()/2-15, frame.getHeight()-(frame.getHeight()/3), 30, 30, 15, 10);
                g2D.setFont(new Font("ComicSansMS",Font.BOLD,20));
                
                String date = String.valueOf(LocalDate.now().getDayOfMonth());
                if(date.length()<2)
                    date = "0"+date;
                
                g2D.drawString(date, frame.getWidth()/2-date.length()-10, frame.getHeight()-(frame.getHeight()/3) + 22);
            }
            
            private void drawSecondNeedle(Graphics2D g2D){
                //California WatchFace Stle Second Hand
                g2D.fillOval(frame.getWidth()/2-6, frame.getHeight()/2-6, 12, 12);
                g2D.setColor(Color.RED);
                g2D.drawLine(frame.getWidth()/2,frame.getHeight()/2+15,frame.getWidth()/2,frame.getHeight()/9);
                g2D.fillOval(frame.getWidth()/2-5, frame.getHeight()/2-5, 10, 10);
                g2D.setColor(Color.BLACK);
                g2D.fillOval(frame.getWidth()/2-3, frame.getHeight()/2-3, 6, 6);
                g2D.setColor(Color.WHITE);
            }
            
            private void drawMinuteNeedle(Graphics2D g2D){
                g2D.drawLine(frame.getWidth()/2,frame.getHeight()/2,frame.getWidth()/2,frame.getHeight()/7); //Initial Line
                
                //California WatchFace Style Minute Hand
                g2D.drawRoundRect(frame.getWidth()/2-5, frame.getHeight()/7, 10, frame.getHeight()/3 - 5, 15, 10);
                g2D.setColor(Color.BLACK);
                g2D.fillRoundRect(frame.getWidth()/2-3, frame.getHeight()/7, 6, frame.getHeight()/3 - 5, 15, 10);
                g2D.setColor(Color.WHITE);
            }
            
            private void drawHourNeedle(Graphics2D g2D){
                g2D.drawLine(frame.getWidth()/2,frame.getHeight()/2,frame.getWidth()/2,frame.getHeight()/4); //Initial Line
                
                //California WatchFace Style Hour Hand
                g2D.drawRoundRect(frame.getWidth()/2-5, frame.getHeight()/4-15, 10, frame.getHeight()/4, 15, 10);
                g2D.setColor(Color.BLACK);
                g2D.fillRoundRect(frame.getWidth()/2-3, frame.getHeight()/4-15, 6, frame.getHeight()/4, 15, 10);
                g2D.setColor(Color.WHITE);
            }
        });
        
        frame.setVisible(true); //Frame Visibility
        
        /*<-----Repainting the Frame----->*/
        Timer refrsh = new Timer(10,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.repaint();
            }
        });
        refrsh.start();
    }
}
