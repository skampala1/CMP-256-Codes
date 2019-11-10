package lab9;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Asif Rasheed, Shaham Kampala
 */
public class Lab9 {
    public static void main(String[] args) {
         JFrame frame = new Frame(); 
         frame.setVisible(true);
    }
}

class DrawArea extends JPanel{
    private ArrayList<Shapes> shapes = new ArrayList<Shapes>(); //Contains list of shapes drawn
    private boolean ellipse = true; 
    private boolean rectangle = false;
    private Color color = Color.blue; //Fill Color, blue by default
    private int iniX, iniY;
    private boolean isFirst = true;
    
    public DrawArea(){
        /*<-----------Mouse Controllers---------->*/
        /*<-----Mouse Listener----->*/
        this.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    deselectAll();
                    for(int i=(shapes.size()-1);i>=0;i--) //To select the last drawn shape first
                        if(shapes.get(i).contains(e.getPoint()))
                            {
                                shapes.get(i).toggleOutline();
                                repaint();
                                return; //Selects only one object at a time
                            }
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                /*Finding the X point and the Y point when the user start to drag*/
                iniX = e.getX();
                iniY = e.getY();
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                isFirst = true;
                shapes.get(shapes.size()-1).toggleOutline();
                repaint();       
            }
            
        });
        
        /*<-----MouseMotion Listener----->*/
        this.addMouseMotionListener(new MouseAdapter(){
            @Override
            public void mouseDragged(MouseEvent e){
                int finX = e.getX();
                int finY = e.getY();
                deselectAll();

                if(!isFirst) shapes.remove(shapes.size()-1);
  
                if(ellipse)
                {
                    shapes.add(new Shapes(new Ellipse2D.Double(Math.min(iniX, finX), Math.min(iniY, finY), Math.abs(finX-iniX), Math.abs(finY-iniY)),color));
                    isFirst = false;
                    repaint();
                }
                    
                if(rectangle)
                {
                    shapes.add(new Shapes(new Rectangle(Math.min(iniX, finX), Math.min(iniY, finY), Math.abs(finX-iniX), Math.abs(finY-iniY)),color));
                    isFirst = false;
                    repaint();
                }             
            }
                
        });   
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        
        /*<-----Drawing the shapes----->*/
        Graphics2D g2 = (Graphics2D) g;
        for(Shapes s : shapes){
            g2.setColor(s.getColor());
            g2.fill(s.getShape());
            /*If the shape is selected, red border*/
            if(s.isSelected())
            {
                g2.setColor(Color.red);
                g2.draw(s.getShape());
            }
        } 
    }
    
    public void callDelete(){
        for(int i=shapes.size()-1;i>=0;i--) //Reverse For Loop helps to delete multiple selected items at once
                    if(shapes.get(i).isSelected())
                {
                    shapes.remove(i);
                    repaint(); //Only repaints if an item is deleted
                }
    }
    
    public void selectPrevious(){
        for(int i=shapes.size()-1;i>0;i--)
            if(shapes.get(i).isSelected())
            {
                shapes.get(i).toggleOutline();
                shapes.get(--i).toggleOutline();
                repaint();
            }
    }
    
    public void selectNext(){
        for(int i=0;i<shapes.size()-1;i++)
            if(shapes.get(i).isSelected())
            {
                shapes.get(i).toggleOutline();
                shapes.get(++i).toggleOutline();
                repaint();
            }
    }
    
    public void deselectAll(){ //Deselects all the selected shapes
        for(Shapes s: shapes)
            if(s.isSelected())
            {
                s.toggleOutline();
                repaint();
            }
    }
    
    public void selectRectangle(){
        rectangle = true;
        ellipse = false;
        deselectAll();
    }
    
    public void selectEllipse(){
        rectangle = false;
        ellipse = true;
        deselectAll();
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public void recolor(){
        for(Shapes s: shapes)
            if(s.changeColor(color))
                repaint();
    }
    
}

