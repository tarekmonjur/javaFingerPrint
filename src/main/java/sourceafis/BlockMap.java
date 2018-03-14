// Part of SourceAFIS: https://sourceafis.machinezoo.com
package sourceafis;

class BlockMap {
	final Cell pixelCount;
	final Cell blockCount;
	final Cell cornerCount;
	final CellGrid corners;
	final BlockGrid blockAreas;
	final CellGrid blockCenters;
	final BlockGrid cornerAreas;
	BlockMap(int width, int height, int maxBlockSize) {
		pixelCount = new Cell(width, height);
		blockCount = new Cell(
			Integers.roundUpDiv(pixelCount.x, maxBlockSize),
			Integers.roundUpDiv(pixelCount.y, maxBlockSize));
		cornerCount = new Cell(blockCount.x + 1, blockCount.y + 1);
		corners = initCorners();
		blockAreas = new BlockGrid(corners);
		blockCenters = initBlockCenters();
		cornerAreas = initCornerAreas();
	}
	private CellGrid initCorners() {
		CellGrid grid = new CellGrid(cornerCount);
		for (int y = 0; y < cornerCount.y; ++y)
			grid.allY[y] = y * pixelCount.y / blockCount.y;
		for (int x = 0; x < cornerCount.x; ++x)
			grid.allX[x] = x * pixelCount.x / blockCount.x;
		return grid;
	}
	private CellGrid initBlockCenters() {
		CellGrid grid = new CellGrid(blockCount);
		for (int y = 0; y < blockCount.y; ++y)
			grid.allY[y] = blockAreas.get(0, y).center().y;
		for (int x = 0; x < blockCount.x; ++x)
			grid.allX[x] = blockAreas.get(x, 0).center().x;
		return grid;
	}
	private BlockGrid initCornerAreas() {
		CellGrid grid = new CellGrid(cornerCount.x + 1, cornerCount.y + 1);
		grid.allY[0] = 0;
		for (int y = 0; y < blockCount.y; ++y)
			grid.allY[y + 1] = blockCenters.get(0, y).y;
		grid.allY[blockCount.y + 1] = pixelCount.y;
		grid.allX[0] = 0;
		for (int x = 0; x < blockCount.x; ++x)
			grid.allX[x + 1] = blockCenters.get(x, 0).x;
		grid.allX[blockCount.x + 1] = pixelCount.x;
		return new BlockGrid(grid);
	}
}
