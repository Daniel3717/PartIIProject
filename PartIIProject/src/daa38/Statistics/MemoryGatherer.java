package daa38.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import daa38.CSP.Auxiliary.UnreasonablyLongTimeException;
import daa38.CSP.Main.Solver;
import daa38.Statistics.Auxiliary.SingleMemoryGatherer;

public class MemoryGatherer {
	
	private static boolean GATHERING_QUEENS = false;

	//adapted from https://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process
    public static void gatherSingleProcess(String lFileIn, String lFileOut, int pVO, int pVS, int pLB) throws IOException, InterruptedException, UnreasonablyLongTimeException {
    	Class<SingleMemoryGatherer> lC = SingleMemoryGatherer.class;
    	
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		
		String classpath = System.getProperty("java.class.path");
		String className = lC.getCanonicalName();
		
		String lWhichOne = "";
		if (GATHERING_QUEENS)
			lWhichOne = "Queens";
		else
			lWhichOne = "Map";
		ProcessBuilder builder = new ProcessBuilder(
		javaBin, "-cp", classpath, "-XX:+UseSerialGC", className, lFileIn, lFileOut, ""+pVO, ""+pVS, ""+pLB, lWhichOne);
		
		builder.inheritIO();
		
		//System.out.println("Gathering with "+lFileIn+" "+lFileOut+" "+pVO+" "+pVS+" "+pLB);
		Process process = builder.start();
		process.waitFor();
		
		//exit value of 0 indicates normal behaviour. exit value of 1 indicates abnormal behaviour.
		//In our case, 1 is most likely exceeded the alloted time. The IOException is the other choice, which is less probable.
		//Either way, the way we should proceed in the IOException case is the same as exceeding the alloted time.
		if (process.exitValue()==1)
			throw new UnreasonablyLongTimeException(Solver.UNREASONABLE_LONG_TIME);
		//System.out.println(process.exitValue());
		//System.out.println("Done gathering");
		
    }
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		while (true)
		{
			ArrayList<String> lCSPFilesIn = new ArrayList<String>();
			ArrayList<String> lCSPFilesOut = new ArrayList<String>();
			int lInstancesGenerated = 0;
			
			if (GATHERING_QUEENS)
			{
				String lBasePath = "NQueens/CSP";
				lInstancesGenerated = 72;
				Generator.massGenerateNQueensCSPs(lBasePath, lInstancesGenerated, lCSPFilesIn, lCSPFilesOut);
			}
			else
			{
				String lBasePath = "MapColouring/CSP";
				lInstancesGenerated = 100;
				Generator.massGenerateMapColouringCSPs(lBasePath, lInstancesGenerated, lCSPFilesIn, lCSPFilesOut);
			}
			
			int[][][] lIndex = new int[3][5][3];
			
			for (int lVO = 0; lVO <= 2; lVO++)
				for (int lVS = 0; lVS <= 4; lVS++)
					for (int lLB = 0; lLB <= 2; lLB++)
					{
						lIndex[lVO][lVS][lLB] = 0;
					}
			
			
			long lTimeBudget = 10000000000L;//in nanoseconds. 10 sec
			
			boolean lNeedAnotherTurn = true;
			while (lNeedAnotherTurn)
			{
				lNeedAnotherTurn = false;
			
				for (int lVO = 0; lVO <= 2; lVO++)
					for (int lVS = 0; lVS <= 4; lVS++)
						for (int lLB = 0; lLB <= 2; lLB++)
						{
				            int value = lVO*100+lVS*10+lLB;
				            //if ((value==0)||(value==111)||(value==122)||(value==231)||(value==242))
							if (lIndex[lVO][lVS][lLB]<lInstancesGenerated)
							{
								System.out.println("Running "+lVO+" "+lVS+" "+lLB+" on lIndex "+lIndex[lVO][lVS][lLB]+". ");
								try
								{
									int lTimeTook = 0;
									gatherSingleProcess(lCSPFilesIn.get(lIndex[lVO][lVS][lLB]),
														lCSPFilesOut.get(lIndex[lVO][lVS][lLB]),
														lVO, lVS, lLB);
									
									if (lTimeTook < lTimeBudget)
										lIndex[lVO][lVS][lLB]++;
									else
										lIndex[lVO][lVS][lLB] = lInstancesGenerated;
								}
								catch (UnreasonablyLongTimeException lULTE)
								{
									System.err.println("Time limit of "+lULTE.getTimeStopped()+" exceeded on "+lVO+lVS+lLB);
									lIndex[lVO][lVS][lLB] = lInstancesGenerated;
								}

								//System.out.println("Next index is "+lIndex[lVO][lVS][lLB]);
								lNeedAnotherTurn = true;
							}
						}
			}
			
			GATHERING_QUEENS = !GATHERING_QUEENS;
		}

	}

}
