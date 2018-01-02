package daa38.CSP.VariableOrdering;

import java.util.Collections;

import daa38.CSP.Auxiliary.StepFrame;

public class LeastConstrainedVariableOrdering extends MostConstrainedVariableOrdering {

	//Basically, we do MostConstrainedVariableOrdering, then reverse the result
	@Override
	public void process(StepFrame pSF) {
		super.process(pSF);
		Collections.reverse(pSF.mVarsToGo);
	}

}
