/**
 * @author Amer Sulieman
 * @version 20 November 2017
 *
 * This is a class that has 3 inner classes in it.
 * 1)Edge class
 * 2)Vertex class
 * 3)Path class
 *
 * Also this class is responsible to run Dijkstra's Alg and get the shortest path from a vertex to
 * the destination we pick.
 */

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class Graph {
    public static final double INFINITY = Double.MAX_VALUE; //very high number to represent infinity
    /**
     * Inner Class that represents The edges between vertices
     */
    class Edge {
        public Vertex destination;//The vertex that we trying to go to
        public double weight;//The weight to go to the destination vertex

        /**
         * An edge constructor which has the vertex we trying to go to and the weight of it
         * @param d destination we trying to reach
         * @param w the weight of that destination
         */
        public Edge(Vertex d, double w) {
            destination = d;
            weight = w;
        }
    }
    /**
     * A class that will be used to represent the priority queue as discussed in class
     * which will have comparable to compare the costs it takes to go from somewhere to other
     * and pick the better one to enqueue. This will be by using the comparable class
     */
    class Path implements Comparable<Path> {
        public Vertex destination;  //The destination we trying to go to
        public double cost;//The weight of the edge

        /**
         * This is a constructor that builds the class
         * which takes the destination and the cost to get to it
         * @param d the destination vertex we trying to reach
         * @param c the cost to get to that vertex
         */
        public Path(Vertex d, double c) {
            destination = d;
            cost = c;
        }
        /**
         * This method is used to check which cost or weight is better to use
         * @param path the path between edges
         * @return return the less cost
         */
        public int compareTo(Path path) {
            double otherCost = path.cost;
            return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
        }
    }
    /**
     * A class that represents the vertices
     */
    class Vertex {
        private String name;         // Vertex name
        private List<Edge> adj;      // Adjacent list
        private double dist;         // destination/the vertex we trying to go to
        private Vertex previous;     // Previous vertex on shortest path
        private int known;           // variable used to check if vertex was processed
        /**
         * A constructor that represents the vertex
         * @param name The vertex name/number vertex
         */
        public Vertex(String name) {
            this.name = name;
            adj = new LinkedList<Edge>();
            reset(); //set everything to infinity
        }
        /**
         * A method that sets all the destinations to infinity and where each node was reached to null
         */
        public void reset() {
            dist = Graph.INFINITY;
            previous = null;
            known = 0;
        }
    }
    public Map<String,Vertex> vertexMap = new HashMap<String,Vertex>();
    /**
     * A method that connects the vertices to each other and set their relations
     * @param sourceName the vertex we start at
     * @param destName the destination we trying to go to
     * @param weight the cost between those to vertices
     */
    public void addEdge(String sourceName, String destName, double weight) {
        Vertex one = getVertex(sourceName);
        Vertex two = getVertex(destName);
        one.adj.add(new Edge(two, weight));
    }

    /**
     * This method return the vertex we looking for
     * @param vertexName the vertex we looking for
     * @return the vertex we want
     */
    public Vertex getVertex(String vertexName) {
        Vertex v = vertexMap.get(vertexName);
        if (v == null) {
            v = new Vertex(vertexName);
            vertexMap.put(vertexName, v);
        }
        return v;
    }
    /**
     * Clears the vertices and set everything to its natural states
     * like infinity,etc..
     */
    private void clearAll() {
        for (Vertex v : vertexMap.values())
            v.reset();
    }

    /**
     * Dijkstra's algorithm to find the shortest path for two nodes
     * @param startName The source or the vertex we starting at
     */
    public void dijkstra(String startName) {
        PriorityQueue<Path> pq = new PriorityQueue<Path>(); //use a priority queue for better enqueueing
        Vertex start = vertexMap.get(startName);    //the source or the vertex we start at is what the user input
        if (start == null) {
            //if the user input a vertex that doesn't exist
            throw new NoSuchElementException("Start vertex not found");
        }
        clearAll();//reset everything
        pq.add(new Path(start, 0)); //add the source to the queue and set its path to 0
        start.dist = 0;//also set its designation to 0
        int nodesSeen = 0;//this is a helper variable that tells us if all the nodes have been proceeded or not
        while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
            Path vertex = pq.remove();
            Vertex v = vertex.destination;
            if (v.known != 0){
                // already processed v
                continue;}
            v.known = 1;//if it was not precessed then set known variable to 1 which will mark it as processed
            nodesSeen++; //increment the number of nodes that have been processed
            for (Edge e : v.adj) {
                Vertex w = e.destination;
                double currentVertexWeight = e.weight;
                if (w.dist > v.dist + currentVertexWeight) {
                    w.dist = v.dist + currentVertexWeight;
                    w.previous = v;
                    pq.add(new Path(w, w.dist));
                }
            }
        }
    }
    /**
     * This method has a lot of responsibilities!!!!!!!!!!!!!!!!!!!!! PAY ATTENTION
     * First, to be clear it has a helper method to print the shortest path
     * Second, This method is also responsible for creating the output file
     * that contains the shortest path and the cost
     * Third, this method writes to the output file by redirecting ever the output statements
     * are printing and write it in the file.
     * @param source the vertex we are starting at
     * @param destName THE vertex we trying to get to
     * @param filename the output file the data will be copied to
     */
    public void printPath(String source,String destName,String filename) throws FileNotFoundException {
        Vertex w = vertexMap.get(destName);//get the destination vertex

        PrintStream output= new PrintStream(new File(filename));//this to redirect the output statements to the file
            //Assign out variable to output stream
            System.setOut(output);
            System.out.println("There are: " + vertexMap.size() + " vertices");//output statement
            System.out.println("The shortest path from " + source + " to " + destName);//output statement
            System.out.println("It takes: " + w.dist);//output statement
            System.out.println("Path: ");//output statement
            //calls helper method that prints the shortest path, which will also have more output statements that will be redirected!!!!!
            printPath(w);
    }
    /**
     * A helper method to print out the shortes path for the vertex
     * @param dest the vertices we pass to get to the destination
     */
    private void printPath(Vertex dest) {
        if (dest.previous != null) {
            printPath(dest.previous);//recursive call
        }
        System.out.println(dest.name);//output statement
    }
}



