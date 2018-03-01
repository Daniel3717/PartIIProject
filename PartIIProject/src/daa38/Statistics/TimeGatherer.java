package daa38.Statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import daa38.CSP.Auxiliary.UnreasonablyLongTimeException;
import daa38.CSP.LookBack.LookBack;
import daa38.CSP.Main.Solver;
import daa38.CSP.ValueSelection.ValueSelection;
import daa38.CSP.VariableOrdering.VariableOrdering;
import daa38.Statistics.Auxiliary.InputProcessing;


public class TimeGatherer {

	private static final double HICCUPS_THRESHOLD_PERCENTAGE = 0.1f; //means 10%
	private static final double HICCUPS_THRESHOLD_FLAT = 10000000;//in nanoseconds, it means 10 milliseconds
	//private static final int HICCUPS_SAMPLES = 2;. It is fixed at 2 at the moment.
	private static final int RANDOM_SAMPLES = 30;
	private static final int MAX_EXCEEDED_TIME_SAMPLES = 5;
	private static final boolean GATHERING_QUEENS = true;//will gather from nQueens when true, otherwise from MapColouring
	
	//returns the time it for the successful solve to complete
	private static long trySolve(Solver pS, VariableOrdering pVO, ValueSelection pVS, LookBack pLB, String pFileIn, String pFileOut) throws IOException, UnreasonablyLongTimeException
	{
		long lTime = -1;
		long lNrTimesExceeded = 0;
		while (lTime==-1)
		{
			try
			{
				lTime = pS.solve(pFileIn, pFileOut, pVO, pVS, pLB);
			}
			catch (UnreasonablyLongTimeException lULTE)
			{
				lNrTimesExceeded++;
				if (lNrTimesExceeded>MAX_EXCEEDED_TIME_SAMPLES)
					throw new UnreasonablyLongTimeException(lULTE.getTimeStopped());
			}
		}
		return lTime;
		
	}
	
	//See InputProcessing intToVO, intToVS, int ToLB to understand which int goes to which class
	//Returns the maximum time it took to solve an instance in nanoseconds
	public static long gather(Integer pOrder, Integer pSelect, Integer pBack, ArrayList<String> pCSPFilesIn, ArrayList<String> pCSPFilesOut) throws IOException, UnreasonablyLongTimeException
	{	
		Solver lSolver = new Solver();

		VariableOrdering lVO = InputProcessing.intToVO(pOrder, lSolver);
		
		ValueSelection lVS = InputProcessing.intToVS(pSelect, lSolver);
		
		LookBack lLB = InputProcessing.intToLB(pBack, lSolver);


		String lWhichOne = "";
		if (GATHERING_QUEENS)
			lWhichOne = "Queens";
		else
			lWhichOne = "Map";
			
		String lData = "Statistics/Time/Data"+lWhichOne+pOrder+pSelect+pBack+".txt";
		FileWriter lFW = new FileWriter(new File(lData),true);
		long lMaxTime=0;
		
		
		for (int lIt = 0; lIt < pCSPFilesIn.size(); lIt++)
		{
			String lFileIn = pCSPFilesIn.get(lIt);
			String lFileOut = pCSPFilesOut.get(lIt);
			
			System.out.println("With "+pOrder+pSelect+pBack+". At file "+lFileIn);
			long lTime = 0;
			
			if (pOrder!=0)
			{
				long lPrevTime = -1;
				lTime = trySolve(lSolver,lVO,lVS,lLB,lFileIn,lFileOut);
				do
				{
					System.out.println("With "+pOrder+pSelect+pBack+". At file "+lFileIn);
					lPrevTime=lTime;
					lTime = trySolve(lSolver,lVO,lVS,lLB,lFileIn,lFileOut);
				}
				while (Math.abs(lTime-lPrevTime)>Math.max(HICCUPS_THRESHOLD_FLAT,HICCUPS_THRESHOLD_PERCENTAGE*lPrevTime));

				if (lTime>lMaxTime)
					lMaxTime=lTime;
			}
			else
			{
				double lAverage = 0;
				
				int lSamplesLeft = RANDOM_SAMPLES;
				
				while (lSamplesLeft>0)
				{
					double lSingleTime = trySolve(lSolver,lVO,lVS,lLB,lFileIn,lFileOut);
					lAverage += lSingleTime;
					lSamplesLeft--;
				}

				lAverage /= RANDOM_SAMPLES;
				lTime = (long) Math.floor(lAverage);
				

				if (lTime>lMaxTime)
					lMaxTime=lTime;
			}
			
			Analyser lA = new Analyser(lFileIn);
			lFW.write(pOrder + "," + pSelect + "," + pBack + "," + 
					  lA.getNrVariables() + "," + lA.getAverageDomainSize() + "," + lA.getAverageNrConstraints()+ "," + 
					  lTime+"\r\n");
			
			
		}
		
		
		lFW.close();
		
		return lMaxTime;
	}
	
