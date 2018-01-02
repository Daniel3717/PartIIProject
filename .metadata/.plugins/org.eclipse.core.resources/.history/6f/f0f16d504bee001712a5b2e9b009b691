package daa38.CSP.LookBack;

import java.util.Stack;

import daa38.CSP.Auxiliary.StepFrame;
import daa38.CSP.Auxiliary.Variable;

public class GaschnigsBackjumping extends Backtrack {

	@Override
	public void process(Stack<StepFrame> pStack) {
		
		//System.out.print("Here");
		
		StepFrame lTopFrame = pStack.peek();
		if (lTopFrame.mNowVarIndex<lTopFrame.mVarsToGo.size())
		{
			Variable lVarEnd = lTopFrame.mVarsToGo.get(lTopFrame.mNowVarIndex);
			if (lVarEnd.mDomain.size()==0) //so, if it is a leaf dead-end
			{
				pStack.pop();
				do
				{
					lTopFrame = pStack.pop();
					
					lTopFrame.mVarsToGo.get(lTopFrame.mNowVarIndex).mValue = -1;
					lTopFrame.mRes.get(lTopFrame.mNowValIndex).liftRestrictions();
				}
				while (lTopFrame.mRes.get(lTopFrame.mNowValIndex).getVarRestrictions(lVarEnd)==null);
				
				lTopFrame.mNowValIndex++;
				pStack.push(lTopFrame);
				
				return;
			}
			//it should be impossible to get here
			System.out.println("IMPOSSIBLE AREA REACHED!");
		}
		
		//if it is an internal dead-end, we just backtrack
		super.process(pStack);
	}

}
