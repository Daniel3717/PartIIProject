package daa38.CSP.Main;

import java.io.IOException;
import java.util.ArrayList;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.AuxTimer;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.LookBack.Backtrack;
import daa38.CSP.LookBack.GaschnigsBackjumping;
import daa38.CSP.LookBack.GraphBasedBackjumping;
import daa38.CSP.LookBack.LookBack;
import daa38.CSP.ValueSelection.ConsistentAssignmentValueSelection;
import daa38.CSP.ValueSelection.ValueSelection;
import daa38.CSP.VariableOrdering.LeastConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.MostConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.RandomVariableOrdering;
import daa38.CSP.VariableOrdering.VariableOrdering;

public class Solver {

	public ArrayList<StepFrame> mSteps;
	
	public ArrayList<Variable> mVariables;
	public ArrayList<Constraint> mConstraints;

	public Solver()
	{
		mSteps = new ArrayList<StepFrame>();
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();
	}
	
	public void solve(String pFileIn, String pFileOut) throws IOException
	{
		mSteps = new ArrayList<StepFrame>();
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();

		CSPFileHandler.readFileProblem(pFileIn, mVariables, mConstraints);
		
		VariableOrdering lVO = new RandomVariableOrdering();
		//VariableOrdering lVO = new LeastConstrainedVariableOrdering();
		//VariableOrdering lVO = new MostConstrainedVariableOrdering();
		
		ValueSelection lVS = new ConsistentAssignmentValueSelection();
		
		LookBack lLB = new Backtrack();
		//LookBack lLB = new GaschnigsBackjumping();
		//LookBack lLB = new GraphBasedBackjumping();
		
		lVO.order(mVariables);
		for (Variable lV : mVariables)
		{
			mSteps.add(new StepFrame(lV));
		}
		
		
		int lIndex = 0;
		//if lIndex reaches mSteps.size(), then we have a solution
		//if lIndex reaches -1, then there is no solution
		while ( (lIndex<mSteps.size()) && (lIndex > -1) )
		{
			StepFrame lNowFrame = mSteps.get(lIndex);
			//DEBUG:
			//System.out.println();
			//System.out.println("Index: "+lIndex+" (while mSteps.size()=="+mSteps.size()+")");
			//lNowFrame.outputFrame();
			//System.out.println();
			
			if (lNowFrame.mNowValIndex==-1)
			{
				lVS.select(lNowFrame);
				
				if (lNowFrame.mValsToGo.size()==0) 
				{
					//so this is a (leaf) dead end
					lIndex = lLB.jump(mSteps, lIndex);
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
				lIndex = lLB.jump(mSteps, lIndex);
			}
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
	}
	
	public static void main(String[] args) throws IOException {
		
		AuxTimer lT = new AuxTimer();
		lT.start();
		
		String lFileIn = "nQueens/8_nQueensCSP_problem.txt";
		String lFileOut = "nQueens/8_nQueensCSP_solution.txt";
		
		//String lFileIn = "1_problem.txt";
		//String lFileOut = "1_solution.txt";
		
		Solver lS = new Solver();
		lS.solve(lFileIn, lFileOut);

		lT.stop();
		lT.show();
	}

}
