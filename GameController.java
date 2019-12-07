package project;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 *
 * @author Asif Rasheed, Shaham Kampala
 */

/*
    Game Controller contains instances of the GameData (Model) & GameView(View).
    Contains Runnable object which is used by the thread for automatic simulation
        when the start button is pressed.
    Continas Thread which is used for automatic simulation when the start button
        is pressed.
    Contains isRunning which is true is the automatic simulation is running. It 
        is false if the simulation is not running.
    Contains sleep which is the sleep time for the thread in milliseconds. It 
        helps in controlling the simulation speed.
    Contains doEdit which helps in identifying if edit mode is on or off.
*/
public class GameController {
    private GameData model;
    private GameView view;
    private Runnable run;
    private boolean isRunning = false;
    private boolean doEdit = false;
    private Thread game;
    private int sleep = 500; //Default Speed: normal

    GameController(GameData model, GameView view){
        /*Run would be used by the thread when the automatic simulation is on*/
        run = new Runnable(){
            @Override
            public void run() {
                try {
                    while(!Thread.currentThread().isInterrupted()){
                            //nextStep method is called
                            //View is Updated with new coordinates and generation
                            //Sleeps for given time in milliseconds
                            view.updateCells(model.nextStep());
                            view.updateGeneration(model.getGeneration()); 
                            Thread.sleep(sleep);
                        }
                    }
                catch (InterruptedException ex) {
                        return;
                    }
            }
        };
        this.model = model;
        this.view = view;
        /*
            Action listener for Edit Button.
            Toggles edit mode to on or off based on current state.
        */
        view.addEditAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(doEdit){
                    view.setEditLabel("Edit: Off");
                    doEdit = false;
                }
                else{
                    view.setEditLabel("Edit: On");
                    doEdit = true;
                }
            }   
        });
        /*
            Action listener for Start Button.
            Toggles automatic simulation based on the current state using thread.
                Enables or diables next button based on the new state.
                Updates the button label based on the new state.
        */
        view.addStartAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                if(isRunning){
                        game.interrupt();
                        isRunning = false;
                        view.disableNext(isRunning);
                        view.setStartLabel("Start");
                    }
                else{
                    game = new Thread(run);
                    game.start();
                    isRunning = true;
                    view.disableNext(isRunning);
                    view.setStartLabel("Stop");
                }
            }
        });
        /*
            Action Listener for the Speed List.
            Updates sleep time based on the selection.
        */
        view.addSpeedSelection(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(view.getSpeedSelection()=="Slow") sleep=1500;
                else if(view.getSpeedSelection()=="Fast") sleep = 100;
                else sleep = 500;
            }
        });
        /*
            Action Listener for Size List.
            Updates cell size based on the selection.
        */
        view.addSizeSelection(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String size = view.getSizeSelection();

                if(size=="Small") view.updateSize(5);
                else if(size=="Medium") view.updateSize(15);
                else view.updateSize(20);

                view.updateCells(model.getCoords());
            }
        });
        /*
            Action Listener for Next Button.
            Calls nextStep method.
        */
        view.addNextAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    view.updateCells(model.nextStep());
                    view.updateGeneration(model.getGeneration());
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        /*
            Action Listener for Pattern List.
            Loads the selected well known pattern from file.
        */
        view.addPatternSelection(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String file = view.getPatternSelection();
                    view.updateCells(model.getCoords(file+".dat",file,view.getCentX(),view.getCentY()));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        /*
            Action Listener for Zoom In button.
            Increased cellSize by 1.
        */
        view.addZoomInAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                view.updateSize(view.getCellSize()+1);
            }
            
        });
        /*
            Action Listener for Zoom Out button.
            Decreased cellSize by 1.
        */
        view.addZoomOutAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                view.updateSize(view.getCellSize()-1);
            } 
        });
        /*
            Mouse Adapter for gridPanel.
            Helps in implementing the following features:
                Drag, Right Click to show popup menu for save and open & click to
                    toggle cell (is doEdit is true).
        */
        MouseAdapter gridMouseAdapt = new MouseAdapter(){
            Point startingPoint;
            boolean dragged = false;
            @Override
            public void mousePressed(MouseEvent e) {
                startingPoint = e.getPoint();
                view.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e){
                view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if(SwingUtilities.isRightMouseButton(e)){
                    view.showMenu(e);
                    return;
                }
                if(doEdit&!dragged){
                    model.toggleCoord(e.getX()/view.getCellSize(), e.getY()/view.getCellSize());
                    view.updateCells(model.getCoords());
                    model.resetGeneration();
                    view.updateGeneration(model.getGeneration());
                }
                dragged = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragged = true;
                // dragging to the left (to see more on the right)
                if(startingPoint.x > e.getX()) model.shiftGrid("Left");
                    
                //dragging to the right (to see more on the left)
                if(startingPoint.x < e.getX()) model.shiftGrid("Right");
                
                //dragging to the top (to see more on the bottom)
                if(startingPoint.y > e.getY()) model.shiftGrid("Up");

                //dragging to the bottom (to see more on the top
                if(startingPoint.y < e.getY()) model.shiftGrid("Down");
                
                    view.updateCells(model.getCoords());
                    startingPoint = e.getPoint();
            }

        };
        
        view.addGridListener(gridMouseAdapt);
        /*
            Action Listener for Exit Button.
            Exits the program with code 0. 
        */
        view.addExitAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        /*
            Action Listener for Save Button.
            Opens File Chooser Save Dialogue where user could select the 
                directory and file name of save file.
        */
        view.addSaveAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                fc.showSaveDialog(null);
                File file = fc.getSelectedFile();
                if(file==null) return;
                try {
                    model.save(file.getPath(), file.getName());
                } catch (IOException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
                view.repaint();
            }
        });
        /*
            Action Listener for Open Button.
            Opens File Chooser Open Dialogue where user could select the 
                directory of file to be opened.
        */
        view.addOpenAction(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                fc.showOpenDialog(null);
                fc.setMultiSelectionEnabled(false);
                File file = fc.getSelectedFile();
                if(file==null) return;
                try {
                    view.updateCells(model.getCoords(file.getPath(),
                            file.getName().replace(".dat", ""),
                            view.getCentX(), view.getCentY()));
                    view.updateGeneration(model.getGeneration());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
