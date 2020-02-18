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
	}/* End of the function initialPositionOfAgents() */


	public static void initialPositionOfAgents_1(Vector<Agent> population ) throws IOException
	{
		if(ConfigParameters.num_People%ConfigParameters.num_Nodes != 0)
		{
			System.out.println("Error!! Numpber of people must be multiple of Nodes(Cities)");
			System.exit(1);
		}
		
		int count[] = new int[ConfigParameters.num_Nodes];
		
		for(int i=0; i<ConfigParameters.num_Nodes; i++)
			count[i] = 0;

		Random randum = new Random();		

		for(int i=0; i<ConfigParameters.num_People; i++)
		{
			int loc = randum.nextInt(ConfigParameters.num_Nodes);			
			population.get(i).location = loc;
			Simulation.nodes.get(loc).people.add(i);
			count[loc] = count[loc] +1;
		}		
		
		Vector<Integer> aac = new Vector<Integer>();
		Vector<Integer> bac = new Vector<Integer>();
		Vector<Integer> cac = new Vector<Integer>();
		Vector<Integer> dac = new Vector<Integer>();
		Vector<Integer> eac = new Vector<Integer>();

		int average = ConfigParameters.num_People/ConfigParameters.num_Nodes;

		for(int i=0; i<ConfigParameters.num_Nodes; i++)
		{
			if(count[i] >=average)
				aac.add(i);
			else if(count[i] >= (0.9*average))
				bac.add(i);
			else if(count[i] >= (0.8*average))	
				cac.add(i);	
			else if(count[i] >= (0.7*average))
				dac.add(i);
			else	
				eac.add(i);
		}
		
		System.out.println("\nAbove 100% of Average :" + aac.size() );//+ " " +aac);		
		for(int i=0; i<aac.size(); i++)
		{
		   int loc = aac.get(i);
		   System.out.println("City Id: " +loc + " : "+ count[loc]);
		}

		System.out.println("\nAbove 90% of Average :" + bac.size());//+ aac.size() + " " +aac);		
		for(int i=0; i<bac.size(); i++)
		{
		   int loc = bac.get(i);
		   System.out.println("City Id: " +loc + " : "+ count[loc]);
		}

		System.out.println("\nAbove 80% of Average :" );//+ aac.size() + " " +aac);		
		for(int i=0; i<cac.size(); i++)
		{
		   int loc = cac.get(i);
		   System.out.println("City Id: " +loc + " : "+ count[loc]);
		}
	
		System.out.println("\nAbove 70% of Average :" );//+ aac.size() + " " +aac);		
		for(int i=0; i<dac.size(); i++)
		{
		   int loc = dac.get(i);
		   System.out.println("City Id: " +loc + " : "+ count[loc]);
		}
	
		System.out.println("\nBelow 70% of Average :" );//+ aac.size() + " " +aac);		
		for(int i=0; i<eac.size(); i++)
		{
		   int loc = eac.get(i);
		   System.out.println("City Id: " +loc + " : "+ count[loc]);
		}
			
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
		//System.out.println("Agent " + agent.self() +" CurrLoc : " + homeLoc + " Random : " + randum + " Sum : " + sum + "  NextLoc : " + agent.location + " neighbors:" + InitialSetup.selfAdjList[homeLoc] );
		
		if(sum > 1.000005)
		{
			System.out.println("Error!! Cummulative Probability is Greater Than ONE : " + sum);
			System.exit(0);
		}		
	}/* End of updationRulesForAgentLocation() */	
}
