import java.util.*;
import java.io.*;
public class Delhi_Metro_Map 
{
	public Delhi_Metro_Map() {
		StationsVer = new HashMap<>();
	}
	public class Vertex {
		HashMap<String, Integer> neighbours = new HashMap<>();
	}

	// FUNCTIONS FOR VERTICES  StationsVer
	static HashMap<String, Vertex> StationsVer;
	public int numOfVetex() {
		return StationsVer.size();
	}
	public boolean containsStations(String vname) {
		return StationsVer.containsKey(vname);
	}
	public void addVertex(String vname) {
		Vertex vtx = new Vertex();
		StationsVer.put(vname, vtx);
	}
	public void removeVertex(String vname) {
		Vertex vtx = StationsVer.get(vname);
		ArrayList<String> keys = new ArrayList<>(vtx.neighbours.keySet());
		for (String key : keys) {
			Vertex nbrVtx = StationsVer.get(key);
			nbrVtx.neighbours.remove(vname);
		}
		StationsVer.remove(vname);

	}

	// FUNCTIONS FOR EDGES
	public int numOfEdges() {
		ArrayList<String> keys = new ArrayList<>(StationsVer.keySet());
		int count = 0;
		for (String key : keys) {
			Vertex vtx = StationsVer.get(key);
			count = count + vtx.neighbours.size();
		}
		return count / 2;
	}
	public boolean containsEdge(String vname1, String vname2) {
		Vertex vtx1 = StationsVer.get(vname1);
		Vertex vtx2 = StationsVer.get(vname2);
		if (vtx1 == null || vtx2 == null || !vtx1.neighbours.containsKey(vname2)) 
			return false;
		return true;
	}
	public void addEdge(String vname1, String vname2, int value) {
		Vertex vtx1 = StationsVer.get(vname1); 
		Vertex vtx2 = StationsVer.get(vname2); 
		if (vtx1 == null || vtx2 == null || vtx1.neighbours.containsKey(vname2)) 
			return;
		vtx1.neighbours.put(vname2, value);
		vtx2.neighbours.put(vname1, value);
	}
	public void removeEdge(String vname1, String vname2) 
	{
		Vertex vtx1 = StationsVer.get(vname1);
		Vertex vtx2 = StationsVer.get(vname2);
		//check if the vertices given or the edge between these vertices exist or not
		if (vtx1 == null || vtx2 == null || !vtx1.neighbours.containsKey(vname2)) 
			return;
		vtx1.neighbours.remove(vname2);
		vtx2.neighbours.remove(vname1);
	}

	// DISPLAYING FUNCTIONS
	public void display_Map() {
		System.out.println("\t Delhi Metro Map");
		System.out.println("\t------------------");	System.out.println("----------------------------------------------------\n");
		ArrayList<String> keys = new ArrayList<>(StationsVer.keySet());

		for (String key : keys) {
			String str = key + " =>\n";
			Vertex vtx = StationsVer.get(key);
			ArrayList<String> vtxnbrs = new ArrayList<>(vtx.neighbours.keySet());
			
			for (String nbr : vtxnbrs)	{
				str = str + "\t" + nbr + "\t";
				if (nbr.length()<16)
					str = str + "\t";
				if (nbr.length()<8)
					str = str + "\t";
				str = str + vtx.neighbours.get(nbr) + "\n";
			}
			System.out.println(str);
		}
		System.out.println("\t------------------");	System.out.println("---------------------------------------------------\n");
	}
	public void display_Stations() {
		System.out.println("\n***********************************************************************\n");
		ArrayList<String> keys = new ArrayList<>(StationsVer.keySet());
		int i=1;
		for(String key : keys) {
			System.out.println(i + ". " + key);
			i++;
		}
		System.out.println("\n***********************************************************************\n");
	}
		
//------------------------------------------------------------------------------------------------------------------------

