import java.lang.reflect.Array;
import java.util.*;

/* Time spent on a5: 5 hours and 30 minutes.
* When you change the above, please do it carefully. Change hh to the hours and mm
* to the minutes and leave everything else as is. If the minutes are 0, change mm
* to 0. This will help us in extracting times and giving you the average and max.
* 
* Name: Philip Cipollina
* Netid (s):pjc272
* What I thought about this assignment: I kept making stupid mistakes that took way too long
* to figure out; there also wasn't as much time for this assignment as I would have liked due
* to prelims.  Not sure how to check if my functions run in the right time.
*
*
*/

/** An instance is a max-heap or a min-heap of distinct values of type E
 *  with priorities of type double. */
public class Heap<E> {

    /** Class Invariant:
     *   1. c[0..size-1] represents a complete binary tree. c[0] is the root;
     *      For each h, c[2h+1] and c[2h+2] are the left and right children of c[h].
     *      If h != 0, c[(h-1)/2] (using integer division) is the parent of c[h].
     *   
     *   2. For h in 0..size-1, c[h] contains the value and its priority.
     *      The values in c[size..] may or may not be null.
     *      DO NOT RELY ON THEM BEING NULL.
     *   
     *   3. The values in c[0..size-1] are all different.
     *   
     *   4. For h in 1..size-1,
     *      if isMaxHeap, (c[h]'s priority) <= (c[h]'s parent's priority),
     *      otherwise,    (c[h]'s priority) >= (c[h]'s parent's priority).
     *   
     *   dict and the tree are in sync, meaning:
     *   
     *   5. The keys of dict are the values in c[0..size-1].
     *      This implies that size = dict.size().
     *   
     *   6. if value v is in c[h], then dict.get(v) returns h.
     */
    protected final boolean isMaxHeap;
    protected data[] c;
    protected int size;
    protected HashMap<E, Integer> dict; // "dict" for dictionary
    
    /** Constructor: an empty max-heap with capacity 10. */
    public Heap() {
        isMaxHeap= true;
        c= createVPArray(10);
        dict= new HashMap<E, Integer>();
    }

    /** Constructor: an empty heap with capacity 10.
     *  It is a max-heap if isMax is true, a min-heap if isMax is false. */
    public Heap(boolean isMax) {
        isMaxHeap= isMax;
        c= createVPArray(10);
        dict= new HashMap<E, Integer>();
    }

    /** A data object houses a value and a priority. */
    class data {
        E val;             // The value
        double priority;   // The priority

        /** An instance with value v and priority p. */
        data(E v, double p) {
            val= v;
            priority= p;
        }

        /** Return a representation of this data object. */
        @Override public String toString() {
            return "(" + val + ", " + priority + ")";
        }
    }

    /** Add v with priority p to the heap.
     *  Throw an illegalArgumentException if v is already in the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. */
    public void insert(E v, double p) throws IllegalArgumentException {
        // TODO #1: Write this whole method. Note that bubbleUp is not implemented,
        // so calling it has no effect (yet). 
        
        // Instructions. 
        // 1. Read the spec of ensureSpace and the hint in its body.
        // 2. Calling bubbleUp is the last thing to be done. Make sure
        // the class invariant is true (except for value v being in wrong place)
        // before bubbling up.
        
        // Testing: The first tests of insert, procedures
        // test00insert and test01insertException, should work if this method
        // is written properly, even if ensureSpace and bubbleUp are not yet
        // written.
    	if(dict.get(v)!=null) throw new IllegalArgumentException("Value v is already in the tree");
        ensureSpace();
        c[size]=new data(v,p);
        dict.put(v, size);
        size=size+1;
        bubbleUp(size-1);
    }

    /** If size = length of c, double the length of array c.
     *  The worst-case time is proportional to the length of c. */
    protected void ensureSpace() {
        //TODO #2. Note: Any method that increases the size of the heap must
        // call this method before increasing the size. 
        
        // If this method is written correctly, testing procedures
        // test10ensureSpace, test11ensureSpace, and
        // test12ensureSpace will work correctly
        if(size==c.length) {
        	data[]d=createVPArray(c.length*2);
        	for(int i=0;i<c.length;i++) {
        		d[i]=c[i];
        	}
        	c=d;
        }
    }

    /** Return the size of this heap.
     *  This operation takes constant time. */
    public int size() { // Do not change this method
        return size;
    }

    /** Swap c[h] and c[k].
     *  Precondition: 0 <= h < heap-size, 0 <= k < heap-size. */
    void swap(int h, int k) {
        assert 0 <= h  &&  h < size  &&  0 <= k  &&  k < size;
        //TODO 3: When bubbling values up and (later on) down, two values,
        // say c[h] and c[k], will have to be swapped. At the same time,
        // the definition of dict has to be maintained.
        // In order to always get this right, use method swap for this.
        // Method swap is tested by testing procedure test13Swap
        // --it will find no errors if you write this method properly.
        // 
        // Read the Assignment A5 Piazza note about dict.put(...).

        data temp=c[h];
        c[h]=c[k];
        c[k]=temp;
        dict.put(c[h].val, h);
        dict.put(c[k].val, k);
    }

