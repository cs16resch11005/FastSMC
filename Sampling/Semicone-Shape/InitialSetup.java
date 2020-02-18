import java.util.ArrayList;
import java.util.Collections;

public class InitialSetup 
{
	public static int num_pop_at_node[]; // stores initial population at each node	
	public static int rows;
	public static int columns;	
	public static ArrayList<Integer>[] adjList;
	public static ArrayList<Integer>[] selfAdjList;
	 
	@SuppressWarnings("unchecked")
	/* constructor */
	InitialSetup()
	{
		rows = (int) Math.sqrt(ConfigParameters.num_Nodes);
		columns = rows;
		adjList = new ArrayList[(rows*columns)];
		selfAdjList = new ArrayList[(rows*columns)];

		// initializing 
		for (int i = 0; i < (rows*columns); i++)
		{ 
			adjList[i] = new ArrayList<Integer>(); 
			selfAdjList[i] = new ArrayList<Integer>();
		} 
	}		

	public void generate_Network_Topology()
	{
		//System.out.println("Entered");
		generateGrid2dGraph();	
		selfLoop2dGridGraph();
		
		
	}
	
	/*This function generates the 2d grid graph with self loop*/
	public void selfLoop2dGridGraph()
	{
		for(int i=0; i<rows*columns; i++)
		{
			selfAdjList[i].addAll(adjList[i]);
			selfAdjList[i].add(i);
			Collections.sort(selfAdjList[i]);			
			//System.out.println(" Node Id : " + i + " --> "+ selfAdjList[i]);
		}
	}
	
	/*This function generates the 2d grid graph */
	public void generateGrid2dGraph()
	{
		int nodeId = 0;

		for(int i=0; i<rows; i++)
		{		

			if(i == 0)
			{		
				for(int j=0; j<columns; j++)
				{
					nodeId = (i*columns) + j;

					if(j == 0)
					{
						adjList[nodeId].add(nodeId+1);
						adjList[nodeId].add(nodeId+columns);
					}
					else if(j == columns-1)
					{
						adjList[nodeId].add(nodeId-1);
						adjList[nodeId].add(nodeId+columns);
					}
					else
					{
						adjList[nodeId].add(nodeId-1);
						adjList[nodeId].add(nodeId+1);
						adjList[nodeId].add(nodeId+columns);
					}
				}
			}
			else if(i == rows-1)
			{

				for(int j=0; j<columns; j++)
				{
					nodeId = (i*columns) + j;
					if(j == 0)
					{
						adjList[nodeId].add(nodeId-columns);
						adjList[nodeId].add(nodeId+1);
					}
					else if(j == columns-1)
					{
						adjList[nodeId].add(nodeId-columns);
						adjList[nodeId].add(nodeId-1);
					}
					else
					{
						adjList[nodeId].add(nodeId-columns);
						adjList[nodeId].add(nodeId-1);
						adjList[nodeId].add(nodeId+1);
					}
				}
			}
			else
			{
				for(int j=0; j<columns; j++)
				{
					nodeId = (i*columns) + j;
					if(j == 0)
					{
						adjList[nodeId].add(nodeId-columns);
						adjList[nodeId].add(nodeId+1);
						adjList[nodeId].add(nodeId+columns);
					}
					else if(j == columns-1)
					{
						adjList[nodeId].add(nodeId-columns);
						adjList[nodeId].add(nodeId-1);
						adjList[nodeId].add(nodeId+columns);
					}
					else
					{
						adjList[nodeId].add(nodeId-columns);
						adjList[nodeId].add(nodeId-1);
						adjList[nodeId].add(nodeId+1);
						adjList[nodeId].add(nodeId+columns);
					}
				}
			}			
		}

		/*for(int i=0; i<rows*columns; i++)
		{
			if(adjList[i].size() <= 4)
			{
				System.out.println( "Node Id : "+ i + " --> " + adjList[i]);
			}
			else
			{
				System.out.println( "Error!! Node has more than 4 Neighbors; Node Id : "+ i + " --> " + adjList[i]);
				System.exit(1);
			}
		}	*/	
	}/*End of the generate_grid_2d_graph() */

	
}