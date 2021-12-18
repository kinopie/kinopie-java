package name.kinopie.nio.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

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
		final String normalizedPath = path.normalize().toString().replaceAll("\\" + File.separator, "/");
		return Arrays.stream(antPathPatterns)
				.anyMatch(antPathPattern -> pathMatcher.match(antPathPattern, normalizedPath));
	}

	public List<String> readAllLines() throws IOException {
		return readAllLines(StandardCharsets.UTF_8);
	}

	public List<String> readAllLines(Charset cs) throws IOException {
		return Files.readAllLines(path, cs);
	}
}
