package dijkstra;

public class FibonacciHeap {
    /** Points to the minimum node in the heap. */
    private Node min;

    /** Number of nodes in the heap. */
    private int node_num;
    
    public class Node {
        /** first child node */
        Node child;

        /** left sibling node */
        Node left;

        /** parent node */
        Node parent;

        /** right sibling node */
        Node right;

        /**
         * true if this node has had a child removed since this node was added
         * to its parent
         */
        boolean ChildCut;
        /** the number of node in our project **/
        int key;
        
        /** key value for this node */
        int value;

        /** number of children of this node (does not count grandchildren) */
        int degree;

        /**
         * Default constructor.  Initializes the right and left pointers,
         * making this a circular doubly-linked list.
         *
         * @param key initial key for node
         */
        public Node( int x_key, int x_value ) {
            right     = this;
            left      = this;
            key       = x_key;
            value     = x_value;
        }

        /**
         * Obtain the key for this node.
         *
         * @return the key
         */
        public final int getKey(  ) {
            return key;
        }
        
        public final int getValue(  ) {
            return value;
        }

    }

    /**
     * Constructs a FibonacciHeap object that contains no elements.
     */
    public FibonacciHeap(  ) {} // FibonacciHeap

    /**
     * Tests if the Fibonacci heap is empty or not. Returns true if the heap is
     * empty, false otherwise.
     * 
     * <p>
     * Running time: O(1) actual
     * </p>
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty(  ) {
        return min == null;
    }


    // isEmpty

    /**
     * Removes all elements from this heap.
     */
    public void clear(  ) {
        min     = null;
        node_num       = 0;
    }


    // clear

    /**
     * Decreases the key value for a heap node, given the new value to take on.
     * The structure of the heap may be changed and will not be consolidated.
     * 
     * <p>
     * Running time: O(1) amortized
     * </p>
     *
     * @param x node to decrease the key of
     * @param k new key value for node x
     *
     * @exception IllegalArgumentException Thrown if k is larger than x.key
     *            value.
     */
    public void decreaseKey( Node x, int new_value ) {
        if( new_value > x.value ) {
            throw new IllegalArgumentException( 
                "decreaseKey() got larger key value" );
        }

        x.value = new_value;

        Node y = x.parent;

        if( ( y != null ) && ( x.value < y.value ) ) {
            cut( x, y );
            cascadingCut( y );
        }

        if( x.value < min.value ) {
            min = x;
        }
    }


    // decreaseKey

    /**
     * Deletes a node from the heap given the reference to the node. The trees
     * in the heap will be consolidated, if necessary. This operation may fail
     * to remove the correct element if there are nodes with key value
     * -Infinity.
     * 
     * <p>
     * Running time: O(log n) amortized
     * </p>
     *
     * @param x node to remove from heap
     */
    public void delete( Node x ) {
        // make x as small as possible
        decreaseKey( x, Integer.MIN_VALUE );

        // remove the smallest, which decreases n also
        removeMin(  );
    }


    // delete

    /**
     * Returns the smallest element in the heap. This smallest element is the
     * one with the minimum key's value.
     * 
     * <p>
     * Running time: O(1) actual
     * </p>
     *
     * @return heap node with the smallest key
     */
    public Node min(  ) {
        return min;
    }


    // min

    /**
     * Removes the smallest element from the heap. This will cause the trees in
     * the heap to be consolidated, if necessary.
     * 
     * <p>
     * Running time: O(log n) amortized
     * </p>
     *
     * @return node with the smallest key
     */
    public Node removeMin(  ) {
        Node z = min;

        if( z != null ) {
            int  numKids   = z.degree;
            Node x         = z.child;
            Node tempRight;

            // for each child of z do...
            while( numKids > 0 ) {
                tempRight     = x.right;

                // remove x from child list
                x.left.right     = x.right;
                x.right.left     = x.left;

                // add x to root list of heap
                x.left             = min;
                x.right            = min.right;
                min.right        = x;
                x.right.left     = x;

                // set parent[x] to null
                x.parent     = null;
                x              = tempRight;
                numKids--;
            }

            // remove z from root list of heap
            z.left.right     = z.right;
            z.right.left     = z.left;

            if( z == z.right ) {
                min = null;
            }
            else {
                min = z.right;
                consolidate(  );
            }

            // decrement size of heap
            node_num--;
        }

        return z;
    }


    // removeMin

    /**
     * Returns the size of the heap which is measured in the number of elements
     * contained in the heap.
     * 
     * <p>
     * Running time: O(1) actual
     * </p>
     *
     * @return number of elements in the heap
     */
    public int size(  ) {
        return node_num;
    }


    // size

    /**
     * Joins two Fibonacci heaps into a new one. No heap consolidation is
     * performed at this time. The two root lists are simply joined together.
     * 
     * <p>
     * Running time: O(1) actual
     * </p>
     *
     * @param h1 first heap
     * @param h2 second heap
     *
     * @return new heap containing h1 and h2
     */
    public static FibonacciHeap union( FibonacciHeap h1, FibonacciHeap h2 ) {
        FibonacciHeap h = new FibonacciHeap(  );

        if( ( h1 != null ) && ( h2 != null ) ) {
            h.min = h1.min;

            if( h.min != null ) {
                if( h2.min != null ) {
                    h.min.right.left      = h2.min.left;
                    h2.min.left.right     = h.min.right;
                    h.min.right             = h2.min;
                    h2.min.left             = h.min;

                    if( h2.min.value < h1.min.value ) {
                        h.min = h2.min;
                    }
                }
            }
            else {
                h.min = h2.min;
            }

            h.node_num = h1.node_num + h2.node_num;
        }

        return h;
    }


