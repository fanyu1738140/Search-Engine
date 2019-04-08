package datastructures.sorting;

import misc.BaseTest;
import org.junit.Test;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Sorter;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapAndSorterStress extends BaseTest {

    @Test(timeout=10*SECOND)
    public void testTopKSortLargeInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }
        IList<Integer> output = Sorter.topKSort(50000, list);
        for (int i = 0; i < 50000; i++) {
            assertEquals(i + 50000, output.get(i));
        }
        assertEquals(50000, output.size());
    }

    @Test(timeout=10*SECOND)
    public void testArrayHeapLargeInput() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 0; i < 100000; i++) {
            heap.insert(i);
        }
        for (int i = 200000; i > 100000; i--) {
            heap.insert(i);
        }
        for (int i = 0; i < 200000; i++) {
            heap.removeMin();
        }
    }
}
