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
    private Song head, tail;

    public DoublyLinkedPlaylist() {
        head = tail = null;
    }

    /**
     * @return the head
     */
    public Song getHead() {
        return head;
    }

    //accessor methods
    /**
     * @return the tail
     */
    public Song getTail() {
        return tail;
    }

    //method to add a song object to the end of the list while maintaining head and tail
    public void addLastSong(String name, String path) {
        if (name != null) {
            //create a new song object to add to list
            Song newSong = new Song(name, path);
            if (getHead() != null) {
                //use the tail.next attribute to create new node at end of list
                //also set the new objects 'prev' attribute to tail node
                //this ensures the list is doubly linked
                tail.next = newSong;
                newSong.prev = getTail();
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
        Song temp = getHead();
        while (temp.next != null) {
            temp = temp.next;
        }
        tail = temp;
    }

    //method to get the length of the linkedList as an int
    public int getLengthOfPlaylist() {
        Song temp = getHead();
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
        //find the associated node object
        Song target = binarySearch(name);
        if (target == null) {
            return;
        }//delete the node by replacing it with another
        if (target == getHead()) {
            head = target.next;
        }
        //link the surrounding nodes back together
        if (target.next != null) {
            target.next.prev = target.prev;
        }
        if (target.prev != null) {
            target.prev.next = target.next;
        }
        //delete the target node
        target = null;
    }

    //method to binary search the linked list playlist
    public Song binarySearch(String target) {
        //boolean found = false;
        //int pointer = 0;
        if (getHead() == null) {
            return null;
        }
        Song start = getHead(), end = getTail();
        //recursive search function, while start and end are not the same, or start not target
        while (start != end) {
            //find the middle
            Song middle = getMiddleForSearch(start, end);
            if (middle.getName().toString().compareToIgnoreCase(target) == 0) {
                return middle;
            } else if (middle.getName().toString().compareTo(target) < 0) {
                //if middle less than target
                if (middle.next != null) {
                    start = middle.next;
                } else {
                    start = middle;
                }
            } else if (middle.getName().toString().compareTo(target) > 0) {
                //if middle more than target
                if (middle.prev != null) {
                    end = middle.prev;
                } else {
                    end = middle;
                }
            }
        }//if start was target at some point
        if (start.getName().toString().compareToIgnoreCase(target) == 0) {
            return start;
        } else {
            return null;
        }
    }
    //method to get middle of list used by binary search
    private Song getMiddleForSearch(Song start, Song end){
        if (start == null){
            return null;
        }
        Song fast = start, slow = start;
        while ((!(fast.getName().toString().compareTo(end.getName().toString()) == 0)) 
                && (!(fast.next.getName().toString().compareTo(end.getName().toString()) == 0))) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    //method for adding a node calls this method when the list is > 1 to check for duplicate song names
    public Song checkDuplicates(String name) {
        Song dupe = getHead();
        while (dupe != null) {
            if (dupe.getName().toString().compareTo(name) == 0) {
                return dupe;
            }
            dupe = dupe.next;
        }
        return null;
    }
}
