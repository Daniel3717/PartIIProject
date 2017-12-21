package daa38.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import daa38.Auxiliary.Constraint;
import daa38.Auxiliary.FileHandler;
import daa38.Auxiliary.StepFrame;
import daa38.Auxiliary.Variable;
import daa38.LookBack.Backtrack;
import daa38.LookBack.LookBack;
import daa38.ValueSelection.ConsistentAssignmentValueSelection;
import daa38.ValueSelection.ValueSelection;
import daa38.VariableOrdering.RandomVariableOrdering;
import daa38.VariableOrdering.VariableOrdering;

public class Solver {

	public static ArrayList<StepFrame> mStack;
	
	public static ArrayList<Variable> mVariables;
	public static ArrayList<Constraint> mConstraints;
	
	// Assumes:
	// 0 <= lNowFrame.mNowVarIndex < lNowFrame.mVarsToGo.size()
	// 0 <= lNowFrame.mNowValIndex < lNowFrame.mValsToGo.size()
	// Correctness of StepFrame construction given as argument
	public static StepFrame buildFrame(StepFrame pSF)
	{
		if (!pSF.mRes.get(pSF.mNowValIndex).enforceRestrictions())
			System.out.println("An enforce restrictions returned false!");
		
		StepFrame lSF = new StepFrame();
		lSF.mExVars = (HashMap<Variable,StepFrame>) pSF.mExVars.clone();
		lSF.mExVars.put(pSF.mVarsToGo.get(pSF.mNowVarIndex), pSF);
		lSF.mVarsToGo = (ArrayList<Variable>) pSF.mVarsToGo.clone();
		lSF.mVarsToGo.remove(pSF.mVarsToGo.get(pSF.mNowVarIndex));
		pSF.mVarsToGo.get(pSF.mNowVarIndex).mValue = pSF.mValsToGo.get(pSF.mNowValIndex);
		
		return lSF;
	}
	
	public static void solve(String pFileIn, String pFileOut) throws IOException
	{
		mStack = new ArrayList<StepFrame>();
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();

		FileHandler.readFileProblem(pFileIn, mVariables, mConstraints);
		
		VariableOrdering lVO = new RandomVariableOrdering();
		ValueSelection lVS = new ConsistentAssignmentValueSelection();
		LookBack lLB = new Backtrack();
		
		StepFrame lFirstFrame = new StepFrame();
		lFirstFrame.mExVars = new HashMap<Variable,StepFrame>();
		lFirstFrame.mVarsToGo = (ArrayList<Variable>) mVariables.clone();
		mStack.add(lFirstFrame);
		
		
		//int step=0;
		//If the stack reaches mVariables.size()+1 frames, then we have a solution
		//If the stack reaches 0 frames, then there is no solution
		while ((mStack.size()>0)&&(mStack.size()<mVariables.size()+1))
		{
			StepFrame lNowFrame = mStack.get(mStack.size()-1);
			//step++;
			//System.out.println(step);
			//lNowFrame.outputFrame();
			
			if (lNowFrame.mNowVarIndex==-1)
			{
				lVO.process(lNowFrame);
			}
			else
			if (lNowFrame.mNowVarIndex < lNowFrame.mVarsToGo.size())
			{
				if (lNowFrame.mNowValIndex==-1)
				{
					lVS.process(lNowFrame);
				}
				else
				if (lNowFrame.mNowValIndex < lNowFrame.mValsToGo.size())
				{
					mStack.add(buildFrame(lNowFrame));
				}
				else //Finished checking all values for current variable
				{
					lNowFrame.mNowVarIndex++;
					lNowFrame.mNowValIndex = -1;
					lNowFrame.mValsToGo.clear();
					lNowFrame.mRes.clear();
				}
			}
			else //Finished checking all variables
			{
				lLB.process(mStack);
			}
		}
		
		if (mStack.size()==0)
		{
			System.out.println("No solution");
		}
		else
			if (mStack.size()==mVariables.size()+1)	
			{
				System.out.println("Found solution:");
				for (Variable lV : mVariables)
				{
					System.out.println("V"+lV.mIndex+"="+lV.mValue);
				}
				FileHandler.writeFileAssignment(pFileOut, mVariables);
			}
			else
			{
				System.out.println("At the end, stack has "+mStack.size()+" frames");
			}
	}
	
	public static void main(String[] args) throws IOException {
		String lFileIn = "nQueensCSP_problem.txt";
		String lFileOut = "nQueensCSP_solution.txt";
		
		solve(lFileIn, lFileOut);
	}

}