	//from pStart(inclusive) until pEnd(exclusive)
	public static ArrayList<String> takeSequence(ArrayList<String> pFullArray, int pStart, int pEnd)
	{
		ArrayList<String> lAL = new ArrayList<String>();
		
		for (int lIt = pStart; lIt<pEnd; lIt++)
			lAL.add(pFullArray.get(lIt));
		
		return lAL;
	}
	
	public static void main(String[] args) throws IOException {
		while (true)
		{
			ArrayList<String> lCSPFilesIn = new ArrayList<String>();
			ArrayList<String> lCSPFilesOut = new ArrayList<String>();
			int lInstancesGenerated = 0;
			
			if (GATHERING_QUEENS)
			{
				String lBasePath = "NQueens/CSP";
				lInstancesGenerated = 100;
				Generator.massGenerateNQueensCSPs(lBasePath, lInstancesGenerated, lCSPFilesIn, lCSPFilesOut);
			}
			else
			{
				String lBasePath = "MapColouring/CSP";
				lInstancesGenerated = 300;
				Generator.massGenerateMapColouringCSPs(lBasePath, lInstancesGenerated, lCSPFilesIn, lCSPFilesOut);
			}
			
			int[][][] lStartSeq = new int[3][5][3];
			int[][][] lEndSeq = new int[3][5][3];
			
			for (int lVO = 0; lVO <= 2; lVO++)
				for (int lVS = 0; lVS <= 4; lVS++)
					for (int lLB = 0; lLB <= 2; lLB++)
					{
						lStartSeq[lVO][lVS][lLB] = 0;
						lEndSeq[lVO][lVS][lLB] = 1;
					}
			
			
			long lTimeBudget = 1000000000L;//in nanoseconds. 1 sec
			int lMaxJump = 1; //if well within time budget, how many instances should we go forward by
			
			boolean lNeedAnotherTurn = true;
			while (lNeedAnotherTurn)
			{
				lNeedAnotherTurn = false;
			
				for (int lVO = 0; lVO <= 2; lVO++)
					for (int lVS = 0; lVS <= 4; lVS++)
						for (int lLB = 0; lLB <= 2; lLB++)
							if ((lStartSeq[lVO][lVS][lLB]!=lEndSeq[lVO][lVS][lLB])&&(lEndSeq[lVO][lVS][lLB]<lInstancesGenerated))
							{
								int lNextNr = 0;
								try
								{
									long lMaxTimeTook = gather(lVO,lVS,lLB,
										takeSequence(lCSPFilesIn,lStartSeq[lVO][lVS][lLB],lEndSeq[lVO][lVS][lLB]),
										takeSequence(lCSPFilesOut,lStartSeq[lVO][lVS][lLB],lEndSeq[lVO][lVS][lLB]));
									if (lMaxTimeTook==0)
										lNextNr = lMaxJump;
									else
										lNextNr = Math.min(lMaxJump,(int) (lTimeBudget/lMaxTimeTook));
								}
								catch (UnreasonablyLongTimeException lULTE)
								{
									System.err.println("Time limit of "+lULTE.getTimeStopped()+" exceeded on "+lVO+lVS+lLB);
								}
								lStartSeq[lVO][lVS][lLB] = lEndSeq[lVO][lVS][lLB];
								lEndSeq[lVO][lVS][lLB] = lStartSeq[lVO][lVS][lLB]+lNextNr;
								System.out.println("For "+lVO+" "+lVS+" "+lLB+" next nr is "+lNextNr);
								lNeedAnotherTurn = true;
							}
			}
		}
	}

}
	
//Threaded implementation inside fors (if needed, did not want to delete it).
	/*
	final int lFVO = lVO;
	final int lFVS = lVS;
	final int lFLB = lLB;
	
	Thread lT = new Thread()
			{
				@Override
				public void run()
				{
					try {
						gather(lFVO,lFVS,lFLB,lCSPFilesIn,lCSPFilesOut);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			};
	lT.start();
	*/