import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;

public class testGenericSMC
{
	public static long totalSATCities = 0;
	public static long totalSampleCities = 0;

	/* calculate number of times simulation to be run using estimation method */
	public int calNumOfSimulations(double ci, double epsilon)
	{
		int num_sims = 0;
		epsilon = 2*epsilon*epsilon;
		ci = Math.log(2/ci);
		num_sims = (int)Math.ceil(ci/epsilon);
		return num_sims;
	}

	public static void main(String args[]) throws Exception
	{
		/*System.setOut(new PrintStream(new OutputStream() {
			public void write(int b){  NO-OP  }
		}));*/		 

		String outFile = System.getProperty("user.dir")+ "/../" +"Output/output.txt";
		String resFile = System.getProperty("user.dir")+ "/../" +"Output/results.txt";
		System.setOut(new PrintStream(new FileOutputStream(outFile)));		

		File fe = new File(resFile);    
		if(fe.exists())
			fe.delete();

		File configFile =  new File(System.getProperty("user.dir")+ "/../" +"Config/config.properties");
		ConfigParameters cp = new ConfigParameters(configFile);
		cp.read_configFile();		
		
		InitialSetup is = new InitialSetup();
		is.generate_Network_Topology();
		
		long startTime = System.currentTimeMillis();

		testGenericSMC th = new testGenericSMC();
		int numRuns   = th.calNumOfSimulations(ConfigParameters.val_Ci, ConfigParameters.val_Epsilon);
		int numSucess  = 0;
		int numFailure = 0;		
		
		System.out.println("\nNum of Runs : " + numRuns);

		for(int j=0; j<numRuns; j++)
		{
			
			Simulation sm = new Simulation(0);
			sm.preConfiguration();

			FileWriter fw =  new FileWriter(resFile, true);
		
			if(sm.isQuerySatisfied)
			{
				numSucess  = numSucess + 1;
				fw.write("\nIteration Id : " + j +"\tSuccess"+"  \tQuery SAT Time: " + Simulation.currentTime+ " \tNum Cities:" + Simulation.maxSATCities);
			}
			else
			{
				numFailure = numFailure + 1;
				fw.write("\nIteration Id : " + j +"\tFailure"+"  \tQuery SAT Time: " + Simulation.currentTime+ " \tNum Cities:" + Simulation.maxSATCities);
			}
			fw.close();
			
			totalSATCities = totalSATCities + Simulation.maxSATCities;
			totalSampleCities = totalSampleCities + Simulation.unionNbrsList1K.size();
		}

		int totalRuns = numFailure+numSucess;

		FileWriter fw =  new FileWriter(resFile, true);
		fw.write("\nnum Sucess  : " + numSucess +"\tnum Failure : " + numFailure);
		fw.write("\nProbability of given query has been satisfied : " + (double)numSucess/totalRuns+"\n");
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);  //Total execution time in milli seconds
		
		double satFraction = (double)totalSATCities/(totalSampleCities); //ConfigParameters.sample_Size); 
		fw.write("\nFraction of cities satisfied query : " +satFraction+"\n");
		System.out.println("\nFraction of cities satisfied query : " +satFraction+"\n");
		System.out.println("\nExecution Time testGenericSMC:" + duration);
		fw.write("\nExecution Time testGenericSMC:" + duration);
		fw.close();
		System.err.println("\nFraction of cities satisfied query : " +satFraction);
		System.err.println("\nExecution Time testGenericSMC(in milli seconds) : " + duration);
		//System.out.println(duration);

	}
}
