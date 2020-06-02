/*
 * This class is a blueprint to create a doubly linked list data structure which
 * represents a playlist and stores Songs as nodes within the list. It can be 
 * traversed backwards and forwards, has a head and tail node of type Song as 
 * attributes, and is Merge sorted upon Song add. Can be Binary searched.
 */
package musicplayerproject;

/**
 *
 * @author Zara
 * @param <E>
 */
public class DoublyLinkedPlaylist<E> { //E = Element. Can be Song or another class

    //attributes
    public Song head, tail;

    public DoublyLinkedPlaylist() {
        head = tail = null;
    }

    //method to add a song object to the end of the list while maintaining head and tail
    public void addLastSong(String name, String path) {
        if (name != null) {
            //create a new song object to add to list
            Song newSong = new Song(name, path);
            if (head != null) {
                //use the tail.next attribute to create new node at end of list
                //also set the new objects 'prev' attribute to tail node
                //this ensures the list is doubly linked
                tail.next = newSong;
                newSong.prev = tail;
                setTailNode();
            } else { //case if list is empty
                head = newSong;
                //call method to set tail node
                setTailNode();
            }
        }
    }

    //private method to set the tail node as items are added to the list
    private void setTailNode() {
        Song temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        tail = temp;
    }

    //method to get the length of the linkedList as an int
    public int getLengthOfPlaylist() {
        Song temp = head;
        int count = 0;
        while (temp != null) {
            count++;
            temp = temp.next;
        }
        return count;
    }

    //method to merge sort the linked list, split and putting back together
    public Song mergeSort(Song head) {
        //base case
        if (head == null || head.next == null) {
            return head;
        }
        //get middle of list
        Song middle = getMiddleNode(head);
        Song middleNext = middle.next;
        //set next of middle node to null
        middle.next = null;
        //apply merge sort to both left and right of the list
        Song left = mergeSort(head);
        Song right = mergeSort(middleNext);
        //merge the lists together once recursion is finished
        return sortedMerge(left, right);
    }

    //method to get the middle of the list used by merge sort method to split the list
    private Song getMiddleNode(Song head) {
        //base case
        if (head == null || head.next == null) {
            return head;
        }
        Song slow = head;
        Song fast = head;
        //move fast by 2 and slow by 1 each iteration
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    //method used by merge sort method to compare strings
    private Song sortedMerge(Song left, Song right) {
        //base case
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        //pick left or right and recur
        if (left.getName().toString().compareTo(right.getName().toString()) < 0) {
            left.next = sortedMerge(left.next, right);
            left.next.prev = left;
            left.prev = null;
            return left;
        } else {
            right.next = sortedMerge(left, right.next);
            right.next.prev = right;
            right.prev = null;
            return right;
        }
    }//end merge sort methods

    //method to remove a node from the list given the name - uses binary search to find the node
    public void Remove(String name) {
        Song target = binarySearch(name);
        if (target == null) {
            return;
        } else {
            Song next = target.next;
            Song prev = target.prev;
            //link the surrounding nodes back together
            prev.next = next;
            next.prev = prev;
            //delete the target node
            target = null;
        }
    }

    //method to binary search the linked list playlist
    public Song binarySearch(String target) {
        boolean found = false;
        int pointer = 0;
        if (head == null) {
            return null;
        }
        Song start = head, end = tail;
        //recursive search function, while start and end are not the same
        while (!(start != end)) {
            //find the middle
            Song middle = getMiddleNode(start);
            if (middle.getName().toString().compareTo(target) == 0) {
                return middle;
            } else if (middle.getName().toString().compareTo(target) < 0) { 
                //if middle more than target
                start = middle.next;
            } else if (middle.getName().toString().compareTo(target) > 0) { 
                //if middle less than target
                end = middle;
            }
        }
        return null;
    }
    
    //method for adding a node calls this method when the list is > 1 to check for duplicate song names
    public Song checkDuplicates(String name){
        Song dupe = head;
        while (dupe != null){
            if (dupe.getName() == name){
                return dupe;
            }
            dupe = dupe.next;
        }
        return null;
    }
}
