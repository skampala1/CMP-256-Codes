package project;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Asif Rasheed
 */
public class GameData {
    /*
        The model stores the coordinates of cells that are alive and their 
            generation. This way for next generation, the algorithm has to look
                at only the cells that are alive and their neighbours instead of
                    the entire grid.
        The data is stored in .dat file in the HardDisk / SSF and is retrieve
            only when the user selects it.

        ArrayList<Point> coords: Contains coordinates of current selection
        int generation: Contains the current generation
    */
    private ArrayList<Point> coords;
    private int generation = 0;


    GameData() throws IOException{
        coords = new ArrayList<Point>();

            /*If any of the famous pattern file is missing*/
            if(new File("Glider.dat").createNewFile()){
                PrintWriter coord = new PrintWriter("Glider.dat");
                coord.println("Glider 0");
                coord.println("1 0");
                coord.println("2 1");
                coord.println("0 2");
                coord.println("1 2");
                coord.println("2 2");
                coord.flush();
                coord.close();
            }
            if(new File("R-Pentomino.dat").createNewFile()){
                PrintWriter coord = new PrintWriter("R-Pentomino.dat");
                coord.println("R-Pentomino 0");
                coord.println("1 0");
                coord.println("2 0");
                coord.println("0 1");
                coord.println("1 1");
                coord.println("1 2");
                coord.flush();
                coord.close();
            }
            if(new File("Small Explorer.dat").createNewFile()){
                PrintWriter coord = new PrintWriter("Small Explorer.dat");
                coord.println("Small Explorer 0");
                coord.println("1 0");
                coord.println("0 1");
                coord.println("1 1");
                coord.println("2 1");
                coord.println("0 2");
                coord.println("2 2");
                coord.println("1 3");
                coord.flush();
                coord.close();
            }
            if(new File("Small Space Ship.dat").createNewFile()){
                PrintWriter coord = new PrintWriter("Small Space Ship.dat");
                coord.println("Small Space Ship 0");
                coord.println("1 0");
                coord.println("2 0");
                coord.println("3 0");
                coord.println("4 0");
                coord.println("0 1");
                coord.println("4 1");
                coord.println("4 2");
                coord.println("0 3");
                coord.println("3 3");
                coord.flush();
                coord.close();
            }
            if(new File("Tumbler.dat").createNewFile()){
                PrintWriter coord = new PrintWriter("Tumbler.dat");
                coord.println("Tumbler 0");
                coord.println("1 2");
                coord.println("2 2");
                coord.println("4 2");
                coord.println("5 2");
                coord.println("1 3");
                coord.println("2 3");
                coord.println("4 3");
                coord.println("5 3");
                coord.println("2 4");
                coord.println("4 4");
                coord.println("0 5");
                coord.println("2 5");
                coord.println("4 5");
                coord.println("6 5");
                coord.println("0 6");
                coord.println("2 6");
                coord.println("4 6");
                coord.println("6 6");
                coord.println("0 7");
                coord.println("1 7");
                coord.println("5 7");
                coord.println("6 7");
                coord.flush();
                coord.close();
            }
    }

    /*Saves game to given directory*/
    public void save(String dir, String save) throws IOException{
        File buffer = new File(dir+".dat");
        buffer.createNewFile(); //If file doesn't exist, will create new one

        PrintWriter f = new PrintWriter(new File(dir+".dat"));
        f.println(save+" "+this.generation);
        for(int i=0;i<coords.size();i++)
        {
          Point p = coords.get(i);
          f.println((int)p.getX()+" "+(int)p.getY());
        }
        f.close();
    }

    /*
        public ArrayList<Point> getCoords(String patter) returns coordinates from file.
        If the file is not found or corrupt, the method returns null.
    */
    public ArrayList<Point> getCoords(String path, String save, int centX, int centY) throws FileNotFoundException{
        coords.clear();
        if(save=="Clear") return coords;

        File buffer = new File(path);
        if(buffer==null) return coords; //If file doesn't exist

        Scanner inp = new Scanner(buffer);
        inp.skip(save);
        this.generation = inp.nextInt();

        while(inp.hasNextInt()) {
          int x = inp.nextInt();
          int y = inp.nextInt();
          coords.add(new Point(x,y));
        }

        inp.close();

        shiftCoords(centX,centY);

        return coords;
    }
    /*Returns currently selected coordinates*/
    public ArrayList<Point> getCoords(){
        return coords;
    }
    
