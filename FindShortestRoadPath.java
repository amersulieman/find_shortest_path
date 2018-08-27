import java.io.*;
import java.util.*;

/**
 * @author  Amer Sulieman
 * @version 20 November 2017
 *
 * In this class, I read the file and split its tokens and return dijkstra algorithm results
 * This class calls the other class Graph.java to write the results into an output file
 */
public class FindShortestRoadPath {
    /**
     * The main method reads an input file, Ignores the c letter which is comments in the file
     * the first number that is read in the file is the source
     * The second number read is the destination
     * The third number read is the cost to get from the source to that destination or the weight of the edge
     */
    public static void main(String[] args) throws IOException {
        Graph graph = new Graph();//create a graph object
        try {
            //this is to make the running prompt accept the file name as a parameter
            FileReader file = new FileReader(args[0]);
            Scanner graphFile = new Scanner(file);
            // Read the edges and insert
            String line;//the line to read
            //as long as there are lines in the file to read, do the following
            while (graphFile.hasNextLine()) {
                line = graphFile.nextLine();//increment line to read to next line
                StringTokenizer st = new StringTokenizer(line);//this is to split line into tokens
                try {
                    if (st.nextToken().equals("c"))//skip the comments in the file
                    {
                        continue;
                    }
                    String source = st.nextToken();//the first number in the line is the source
                    String dest = st.nextToken();//the second number in the line is destination we going to
                    int cost = Integer.parseInt(st.nextToken());// the last number is the cost
                    graph.addEdge(source, dest, cost);//then add the edge with its weight and destination an source
                } catch (NumberFormatException e) {
                    System.err.println(e);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        String sourceID = args[1];//second command in terminal to be the source
        String destinationID = args[2];//thirst command in the terminal is the destination
        graph.dijkstra(sourceID);//then run dijkstra's algorithm on the source
        graph.printPath(sourceID, destinationID, args[3]);//print the path to get to the destination wanted
    }
}
