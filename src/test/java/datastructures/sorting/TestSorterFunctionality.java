package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Sorter;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSorterFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testNullInput() {
        IList<Integer> list = null;
        try {
            Sorter.topKSort(10, list);
            fail("should raise IllegalArgumentException");
        } catch (Exception e) {
            // do noting is fine
        }

        list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        try {
            Sorter.topKSort(-1, list);
            fail("should raise IllegalArgumentException");
        } catch (Exception e) {
            // do noting is fine
        }
    }

    @Test(timeout=SECOND)
    public void testTopKSortBasics() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(12);
        list.add(72);
        list.add(82);
        list.add(562);
        list.add(22);
        list.add(0);
        list.add(912);
        list.add(152);
        list.add(422);
        list.add(332);
        list.add(122);

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        assertEquals(152, top.get(0));
        assertEquals(332, top.get(1));
        assertEquals(422, top.get(2));
    }


    @Test(timeout=SECOND)
    public void testKZInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        IList<Integer> slist = Sorter.topKSort(0, list);
        assertEquals(0, slist.size());
    }

    @Test(timeout=SECOND)
    public void testBiggerInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        IList<Integer> slist = Sorter.topKSort(5, list);
        assertEquals(2, slist.get(1));
    }
}
