// Part of SourceAFIS: https://sourceafis.machinezoo.com
package sourceafis;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor enum SkeletonType {
	RIDGES("ridges-"), VALLEYS("valleys-");
	final String prefix;
}
