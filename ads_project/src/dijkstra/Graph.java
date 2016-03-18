package dijkstra;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Graph {
    private int source;
    private int nodesNumber;
    private int edgesNumber;
    private ArrayList<ArrayList<Integer>> graph;
    
    public Graph(String fileName)
    {
        this.graph=new ArrayList<ArrayList<Integer>>();
        File file = new File(fileName);
        Scanner scanner;          // use scanner to scan int
        try {
            scanner = new Scanner(file);
            if(scanner.hasNextInt())
                this.source=Integer.parseInt(scanner.next());
            if(scanner.hasNextInt())
                this.nodesNumber=Integer.parseInt(scanner.next());
            if(scanner.hasNextInt())
                this.edgesNumber=Integer.parseInt(scanner.next());
            for(int i=0;i<nodesNumber;i++)
                this.graph.add(new ArrayList<Integer>());
            while(scanner.hasNextInt())
            {        // when the file has next line, move on            
                Integer s = Integer.parseInt(scanner.next());// change format to Integer
                Integer d = Integer.parseInt(scanner.next());
                Integer c = Integer.parseInt(scanner.next());
                this.graph.get(s).add(d);
                this.graph.get(s).add(c);
                this.graph.get(d).add(s);
                this.graph.get(d).add(c);              
            }
            scanner.close();
        } catch (FileNotFoundException e) {// catch exception
            System.err.println("Error: " + fileName + " not found");// print out error info
        }  

    }
    // create graph in random mode
    public Graph(int number, double density, int x)
    {
        this.source=x;
        this.nodesNumber=number;
        this.edgesNumber = (int) (number*(number-1)*density/200);
        System.out.println("\nFrom the file,we can get:\nThe Number of nodes is: "+nodesNumber + ";\nThe Density of edges is: "+density +"%;\nThe Source Node is: "+ x +";\nThe Number of edges is: "+ edgesNumber);
        if (edgesNumber < number-1) {// dispatch this situation
            System.out.println("The density is too low to create graph!!");
            System.exit(0);
        }        
        boolean isConnected = false;
        Random ra = new Random();// random generate num  
        while(!isConnected)
        {               
            if (edgesNumber == (number*(number-1)/2)) 
            {// calculate edge number
                this.graph=new ArrayList<ArrayList<Integer>>();
                for(int i=0;i<number;i++)
                    this.graph.add(new ArrayList<Integer>());
                // input the graph information to adjacency matrix
                for (int i = 0; i < number; i++) {
                    for (int j = i + 1; j < number; j++) {
                        int c = ra.nextInt(1000);
                        this.graph.get(i).add(j);
                        this.graph.get(i).add(c);
                        this.graph.get(j).add(i);
                        this.graph.get(j).add(c);
                    }
                }
                isConnected= true;
            }
            else
            {
                boolean[][] graphMatrix = new boolean[number][number];
                boolean[] connect = new boolean[number];
                this.graph=new ArrayList<ArrayList<Integer>>();
                for(int i=0;i<number;i++)
                    this.graph.add(new ArrayList<Integer>());
                for (int i = 0; i < edgesNumber; i++) 
                {
                    int from = 0;
                    int to = 0;
                    int cost = ra.nextInt(1000);

                    do {
                        from = ra.nextInt(number);
                        to = ra.nextInt(number);             
                    } while (from == to);   

                    while(graphMatrix[from][to])
                    {
                        do {
                            from = ra.nextInt(number);
                            to = ra.nextInt(number);             
                            } while (from == to);
                    } 

                    graphMatrix[from][to] = graphMatrix[to][from] = true;
                    this.graph.get(from).add(to);
                    this.graph.get(from).add(cost);
                    this.graph.get(to).add(from);
                    this.graph.get(to).add(cost);
                } 
                // check if the new created graph is connect
                check(graphMatrix,connect,0);
                isConnected=true;  
                for (int i = 0; i < connect.length; i++) {
                    if(!connect[i]){
                    isConnected= false;
                    break;
                    }
                }   
            }
        }
    }
    // help check if the graph is connect
    private void check(boolean[][] graphMatrix, boolean[] connect, int i) {
        connect[i] = true;
        for (int j = 0; j < connect.length; j++) {
            if (graphMatrix[i][j] && !connect[j]) {
                check(graphMatrix, connect, j);
            }
        }   
    }
    // print the graph adjacency matrix
    public void printGraph()
    {
        for(int j=0;j<this.graph.size();j++)
        {
            ArrayList<Integer> node=graph.get(j);
            System.out.printf("From: "+j+"\t\t");
            for(int i=0;i<node.size();i++)
            {
                System.out.printf("To: "+node.get(i));
                System.out.print("\t");
                if(i+1<node.size())
                {
                    i++;
                    System.out.printf("Cost: "+node.get(i)+"\t");
                }
                
            }
            System.out.println("");
        }
    }
    
    public int getSource()
    {
        return source;
    }
    public int getNumber()
    {
        return nodesNumber;
    }
    public int getEdges()
    {
        return edgesNumber;
    }
    public ArrayList<ArrayList<Integer>> getGraph()
    {
        return graph;
    }
}
