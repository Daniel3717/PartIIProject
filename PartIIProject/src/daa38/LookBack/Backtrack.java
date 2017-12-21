package daa38.LookBack;

import java.util.ArrayList;

import daa38.Auxiliary.StepFrame;

public class Backtrack implements LookBack {

	@Override
	public void process(ArrayList<StepFrame> pStack) {
		pStack.remove(pStack.size()-1);
		
		if (pStack.size()==0) //Failed to find a solution
			return;
		
		StepFrame currentFrame = pStack.get(pStack.size()-1);
		
		//System.out.println("Backtrack to:");
		//currentFrame.outputFrame();
		
		
		currentFrame.mRes.get(currentFrame.mNowValIndex).liftRestrictions();
		currentFrame.mVarsToGo.get(currentFrame.mNowVarIndex).mValue = -1;
		currentFrame.mNowValIndex++;
	}

}
