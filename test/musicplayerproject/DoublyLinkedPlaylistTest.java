/*
 * Name: Zara Duncanson
 * Student ID: P229768
 * Date: 27/05/20
 * Task: Final Project
 * This is a JUnit test class
 */
package musicplayerproject;

import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author Zara
 */
public class DoublyLinkedPlaylistTest {
    
    public DoublyLinkedPlaylistTest() {
    }
    //test list
    DoublyLinkedPlaylist<String> testList = new DoublyLinkedPlaylist<>();
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getHead method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testGetHead() {
        System.out.println("getHead");
        DoublyLinkedPlaylist instance = new DoublyLinkedPlaylist();
        Song expResult = null;
        Song result = instance.getHead();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTail method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testGetTail() {
        System.out.println("getTail");
        DoublyLinkedPlaylist instance = new DoublyLinkedPlaylist();
        Song expResult = null;
        Song result = instance.getTail();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addLastSong method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testAddLastSong() {
        testList.addLastSong("name", "path");
        assertEquals(1, testList.getLengthOfPlaylist());
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getLengthOfPlaylist method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testGetLengthOfPlaylist() {
        System.out.println("getLengthOfPlaylist");
        DoublyLinkedPlaylist instance = new DoublyLinkedPlaylist();
        int expResult = 0;
        int result = instance.getLengthOfPlaylist();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mergeSort method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testMergeSort() {
        System.out.println("mergeSort");
        Song head = null;
        DoublyLinkedPlaylist instance = new DoublyLinkedPlaylist();
        Song expResult = null;
        Song result = instance.mergeSort(head);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Remove method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testRemove() {
        System.out.println("Remove");
        String name = "";
        DoublyLinkedPlaylist instance = new DoublyLinkedPlaylist();
        instance.Remove(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of binarySearch method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testBinarySearch() {
        System.out.println("binarySearch");
        String target = "";
        DoublyLinkedPlaylist instance = new DoublyLinkedPlaylist();
        Song expResult = null;
        Song result = instance.binarySearch(target);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkDuplicates method, of class DoublyLinkedPlaylist.
     */
    @Test
    public void testCheckDuplicates() {
        System.out.println("checkDuplicates");
        String name = "";
        DoublyLinkedPlaylist instance = new DoublyLinkedPlaylist();
        Song expResult = null;
        Song result = instance.checkDuplicates(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
