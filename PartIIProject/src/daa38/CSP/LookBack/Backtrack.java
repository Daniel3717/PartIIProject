package daa38.CSP.LookBack;

import java.util.Stack;

import daa38.CSP.Auxiliary.StepFrame;

public class Backtrack implements LookBack {

	@Override
	public void process(Stack<StepFrame> pStack) {
		pStack.pop();
		
		if (pStack.empty()) //Failed to find a solution
			return;
		
		StepFrame currentFrame = pStack.peek();
		
		//System.out.println("Backtrack to:");
		//currentFrame.outputFrame();
		
		
		currentFrame.mRes.get(currentFrame.mNowValIndex).liftRestrictions();
		currentFrame.mVarsToGo.get(currentFrame.mNowVarIndex).mValue = -1;
		currentFrame.mNowValIndex++;
	}

}
