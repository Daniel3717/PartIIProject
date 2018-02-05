package daa38.CSP.ValueSelection;

import daa38.CSP.Main.Solver;

public abstract class LookAhead extends ArcConsistencyBase {

	protected LookAhead(Solver pSolver)
	{
		super(pSolver);
	}
	
	@Override
	protected abstract int secondVariableStart(int pFirstVariableStart);

	@Override
	protected boolean repeatWhile(boolean pRestrictedSomething) {
		return false;
	}

}
