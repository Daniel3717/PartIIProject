package daa38.CSP.ValueSelection;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Main.Solver;

public abstract class ValueSelection {
	
	protected Solver mSolver;
	
	protected ValueSelection(Solver pSolver)
	{
		mSolver = pSolver;
	}
	
	public abstract void select(StepFrame pSF);
}
