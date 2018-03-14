// Part of SourceAFIS: https://sourceafis.machinezoo.com
package sourceafis;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor enum MinutiaType {
	ENDING("ending"), BIFURCATION("bifurcation");
	final String json;
}
