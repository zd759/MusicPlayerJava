/*
 * Song class represents an instance of a Song which has attributes of name and 
 * path, and next and previous songs of type Song. It is a node of the LinkedList.
 */
package musicplayerproject;

/**
 *
 * @author Zara
 * @param <T>
 */
public class Song <T> {
    //attributes
    private final T name;
    private final T path;
    public Song prev, next;
    
    //default constructor for new node
    public Song(T name, T path){
        this.name = name;
        this.path = path;
    }
    
    //assessor methods
    /**
     * @return the name
     */
    public T getName() {
        return name;
    }

    /**
     * @return the path
     */
    public T getPath() {
        return path;
    }
    
    //override to strnig method for displaying in listView
    @Override
    public String toString(){
        return name.toString();
    }
}
