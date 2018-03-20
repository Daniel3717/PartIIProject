package daa38.CSP.Main;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openjdk.jol.info.GraphLayout;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.UnreasonablyLongTimeException;
import daa38.CSP.Auxiliary.AuxTimer;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.LookBack.Backtrack;
import daa38.CSP.LookBack.GaschnigsBackjumping;
import daa38.CSP.LookBack.GraphBasedBackjumping;
import daa38.CSP.LookBack.LookBack;
import daa38.CSP.ValueSelection.ArcConsistency;
import daa38.CSP.ValueSelection.ArcConsistencyBase;
import daa38.CSP.ValueSelection.ConsistentAssignmentValueSelection;
import daa38.CSP.ValueSelection.ForwardChecking;
import daa38.CSP.ValueSelection.FullLookAhead;
import daa38.CSP.ValueSelection.PartialLookAhead;
import daa38.CSP.ValueSelection.ValueSelection;
import daa38.CSP.VariableOrdering.LeastConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.MostConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.RandomVariableOrdering;
import daa38.CSP.VariableOrdering.VariableOrdering;

public class Solver {
	
	//This is for memory analysis
	//They need to be in a group
	//When called individually, JOL will count duplicates on Objects such as Variables or Constraints
	public static class MemoryAnalysisGroup
	{
		public Solver mSolver;
		public VariableOrdering mVO;
		public ValueSelection mVS;
		public LookBack mLB;
		
		public MemoryAnalysisGroup(Solver pSolver, VariableOrdering pVO, ValueSelection pVS, LookBack pLB)
		{
			mSolver = pSolver;
			mVO = pVO;
			mVS = pVS;
			mLB = pLB;
		}
	}
	

	//I have made it public so the gatherer can modify it if he considers it is the case
	//For the purpose of the Solver, it can be assumed constant
	public static long UNREASONABLE_LONG_TIME = 1000000000L;//in nanoseconds, amounts to 1 second
	
	public ArrayList<StepFrame> mSteps;
	
	public ArrayList<Variable> mVariables;
	public ArrayList<Constraint> mConstraints;
	
	//The mVarsAssigned and mVarsLeft is a service provided by the solver
	//Thus, the solver is responsible for maintaining those
	
	//mVarsAssigned is always in the order the Variables are assigned
	public ArrayList<Variable> mVarsAssigned;
	
	//mVarsLeft will be in reverse order in which the Variables will be assigned in case of static variable ordering
	//mVarsLeft will be in no specific order in the case of dynamic variable ordering
	public ArrayList<Variable> mVarsLeft;
	
	//previous way of calling this. Since I want the default to be measuring time but Java does not support default
	//parameters, I have to do it this way
	public long solve(String pFileIn, String pFileOut, VariableOrdering pVO, ValueSelection pVS, LookBack pLB) throws IOException, UnreasonablyLongTimeException
	{
		return solve(pFileIn, pFileOut, pVO, pVS, pLB, true);
	}
	
	//returns time (in nanoseconds) or memory (in bytes) it took to solve the CSP
	//(excluding reading/writing time)
	//(also, memory is measured only via Solver's reference graph. 
	//This can be done because my current implementations of heuristics are state-free (except for the Solver member))
	//Note that tracking memory using JOL invalidates GC tracking, so if you want to gather using GC-tracking, run this in return time mode
	public long solve(String pFileIn, String pFileOut, VariableOrdering pVO, ValueSelection pVS, LookBack pLB, boolean pIsReturningTime) throws IOException, UnreasonablyLongTimeException
	{
		mSteps = new ArrayList<StepFrame>();
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();
		mVarsAssigned = new ArrayList<Variable>();
		mVarsLeft = new ArrayList<Variable>();

		CSPFileHandler.readFileProblem(pFileIn, mVariables, mConstraints);
		
		boolean lStaticOrdering = false;
			
		long lGCPrevTime = 0; //in milliseconds
		List<GarbageCollectorMXBean> lGCPrevList = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean lGC : lGCPrevList) {
        	lGCPrevTime += lGC.getCollectionTime();
        }
        
