import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class Simulation 
{

	int numCores = Runtime.getRuntime().availableProcessors();

	public static Vector<Agent> population        = new Vector<Agent>();	
	public static Vector<City>  nodes             = new Vector<City>();
	public static int numHCUs                     = 0;
	public static int currentTime                 = 0;		

	public static int cityLevels[] = new int[ConfigParameters.num_Nodes];
	public static int sampleCities[] = new int[ConfigParameters.sample_Size];
    public static ArrayList<Integer> unionNbrsList1K = new ArrayList<Integer>();
    public static ArrayList<Integer> unionNbrsList2K = new ArrayList<Integer>();

	public static int samplePopulation[];	
	public  ModelEpidemic me;      	
	public boolean isQuerySatisfied;
	public static int maxSATCities;
	public static int maxLevel;
	
	Simulation(int _numHCUs)
	{
		numHCUs         = _numHCUs;		
		currentTime     = 0;
		isQuerySatisfied        = false;		
		me              = new ModelEpidemic();
		population.clear();
		nodes.clear();		
		unionNbrsList1K.clear();
		unionNbrsList2K.clear();
		maxSATCities = 0;
		maxLevel = 2*ConfigParameters.sim_Time;
	}
	
	public void preConfiguration() throws IOException 
	{
		
		for(int i=0; i<ConfigParameters.num_People; i++)
		{
			Agent agent = new Agent(i);					
			population.add(agent);
		}	
		
		for(int i=0; i<ConfigParameters.num_Nodes; i++)
		{
			City city = new City(i);
			city.people = new Vector<Integer>();
			nodes.add(city);
			cityLevels[i] = 2*ConfigParameters.sim_Time;
		}		
		
		ModelEpidemic.initialPositionOfAgents(population);
		generateSampleCities();
		findSamplePopulation();
		//printNodeLevels();		
		Run();		
	}/* End of preConfiguration() */	
	
	public void printNodeLevels()
	{
		System.out.println("nodeId --> Level");		
		for(int i=0; i<unionNbrsList2K.size(); i++)
		{			
			int cityid = unionNbrsList2K.get(i);
			System.out.println(cityid + " " + cityLevels[cityid]);
		}
		System.out.println();
	}/*End of the printNodeLevels()*/
	
	public void preRun() throws IOException
	{
		if(checkQuerySatisfied())
		{
			System.out.println("Query Has Satisfied !! ");
			isQuerySatisfied = true;
		}
	}/* End of preRun()*/	
	
	public void Run() throws IOException
	{
		//ConfigParameters.sim_Time
		for(int i=0; i<ConfigParameters.sim_Time; i++)
		{
			if(isQuerySatisfied)
				break;
			
			currentTime = i;
			preRun();			
			nextPositionOfAgents();			
			postRun(i);			
		}
		preRun();			
	}/* End of Run()*/

	public void postRun(int time) throws IOException
	{				
		
	}/* End of postRun()*/
	
	/*function to check the fraction of cities with in random sample contains the given threshold of agents*/
	public boolean checkQuerySatisfied()
	{
		if(currentTime == 0)
			return false;
		
		int count[] = new int[(ConfigParameters.num_Nodes)];
		
		for(int i=0; i<ConfigParameters.num_Nodes; i++)
			count[i] = 0;		
		
		for(int i=0; i<samplePopulation.length; i++)
		{
			int agentId = samplePopulation[i];
			int loc = population.get(agentId).location;
			count[loc] =  count[loc] + 1;			
		}		
		
		int avgPopCity = ConfigParameters.num_People/ConfigParameters.num_Nodes;
		
		boolean isSatisfied = false;
		int satCities = 0;
		
		for(int i=0; i<unionNbrsList1K.size(); i++)
		{
			int cityId = unionNbrsList1K.get(i);
			
			if(count[cityId] >= (ConfigParameters.inf_Threshold * avgPopCity ))
			{
				satCities = satCities+1;
			}
			//System.out.println("City Id " + cityId + " :" + count[cityId]);
		}
		
		//System.out.println("Number of Cities Satisfied : " + satCities);
		
		if(satCities >= (unionNbrsList1K.size()))
		{
			maxSATCities = unionNbrsList1K.size();
			isSatisfied = true;
		}
		else
		{
			if(maxSATCities < satCities)
				maxSATCities = satCities;
		}
		
		return isSatisfied;
		
	}/* End of checkQuerySatisfied() */	
	
	
	/*function to check the fraction of cities with in random sample contains the given threshold of agents */
	public boolean checkQuerySatisfied_1()
	{
		if(currentTime == 0)
			return false;
		
		int count[] = new int[(ConfigParameters.num_Nodes)];
		
		for(int i=0; i<ConfigParameters.num_Nodes; i++)
			count[i] = 0;		
		
		for(int i=0; i<samplePopulation.length; i++)
		{
			int agentId = samplePopulation[i];
			int loc = population.get(agentId).location;
			count[loc] =  count[loc] + 1;			
		}		
		
		int avgPopCity = ConfigParameters.num_People/ConfigParameters.num_Nodes;
		
		boolean isSatisfied = false;
		int satCities = 0;
		
		for(int i=0; i<sampleCities.length; i++)
		{
			int cityId = sampleCities[i];
			
			if(count[cityId] >= (ConfigParameters.inf_Threshold * avgPopCity ))
			{
				satCities = satCities+1;
			}
			//System.out.println("City Id " + cityId + " :" + count[cityId]);
		}
		
		System.out.println("Number of Cities Satisfied : " + satCities);
		
		if(satCities >= (ConfigParameters.local_Prob * ConfigParameters.sample_Size))
		{
			maxSATCities = ConfigParameters.sample_Size;
			isSatisfied = true;
		}
		else
		{
			if(maxSATCities < satCities)
				maxSATCities = satCities;
		}
		
		return isSatisfied;
		
	}/* End of checkQuerySatisfied() */	


	public void postConfiguration()
	{
		
	}/* End of postConfiguration()*/	
	
	public void nextPositionOfAgents() throws IOException 
	{
		int diffLevel = (maxLevel-currentTime);
		for(int i=0; i<samplePopulation.length; i++)
		{
			int agentId = samplePopulation[i];	
			Agent agent = population.get(agentId);
			//int loc = agent.location;
			//if(cityLevels[(agent.location)] <= diffLevel )
			{
				ModelEpidemic.updationRulesForAgentLocation(agent, Math.random());
			}					
		}		
	}/*End of nextPositionOfAgents() */		
	
	
	public int calculateSampleSize()
	{
		int sum=0;		
		for(int i=0; i<unionNbrsList2K.size(); i++)
		{
		    int cityId = unionNbrsList2K.get(i);			
		    int strength = nodes.get(cityId).people.size();
			sum = sum + strength;
		}		
	 	return sum;	
	}
	
	public void findSamplePopulation()
	{
		int avgPopCity = ConfigParameters.num_People/ConfigParameters.num_Nodes;
		int count = 0;		
	
		//System.out.println("Total People : " + unionNbrsList2K.size()*avgPopCity);

		int maxSampleSize = calculateSampleSize(); //(int)Math.round(unionNbrsList2K.size()*avgPopCity*1.1);
		
		//System.out.println("Expected People : " + maxSampleSize);

		samplePopulation = new int[maxSampleSize];	
		//System.out.print("\nunion list size : " + unionNbrsList.size() + "\n");		
		for(int i=0; i<unionNbrsList2K.size(); i++)
		{
			int cityId = unionNbrsList2K.get(i);			
		    //int startAgent =(cityId*avgPopCity);		    
			//System.out.print("\n");
			int strength = nodes.get(cityId).people.size();
			
			for(int j=0; j<strength; j++)
			{
				
				samplePopulation[count] =  nodes.get(cityId).people.get(j);				
				//System.out.print(j + " ");				
				count = count + 1;
			}					
			//System.out.print("\n");			
		}		

	}/*End of samplePopulation() */
	
	@SuppressWarnings("unchecked")
	public void generateSampleCities()
	{
		ArrayList<Integer>[] nbrList = new ArrayList[(ConfigParameters.sample_Size)];		
		//System.out.println("Generate Sample of Nodes");
		//System.out.println();
		for(int i=0; i<ConfigParameters.sample_Size; i++)
		{
			//nbrList[i] = new ArrayList<Integer>();
			Random randum = new Random();
			int source = randum.nextInt(ConfigParameters.num_Nodes);
			sampleCities[i] = source;
			//System.out.print(source + " ");
			nbrList[i] = findAllNodesAtDistanceK(source, ConfigParameters.sim_Time);
		}
		//System.out.println();

		Set<Integer> set = new HashSet<Integer>();
		
		for(int i=0; i<ConfigParameters.sample_Size; i++)
		{
			set.addAll(nbrList[i]);
		}   
		
		unionNbrsList1K = new ArrayList<Integer>(set);
		Collections.sort(unionNbrsList1K);
		//System.out.println("Union of Neighbors List Size : " + unionNbrsList1K.size());
		//System.out.println("Union of 1KNeighbors List : " + unionNbrsList1K +" size : "+ unionNbrsList1K.size());
		generateSampleCities2();
	}
	
	@SuppressWarnings("unchecked")
	public void generateSampleCities2()
	{
		ArrayList<Integer>[] nbrList = new ArrayList[(ConfigParameters.sample_Size)];		
		//System.out.println("Generate Sample of Nodes");
		//System.out.println();
		for(int i=0; i<ConfigParameters.sample_Size; i++)
		{
			//nbrList[i] = new ArrayList<Integer>();
			//Random randum = new Random();
			int source = sampleCities[i] ; //randum.nextInt(ConfigParameters.num_Nodes);
			//System.out.print(source + " ");
			nbrList[i] = findAllNodesAtDistance2K(source, 2*ConfigParameters.sim_Time);
		}
		//System.out.println();
		Set<Integer> set = new HashSet<Integer>();		
		for(int i=0; i<ConfigParameters.sample_Size; i++)
		{
			set.addAll(nbrList[i]);
		}   		
		unionNbrsList2K = new ArrayList<Integer>(set);
		Collections.sort(unionNbrsList2K);
		//System.out.println("Union of Neighbors List Size : " + unionNbrsList2K.size());
		//System.out.println("Union of 2KNeighbors List : " + unionNbrsList2K +" size : "+ unionNbrsList2K.size());		
	}
	
	
	/* This function finds all the nodes reachable from source node with in distance K */
	public ArrayList<Integer> findAllNodesAtDistance2K(int source, int distance)
	{
		// array to store level of each node  
		int level[]  = new int[(InitialSetup.rows)*(InitialSetup.columns)];  
		boolean marked[] = new boolean[(InitialSetup.rows)*(InitialSetup.columns)];	    
		//int root = source;
		
		ArrayList<Integer> nbrList  = new ArrayList<Integer>();		
		Queue<Integer> queue = new LinkedList<Integer>(); 
		queue.add(source);		
		level[source] = 0;  // initialize level of source node to 0
		cityLevels[source] = 0;
		marked[source] = true;	// marked it as visited  
		//System.out.println("\n Node Id --> Level\n");  
  
		while (queue.size() > 0)  
		{  
			source = queue.peek();
			nbrList.add(source);
			//System.out.println("  " +source +" --> " + level[source] );  
			queue.remove();  
			
			for (int i = 0; i < InitialSetup.adjList[source].size(); i++)  
			{  
				int b = InitialSetup.adjList[source].get(i); // b is neighbor of node source
				if((!marked[b]) && (level[source]<distance)) // if b is not marked already
				{  
					queue.add(b); // enqueue b in queue    
					level[b] = level[source] + 1; // level of b is level of x + 1  
					if(cityLevels[b] > level[b])
					{
						//if(cityLevels[b] != (2*ConfigParameters.sim_Time))
						//{	System.out.println("Enter Node Id :" + b + " Current level :" + cityLevels[b] + " Next level :" + level[b]);}
						
						cityLevels[b]=level[b];
					}						
					marked[b] = true; // mark b 
				}  
			}  
		}	  
		//System.out.println("Node : "+ root + " total neighbors :" + nbrList.size());
		return nbrList;
	}/* End of the findAllNodesAtDistanceK() */
	
	
	/* This function finds all the nodes reachable from source node with in distance K */
	public ArrayList<Integer> findAllNodesAtDistanceK(int source, int distance)
	{
		// array to store level of each node  
		int level[]  = new int[(InitialSetup.rows)*(InitialSetup.columns)];  
		boolean marked[] = new boolean[(InitialSetup.rows)*(InitialSetup.columns)];	    
		//int root = source;
		
		ArrayList<Integer> nbrList  = new ArrayList<Integer>();		
		Queue<Integer> queue = new LinkedList<Integer>(); 
		queue.add(source);		
		level[source] = 0;  // initialize level of source node to 0  
		marked[source] = true;	// marked it as visited  
		//System.out.println("\n Node Id --> Level\n");  
  
		while (queue.size() > 0)  
		{  
			source = queue.peek();
			nbrList.add(source);
			//System.out.println("  " +source +" --> " + level[source] );  
			queue.remove();  
			
			for (int i = 0; i < InitialSetup.adjList[source].size(); i++)  
			{  
				int b = InitialSetup.adjList[source].get(i); // b is neighbor of node source
				if((!marked[b]) && (level[source]<distance)) // if b is not marked already
				{  
					queue.add(b); // enqueue b in queue    
					level[b] = level[source] + 1; // level of b is level of x + 1    
					marked[b] = true; // mark b 
				}  
			}  
		}	  
		//System.out.println("Node : "+ root + " total neighbors :" + nbrList.size());
		return nbrList;
	}/* End of the findAllNodesAtDistanceK() */
}