    /** If a value with priority p1 should be above a value with priority
     *       p2 in the heap, return 1.
     *  If priority p1 and priority p2 are the same, return 0.
     *  If a value with priority p1 should be below a value with priority
     *       p2 in the heap, return -1.
     *  This is based on what kind of a heap this is,
     *  E.g. a min-heap, the value with the smallest priority is in the root.
     *  E.g. a max-heap, the value with the largest priority is in the root.
     */
    public int compareTo(double p1, double p2) {
        if (p1 == p2) return 0;
        if (isMaxHeap) {
            return p1 > p2 ? 1 : -1;
        }
        return p1 < p2 ? 1 : -1;
    }

    /** If c[m] should be above c[n] in the heap, return 1.
     *  If c[m]'s priority and c[n]'s priority are the same, return 0.
     *  If c[m] should be below c[n] in the heap, return -1.
     *  This is based on what kind of a heap this is,
     *  E.g. a max-heap, the value with the largest priority is in the root.
     *  E.g. a min-heap, the value with the smallest priority is in the root.
     */
    public int compareTo(int m, int n) { 	
    	return compareTo(c[m].priority, c[n].priority);
    }

    /** Bubble c[k] up the heap to its right place.
     *  Precondition: 0 <= k < size and
     *       The class invariant is true, except perhaps
     *       that c[k] belongs above its parent (if k > 0)
     *       in the heap, not below it. */
    void bubbleUp(int k) {
        // TODO #4 This method should be called within insert in order
        // to bubble a value up to its proper place, based on its priority.
        // Do not use recursion. Use iteration.
        // Use method compareTo to test whether value k is in its right place.
        // If this method is written properly, testing procedures beginning with
        // test15, test16, and test17 will not find any errors.
        assert 0 <= k  &&  k < size;
        while(0<k) {
        	if(compareTo(k,(k-1)/2)>0) {
        		swap(k,(k-1)/2);k=(k-1)/2;
        	}
        	else k=0;
        	}
    }

    /** If this is a max-heap, return the heap value with highest priority
     *  If this is a min-heap, return the heap value with lowest priority.
     *  Do not change the heap. This operation takes constant time.
     *  Throw a NoSuchElementException if the heap is empty. */
    public E peek() {
        // TODO 5: Do peek. This is an easy one. If it is correct,
        //         test25MinPeek() and test26MaxPeek will show no errors.
    	if(size==0)throw new NoSuchElementException();
        return c[0].val;
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: 0 <= k < size   and
     *           Class invariant is true except that perhaps
     *           c[k] belongs below one or both of its children. */
    void bubbleDown(int k) {
        // TODO 6: We suggest implementing and using upperChild, though
        //         you don't have to. DO NOT USE RECURSION. Use iteration.
        //         When this method is correct, all testing procedures
        //         beginning with test30, and test31 will not find errors.
        assert 0 <= k  &&  k < size;
        while(k<size) {
        	int child=upperChild(k);
        	if(compareTo(k,child)<0) {
        		swap(k,child);k=child;
        	}
        	else k=size;
        }

    }

    /** If c[n] doesn't exist or has no child, return n.
     *  If c[n] has one child, return its index.
     *  If c[n] has two children with the same priority, return the
     *      index of the right one.
     *  If c[n] has two children with different priorities return the
     *      index of the one that must appear above the other in a heap. */
    protected int upperChild(int n) {
    	int lchild=2*n+1; int rchild=2*n+2;
        if(n>=size||n<0) return n;//doesn't exist
        if(lchild>=size||lchild<=0) return n;//no children
        if(rchild>=size||rchild<=0) return lchild;//one child only
        if(compareTo(lchild,rchild)==0) return rchild;//same priority
        if(compareTo(rchild,lchild)==-1) return lchild;//left is correct
        if(compareTo(rchild,lchild)==1) return rchild;//right is correct
        return n;//all other cases
    }

    /** If this is a max-heap, remove and return heap value with highest priority.
     * If this is a min-heap, remove and return heap value with lowest priority.
     * The expected time is logarithmic and the worst-case time is linear
     * in the size of the heap.
     * Throw a NoSuchElementException if the heap is empty. */
    public E poll() {
        // TODO 7: When this method is written correctly, testing procedure
        //         beginning with test32, test33, and test34 will not find errors.
        // 
        //         Note also testing procedure test40DuplicatePriorities
        //         This method tests to make sure that when bubbling up or down,
        //         two values with the same priority are not swapped.
    	if(size==0) throw new NoSuchElementException();
    	E value=c[0].val;dict.remove(value);//store value of top in local variable
    	size=size-1;
    	if(size==0) {c[0]=null;return value;}
    	c[0]=c[size];dict.put(c[0].val, 0);
    	bubbleDown(0);
    	return value;

    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  Throw an IllegalArgumentException if v is not in the heap. */
    public void changePriority(E v, double p) {
        // TODO  8: When this method is correctly implemented, testing procedures
        //          beginning with test50 won't find errors.
        //          Also, if these work, testing procedures test70Strings
        //          and test90bigTests should not find errors.

    	Integer h=dict.get(v);
    	if(h==null) throw new IllegalArgumentException();
    	double temp=c[h].priority;
    	c[h].priority=p;
    	if(compareTo(p,temp)>0)bubbleUp(h);
    	if(compareTo(p,temp)<0)bubbleDown(h);
    }

    /** Create and return an array of size n.
     *  This is necessary because generics and arrays don't interoperate nicely.
     *  A student in CS2110 would not be expected to know about the need
     *  for this method and how to write it. We had to search the web to
     *  find out how to do it. */
    data[] createVPArray(int n) {
        return (data[]) Array.newInstance(data.class, n);
    }
}