//Ellipse is the Shape selected by default when the application launches
//Blue is the Color selected by default when the application launches
//User needs to press control in order to avoid accidental shortcuts

class Frame extends JFrame{
    private JPanel buttonPanel = new JPanel(); //Button Panel
    private Dimension size = Toolkit.getDefaultToolkit().getScreenSize(); //Display Resolution
    private DrawArea draw;
    Frame(){ 
        /*<-----JFrame----->*/
        this.setSize(size.width/2, size.height/2);
        this.setTitle("Michelangelo v1.1");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(true);
        draw = new DrawArea();
        draw.setBackground(Color.WHITE);
        draw.setPreferredSize(size);
     
        /*<-----Adding the buttons----->*/
        JButton ellipseButton = new JButton("Ellipse");
        ellipseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.selectEllipse();
                requestFocusInWindow();
            }
        });
        JButton rectangleButton = new JButton("Rectangle");
        rectangleButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.selectRectangle();
                requestFocusInWindow();
            }
        });
        JButton blueButton = new JButton("Blue");
        blueButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               draw.setColor(Color.BLUE);
               draw.recolor();
               requestFocusInWindow();
            }
        });
        JButton greenButton = new JButton("Green");
        greenButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setColor(Color.green);
                draw.recolor();
                requestFocusInWindow();
            }
        });
        JButton magentaButton = new JButton("Magenta");
        magentaButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.setColor(Color.magenta);
                draw.recolor();
                requestFocusInWindow();
            }
            
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.callDelete();
                requestFocusInWindow();
            }
        });
        
        buttonPanel.add(ellipseButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(blueButton);
        buttonPanel.add(greenButton);
        buttonPanel.add(magentaButton);
        buttonPanel.add(deleteButton);
        
        /*<-----Keyboard Shortcuts----->*/
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e)
            {
                    switch(e.getKeyChar())
                    {
                        case 'b'|'B':
                            draw.setColor(Color.BLUE);
                            draw.recolor();
                            requestFocusInWindow();
                            break;
                        case KeyEvent.VK_DELETE:
                            draw.callDelete();
                            break;
                        case 'e'|'E':
                            draw.selectEllipse();
                            requestFocusInWindow();
                            break;
                        case 'g'|'G':
                            draw.setColor(Color.GREEN);
                            draw.recolor();
                            requestFocusInWindow();
                            break;
                        case 'm'|'M':
                            draw.setColor(Color.MAGENTA);
                            draw.recolor();
                            requestFocusInWindow();
                            break;
                        case 'r'|'R':
                            draw.selectRectangle();
                            requestFocusInWindow();
                            break;
                    }
                    
                    if(e.getKeyCode()==KeyEvent.VK_LEFT)
                        draw.selectPrevious();
                    if(e.getKeyCode()==KeyEvent.VK_RIGHT)
                        draw.selectNext();
                    if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
                        draw.deselectAll();
            }
        });
        
        buttonPanel.setBackground(Color.white);
        buttonPanel.setPreferredSize(new Dimension(size.width,40));
        
        JPanel mainPanel = new JPanel();
        mainPanel.add(buttonPanel);
        mainPanel.add(draw);
        
        this.add(mainPanel);
    }
}


class Shapes{
    private Shape shape;
    private Color color;
    private boolean outline = false; //outline is true if selected, else false. Not selected by default
    
    Shapes(Shape shape, Color color){
        this.shape = shape;
        this.color = color;
    }
    
    public Shape getShape(){
        return shape; //Returns the shape
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public boolean changeColor(Color color){
        if(isSelected())
        {
            setColor(color);
            return true;
        }
        return false;
    }
    
    public void toggleOutline(){
        outline = !outline; //If the object is selected, it will deselect it
        //Else the object will be selected
    }
    
    public boolean isSelected(){
        return outline; 
    }
    
    public boolean contains(Point point){
        if(shape.contains(point)) return true;
        return false;
    }
}