package daa38.GCTesting;

import java.util.ArrayList;

import daa38.Statistics.Auxiliary.SingleMemoryGatherer;
import daa38.Statistics.Auxiliary.SyncMemoryConsumptionTracker;

public class SystemGCTest {

	public static void main(String[] args) {
		SyncMemoryConsumptionTracker lSMCT = new SyncMemoryConsumptionTracker();
		SingleMemoryGatherer.installGCMonitoring(lSMCT);
		
		int lGCs = 10;
		for (int lTry = 1; lTry<lGCs; lTry++)
		{
			System.out.println(lTry+" max memory initially "+Runtime.getRuntime().maxMemory()+", free memory initially "+Runtime.getRuntime().freeMemory());
		
			ArrayList<Object> lAL = new ArrayList<Object>();
			for (int lObjects = 1; lObjects < 1000000*lTry; lObjects++)
			{
				lAL.add(new Object());
			}
			lAL.clear();
			System.out.println(lTry+" max memory before gc "+Runtime.getRuntime().maxMemory()+", free memory before gc "+Runtime.getRuntime().freeMemory());
			//System.gc();
			System.out.println(lTry+" max memory after  gc "+Runtime.getRuntime().maxMemory()+", free memory after  gc "+Runtime.getRuntime().freeMemory());
			System.out.println();
		}
		
		//So... you could force gc if you filled the memory up with useless stuff
		//Also, this is due to the way you implemented things, but if you do the
		//force garbage collection thing at the start of each iteration in solver
		//then you may get (somewhat?) viable results
	}

}
