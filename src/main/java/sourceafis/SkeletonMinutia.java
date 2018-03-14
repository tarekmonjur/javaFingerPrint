// Part of SourceAFIS: https://sourceafis.machinezoo.com
package sourceafis;

import java.util.ArrayList;
import java.util.List;

class SkeletonMinutia {
	final Cell position;
	final List<SkeletonRidge> ridges = new ArrayList<>();
	SkeletonMinutia(Cell position) {
		this.position = position;
	}
	void attachStart(SkeletonRidge ridge) {
		if (!ridges.contains(ridge)) {
			ridges.add(ridge);
			ridge.start(this);
		}
	}
	void detachStart(SkeletonRidge ridge) {
		if (ridges.contains(ridge)) {
			ridges.remove(ridge);
			if (ridge.start() == this)
				ridge.start(null);
		}
	}
	@Override public String toString() {
		return String.format("%s*%d", position.toString(), ridges.size());
	}
}
