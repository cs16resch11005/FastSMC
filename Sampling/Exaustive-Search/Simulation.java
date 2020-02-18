import java.io.IOException;
import java.util.Vector;

public class Simulation 
{

	int numCores = Runtime.getRuntime().availableProcessors();

	public static Vector<Agent> population        = new Vector<Agent>();	
	public static Vector<City>  nodes             = new Vector<City>();
	public static int numHCUs                     = 0;
	public static int currentTime                 = 0;		

	public  ModelEpidemic me;      	
	public boolean isQuerySatisfied;
	
	public static int maxSATCities;

	
	Simulation(int _numHCUs)
	{
		numHCUs         = _numHCUs;		
		currentTime     = 0;
		isQuerySatisfied        = false;		
		me              = new ModelEpidemic();
		population.clear();
		nodes.clear();		
		maxSATCities=0;
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
		}		
		
		ModelEpidemic.initialPositionOfAgents(population);
		Run();		
	}/* End of preConfiguration() */	
	
		
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
	
	public boolean checkQuerySatisfied()
	{
		if(currentTime == 0)
			return false;
		
		int count[] = new int[(ConfigParameters.num_Nodes)];
		
		for(int i=0; i<ConfigParameters.num_Nodes; i++)
		{
			count[i] = 0;
		}		
		
		for(int i=0; i<ConfigParameters.num_People; i++)
		{
			int loc = population.get(i).location;
			count[loc] =  count[loc] + 1;			
		}		
		
		int avgPopCity = ConfigParameters.num_People/ConfigParameters.num_Nodes;
		
		boolean isSatisfied = false;
		int satCities = 0;
		
		for(int i=0; i<ConfigParameters.num_Nodes; i++)
		{
			if(count[i] >= (ConfigParameters.inf_Threshold * avgPopCity ))
					satCities = satCities+1;			
			//System.out.println("City Id " + i + " :" + count[i]);
		}
		
		System.out.println("Number of Cities Satisfied : " + satCities);
		
		if(satCities >= (ConfigParameters.num_Nodes))//ConfigParameters.local_Prob*
		{
			maxSATCities = ConfigParameters.num_Nodes;
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
		for(int i=0; i<ConfigParameters.num_Nodes; i++)
		{
			int strength  =  nodes.get(i).people.size();
			
			for(int j=0; j<strength; j++)
			{
				int id = nodes.get(i).people.get(j);
				ModelEpidemic.updationRulesForAgentLocation(population.get(id), Math.random());
			}		
		}
	}/*End of nextPositionOfAgents() */			
	
}
