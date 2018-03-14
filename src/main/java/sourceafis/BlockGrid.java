// Part of SourceAFIS: https://sourceafis.machinezoo.com
package sourceafis;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor class BlockGrid {
	final CellGrid corners;
	Block get(int x, int y) {
		return Block.between(corners.get(x, y), corners.get(x + 1, y + 1));
	}
	Block get(Cell at) {
		return get(at.x, at.y);
	}
}
