package daa38.CSP.Auxiliary;

public class AuxTimer {
	
	private Long mStartTime = null;
	private Long mStopTime = null;
	private Long mLapTime = null;
	
	public void start()
	{
		mStartTime = System.nanoTime();
	}
	
	public void stop()
	{
		mStopTime = System.nanoTime();
		
		mLapTime = mStopTime-mStartTime;
	}
	
	public long getTime()
	{
		if (mStopTime==null)
			return (System.nanoTime()-mStartTime);
		else
			return mLapTime;
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
		
		System.out.println("It took "+mLapTime+" nanoseconds");
	}
}
