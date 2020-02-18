import java.io.IOException;
import java.util.Vector;
import java.util.Random;

public class ModelEpidemic
{
		
	public static double[] stateRandum;
	
	public static void initialPositionOfAgents(Vector<Agent> population ) throws IOException
	{
		if(ConfigParameters.num_People%ConfigParameters.num_Nodes != 0)
		{
			System.out.println("Error!! Numpber of people must be multiple of Nodes(Cities)");
			System.exit(1);
		}
		
		Random randum = new Random();		
		for(int i=0; i<ConfigParameters.num_People; i++)
		{
			int loc = randum.nextInt(ConfigParameters.num_Nodes);			
			population.get(i).location = loc;
			Simulation.nodes.get(loc).people.add(i);
		}		
		
		/*for(int i=0; i<ConfigParameters.num_People; i++)
		{
			System.out.println("Agent Id " + i + " : " +  population.get(i).location);
		}*/
		
	}/* End of the function initialPositionOfAgents() */
	
	public static void updationRulesForAgentLocation(Agent agent, double randum)
	{
		int homeLoc = agent.location;		
		int neighbors = InitialSetup.selfAdjList[homeLoc].size();
		
		double sum = 0.0;
        double uniformProb = 1.0/neighbors;        
         
		for(int i=0; i<neighbors; i++) 
		{
		   sum = sum + uniformProb;
		 
		   if(randum <= sum)
		   {			 		  
			  agent.location = InitialSetup.selfAdjList[homeLoc].get(i);
			  break;
		   }		   
		}		
	
		//System.out.println("Agent " + agent.self() +" CurrLoc : " + homeLoc + " Random : " + randum + " Sum : " + sum + "  NextLoc : " + agent.location + " neighbors:" + InitialSetup.selfAdjList[homeLoc]);
		
		if(sum > 1.000005)
		{
			System.out.println("Error!! Cummulative Probability is Greater Than ONE : " + sum);
			System.exit(0);
		}		
	}/* End of updationRulesForAgentLocation() */	
}
