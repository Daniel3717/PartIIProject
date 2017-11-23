package daa38;

import java.util.ArrayList;

public class Backtrack implements LookBack {

	@Override
	public void process(ArrayList<StepFrame> pStack) {
		StepFrame lastFrame = pStack.remove(pStack.size()-1);
		
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