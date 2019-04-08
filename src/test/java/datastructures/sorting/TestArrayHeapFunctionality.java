package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testInsertNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Should raise IllegalArgumentException");
        } catch (Exception e) {
            /* do nothing it is fine */
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.removeMin());
        assertEquals(3, heap.removeMin());
        try {
            heap.removeMin();
            fail("should raise EmptyContainerException");
        } catch (Exception e) {
            /* do nothing it is fine */
        }
    }

    @Test(timeout=SECOND)
    public void testPeekBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 10; i++) {
            heap.insert(i);
        }
        for (int j = 0; j < 10; j++) {
            assertEquals(heap.peekMin(), heap.removeMin());
        }
        try {
            assertEquals(heap.peekMin(), heap.removeMin());
            fail("should raise EmptyContainerException");
        } catch (Exception e){
            /* do nothing is fine */
        }
    }

    @Test(timeout=SECOND)
    public void testInsertandRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        heap.insert(6);
        heap.insert(2);
        heap.insert(8);
        heap.insert(0);
        heap.insert(5);
        heap.insert(7);
        heap.insert(9);
        heap.insert(3);
        assertEquals(0, heap.removeMin());
        assertEquals(2, heap.removeMin());
        assertEquals(3, heap.removeMin());
        assertEquals(3, heap.removeMin());
        assertEquals(5, heap.removeMin());
        assertEquals(6, heap.removeMin());
        assertEquals(7, heap.removeMin());
        assertEquals(8, heap.removeMin());
        assertEquals(9, heap.removeMin());
        assertEquals(0, heap.size());
    }

    @Test(timeout=SECOND)
    public void testInsertThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("should raise Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex){
            /* do nothing is fine*/
        }
    }

    @Test(timeout=SECOND)
    public void testResize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 100000; i++) {
            heap.insert(i);
        }
    }
}