	// Normal DFS 
	public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> vis) {
		if (containsEdge(vname1, vname2)) 
			return true;

		vis.put(vname1, true);  	// mark as visited

		Vertex vtx = StationsVer.get(vname1);
		ArrayList<String> nbrs = new ArrayList<>(vtx.neighbours.keySet());

		for (String nbr : nbrs) {		// Traverse the neighbours
			if (!vis.containsKey(nbr))
				if (hasPath(nbr, vname2, vis))
					return true;
		}
		return false;
	}

	// DjPair class for Priority Queues
	private class DjPair implements Comparable<DjPair> {
		String vname;	int distCost;
		@Override
		public int compareTo(DjPair o) {
			return o.distCost - this.distCost;
		}
	}

	// Shortest Path algo  = Dijkstras
	public int dijkstra(String src, String des, boolean nan) {
		int val = 0;
		HashMap<String, DjPair> unVisited = new HashMap<>();
		PriorityQueue<DjPair> queue = new PriorityQueue<>((a, b) -> a.distCost - b.distCost);

		for (String key : StationsVer.keySet()) {
			DjPair newPair = new DjPair();
			newPair.vname = key;
			newPair.distCost = Integer.MAX_VALUE;
			if (key.equals(src)) {
				newPair.distCost = 0;
			}
			queue.add(newPair);
			unVisited.put(key, newPair);
		}

		// Keep removing the pairs while the queue is not empty
		while (!queue.isEmpty()) {
			DjPair curr = queue.poll();
			if (curr.vname.equals(des)) {
				val = curr.distCost;
				break;
			}
			unVisited.remove(curr.vname);

			Vertex v = StationsVer.get(curr.vname);
			for (String nbr : v.neighbours.keySet()) {
				if (unVisited.containsKey(nbr)) {
					int oldDist = unVisited.get(nbr).distCost;
					Vertex k = StationsVer.get(curr.vname);
					int newDist;
					if (nan)
						newDist = curr.distCost + 120 + 40 * k.neighbours.get(nbr);
					else
						newDist = curr.distCost + k.neighbours.get(nbr);
						
					if (newDist < oldDist) {
						DjPair gp = unVisited.get(nbr);
						gp.distCost = newDist;
						// Update priority by removing and re-adding the pair to the queue
						queue.remove(gp);
						queue.add(gp);
					}
				}
			}
		}
		return val;
	}
	
	// Printing the codes of the Stations
	public static void printCodelist() {
		System.out.println("List of stations:\n");
		ArrayList<String> keys = new ArrayList<>(StationsVer.keySet());
		int i = 1;
		for (String key : keys) {
			System.out.println(i + ". " + key);
			i++;
		}
	}

	private class StationInfo {
		String stationName;
		String pathSoFar;
		int minDistance;
	}
	
	public String getMinimumDistance(String source, String destination) {
		int minDistance = Integer.MAX_VALUE;
		String shortestPath = "";
		HashMap<String, Boolean> processedStations = new HashMap<>();
		LinkedList<StationInfo> stack = new LinkedList<>();
	
		StationInfo startStation = new StationInfo();
		startStation.stationName = source;
		startStation.pathSoFar = source + "  ";
		startStation.minDistance = 0;
		
		stack.addFirst(startStation);
	
		while (!stack.isEmpty()) {
			StationInfo currentStation = stack.removeFirst();
			if (processedStations.containsKey(currentStation.stationName)) 
				continue;
			processedStations.put(currentStation.stationName, true);
	
			if (currentStation.stationName.equals(destination)) {
				int distance = currentStation.minDistance;
				if (distance < minDistance) {
					shortestPath = currentStation.pathSoFar;
					minDistance = distance;
				}
				continue;
			}
	
			Vertex currentVertex = StationsVer.get(currentStation.stationName);
			ArrayList<String> neighbors = new ArrayList<>(currentVertex.neighbours.keySet());
	
			for (String neighbor : neighbors) {
				if (!processedStations.containsKey(neighbor)) {
					StationInfo nextStation = new StationInfo();
					nextStation.stationName = neighbor;
					nextStation.pathSoFar = currentStation.pathSoFar + neighbor + "  ";
					nextStation.minDistance = currentStation.minDistance + currentVertex.neighbours.get(neighbor); 
					stack.addFirst(nextStation);
				}
			}
		}
		shortestPath = shortestPath + Integer.toString(minDistance);
		return shortestPath;
	}
	
	// Finding number of InterChanges between two stations 
	public ArrayList<String> get_Interchanges(String str) {
		ArrayList<String> arr = new ArrayList<>();
		String res[] = str.split("  ");
		arr.add(res[0]);
		int count = 0;
		for(int i=1;i<res.length-1;i++) {
			int index = res[i].indexOf('~');
			String s = res[i].substring(index+1);
			if(s.length()==2){
				String prev = res[i-1].substring(res[i-1].indexOf('~')+1);
				String next = res[i+1].substring(res[i+1].indexOf('~')+1);
				if(prev.equals(next)) 
					arr.add(res[i]);
				else{
					arr.add(res[i]+" ==> "+res[i+1]);
					i++;
					count++;
				}
			}
			else
				arr.add(res[i]);
		}
		arr.add(Integer.toString(count));
		arr.add(res[res.length-1]);
		return arr;
	}

	// CREATE MAP
	public static void Create_Metro_Map(Delhi_Metro_Map g) {
		// ADDING VERTICES
		g.addVertex("Noida Sector 62~B");			g.addVertex("Botanical Garden~B");
		g.addVertex("Yamuna Bank~B");				g.addVertex("Rajiv Chowk~BY");
		g.addVertex("Vaishali~B");				g.addVertex("Moti Nagar~B");
		g.addVertex("Janak Puri West~BO");		g.addVertex("Dwarka Sector 21~B");
		g.addVertex("Huda City Center~Y");		g.addVertex("Saket~Y");
		g.addVertex("Vishwavidyalaya~Y");			g.addVertex("Chandni Chowk~Y");
		g.addVertex("New Delhi~YO");				g.addVertex("AIIMS~Y");
		g.addVertex("Shivaji Stadium~O");			g.addVertex("DDS Campus~O");
		g.addVertex("IGI Airport~O");				g.addVertex("Rajouri Garden~BP");
		g.addVertex("Netaji Subhash Place~PR");	g.addVertex("Punjabi Bagh West~P");

		// ADDING EDGES
		g.addEdge("Noida Sector 62~B",	 	"Botanical Garden~B",		8);
		g.addEdge("Botanical Garden~B", 		"Yamuna Bank~B", 			10);
		g.addEdge("Yamuna Bank~B", 			"Vaishali~B", 				8);
		g.addEdge("Yamuna Bank~B", 			"Rajiv Chowk~BY", 			6);
		g.addEdge("Rajiv Chowk~BY", 			"Moti Nagar~B", 				9);
		g.addEdge("Moti Nagar~B", 			"Janak Puri West~BO", 		7);
		g.addEdge("Janak Puri West~BO", 		"Dwarka Sector 21~B", 		6);
		g.addEdge("Huda City Center~Y", 		"Saket~Y", 					15);
		g.addEdge("Saket~Y", 				"AIIMS~Y", 					6);
		g.addEdge("AIIMS~Y", 				"Rajiv Chowk~BY", 			7);
		g.addEdge("Rajiv Chowk~BY", 			"New Delhi~YO", 				1);
		g.addEdge("New Delhi~YO", 			"Chandni Chowk~Y", 			2);
		g.addEdge("Chandni Chowk~Y", 		"Vishwavidyalaya~Y", 		5);
		g.addEdge("New Delhi~YO", 			"Shivaji Stadium~O", 		2);
		g.addEdge("Shivaji Stadium~O",		"DDS Campus~O", 				7);
		g.addEdge("DDS Campus~O", 			"IGI Airport~O", 			8);
		g.addEdge("Moti Nagar~B", 			"Rajouri Garden~BP", 		2);
		g.addEdge("Punjabi Bagh West~P", 	"Rajouri Garden~BP", 		2);
		g.addEdge("Punjabi Bagh West~P", 	"Netaji Subhash Place~PR", 	3);
	}
	
	public static void main(String[] args) throws IOException
	{
		Delhi_Metro_Map g = new Delhi_Metro_Map();															// Object of metro 
		Create_Metro_Map(g);																				// create the map
		System.out.println("\n\t\t\t**** WELCOME TO THE Delhi Metro APP *****");
		BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));							// take the input

		// int choice = Integer.parseInt(inp.readLine());
		// Starting taking choice
		while(true) 
		{
			System.out.println("\t\t\t\t~~How Can I Help You ~~\n\n");
			System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
			System.out.println("2. SHOW THE METRO MAP");
			System.out.println("3. GET Shortest DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			System.out.println("4. GET Shortest TIME TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			System.out.println("5. GET Shortest PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			System.out.println("6. EXIT THE MENU");
			System.out.println("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 6) : ");
			int choice = -1;

			try {
				choice = Integer.parseInt(inp.readLine());
			} catch(Exception e) {
				// default will handle
			}
			System.out.print("\n***********************************************************\n");

			if(choice == 6)
				System.exit(0);

			switch(choice) {
				case 1:
					g.display_Stations();
					break;
				case 2:
					g.display_Map();
					break;
				case 3:
					ArrayList<String> keys = new ArrayList<>(StationsVer.keySet());
					 printCodelist();
					System.out.println("Enter 1 :");
					int ch = Integer.parseInt(inp.readLine());
					
					String st1 = "", st2 = "";
					System.out.println("Enter the SOURCE and DESTINATION Station's Serial Number");
					if (ch == 1) {
						st1 = keys.get(Integer.parseInt(inp.readLine())-1);
						st2 = keys.get(Integer.parseInt(inp.readLine())-1);
					}else  {
						System.out.println("Invalid choice");
						System.exit(0);
					}
					HashMap<String, Boolean> processed = new HashMap<>();
					if(!g.containsStations(st1) || !g.containsStations(st2) || !g.hasPath(st1, st2, processed))
						System.out.println("THE INPUTS ARE INVALID");
					else
						System.out.println("Shortest DISTANCE from "+st1+" TO "+st2+" IS-----> " + g.dijkstra(st1, st2, false) + "KM\n" );
					break;
				
				case 4:
					ArrayList<String> keyss = new ArrayList<>(StationsVer.keySet());
					printCodelist();
					String strr1; String strr2;
					System.out.println("Enter The SOURCE and DESTINATION Station's Serial Number");
					
					strr1 = keyss.get(Integer.parseInt(inp.readLine())-1);
					strr2 = keyss.get(Integer.parseInt(inp.readLine())-1);
					
					HashMap<String, Boolean> processed1 = new HashMap<>();
					if(!g.containsStations(strr1) || !g.containsStations(strr2) || !g.hasPath(strr1, strr2, processed1))
						System.out.println("THE INPUTS ARE INVALID");
					else{
						double calcVal = ((double) g.dijkstra(strr1, strr2, false) / 25) * 60;
						System.out.println("Shortest Time from "+strr1+" TO "+strr2+" IS-----> " + calcVal + " Minutes\n" );
					}break;
				case 5:
					System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
					String s1 = inp.readLine();
					String s2 = inp.readLine();
				
					HashMap<String, Boolean> processed2 = new HashMap<>();
					if(!g.containsStations(s1) || !g.containsStations(s2) || !g.hasPath(s1, s2, processed2))
						System.out.println("THE INPUTS ARE INVALID");
					else 
					{
						ArrayList<String> str = g.get_Interchanges(g.getMinimumDistance(s1, s2));
						int len = str.size();
						System.out.println("------------------------------------");
						
						System.out.println("SOURCE STATION : " + s1);
						System.out.println("SOURCE STATION : " + s2);
						System.out.println("DISTANCE : " + str.get(len-1));
						System.out.println("NUMBER OF INTERCHANGES : " + str.get(len-2));  
						//System.out.println(str);
						System.out.println("~~~~~~~~~~~~~");
						System.out.println("START  ==>  " + str.get(0));
						for(int i=1; i<len-3; i++)
							System.out.println(str.get(i));
						System.out.print(str.get(len-3) + "   ==>    END");
						System.out.println("\n~~~~~~~~~~~~~");
					}
					break;
				
				default:  
					System.out.println("Please enter a valid option! ");
					System.out.println("The options you can choose are from 1 to 6. ");
			}
		}
	}	
}
