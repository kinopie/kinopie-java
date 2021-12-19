package name.kinopie.nio.file;

import java.nio.file.Path;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractFileVisitContext implements FileVisitContext {

	@NonNull
	private final Path path;

	@Override
	public boolean pathMatchesAny(String... antPathPatterns) {
		return PathUtils.anyMatch(path, antPathPatterns);
	}
}
