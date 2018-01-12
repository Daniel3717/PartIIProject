package daa38.CSP.LookBack;

import java.util.ArrayList;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Main.Solver;

public abstract class LookBack {
	
	protected Solver mSolver;
	
	public LookBack(Solver pSolver)
	{
		mSolver=pSolver;
	}
	
	//It returns the index to which it jumps
	//It also modifies pSteps
	//Even though pSteps could be accessed through mSolver.mSteps, I am passing it explicitly
	//to draw attention to the fact that it does get modified.
	public abstract int jump(ArrayList<StepFrame> pSteps, int pIndex);
}
