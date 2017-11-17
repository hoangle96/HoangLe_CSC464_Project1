/**
 * @author hoangle _ ID 2977103
 * @param String s : path of the file from the root of the project
 * @return String matching: matching and the cardinality of the matching
 * @date November 13, 2017
 * */

import java.util.*;
import java.io.*;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.Graph;

public class part1 {
	public static DefaultDirectedGraph constructGraph(String s) {
		//read input from an external file, file is located at the root of the project
		//output an directed graph
		
		//reading input
		//s is the name of the file
		File myFile = new File(s);		
		
		int numberLine = 0; //count number of line the input file
		try {			
			Scanner scan = new Scanner(myFile);
			System.out.println("Input file:");
			while (scan.hasNextLine()) {
				System.out.println(scan.nextLine());
				numberLine++;
			}
			scan.close();
		} catch (Exception e) {
		}		
		
		String[] myGraph = new String[numberLine];	//each line will be an element of this array
		try {
			int i = 0;
			Scanner scan = new Scanner(myFile);
			while (scan.hasNextLine()) {
				String ln = scan.nextLine();
				myGraph[i] = ln;
				i++;
			}
			scan.close();
		} catch (Exception e) {
		}
//		To construct the graph, the following steps were performed:
//		(1) define new graph then add source and sink
//		(2) parse string 
//		(3) connect the left vertices to s
//		(4) put vertices in correct order 
//		(5) connect right vertices to t
		
//		(1) define new graph: new directed graph with String-typed vertex, and DefaultEdge-typed edges.	Graph, vertex, and edge type are defined in jGrapht		
		DefaultDirectedGraph<String, DefaultEdge> directGraph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);		
//		(1.5) add source and sink
		directGraph.addVertex("s");
		directGraph.addVertex("t");

//		(2) parse string, each line will be splited, the first token will be a vertex connected to s, the rests will be vertices connected to t
		for(int i = 0; i< numberLine; i++) {
			String[] splitSpace = myGraph[i].split(" ");
			directGraph.addVertex(splitSpace[0]);
//			(3) Connect left nodes to s
			directGraph.addEdge("s", splitSpace[0]);
			for (int j = 1; j < splitSpace.length; j++) {
				directGraph.addVertex(splitSpace[j]);
//				(4) put vertices in correct order 
				directGraph.addEdge(splitSpace[0], splitSpace[j]);
//				(5) Connect right nodes to t
				directGraph.addEdge(splitSpace[j], "t");
			}
		}
		System.out.println("The original graph is:");
		System.out.println(directGraph.toString() + "\n");
		return directGraph;	
	}
	
	public static String fordfulkerson(String s) {
		DefaultDirectedGraph directGraph = constructGraph(s);
//		find shortest path using built-in Dijkstra algorithm from jgrapht
		DijkstraShortestPath<String, DefaultEdge> pathFinding = new DijkstraShortestPath<>(directGraph);
		int maxFlow = 0;
		String matching = "";	
		while(pathFinding.getPath("s","t")!=null) {
			maxFlow++;
			for(DefaultEdge e: pathFinding.getPath("s","t").getEdgeList()) {
				String leftEnd  = (String) directGraph.getEdgeSource(e);
				String rightEnd = (String) directGraph.getEdgeTarget(e);
				//identify each edge of the path, if it is in the path, then add the vertices that are adjacent to s and t to the matching
				if(leftEnd.equals("s")) {
					matching = matching + rightEnd;
				} else if(rightEnd.equals("t")){					
					matching = matching +" is matched with "+ leftEnd + "\n";
				}				
//				constructing an residual graph on the original graph
//				since the capacity is 1 in this part, the residual graph will be constructed as following: if there is a forward edge, it will be replaced by a backward edge.
				DefaultEdge pathforward = (DefaultEdge) directGraph.getEdge(leftEnd, rightEnd);
				directGraph.removeEdge(e);	
				//if there is no forward edge in the graph -> matching -> add reversed edge				
				if(!directGraph.containsEdge(pathforward)) {					
					DefaultEdge pathBackward = (DefaultEdge) directGraph.addEdge(rightEnd, leftEnd);						
				}							
			}
		}
		matching = matching + "\nMax Cardinality: " + Integer.toString(maxFlow);
		return matching;		
	}
		
	public static void main(String[] args) 
	{
		//put in the file name
		String matching = fordfulkerson("part1.txt");
		System.out.println(matching);
	}
}
