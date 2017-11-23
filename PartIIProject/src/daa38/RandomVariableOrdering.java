package daa38;

import java.util.Collections;

public class RandomVariableOrdering implements VariableOrdering {

	@Override
	public void process(StepFrame pSF) {
		Collections.shuffle(pSF.mVarsToGo);
		pSF.mNowVarIndex=0;
	}

}