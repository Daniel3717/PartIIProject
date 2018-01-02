package daa38.CSP.VariableOrdering;

import java.util.Collections;

import daa38.CSP.Auxiliary.StepFrame;

public class RandomVariableOrdering implements VariableOrdering {

	@Override
	public void process(StepFrame pSF) {
		Collections.shuffle(pSF.mVarsToGo);
		pSF.mNowVarIndex=0;
	}

}
