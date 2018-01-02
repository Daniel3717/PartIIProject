package daa38.MapColouring.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class MapColouringFileHandler {
	
	private static int countDigits(int pInt)
	{
		if (pInt==0)
			return 1;
		
		int lNrDigits = 0;
		while (pInt>0)
		{
			lNrDigits++;
			pInt/=10;
		}
		
		return lNrDigits;
	}
	
	private static String spaceFormat(int pInt, int pNrDigits)
	{
		int lNrDigits = countDigits(pInt);
		
		String lOut = "";
		while (lNrDigits<pNrDigits)
		{
			lOut+=" ";
			lNrDigits++;
		}
		lOut+=pInt;
		
		return lOut;
	}
	
	//The map must be a matrix (so all lines have the same number of columns)
	//and must have at least one element. All elements must be positive.
	public static void writeMap(String pPath, int[][] pMap) throws IOException	
	{
		//It is not mandatory, but I will make it so that in the out file
		//all elements occupy the same character space.
		//This is for better visualisation
		
		Writer lMapOut = new FileWriter(new File(pPath));
	
		int lRows = pMap.length;
		int lCols = pMap[0].length;
		
		lMapOut.write(lRows+" "+lCols+"\r\n");
		
		int lMax = 0;
		for (int lRow = 0; lRow < lRows; lRow++)
		{
			for (int lCol = 0; lCol < lCols; lCol++)
			{
				if (pMap[lRow][lCol]>lMax)
					lMax=pMap[lRow][lCol];
			}
		}
		
		int lNrDigits = countDigits(lMax);
		
		for (int lRow = 0; lRow < lRows; lRow++)
		{
			for (int lCol = 0; lCol < lCols; lCol++)
			{
				lMapOut.write(spaceFormat(pMap[lRow][lCol],lNrDigits)+" ");
			}
			lMapOut.write("\r\n");
		}
		
		lMapOut.close();
	}
	
	public static int[][] readMap(String pPath) throws FileNotFoundException
	{
		Scanner lMapIn = new Scanner(new File(pPath));
		
		int lRows = lMapIn.nextInt();
		int lCols = lMapIn.nextInt();
		
		int[][] lMap = new int[lRows][lCols];
		
		for (int lRow = 0; lRow < lRows; lRow++)
		{
			for (int lCol = 0; lCol < lCols; lCol++)
			{
				lMap[lRow][lCol] = lMapIn.nextInt();
			}
		}
		
		lMapIn.close();
		
		return lMap;
	}
	
}
