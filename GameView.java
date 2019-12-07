package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author Asif Rasheed, Mohamad Abdelaty
 */

/*
    The GameView extends JFrame, contains instances of ButtonPanel, SavePanel,
        GridPanel, JMenuBar, JMenu and JMenuItems.
*/
public class GameView extends JFrame{
    private ButtonPanel buttonPanel;
    private SavePanel savePanel;
    private GridPanel gridPanel;
    private JMenuBar menu;
    private JMenu file;
    private JMenuItem save, open, exit;
    {
        buttonPanel = new ButtonPanel();
        savePanel = new SavePanel();
        gridPanel = new GridPanel();
        save = new JMenuItem("Save");
        open = new JMenuItem("Open");
        exit = new JMenuItem("Exit");
        file = new JMenu("File");
        menu = new JMenuBar();
        file.add(save);
        file.add(open);
        file.add(exit);
        menu.add(file);
    }
    public GameView(){
        add(savePanel);
        add(buttonPanel,BorderLayout.SOUTH);
        add(gridPanel,BorderLayout.CENTER);
        
        setJMenuBar(menu);
        /*
        Packs the frame first and uses the resulting frame width as the 
            minimum frame width to make sure that all the buttons in the 
                ButtonPanel are visible
        */
        pack();
        setTitle("Conway's Game of Life");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(getWidth()+100,size.height/2);
        
        /*Setting Icon*/
        setIconImage(new ImageIcon("icon.png").getImage());
        
        /*
            Minimum size is set so that there is enough space for all controls
        */
        setMinimumSize(getSize()); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    /*
        The following methods calls their corresponding methods from ButtonPanel,
            GridPanel or SavePanel so that any class/method with instance of
                GameView would be able to access them.
    */
    public void addOpenAction(ActionListener action){
        savePanel.addOpenAction(action);
        open.addActionListener(action);
    }
    public void addSaveAction(ActionListener action){
        savePanel.addSaveAction(action);
        save.addActionListener(action);
    }
    public void addExitAction(ActionListener action){
        exit.addActionListener(action);
    }
    public void addNextAction(ActionListener action){
        buttonPanel.addNextAction(action);
    }
    public void addStartAction(ActionListener action){
        buttonPanel.addStartAction(action);
    }
    public void updateGeneration(int generation){
        buttonPanel.updateGeneration(generation);
    }
    public void updateSize(int size){
        gridPanel.updateSize(size);
    }
    public void updateCells(ArrayList<Point> coords){
        gridPanel.updateCells(coords);
    }
    public void clearCells(){
        gridPanel.clearCells();
    }
    public void addPatternSelection(ActionListener action){
        buttonPanel.addPatternSelection(action);
    }
    public void addSpeedSelection(ActionListener action){
        buttonPanel.addSpeedSelection(action);
    }
    public void addSizeSelection(ActionListener action){
        buttonPanel.addSizeSelection(action);
    }
    public String getPatternSelection(){
        return buttonPanel.getPatternSelection();
    }
    public String getSizeSelection(){
        return buttonPanel.getSizeSelection();
    }
    public String getSpeedSelection(){
        return buttonPanel.getSpeedSelection();
    }
    public void setStartLabel(String label){
        buttonPanel.setStartLabel(label);
    }
    public String getStartLabel(){
        return buttonPanel.getStartLabel();
    }
    public int getCentX(){
        return gridPanel.getCentX();
    }
    public int getCentY(){
        return gridPanel.getCentY();
    }
    public void showMenu(MouseEvent e){
        savePanel.showMenu(e);
    }
    public void disableNext(boolean condition){
        buttonPanel.disableNext(condition);
    }
    public void addEditAction(ActionListener action){
        buttonPanel.addEditAction(action);
    }
    public void setEditLabel(String label){
        buttonPanel.setEditLabel(label);
    }
    public int getCellSize(){
        return gridPanel.getCellSize();
    }
    public void addGridListener(MouseAdapter listener){
        gridPanel.addMouseListener(listener);
        gridPanel.addMouseMotionListener(listener);
    }
    public int getNumberCells(){
        return gridPanel.getNumberCells();
    }
    public void addZoomInAction(ActionListener action){
        buttonPanel.addZoomInAction(action);
    }
    public void addZoomOutAction(ActionListener action){
        buttonPanel.addZoomOutAction(action);
    }
}

/*
    SavePanel is a popup menu with save and open buttons.
*/
class SavePanel extends JPopupMenu{
    JMenuItem save, open;
    {
        save = new JMenuItem("Save");
        open = new JMenuItem("Open");
    }
    public SavePanel(){
        add(save);
        add(open);
    }
    public void showMenu(MouseEvent e){
        show(e.getComponent(),e.getX(),e.getY());
    }
    public void addSaveAction(ActionListener action){
        save.addActionListener(action);
    }
    public void addOpenAction(ActionListener action){
        open.addActionListener(action);
    }
}

/*
    ButtonPanel contains the main controls of the game.
*/
class ButtonPanel extends JPanel{
    private JButton next, start, edit, zoomIn, zoomOut;
    private JComboBox saves, speed, zoom;
    private ArrayList<String> savesList, speedList, zoomList;
    private JLabel generation;
    public ButtonPanel(){
        this.setBackground(java.awt.Color.WHITE);
        next = new JButton("Next");
        start = new JButton("Start");
        edit = new JButton("Edit: Off");
        zoomIn = new JButton("+");
        zoomOut = new JButton("-");

        savesList = new ArrayList<String>();
        zoomList = new ArrayList<String>();
        speedList = new ArrayList<String>();

        savesList.add("Clear");
        savesList.add("Glider");
        savesList.add("R-Pentomino");
        savesList.add("Small Explorer");
        savesList.add("Small Space Ship");
        savesList.add("Tumbler");

        saves = new JComboBox(savesList.toArray());
        saves.setSelectedIndex(0);

        speedList.add("Slow");
        speedList.add("Normal");
        speedList.add("Fast");

        speed = new JComboBox(speedList.toArray());
        speed.setSelectedIndex(1);

        zoomList.add("Small");
        zoomList.add("Medium");
        zoomList.add("Big");

        zoom = new JComboBox(zoomList.toArray());
        zoom.setSelectedIndex(1);
        
        JPanel patternPanel = new JPanel(new BorderLayout());
        patternPanel.setBackground(this.getBackground());
        patternPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        patternPanel.add(saves,BorderLayout.CENTER);
        JLabel patternLabel = new JLabel("Pattern Selection");
        patternLabel.setForeground(Color.GRAY);
        patternLabel.setHorizontalAlignment(JLabel.CENTER);
        patternPanel.add(patternLabel,BorderLayout.SOUTH);
        
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(this.getBackground());
        gamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        gamePanel.add(next,BorderLayout.WEST);
        gamePanel.add(start,BorderLayout.CENTER);
        gamePanel.add(speed,BorderLayout.EAST);
        JLabel gameLabel = new JLabel("Game Controls");
        gameLabel.setHorizontalAlignment(JLabel.CENTER);
        gameLabel.setForeground(Color.GRAY);
        gamePanel.add(gameLabel,BorderLayout.SOUTH);
        
        JPanel zoomPanel = new JPanel(new BorderLayout());
        zoomPanel.setBackground(this.getBackground());
        zoomPanel.add(zoomIn,BorderLayout.EAST);
        zoomPanel.add(zoomOut,BorderLayout.WEST);
        zoomPanel.add(zoom, BorderLayout.CENTER);
        JLabel zoomLabel = new JLabel("Zoom Controls");
        zoomLabel.setHorizontalAlignment(JLabel.CENTER);
        zoomLabel.setForeground(Color.GRAY);
        zoomPanel.add(zoomLabel,BorderLayout.SOUTH);

        generation = new JLabel("Generation: 0");
        generation.setHorizontalAlignment(JLabel.CENTER);

        add(patternPanel);
        add(gamePanel);
        add(zoomPanel);
        add(edit);
        add(generation);  
    }
    public void addZoomInAction(ActionListener action){
        zoomIn.addActionListener(action);
    }
    public void addZoomOutAction(ActionListener action){
        zoomOut.addActionListener(action);
    }
    public void addStartAction(ActionListener action){
        start.addActionListener(action);
    }
    public void addNextAction(ActionListener action){
        next.addActionListener(action);
    }
    public void updateGeneration(int generation){
        this.generation.setText("Generation: "+generation);
    }
    public void addPatternSelection(ActionListener action){
        saves.addActionListener(action);
    }
    public void addSpeedSelection(ActionListener action){
        speed.addActionListener(action);
    }
    public void addSizeSelection(ActionListener action){
        zoom.addActionListener(action);
    }
    public String getPatternSelection(){
        return (String) saves.getSelectedItem();
    }
    public String getSpeedSelection(){
        return (String) speed.getSelectedItem();
    }
    public String getSizeSelection(){
        return (String) zoom.getSelectedItem();
    }
    public void setStartLabel(String label){
        start.setText(label);
    }
    public String getStartLabel(){
        return start.getText();
    }
    public void disableNext(boolean condition){
        next.setEnabled(!condition);
    }
    public void addEditAction(ActionListener action){
        edit.addActionListener(action);
    }
    public void setEditLabel(String label){
        edit.setText(label);
    }
}

/*
    GridPanel contains the main grid where the cells are drawn. 
    Only the cells that are alive are store, it is stored in cells.
    nHCells contains number of horizontal cells and nVCells contains number
        of vertical cells. Their values depend on the Panel's dimension and the
            cellSize which gives an illusion of infinite cells.
    cellSize is the height and width of each cell (since cells are square). 
        Updating cellSize gives an illusion of zoom in and zoom out which helps
            in infinite zoom in and zoom out.
    centX is the X coordinate of center cell and centY is the Y coordinate of
        the center cell.
*/
class GridPanel extends JPanel{
    private ArrayList<Point> cells;
    private double nHCells, nVCells;
    private int cellSize;
    private int centX, centY; //Center Points
    public GridPanel(){
        setBackground(Color.GRAY);
        cellSize = 15; //Default Size (Medium)
        cells = new ArrayList<>();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        nHCells = getWidth()/(double)cellSize;
        nVCells = getHeight()/(double)cellSize;
        centX = (int) (nHCells/2);
        centY = (int) (nVCells/2);

        g.setColor(Color.LIGHT_GRAY);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.YELLOW);
        for(int i=0;i<cells.size();i++)
            g2.fill(new Rectangle2D.Double(cells.get(i).getX()*cellSize,
            cells.get(i).getY()*cellSize, cellSize, cellSize));
        g2.setColor(Color.LIGHT_GRAY);

        for(int i=0;i<nHCells;i++)
            for(int j=0;j<nVCells;j++)
                g2.draw(new Rectangle2D.Double(i*cellSize,j*cellSize,cellSize,cellSize));
    }
    public void updateSize(int size){
        if(size<1) return;
        cellSize = size;
        repaint();
    }
    public void updateCells(ArrayList<Point> coords){
        cells.clear();
        for(Point p: coords)
            cells.add((Point) p.clone());
        repaint();
    }
    public void clearCells(){
        cells.clear();
        repaint();
    }
    public int getCellSize(){
        return cellSize;
    }
    public int getCentX(){
        return (int) centX;
    }
    public int getCentY(){
        return (int) centY;
    }
    public int getNumberCells(){
        return (int) (nHCells*nVCells);
    }
}
