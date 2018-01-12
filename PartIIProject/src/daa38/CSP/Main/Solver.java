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
import daa38.CSP.ValueSelection.ForwardChecking;
import daa38.CSP.ValueSelection.ValueSelection;
import daa38.CSP.VariableOrdering.LeastConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.MostConstrainedVariableOrdering;
import daa38.CSP.VariableOrdering.RandomVariableOrdering;
import daa38.CSP.VariableOrdering.VariableOrdering;

public class Solver {

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
	
	public void solve(String pFileIn, String pFileOut) throws IOException
	{
		mSteps = new ArrayList<StepFrame>();
		mVariables = new ArrayList<Variable>();
		mConstraints = new ArrayList<Constraint>();
		mVarsAssigned = new ArrayList<Variable>();
		mVarsLeft = new ArrayList<Variable>();

		CSPFileHandler.readFileProblem(pFileIn, mVariables, mConstraints);
		
		//VariableOrdering lVO = new RandomVariableOrdering(this);
		//VariableOrdering lVO = new LeastConstrainedVariableOrdering(this);
		VariableOrdering lVO = new MostConstrainedVariableOrdering(this);
		
		//ValueSelection lVS = new ConsistentAssignmentValueSelection(this);
		ValueSelection lVS = new ForwardChecking(this);
		
		LookBack lLB = new Backtrack(this);
		//LookBack lLB = new GaschnigsBackjumping(this);
		//LookBack lLB = new GraphBasedBackjumping(this);
		
		boolean lStaticOrdering = true;
		
		if (lStaticOrdering)
		{
			mVarsLeft = (ArrayList<Variable>) mVariables.clone();
			
			while (!mVarsLeft.isEmpty())
			{
				Variable lNextVar = lVO.order(mVarsLeft);
				mSteps.add(new StepFrame(lNextVar));
				mVarsLeft.remove(lNextVar);
			}
			
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

		mVarsLeft = (ArrayList<Variable>) mVariables.clone();
		
		
		int lIndex = 0;
		//if lIndex reaches mSteps.size(), then we have a solution
		//if lIndex reaches -1, then there is no solution
		while ( (lIndex<mSteps.size()) && (lIndex > -1) )
		{
			StepFrame lNowFrame = mSteps.get(lIndex);
			
			/*
			//DEBUG:
			System.out.println();
			System.out.println("Index: "+lIndex+" (while mSteps.size()=="+mSteps.size()+")");
			lNowFrame.outputFrame();
			System.out.println();
			*/
			
			if (lNowFrame.mNowValIndex==-1)
			{
				lVS.select(lNowFrame);
				
				if (lNowFrame.mValsToGo.size()==0) 
				{
					//so this is a (leaf) dead end
					
					int lPrevIndex = lIndex;
					
					lIndex = lLB.jump(mSteps,lIndex);
					
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
				
				//We have assigned a variable, so we must update mVarsAssigned and mVarsLeft
				Variable lVarAssigned = mSteps.get(lIndex).mVar;
				mVarsAssigned.add(lVarAssigned);
				mVarsLeft.remove(lVarAssigned);
				
				
				lIndex++;
				
				if (lIndex < mSteps.size())
				{
					mSteps.get(lIndex).resetFrame();
				}
			}
			else //Finished checking all values for current variable
			{
				//so this is a (internal) dead end

				int lPrevIndex = lIndex;
				
				lIndex = lLB.jump(mSteps, lIndex);

				//We have deassigned variables, so we must update mVarsAssigned and mVarsLeft
				for (int lInt = lPrevIndex; lInt>lIndex; lInt--)
				{
					Variable lVarNow = mVarsAssigned.remove(mVarsAssigned.size()-1);
					mVarsLeft.add(lVarNow);
				}
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