    /*Returns the current generation*/
    public int getGeneration(){
        final int ret = generation;
        return ret;
    }

    /*Increases generation by 1*/
    public int increaseGeneration(){
        return ++generation;
    }

    /*Arranges the coordinates with respect to the given Center Coordinates*/
    public void shiftCoords(int centX,int centY){
        int x=0;
        int y=0;
        for(Point p: coords)
        {
            x+=p.getX();
            y+=p.getY();
        }
        x=x/coords.size();
        y=y/coords.size();
        int distX = centX-x;
        int distY = centY-y;
        
        for(int i=0;i<coords.size();i++){
            Point p = coords.get(i);
            p.move((int)(p.getX()+distX), (int)(p.getY()+distY));
            coords.set(i, p);
        }
    }

    /*Adds a coordinate to the list*/
    public void addCoord(int x, int y){
        coords.add(new Point(x,y));
    }

    /*Deletes the given coordinate from list*/
    public boolean removeCoord(int x, int y){
        return coords.remove(new Point(x,y));
    }

    /*Removes the coordinate if it already exist, else adds it*/
    public void toggleCoord(int x, int y){
        if(!removeCoord(x,y)) addCoord(x,y);
    }

    /*Returns true if the cell is alive (if the coordinates are on the list)*/
    public boolean isAlive(int x, int y){
        return coords.contains(new Point(x,y));
    }

    /*Updates the coordinates*/
    public void updateCoords(ArrayList<Point> coords){
        this.coords = coords;
    }
    
    /*Resets generation count*/
    public void resetGeneration(){
        generation = 0;
    }
    
    /*
        Shifts cells, used for drag
        Shifts the cells to the desired direction based on drag which helps
            in giving an illusion of infinite cells.
    */
    public void shiftGrid(String direction){
        for(int i=0; i<coords.size(); i++){
                if(direction.equals("Left")) {
                    Point coord = coords.get(i);
                    coord.move((int)coord.getX()-1,(int)coord.getY());
                    coords.set(i, coord);
                }
                if(direction.equals("Right")) {
                  Point coord = coords.get(i);
                  coord.move((int)coord.getX()+1,(int)coord.getY());
                  coords.set(i, coord);
                }
                if(direction.equals("Up")) {
                  Point coord = coords.get(i);
                  coord.move((int)coord.getX(),(int)coord.getY()-1);
                  coords.set(i, coord);
                }
                if(direction.equals("Down")) {
                  Point coord = coords.get(i);
                  coord.move((int)coord.getX(),(int)coord.getY()+1);
                  coords.set(i, coord);
                }
            }
    }
    /*
        nextStep method implements the next step algorithm. 
            Create a new ArrayList of Point, adds only the alive cells with 2 or 
                3 neighbours and dead cells with exactly 3 neighbours and update
                    the old coordinates with the new ArrayList.
    */
    public ArrayList<Point> nextStep() throws InterruptedException{
        ArrayList<Point> newCoords = new ArrayList<>();
        Thread kk = new Thread(new Runnable(){
            @Override
            public void run() { 
            for(int i=0; i<coords.size();i++){
            Point p = coords.get(i);
            int x = (int) p.getX();
            int y = (int) p.getY();
            int neightbour = countNeighbour(x,y);

            if(neightbour==2|neightbour==3) newCoords.add(new Point(x,y));

            for(int y2= y-1; y2<y+2; y2++)
                for(int x2= x-1; x2<x+2; x2++)
                    if(!(x2==x&y2==y)&!isAlive(x2,y2)&countNeighbour(x2,y2)==3&!newCoords.contains(new Point(x2,y2)))
                      newCoords.add(new Point(x2,y2));
                }
            }   
        });
        kk.start();
        kk.join();
        
        updateCoords(newCoords);
        increaseGeneration();
        return newCoords;
    }
    /*
        Counts the number of neighbours for the given coordinate.
    */
    public int countNeighbour(int x, int y){
        int neightbour = 0;

        for(int y2= y-1; y2<y+2; y2++)
            for(int x2= x-1; x2<x+2; x2++)
                if (!(x2==x&y2==y)&isAlive(x2, y2)) ++neightbour;

        return neightbour;
    }
}
