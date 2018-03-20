package daa38.JolTesting;

import java.util.ArrayList;
import java.util.HashMap;

import org.openjdk.jol.info.GraphLayout;

public class JolTestCollectionsMultipleRef {

	public static class ExtraIntegerLayer
	{
		private Integer mInt;
		
		public ExtraIntegerLayer(Integer pInt)
		{
			mInt = pInt;
		}
		
	}
	
	public static class Base
	{
		public static Integer mIndex = 122;
		public static String mString = "123";
		
		private ArrayList<ExtraIntegerLayer> mAL;
		public Base()
		{
			mAL = new ArrayList<ExtraIntegerLayer>();
			for (int i=0; i<5; i++)
				mAL.add(new ExtraIntegerLayer(mIndex++));
		}
	}
	
	public static class Ext1
	{
		private ArrayList<Base> mBases;
		private Integer mInt;
		
		public Ext1()
		{
			mBases = new ArrayList<Base>();
			mInt = -1;
		}
		
		public void addBase(Base pBase)
		{
			mBases.add(pBase);
		}
	}
	
	public static class Ext2
	{
		private HashMap<Integer, Base> mBases;
		private Integer mInt;
		private Integer mCount;
		
		public Ext2()
		{
			mBases = new HashMap<Integer, Base>();
			mInt = -2;
			mCount = 0;
		}
		
		public void addBase(Base pBase)
		{
			mBases.put(mCount++,pBase);
		}
	}
	
	public static class Ext12
	{
		private Ext1 mExt1;
		private Ext2 mExt2;
		private Integer mInt;
		
		public Ext12(Ext1 pExt1, Ext2 pExt2)
		{
			mExt1 = pExt1;
			mExt2 = pExt2;
			mInt = -12;
		}
	}
	
	public static void main(String[] args) {
		
		Ext1 lExt1 = new Ext1();
		Ext2 lExt2 = new Ext2();
		
		Ext12 lExt12 = new Ext12(lExt1, lExt2);
		
		for (int i=0; i<5; i++)
		{
			Base lBase = new Base();
			lExt1.addBase(lBase);
			lExt2.addBase(lBase);
		}
		
		System.out.println(GraphLayout.parseInstance(lExt12).toPrintable());
		System.out.println(GraphLayout.parseInstance(lExt12).totalSize());
		
	}
}
