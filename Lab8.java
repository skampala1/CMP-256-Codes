package lab.pkg8;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asif Rasheed, Shaham Kampala
 */
public class Lab8 {
    public static void main(String[] args) {
        
      ArrayList<String> books = new ArrayList<String>();
      for(int i=0; i<15; i++)
          books.add("Book "+(i+1));
      
      Library library = new Library(books);
      
      for(int i=0;i<10;i++)
          new Thread(new Visitor(library)).start();
    }
}

class BookUnavailableException extends Throwable{
    String message;
    
    BookUnavailableException(){
        message = "The book is unavailable!";
    }
    BookUnavailableException(String book){
        message = book + " is unavailable!";
    }
    
    @Override
    public String getMessage(){
        return message;
    }
}

class Library{
    private ArrayList<String> books;
    
    public Library(ArrayList<String> books){
        this.books = (ArrayList<String>) books.clone();
    }
    
    public synchronized void borrowBook(String book) throws BookUnavailableException{
        boolean found = false;
        
            for(int i=0; i<books.size(); i++)
                if(books.get(i).equals(book))
                {
                    System.out.println(" borrows "+book);
                    books.remove(i);
                    found = true;
                
        }
        
        if(!found)
            throw new BookUnavailableException(book);
    }
    
    public synchronized void returnBook(String book){
                    books.add(book);
                    System.out.println(" returns "+book);
                
    }
    
    public ArrayList<String> getAvailableBooks(){
        return (ArrayList<String>) this.books.clone();
    }
}

class Visitor implements Runnable{
    private Library library;
    private static int COUNT =0;
    private int id;
    
    Visitor(Library library){
        this.library = library;
        id=++COUNT;
    }
    
    @Override
    public void run() {
        while(true)
        {            
            ArrayList<String> uBooks = library.getAvailableBooks();
            Random rand = new Random();
        
            synchronized(library){                
                String book = uBooks.get(rand.nextInt(uBooks.size()));
                
                try{
                    System.out.print("Visitor "+id);
                    library.borrowBook(book);
                    library.notify();
                    library.wait();
                } catch (BookUnavailableException ex) {
                    this.run();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Visitor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.print("Visitor "+id);
                library.returnBook(book);
                library.notify();    
                try {
                    library.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Visitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
     }
}

