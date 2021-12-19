package name.kinopie.nio.file;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtils {

	private static PathMatcher pathMatcher = new AntPathMatcher();

	public static boolean anyMatch(Path path, String... antPathPatterns) {
		final String normalizedPath = path.normalize().toString().replaceAll(Pattern.quote(File.separator), "/");
		return Arrays.stream(antPathPatterns)
				.anyMatch(antPathPattern -> pathMatcher.match(antPathPattern, normalizedPath));
	}
}
