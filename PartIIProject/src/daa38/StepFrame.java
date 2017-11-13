package daa38;

import java.util.ArrayList;
import java.util.HashMap;

public class StepFrame {
	HashMap<Variable, StepFrame> mAssVars; //Variables assigned before this frame
	ArrayList<Variable> mVarsToAss; //Variables to go through during this frame
	int mCurVar; //Current variable
	ArrayList<Integer> mValsToAss; //Values that can be used for current variable
	ArrayList<VariableRestrictions> mRes; //Restrictions imposed on other variables due to value at same index
	int mCurVal; //Current value
}
