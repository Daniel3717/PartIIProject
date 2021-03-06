package daa38.Statistics.Auxiliary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;

import daa38.CSP.Auxiliary.UnreasonablyLongTimeException;
import daa38.CSP.LookBack.LookBack;
import daa38.CSP.Main.Solver;
import daa38.CSP.ValueSelection.ValueSelection;
import daa38.CSP.VariableOrdering.VariableOrdering;
import daa38.Statistics.Analyser;

public class SingleMemoryGatherer {

	
	//Format of args:
	//args[0] - input file path. File content must be CSP format.
	//args[1] - output file path.
	//args[2] - which Variable Ordering to use.
	//			possible values are:
	//			- 1 = MostConstrainedVariableOrdering
	//			- 2 = LeastConstrainedVariableOrdering
	//			- anything else = RandomVariableOrdering
	//args[3] - which Value Selection to use
	//			possible values are:
	//			- 1 = ForwardChecking
	//			- 2 = FullLookAhead
	//			- 3 = ArcConsistency
	//			- 4 = PartialLookAhead
	//			- anything else = ConsistentAssignmentValueSelection
	//args[4] - which LookBack to use
	//			possible values are:
	//			- 1 = GaschnigsBackjumping
	//			- 2 = GraphBasedBackjumping
	//			- anything else = Backtrack
	//args[5] - optional argument. can specify the type of problem (Queens, Map) for better statistics organisation
	//any other arguments beyond this point are ignored
	public static void main(String[] args) throws IOException, UnreasonablyLongTimeException {
		SyncMemoryConsumptionTracker lSMCT = new SyncMemoryConsumptionTracker();
		installGCMonitoring(lSMCT);
		
		String lInPath = args[0];
		String lOutPath = args[1];
		int lOrder = args[2].charAt(0) - '0';
		int lSelect = args[3].charAt(0) - '0';
		int lBack = args[4].charAt(0) - '0';
		String lWhichOne = "";
		if (args.length>=6)
			lWhichOne = args[5];
		
		Solver lS = new Solver();
		VariableOrdering lVO = InputProcessing.intToVO(lOrder, lS);
		ValueSelection lVS = InputProcessing.intToVS(lSelect, lS);
		LookBack lLB = InputProcessing.intToLB(lBack, lS);
		
		//This one is using GC-tracking so we're running in time-track mode
		lS.solve(lInPath, lOutPath, lVO, lVS, lLB);
		
		lSMCT.endTracking();
		//System.out.println("A total of "+lSMCT.getTotalBytesUsed()+" bytes was consumed. A max of "+lSMCT.getMaxBytesUsed()+" bytes was reached.");

		//Add-on after JOL. Here we will also use JOL to track and add the new statistic in the file output.
		long lJOLMemory = lS.solve(lInPath, lOutPath, lVO, lVS, lLB, false); //in bytes
		
		//Note that this will trigger calls to our memory tracker from the garbage collectors
		//I have tested without this and with a serial garbage collector and the behaviour was varying as expected
		//I also wasn't receiving any bad debug information about calling the addBytes after endTracking
		//So I'm more than 99% sure that lSMCT.getTotalBytesUsed and lSMCT.getMaxBytesUsed will return the correct values
		//Uninfluenced by the additional memory consumption of the statistical analysis and output which follow below
		String lData = "Statistics/Memory/Data"+lWhichOne+lOrder+lSelect+lBack+".txt";
		FileWriter lFW = new FileWriter(new File(lData),true);
		
		Analyser lA = new Analyser(lInPath);
		lFW.write(lOrder + "," + lSelect + "," + lBack + "," + 
				  lA.getNrVariables() + "," + lA.getAverageDomainSize() + "," + lA.getAverageNrConstraints()+ "," + 
				  lSMCT.getTotalBytesUsed() + "," + lSMCT.getMaxBytesUsed() + "," + lJOLMemory + "\r\n");
		
		lFW.close();
		
	}
	
	//This method was adapted from here: http://www.fasterj.com/articles/gcnotifs.shtml
	public static void installGCMonitoring(SyncMemoryConsumptionTracker pSMCT){
		
	    //get all the GarbageCollectorMXBeans - there's one for each heap generation
	    //so probably two - the old generation and young generation
	    List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
	    
	    //Install a notification handler for each bean
	    for (GarbageCollectorMXBean gcbean : gcbeans) {
	      NotificationEmitter emitter = (NotificationEmitter) gcbean;
	      
	      //use an anonymously generated listener for this example
	      // - proper code should really use a named class
	      NotificationListener listener = new NotificationListener() {
	    	  
	        //implement the notifier callback handler
	        @Override
	        public void handleNotification(Notification notification, Object handback) {
	          //we only handle GARBAGE_COLLECTION_NOTIFICATION notifications here
	          if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
	        	  
	        	//get the information associated with this notification
	            GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
	            
	            long lBytesUsedBefore = 0;
	            for (MemoryUsage lMU : info.getGcInfo().getMemoryUsageBeforeGc().values())
	            {
	            	lBytesUsedBefore += lMU.getUsed();
	            }
	            
	            long lBytesUsedAfter = 0;
	            for (MemoryUsage lMU : info.getGcInfo().getMemoryUsageAfterGc().values())
	            {
	            	lBytesUsedAfter += lMU.getUsed();
	            }
	            
	            //System.out.println("Custom: "+lBytesUsedBefore+" bytes used before; "+lBytesUsedAfter+" bytes used after");
	            long lBytesUsed = lBytesUsedBefore-lBytesUsedAfter;
	            pSMCT.addBytes(lBytesUsed);
	            pSMCT.maxBytes(lBytesUsedBefore);
	            
	          }
	        }
	      };

	      //Add the listener
	      emitter.addNotificationListener(listener, null, null);
	    }
	  }
}
