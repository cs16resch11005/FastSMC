import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigParameters
{
	
	  private File config_File; 

	  public static int     num_Nodes;
	  public static int     sim_Time;
	  public static int     num_People;
	  public static int     num_HCUs;
	  public static int     num_Infected;
	  public static int 	sample_Size;
	  public static double  local_Prob;
	  public static double  inf_Threshold;

	  public static double  beta;
	  public static double  delta;
	  
	  public static double  val_Ci;	  
	  public static double  val_Epsilon;
	 
	  public static int     strategy_Name;
	  public static int     compare_Strategy;
	  
	  public static String  path_init_infected_people;
	  public static String  path_init_pop_at_each_node;	
	  
	  public static String  path_position_folder;
	  public static String  path_state_folder;
	  public static String  path_output_folder; 

	  public static String query;
	  /* constructor */
	  ConfigParameters(File _configFile)
	  {
	    config_File = _configFile;		
	  }/* End of ESP() */

	  /* reading the configuration file and assign values to input variables */
	  public void read_configFile() throws Exception 
	  {
	         
	     FileReader fr = null;
		
		 try 
		 {
			             fr  = new FileReader(config_File);
			Properties props = new Properties();
			props.load(fr);

			num_Nodes 		              = Integer.parseInt(props.getProperty("num_nodes"));
			sim_Time 		              = Integer.parseInt(props.getProperty("time_units"));
			num_People			          = Integer.parseInt(props.getProperty("num_people"));
			num_HCUs			          = Integer.parseInt(props.getProperty("num_HCUs"));
			num_Infected				  = Integer.parseInt(props.getProperty("num_infected"));
			sample_Size					  = Integer.parseInt(props.getProperty("sample_size"));
			local_Prob					  = Double.parseDouble(props.getProperty("local_prob"));
			inf_Threshold				  = Double.parseDouble(props.getProperty("inf_threshold"));;
			beta					      = Double.parseDouble(props.getProperty("beta"));
			delta					      = Double.parseDouble(props.getProperty("delta"));
			
			val_Ci					      = Double.parseDouble(props.getProperty("val_ci"));
			val_Epsilon					      = Double.parseDouble(props.getProperty("val_epsilon"));
			
			
			strategy_Name				  = Integer.parseInt(props.getProperty("strategy_name"));
			compare_Strategy			  = Integer.parseInt(props.getProperty("compare_strategy"));

			path_init_infected_people         = System.getProperty("user.dir")+ "/../" + props.getProperty("init_infected_people");
	        path_init_pop_at_each_node		  = System.getProperty("user.dir")+ "/../" + props.getProperty("init_pop_at_each_node");
	        	
	       	path_position_folder              = System.getProperty("user.dir")+ "/../" + props.getProperty("location_of_position_folder");
	       	path_state_folder                 = System.getProperty("user.dir")+ "/../" + props.getProperty("location_of_state_folder");
	      	path_output_folder		          = System.getProperty("user.dir")+ "/../" + props.getProperty("location_of_output_folder"); 
	        
	       	query							  = props.getProperty("query");
	       	
	       	System.out.println("Num of Nodes             :" + num_Nodes);		
	       	System.out.println("Simulation Time Units    :" + sim_Time);		
	       	System.out.println("Total Population         :" + num_People);		
	       	System.out.println("Num of Health Care Units :" + num_HCUs);	
	       	System.out.println("Num of initial Infected  :" + num_Infected);
	       	System.out.println("HCU Sample Size          :" + sample_Size);
	       	System.out.println("local probability        :" + local_Prob);
	       	System.out.println("Infected  Threshold      :" + inf_Threshold); 
	       	
	       	System.out.println("Infection Rate           :" + beta);
	       	System.out.println("Recovery Rate            :" + delta);
	       	
	    	System.out.println("CI			             :" + val_Ci);
	       	System.out.println("Epsilon                  :" + val_Epsilon);
	       	
	       	System.out.println(path_init_infected_people);	
	       	System.out.println(path_init_pop_at_each_node);

	       	System.out.println(path_position_folder);
	       	System.out.println(path_state_folder);
	       	System.out.println(path_output_folder);
	       	System.out.println(query);
			
		   }
		   catch(FileNotFoundException ex)
	       {
	          // System.out.println(" \n Config File is not found, please pass it through constructor!! \n");
		   }
	   	   catch(IOException ex)
	       {
	            ex.printStackTrace();
	  	   }
	       catch(Exception e)
	       {
		     // System.out.println(" \n Error occured, please try again!! \n ");
			  return;
		   }
	       finally
	       {
	              fr.close();                
	       }         

	   }/* End Of read_configFile() */
}
