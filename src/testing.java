import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.interfaces.MatchingAlgorithm.*;
import org.jgrapht.alg.matching.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;

public class testing{
	public static void main(String[] args) {
		String[] myGraph = new String[1000];
		DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> g = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> (DefaultWeightedEdge.class);	
		DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> rC = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> (DefaultWeightedEdge.class);	
		//keep track of all the vertices in the graph
		Set<String> ve=new HashSet<String>();
		// keep track of all the vertices in X
		Set<String> ri=new HashSet<String>();
		// keep track of all the vetices in Y
		Set<String> le= new HashSet<String>();
		Map<String, Integer> price = new HashMap<String, Integer>();
		int noLine = 0;
		// this section reads from the file and stores each line to a string
		// The file is read from the name of the project folder inside the root project folder
		// example: C:\Users\zacha\algorithm_project_1\algorithms_project_1 is where I read the file in testing
		File myFile = new File("part2.txt");
		try {
			int j = 0;
			Scanner scan = new Scanner(myFile);
			while (scan.hasNextLine()) {
				String i = scan.nextLine();
				myGraph[j] = i;
				j++;
				System.out.println(i);
				noLine++;
			}
			scan.close();
		} catch (Exception e) {
		}
		
		// the following is for the construction of the graph
		int l = 0;
		g.addVertex("s");
		g.addVertex("t");
		/*rC.addVertex("s");
		rC.addVertex("t");*/
		while (myGraph[l]!= null) {
			String[] mySplit = myGraph[l].split(" ");
			g.addVertex(mySplit[0]);
			g.addEdge("s", mySplit[0]);
			/*rC.addVertex(mySplit[0]);
			rC.addEdge("s", mySplit[0]);*/
			ve.add(mySplit[0]);
			le.add(mySplit[0]);
			for (int k = 1; k < mySplit.length; k++) {
				if(k%2 !=0) {
					if(!ri.contains(mySplit[k])) {
						ri.add(mySplit[k]);
						ve.add(mySplit[k]);
					}
					g.addVertex(mySplit[k]);
					g.addEdge(mySplit[k], "t");
					g.addEdge(mySplit[0], mySplit[k]);
					/*rC.addVertex(mySplit[k]);
					rC.addEdge(mySplit[k], "t");
					rC.addEdge(mySplit[0], mySplit[k]);*/
				}else {
					g.setEdgeWeight(g.getEdge(mySplit[0], mySplit[k-1]), Double.parseDouble(mySplit[k]));
					//rC.setEdgeWeight(rC.getEdge(mySplit[0],  mySplit[k-1]), Double.parseDouble(mySplit[k]));
				}
			}
			l++;
		}
		/*KuhnMunkresMinimalWeightBipartitePerfectMatching<String, DefaultWeightedEdge> swc;
		swc= new KuhnMunkresMinimalWeightBipartitePerfectMatching<String, DefaultWeightedEdge>((Graph<String, DefaultWeightedEdge>)g,le,ri);
		MatchingAlgorithm.Matching<DefaultWeightedEdge> match;
		match=swc.computeMatching();
		System.out.println("Paths used:" );
		int total=0;
		for(DefaultWeightedEdge edge: match.getEdges()) {
			System.out.println("" + g.getEdgeSource(edge)+ " -> "+g.getEdgeTarget(edge));
			total += match.getWeight();
		}
		System.out.print("Minimun Weight: "+total);	*/
//		DijkstraShortestPath<String, DefaultWeightedEdge> g2 = new DijkstraShortestPath<>(g);
//		DijkstraShortestPath<String, DefaultWeightedEdge> g3 = new DijkstraShortestPath<>(rC);
//		double[] weights = new double[10000000];
//		int maxFlow = 0;
//		String Matching = "";
//			// set all prices on the left to 0
//			for(String item: le) {
//					price.put(item, 0);
//			}
//			// set all prices on the right to the minimum value edge entering the right
//			for (String item: ri) {
//				// need to find the mimimal value
//				double min = Double.MAX_VALUE;
//				for(DefaultWeightedEdge item2: g.incomingEdgesOf(item)) {
//					if(min>g.getEdgeWeight(item2)) {
//						min = g.getEdgeWeight(item2);
//					}
//					price.put(item, (int) min);
//				}
//			}
//			// this is  for ford fulkerson
//			while(maxFlow<=noLine) {
//				int p = 0;
//				//maxFlow++;
//				for (String item: ve) {
//					weights[p] = g2.getPathWeight("s", item);
//					System.out.println("path from s to "+ item + " " + weights[p] + "\n");
//					p++;
//				}
//				if(g2.getPath("s", "t") != null) {
//					maxFlow++;
//					for (DefaultWeightedEdge item: g2.getPath("s", "t").getEdgeList()) {
//						String sourceV = g.getEdgeSource(item);
//						String targetV = g.getEdgeTarget(item);
//						double temp = g.getEdgeWeight(item);
//						g.removeEdge(item);
//						/*if (!rC.containsEdge(g.getEdge(sourceV, targetV))) {
//							rC.addVertex(sourceV);
//							rC.addVertex(targetV);
//							rC.addEdge(targetV, sourceV);
//							rC.setEdgeWeight(rC.getEdge(targetV, sourceV), temp);
//						}*/
//						if(!g.containsEdge(g.getEdge(sourceV, targetV))) {
//							g.addEdge(targetV, sourceV);
//						}
//					}
//				}
//			}
			System.out.println(g.toString());
		}
		
}