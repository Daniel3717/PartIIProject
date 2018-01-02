package daa38.CSP.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import daa38.CSP.Auxiliary.Constraint;
import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.AuxTimer;
import daa38.CSP.Auxiliary.Variable;
import daa38.CSP.LookBack.Backtrack;
import daa38.CSP.LookBack.GaschnigsBackjumping;
import daa38.CSP.LookBack.LookBack;
import daa38.CSP.ValueSelection.ConsistentAssignmentValueSelection;
import daa38.CSP.ValueSelection.ValueSelection;
import daa38.CSP.VariableOrdering.LeastConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.MostConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.RandomVariableOrdering;
import daa38.CSP.VariableOrdering.VariableOrdering;

public class Solver {

	public static Stack<StepFrame> mStack;
	
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
		mStack = new Stack<StepFrame>();
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();

		CSPFileHandler.readFileProblem(pFileIn, mVariables, mConstraints);
		
		VariableOrdering lVO = new MostConstrainedVariableOrdering();
		ValueSelection lVS = new ConsistentAssignmentValueSelection();
		LookBack lLB = new Backtrack();
		//LookBack lLB = new GaschnigsBackjumping();
		
		StepFrame lFirstFrame = new StepFrame();
		lFirstFrame.mExVars = new HashMap<Variable,StepFrame>();
		lFirstFrame.mVarsToGo = (ArrayList<Variable>) mVariables.clone();
		mStack.push(lFirstFrame);
		
		
		//int step=0;
		//If the stack reaches mVariables.size()+1 frames, then we have a solution
		//If the stack reaches 0 frames, then there is no solution
		while ((mStack.size()>0)&&(mStack.size()<mVariables.size()+1))
		{
			StepFrame lNowFrame = mStack.peek();
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
					if (lNowFrame.mValsToGo.size()==0) //if we cannot give any values to the variable, it's a dead end
					{
						lLB.process(mStack);
					}
				}
				else
				if (lNowFrame.mNowValIndex < lNowFrame.mValsToGo.size())
				{
					mStack.push(buildFrame(lNowFrame));
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
		
		
		if (mStack.empty())
		{
			//System.out.println("No solution");
		}
		else
			if (mStack.size()==mVariables.size()+1)	
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
				//System.out.println("At the end, stack has "+mStack.size()+" frames");
			}
	}
	
	public static void main(String[] args) throws IOException {
		
		AuxTimer lT = new AuxTimer();
		lT.start();
		
		String lFileIn = "nQueens/12_nQueensCSP_problem.txt";
		String lFileOut = "nQueens/12_nQueensCSP_solution.txt";
		
		//String lFileIn = "1_problem.txt";
		//String lFileOut = "1_solution.txt";
		
		solve(lFileIn, lFileOut);

		lT.stop();
		lT.show();
	}

}