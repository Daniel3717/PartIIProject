package daa38.MapColoring;

import java.io.IOException;

import daa38.Auxiliary.FileHandler;

public class MapColoring {

	public static int[][] generateMap(int pNrZones, int pSize)
	{
		return ForestMapGenerator.generateMap(pNrZones, pSize);
	}
	
	public static void main(String[] args) throws IOException {

		int[][] lMap = generateMap(99,50);

		FileHandler.writeMap("MapMap.txt", lMap);
		
		int[][] lMapRead = FileHandler.readMap("MapMap.txt");
		
		for (int lRow = 0; lRow<lMapRead.length; lRow++)
		{
			for (int lCol = 0; lCol<lMapRead[lRow].length; lCol++)
			{
				System.out.print(lMapRead[lRow][lCol]+" ");
			}
			System.out.println();
		}
	}
}