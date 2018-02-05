package daa38.CSP.ValueSelection;

import daa38.CSP.Main.Solver;

public class ArcConsistency extends ArcConsistencyBase {

	public ArcConsistency(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	protected int secondVariableStart(int pFirstVariableStart) {
		return 0;
	}

	@Override
	protected boolean repeatWhile(boolean pRestrictedSomething) {
		return pRestrictedSomething;
	}

}
