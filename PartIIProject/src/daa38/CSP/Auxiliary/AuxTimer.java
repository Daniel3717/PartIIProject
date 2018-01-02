package daa38.CSP.Auxiliary;

public class AuxTimer {
	
	private Long mStartTime = null;
	private Long mStopTime = null;
	
	public void start()
	{
		mStartTime = System.currentTimeMillis();
	}
	
	public void stop()
	{
		mStopTime = System.currentTimeMillis();
	}
	
	public void show()
	{
		if (mStartTime==null)
		{
			System.out.println("Timer not started");
			return;
		}
		
		if (mStopTime==null)
		{
			System.out.println("Timer not stopped");
			return;
		}
		
		if (mStartTime>mStopTime)
		{
			System.out.println("Timer stopped before started..., but you still get the time");
		}
		
		System.out.println("It took "+((mStopTime-mStartTime))+" milliseconds");
	}
}