        long lMaxMemoryConsumed = 0;
        MemoryAnalysisGroup lMAG = new MemoryAnalysisGroup(this,pVO,pVS,pLB);
        
		AuxTimer lTimer = new AuxTimer();	
		lTimer.start();
		
		if (lStaticOrdering)
		{
			ArrayList<Variable> mAuxVars = (ArrayList<Variable>) mVariables.clone();
			
			while (!mAuxVars.isEmpty())
			{
				Variable lNextVar = pVO.order(mAuxVars);
				mVarsLeft.add(lNextVar);
				mAuxVars.remove(lNextVar);
			}
			Collections.reverse(mVarsLeft);
			
			/*
			//DEBUG: variable order
			System.out.println("Variable order (name,nr of values,nr of constraints):");
			for (StepFrame lSF : mSteps)
			{
				System.out.print("("+lSF.mVar.mName);
				System.out.print(","+lSF.mVar.mDomain.size());
				System.out.print(","+lSF.mVar.mConstraints.size());
				System.out.print(") ");
			}
			System.out.println();
			*/
		}
		else
		{
			mVarsLeft = (ArrayList<Variable>) mVariables.clone();
		}
		
		for (int lIt = 0; lIt<mVariables.size(); lIt++)
		{
			mSteps.add(new StepFrame());
		}
		
		int lIndex = 0;
		//if lIndex reaches mSteps.size(), then we have a solution
		//if lIndex reaches -1, then there is no solution
		while ( (lIndex<mSteps.size()) && (lIndex > -1) )
		{
			if (lTimer.getTime()>UNREASONABLE_LONG_TIME)
			{
				lTimer.stop();
				throw new UnreasonablyLongTimeException(lTimer.getTime());
			}
			
			StepFrame lNowFrame = mSteps.get(lIndex);
			//DEBUG:
			//lNowFrame.outputFrame();
			

			if (lNowFrame.mVar == null)
			{
				if (lStaticOrdering)
				{
					Variable lNextVar = mVarsLeft.remove(mVarsLeft.size()-1);
					lNowFrame.mVar = lNextVar;
					mVarsAssigned.add(lNextVar);
				}
				else
				{
					Variable lNextVar = pVO.order(mVarsLeft);
					mVarsLeft.remove(lNextVar);
					lNowFrame.mVar = lNextVar;
					mVarsAssigned.add(lNextVar);
				}
			}
			else
			if (lNowFrame.mNowValIndex==-1)
			{
				pVS.select(lNowFrame);
				
				if (lNowFrame.mValsToGo.size()==0) 
				{
					//so this is a (leaf) dead end
					
					/*
					//We compute this before LookBack, as until this point the memory used is increasing
					if (!pIsReturningTime)
					{
						//Wouldn't want to do this when measuring time since it is quite time consuming
						long lNowOccupying = GraphLayout.parseInstance(lMAG).totalSize();
						if (lNowOccupying > lMaxMemoryConsumed)
							lMaxMemoryConsumed = lNowOccupying;
						//System.out.println("Occupying "+lNowOccupying+" bytes");
					}
					*/
					
					int lPrevIndex = lIndex;
					
					lIndex = pLB.jump(mSteps,lIndex);
					
					//We have deassigned variables, so we must update mVarsAssigned and mVarsLeft
					for (int lInt = lPrevIndex; lInt>lIndex; lInt--)
					{
						Variable lVarNow = mVarsAssigned.remove(mVarsAssigned.size()-1);
						mVarsLeft.add(lVarNow);
					}
				}
			}
			else
			if (lNowFrame.mNowValIndex < lNowFrame.mValsToGo.size())
			{
				mSteps.get(lIndex).assignValue();
				
				lIndex++;
				
				if (lIndex < mSteps.size())
				{
					mSteps.get(lIndex).resetFrame();
				}
			}
			else //Finished checking all values for current variable
			{
				//so this is a (internal) dead end
				
				/*
				//We compute this before LookBack, as until this point the memory used is increasing
				if (!pIsReturningTime)
				{
					//Wouldn't want to do this when measuring time since it is quite time consuming
					long lNowOccupying = GraphLayout.parseInstance(lMAG).totalSize();
					if (lNowOccupying > lMaxMemoryConsumed)
						lMaxMemoryConsumed = lNowOccupying;
					//System.out.println("Occupying "+lNowOccupying+" bytes");
				}
				*/
				
				
				int lPrevIndex = lIndex;
				
				lIndex = pLB.jump(mSteps, lIndex);

				//We have deassigned variables, so we must update mVarsAssigned and mVarsLeft
				for (int lInt = lPrevIndex; lInt>lIndex; lInt--)
				{
					Variable lVarNow = mVarsAssigned.remove(mVarsAssigned.size()-1);
					mVarsLeft.add(lVarNow);
				}
			}
		}
		
