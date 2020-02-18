import java.util.HashMap;

public class Agent
{	
	protected int id;	
	protected int location;
	protected HashMap<Integer, Integer> attributes;

		
	Agent(int id)
	{
		this.id = id;
	}
	
	int self()
	{
		return id;
	}
	
	public int getLocation()
	{
		return location;
	}
	
}
