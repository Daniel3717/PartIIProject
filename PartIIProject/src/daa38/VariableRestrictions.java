package daa38;

import java.util.ArrayList;
import java.util.Collection;

public class VariableRestrictions {
	Variable mVariable;
	Collection<Integer> mImpossibleValues;
	
	public VariableRestrictions(Variable pVariable)
	{
		mVariable = pVariable;
		mImpossibleValues = new ArrayList<Integer>();
	}
}