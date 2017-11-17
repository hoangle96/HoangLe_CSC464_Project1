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
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

public class part2 {
	public static double min(double[] input) {
		double m = input[0];
		for(int i = 0; i < input.length; i++) {
			if(m - input[i] > 0) {
				m = input[i];				
			}			
		}
		return m;		
	}
	
	public static void main(String[] args) {
		int numberLine = 0;
		File myFile = new File("part2.txt");
		try {
			
			Scanner scan = new Scanner(myFile);
			while (scan.hasNextLine()) {
				System.out.println(scan.nextLine());
				numberLine++;
			}
			scan.close();
		} catch (Exception e) {
		}
		System.out.println(numberLine);
		String[] myGraph = new String[10000000];
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
		
		DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> directedWeightedGraph = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);	
		directedWeightedGraph.addVertex("s");
		directedWeightedGraph.addVertex("t");		

		int j = 0;
		while (myGraph[j]!= null) {
			String[] mySplit = myGraph[j].split(" ");
			directedWeightedGraph.addVertex(mySplit[0]);
			directedWeightedGraph.addEdge("s", mySplit[0]);
			for (int k = 1; k < mySplit.length; k++) {
				if(k%2 !=0) {
					directedWeightedGraph.addVertex(mySplit[k]);
					directedWeightedGraph.addEdge(mySplit[k], "t");
					directedWeightedGraph.addEdge(mySplit[0], mySplit[k]);
				}else {
					directedWeightedGraph.setEdgeWeight(directedWeightedGraph.getEdge(mySplit[0], mySplit[k-1]), Double.parseDouble(mySplit[k]));
				}
			}
			j++;
		}
		
//		Set Edge Weight of edges from the source and to the sink to 0
		for(DefaultWeightedEdge e: directedWeightedGraph.edgesOf("s")) {
			directedWeightedGraph.setEdgeWeight(e,0.0);
		}
		
		for(DefaultWeightedEdge e: directedWeightedGraph.edgesOf("t")) {
			directedWeightedGraph.setEdgeWeight(e,0.0);
		}
		
//		Calculate the price of the vertices		
		Map<String, Double> price = new HashMap<String, Double>();
		for(DefaultWeightedEdge e: directedWeightedGraph.outgoingEdgesOf("s")) {
			String v = directedWeightedGraph.getEdgeTarget(e);
			price.put(v, 0.0);
		}
		
		for(DefaultWeightedEdge e: directedWeightedGraph.incomingEdgesOf("t")) {
			double[] weight = new double[numberLine];
			int i = 0;
			String v = directedWeightedGraph.getEdgeSource(e);
			for(DefaultWeightedEdge e2: directedWeightedGraph.incomingEdgesOf(v)) {
				weight[i] = directedWeightedGraph.getEdgeWeight(e2);
				i++;			
			}
			price.put(v, min(weight));
		}
		
		
		//print to test
		System.out.println(directedWeightedGraph.toString());
		Set<DefaultWeightedEdge>edgeList = directedWeightedGraph.edgeSet();
		Set<String> verticesList = directedWeightedGraph.vertexSet();
//		
//		System.out.println("List of edges");
		for(DefaultWeightedEdge e: edgeList) {
			String leftEnd  = directedWeightedGraph.getEdgeSource(e);
			String rightEnd = directedWeightedGraph.getEdgeTarget(e);			
//			System.out.println(leftEnd+" "+ rightEnd + " "+ Double.toString(directedWeightedGraph.getEdgeWeight(e)));
		}
		
//		System.out.println("List of vertices");
		for(String v: verticesList) {
			if(!v.equals("s")&&!v.equals("t")) {
			System.out.println(v +" "+ Double.toString(price.get(v)));
			}
		}
		
		DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> residualGraph = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);	
		DijkstraShortestPath<String, DefaultWeightedEdge> pathFinding = new DijkstraShortestPath<>(directedWeightedGraph);
		
		//construct residual graph
		for(String v: verticesList) {
			residualGraph.addVertex(v);			
		}		
		
		//compute reduced cost
		for(DefaultWeightedEdge e: edgeList) {
			String leftEnd  = directedWeightedGraph.getEdgeSource(e);
			String rightEnd = directedWeightedGraph.getEdgeTarget(e);
			if(!leftEnd.equals("s")&&!rightEnd.equals("t")&&!leftEnd.equals("t")&&!rightEnd.equals("s")) {
				directedWeightedGraph.setEdgeWeight(e, price.get(leftEnd) + directedWeightedGraph.getEdgeWeight(e) - price.get(rightEnd));
				
			}
			System.out.println("Reduced Cost");
			System.out.println(leftEnd+" "+ rightEnd + " "+ Double.toString(directedWeightedGraph.getEdgeWeight(e)));
		}
		
//		Good till here 11/16 01:02 AM
		System.out.println("Finding Path");
		int maxFlow = 0;
		String pathUsed = "";
		String matching = "";
		double weight = 0.0;
//		while(maxFlow != numberLine) {
			while(pathFinding.getPath("s","t")!=null) {
				maxFlow++;				
				for(DefaultWeightedEdge e: pathFinding.getPath("s","t").getEdgeList()) {
					String leftEnd  = directedWeightedGraph.getEdgeSource(e);
					String rightEnd = directedWeightedGraph.getEdgeTarget(e);
					weight += directedWeightedGraph.getEdgeWeight(e);
					System.out.println(leftEnd+ " -> " + rightEnd+" "+ Double.toString(weight));
//					System.out.println(leftEnd + " " + rightEnd);
					if(leftEnd.equals("s")) {
						matching = matching + rightEnd;
					} else if(rightEnd.equals("t")){					
						matching = matching +" is matched with "+ leftEnd + "\n";
					}
					DefaultWeightedEdge pathforward = directedWeightedGraph.getEdge(leftEnd, rightEnd);
					//Constructing residual graph inside the original graph: begin with remove the forward edge, and add the backward edge
					directedWeightedGraph.removeEdge(e);
					
					
					if(!directedWeightedGraph.containsEdge(pathforward)) {					
						//since the capacity is 1 in this part, if there is a forward path, a backward path will be in the residual graph
						DefaultWeightedEdge pathbackward = residualGraph.addEdge(rightEnd, leftEnd);
						residualGraph.setEdgeWeight(pathbackward, -1.0*weight);
//						System.out.println(leftEnd+" <- "+rightEnd + Double.toString(directedWeightedGraph.getEdgeWeight(pathbackward)));
					}
					for(String v: verticesList) {
						if(!v.equals("s")&&!v.equals("t")) {
							double priceV = price.get(v);
							price.put(v, priceV + weight);
//							System.out.println(v +" "+ Double.toString(price.get(v)));
						}		
					}
					System.out.println(directedWeightedGraph.toString());
				}
				
				weight = 0.0;
			}
			
//		}
		System.out.println(directedWeightedGraph.toString());
		System.out.println(matching);	
	}
}