    // union
    /**
     * mergeLists merge two circularly double linked lists into one
     * need two min-pointer to those two lists, and return the min-pointer to the merged one
     */
    private static Node mergeLists(Node one, Node two) {
        if (one == null && two == null) { // Both null, resulting list is null.
            return null;
        }
        else if (one != null && two == null) { // Two is null, result is one.
            return one;
        }
        else if (one == null && two != null) { // One is null, result is two.
            return two;
        }
        else { // Both non-null; actually do the splice.
            Node oneNext = one.right;
            one.right = two.right;
            one.right.left = one;
            two.right = oneNext;
            two.right.left = two;

            return one.value < two.value? one : two;
        }
    }
    
    public Node insert(int key, int value) {   // insert into fibheap	
        Node result = new Node(key, value);
        min = mergeLists(min, result); //merge the new entry node into the top level list
        ++node_num;
        return result;  //return reference to the new node
    }
    
    /**
     * Performs a cascading cut operation. This cuts y from its parent and then
     * does the same for its parent, and so on up the tree.
     * 
     * <p>
     * Running time: O(log n); O(1) excluding the recursion
     * </p>
     *
     * @param y node to perform cascading cut on
     */
    protected void cascadingCut( Node y ) {
        Node z = y.parent;

        // if there's a parent...
        if( z != null ) {
            // if y is unmarked, set it marked
            if( !y.ChildCut ) {
                y.ChildCut = true;
            }
            else {
                // it's marked, cut it from parent
                cut( y, z );

                // cut its parent as well
                cascadingCut( z );
            }
        }
    }


    // cascadingCut

    /**
     * Consolidates the trees in the heap by joining trees of equal degree
     * until there are no more trees of equal degree in the root list.
     * 
     * <p>
     * Running time: O(log n) amortized
     * </p>
     */
    protected void consolidate(  ) {
        int    arraySize = node_num + 1;
        Node[] array = new Node[ arraySize ];

        // Initialize degree array
        for( int i = 0; i < arraySize; i++ ) {
            array[ i ] = null;
        }

        // Find the number of root nodes.
        int  numRoots = 0;
        Node x = min;

        if( x != null ) {
            numRoots++;
            x = x.right;

            while( x != min ) {
                numRoots++;
                x = x.right;
            }
        }

        // For each node in root list do...
        while( numRoots > 0 ) {
            // Access this node's degree..
            int  d    = x.degree;
            Node next = x.right;

            // ..and see if there's another of the same degree.
            while( array[ d ] != null ) {
                // There is, make one of the nodes a child of the other.
                Node y = array[ d ];

                // Do this based on the key value.
                if( x.value > y.value ) {
                    Node temp = y;
                    y     = x;
                    x     = temp;
                }

                // Node y disappears from root list.
                link( y, x );

                // We've handled this degree, go to next one.
                array[ d ] = null;
                d++;
            }

            // Save this node for later when we might encounter another
            // of the same degree.
            array[ d ]     = x;

            // Move forward through list.
            x = next;
            numRoots--;
        }

        // Set min to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        min = null;

        for( int i = 0; i < arraySize; i++ ) {
            if( array[ i ] != null ) {
                // We've got a live one, add it to root list.
                if( min != null ) {
                    // First remove node from root list.
                    array[ i ].left.right     = array[ i ].right;
                    array[ i ].right.left     = array[ i ].left;

                    // Now add to root list, again.
                    array[ i ].left             = min;
                    array[ i ].right            = min.right;
                    min.right                 = array[ i ];
                    array[ i ].right.left     = array[ i ];

                    // Check if this is a new min.
                    if( array[ i ].value < min.value ) {
                        min = array[ i ];
                    }
                }
                else {
                    min = array[ i ];
                }
            }
        }
    }


    // consolidate

    /**
     * The reverse of the link operation: removes x from the child list of y.
     * This method assumes that min is non-null.
     * 
     * <p>
     * Running time: O(1)
     * </p>
     *
     * @param x child of y to be removed from y's child list
     * @param y parent of x about to lose a child
     */
    protected void cut( Node x, Node y ) {
        // remove x from childlist of y and decrement degree[y]
        x.left.right     = x.right;
        x.right.left     = x.left;
        y.degree--;

        // reset y.child if necessary
        if( y.child == x ) {
            y.child = x.right;
        }

        if( y.degree == 0 ) {
            y.child = null;
        }

        // add x to root list of heap
        x.left             = min;
        x.right            = min.right;
        min.right        = x;
        x.right.left     = x;

        // set parent[x] to nil
        x.parent     = null;

        // set mark[x] to false
        x.ChildCut = false;
    }


    // cut

    /**
     * Make node y a child of node x.
     * 
     * <p>
     * Running time: O(1) actual
     * </p>
     *
     * @param y node to become child
     * @param x node to become parent
     */
    protected void link( Node y, Node x ) {
        // remove y from root list of heap
        y.left.right     = y.right;
        y.right.left     = y.left;

        // make y a child of x
        y.parent = x;

        if( x.child == null ) {
            x.child     = y;
            y.right     = y;
            y.left      = y;
        }
        else {
            y.left              = x.child;
            y.right             = x.child.right;
            x.child.right     = y;
            y.right.left      = y;
        }

        // increase degree[x]
        x.degree++;

        // set mark[y] false
        y.ChildCut = false;
    }

    
        
}
