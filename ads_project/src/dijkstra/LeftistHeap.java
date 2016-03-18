package dijkstra;

  
class LeftistHeapNode {
    int key, value; //key is the number of nodes, value is nodes' distance
    int sValue;     // sValue is used to check if it is Leftist tree
    LeftistHeapNode left, right, parent;             
 
    public LeftistHeapNode(int x_key, int x_value)
    {    	
    	this.key = x_key;
    	this.value = x_value;
    }
    
    public int getKey() {
        return key;
    }
    
    public int getValue() {
        return value;
    }
}
 
/** Class LeftistHeap **/
class LeftistHeap
{
    public LeftistHeapNode root; 
 
    /** Constructor **/
    public LeftistHeap() 
    {
        root = null;
    }
 
    /** Check if heap is empty **/
    public boolean isEmpty() 
    {
        return root == null;
    }
 
    /** Make heap empty **/ 
    public void clear( )
    {
        root = null;
    }
 
    /** Function to insert data **/
    public LeftistHeapNode insert(int x_key, int x_value)                               // here we should have two values: number of the nodes, min distances
    {
        LeftistHeapNode result = new LeftistHeapNode( x_key, x_value);
    	root = merge(result, root);
    	return result;
    }
 
    /** Function merge **/
    public void merge(LeftistHeap rhs)
    {
        if (this == rhs)    
            return;
        root = merge(root, rhs.root);
        rhs.root = null;
    }
 
    /** Function merge **/
    LeftistHeapNode merge(LeftistHeapNode x, LeftistHeapNode y)
    {
        if (x == null)
            return y;
        if (y == null)
            return x;
        if (x.value > y.value)
        {
        	LeftistHeapNode temp = x;
            x = y;
            y = temp;
        	
        }
        
        x.right = merge(x.right, y);
        x.right.parent = x;
 
        if(x.left == null) 
        {
            x.left = x.right;
            x.left.parent = x;
            x.right = null;         
        } 
        else 
        {
            if(x.left.sValue < x.right.sValue) 
            {
            	LeftistHeapNode temp = x.left;
            	x.left = x.right;
            	x.right = temp;
            	
            	x.left.parent = x;
            	x.right.parent = x;
            }
            x.sValue = x.right.sValue + 1;
        }        
        return x;
    }
     
    /** Function to delete minimum element **/
    public LeftistHeapNode deleteMin( )
    {
        if (isEmpty() )
            return null;
        LeftistHeapNode minItem = root;
        root = merge(root.left, root.right);
        if(root != null)
            {
        	root.parent = null;            
            }
        return minItem;
    }
 
    /** Inorder traversal **/
    public void inorder()
    {
        inorder(root);
        System.out.println();
    }
    private void inorder(LeftistHeapNode r)
    {
        if (r != null)
        {
            inorder(r.left);
            System.out.print(r.key +" "+ r.value + " ");            
            inorder(r.right);
        }
    }
    // delete arbitary element
    public void delete(LeftistHeapNode x)
    {
        LeftistHeapNode q = x.parent;
    	    	
    	//x is root
    	if(q == null)
    	{
    		deleteMin();
    		return;
    	}
    	
    	LeftistHeapNode p = merge(x.left, x.right);   	
    	// x is a leaf node
    	if(p==null) {
    		if(q.left == x) {
        		q.left = null;
        	} 
        	else if (q.right == x) {
        		q.right =null;
        	}
    	} // x is not a leaf node
  	    else if(p!=null) {
  		    p.parent = q;
  		
  		    if(q != null && q.left== x )
      	     {
      		   q.left = p;
      	     }
      	    if(q != null && q.right== x )
      	    {
      		   q.right = p;
      	    } 
  	    } 
  	    
    	while( q != null) 
    	{ //if x is leaf 
      	  if(p==null){
        		if(q.left==null) {
        			q.left=q.right;
        			q.right = null;
        		} else if (q.right == null&& q.left.sValue==0){
        			
        		}
        		
        		if(q.right==null){
        			q.sValue = 0;
        		} else {
        		    q.sValue = q.right.sValue + 1;
        		}
        		p = q;
        		q = q.parent;   		
      	  } // x has two children
      	  else if ( p!= null && q.left!=null && q.right!=null) {	
        		if(q.left.sValue < q.right.sValue) {
        			LeftistHeapNode temp = q.left;
        			q.left = q.right;
        			q.right = temp;
        		}
        		else if (q.right.sValue + 1 == q.left.sValue) {
        			return;
        		}
        		q.sValue = q.right.sValue + 1;
        		p = q;
        		q = q.parent;
    	  } // x has only right child
      	  else if ( p!=null && q.left == null) {
      		    LeftistHeapNode temp = q.left;
			    q.left = q.right;
			    q.right = temp;
			    q.sValue = 0;
      		    p = q;
      		    q = q.parent;
      	  } // x has only left child
      	  else if ( p!=null && q.right == null) {
      		    q.sValue = 0;
      		    p = q;
      		    q = q.parent;
      	  }
    	}    		
    	}
    // decrease node x's value to new_value
	public LeftistHeapNode decreaseKey (LeftistHeapNode x, int new_value) {
		//if x is root
		if(x.parent== null) {
			x.value = new_value;
			return x;
		} 
		else {// if x is not root
			//if x's new_value >= x.parent.value, just change the key's value to new_value without any other operation
			if( new_value == x.parent.value || new_value > x.parent.value) {
				x.value = new_value;
				return x;
			} 
			else { // if x's new_value < x.parent.value, delete this node and then insert a new node with new_value to the LeftistHeap
				delete(x);
				x = insert(x.key, new_value);
				return x;
			}
		}
		
    }
}

