package dijkstra;

public class Dijkstra {

	public static void main(String[] args) {
	    try {
	      if ( args.length < 1 || args[0].charAt(0) != '-' ) 
	          throw new Exception();
	      switch ( args[0].charAt(1) ) {   
	         case 'l': // l choose Leftist mode
     	 	           if(args.length != 2){
     	 	           throw new Exception();
   	 	               }
   	 	               Method.Leftist(args[1]);  
                       break;
	         case 'f': // f choose Fibonacci mode
		      	 	   if(args.length != 2){
		      	 	    throw new Exception();
		    	 	   }
		    	 	   Method.Fib(args[1]);  
		               break;
	         case 'r': // r choose random generate graph mode
	        	 	   if (args.length != 4) {
	        	 	      throw new Exception();
					   }
	        	 	   int n = Integer.parseInt(args[1]);
	        	 	   double d = Double.parseDouble(args[2]);
	        	 	   int x = Integer.parseInt(args[3]);
	        	       Method.randomInput(n,d,x);
	                   break;
	         default:  throw new Exception();         
	      }
	    }
	    catch (Exception e)
	    {
	        System.err.println("Invalid Input! Please input as the format:' Dijkstra <option>'");
	        System.err.println("options: \t -r n d x \t random mode(n--# of vertices, d--d% density of connection, x--start vertice");
	        System.err.println("options: \t -l file_name \t user input file with simple method");
	        System.err.println("options: \t -f file_name \t user input file with f-heap method");
	    }
	}

}
