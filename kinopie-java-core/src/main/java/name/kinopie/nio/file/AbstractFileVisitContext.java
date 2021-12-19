package name.kinopie.nio.file;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractFileVisitContext implements FileVisitContext {

	private static PathMatcher pathMatcher = new AntPathMatcher();

	@NonNull
	private final Path path;

	@Override
	public boolean pathMatches(String... antPathPatterns) {
		final String normalizedPath = path.normalize().toString().replaceAll(Pattern.quote(File.separator), "/");
		return Arrays.stream(antPathPatterns)
				.anyMatch(antPathPattern -> pathMatcher.match(antPathPattern, normalizedPath));
	}
}