		//We also compute this at the end
		if (!pIsReturningTime)
		{
			//Wouldn't want to do this when measuring time since it is quite time consuming
			long lNowOccupying = GraphLayout.parseInstance(lMAG).totalSize();
			if (lNowOccupying > lMaxMemoryConsumed)
				lMaxMemoryConsumed = lNowOccupying;
			//System.out.println("Occupying "+lNowOccupying+" bytes");
		}
		
		lTimer.stop();
		long lGCNowTime = 0; //in milliseconds
		List<GarbageCollectorMXBean> lGCNowList = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean lGC : lGCNowList) {
        	lGCNowTime += lGC.getCollectionTime();
        }
		
		if (lIndex == -1)
		{
			//System.out.println("No solution");
		}
		else
			if (lIndex == mVariables.size())	
			{
				//System.out.println("Found solution:");
				for (Variable lV : mVariables)
				{
					//System.out.println("V"+lV.mIndex+"="+lV.mValue);
				}
				CSPFileHandler.writeFileAssignment(pFileOut, mVariables);
			}
			else
			{
				//Should be impossible to reach
				System.out.println("Impossible area reached in Solver.java");
			}
		
        
        //System.out.println("NowTime is "+ lGCNowTime+" and PrevTime is "+lGCPrevTime+" and Timer time is "+lTimer.getTime()/1000000);
        long lActualTimeSpent = lTimer.getTime() - (lGCNowTime - lGCPrevTime)*1000000;
        
        if (pIsReturningTime)
        	return lActualTimeSpent;//in nanoseconds
        else
        	return lMaxMemoryConsumed;//in bytes
	}
	
	public static void main(String[] args) throws IOException, UnreasonablyLongTimeException {
		
		AuxTimer lT = new AuxTimer();
		lT.start();

		Solver lS = new Solver();
		
		//VariableOrdering lVO = new RandomVariableOrdering(lS);
		VariableOrdering lVO = new MostConstrainedVariableOrdering(lS);
		//VariableOrdering lVO = new LeastConstrainedVariableOrdering(lS);
		
		//ValueSelection lVS = new ConsistentAssignmentValueSelection(lS);
		ValueSelection lVS = new ForwardChecking(lS);
		//ValueSelection lVS = new ArcConsistency(lS);
		//ValueSelection lVS = new PartialLookAhead(lS);
		//ValueSelection lVS = new FullLookAhead(lS);
		
		//LookBack lLB = new Backtrack(lS);
		LookBack lLB = new GaschnigsBackjumping(lS);
		//LookBack lLB = new GraphBasedBackjumping(lS);
		
		for (int lInstance = 4; lInstance<=100; lInstance++)
		{
			String lFileIn = "nQueens/CSP"+lInstance+".in";
			String lFileOut = "nQueens/CSP"+lInstance+".out";
			
			long lTimeTook = lS.solve(lFileIn, lFileOut, lVO, lVS, lLB);
			System.out.println("Instance "+lInstance+" took "+ lTimeTook+" nanoseconds");
		}

		lT.stop();
		lT.show();
	}

}
