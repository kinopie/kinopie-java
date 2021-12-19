package name.kinopie.nio.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractFileVisitContext {
	private static PathMatcher pathMatcher = new AntPathMatcher();
	@NonNull
	private final FileTreeWalkContext fileTreeWalkContext;
	@NonNull
	private final Path path;

	public boolean pathMatches(String... antPathPatterns) {
		final String normalizedPath = path.normalize().toString().replaceAll(Pattern.quote(File.separator), "/");
		return Arrays.stream(antPathPatterns)
				.anyMatch(antPathPattern -> pathMatcher.match(antPathPattern, normalizedPath));
	}

	public List<String> readAllLines() throws IOException {
		return Files.readAllLines(path);
	}

	public List<String> readAllLines(Charset cs) throws IOException {
		return Files.readAllLines(path, cs);
	}
}
