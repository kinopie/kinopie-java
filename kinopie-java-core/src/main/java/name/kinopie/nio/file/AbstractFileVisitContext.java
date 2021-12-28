package name.kinopie.nio.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PathMatcher;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@ToString(exclude = "logger")
public abstract class AbstractFileVisitContext implements FileVisitContext {

	private static final String ESCAPED_FILE_SEPARATOR = Pattern.quote(File.separator);

	private Logger logger = LoggerFactory.getLogger(getClass());

	@NonNull
	private PathMatcher pathMatcher;

	@NonNull
	private final Path startingPath;

	@NonNull
	private final Path currentPath;

	@Override
	public boolean currentPathMatches(String... antPathPatterns) {
		final String normalizedPath = getNormalizedCurrentPathString().replaceAll(ESCAPED_FILE_SEPARATOR, "/");
		boolean anyMatch = Arrays.stream(antPathPatterns)
				.anyMatch(antPathPattern -> pathMatcher.match(antPathPattern, normalizedPath));
		if (anyMatch) {
			String antPathPatternStrings = Arrays.toString(antPathPatterns);
			logger.info("Path:'{}' matches {}.", getNormalizedCurrentPath(), antPathPatternStrings);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onStartingPath() {
		return startingPath.equals(currentPath);
	}

	@Override
	public boolean isEmptyDir() throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(getCurrentPath())) {
			return !stream.iterator().hasNext();
		}
	}

	@Override
	public void editFile(Charset cs, UnaryOperator<List<String>> editor) throws IOException {
		List<String> allLines = Files.readAllLines(getCurrentPath(), cs);
		logger.debug("Read all lines from the path:'{}'.", getNormalizedCurrentPath());
		allLines.stream().forEach(logger::debug);
		allLines = editor.apply(allLines);
		logger.debug("Write all lines to the path:'{}'.", getNormalizedCurrentPath());
		allLines.stream().forEach(logger::debug);
		Files.write(getCurrentPath(), allLines, cs, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	@Override
	public void changeCharset(Charset from, Charset to) throws IOException {
		List<String> allLines = Files.readAllLines(getCurrentPath(), from);
		Files.write(getCurrentPath(), allLines, to, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		logger.debug("Changed charset of the path:'{}' from '{}' to '{}'.", getNormalizedCurrentPath(), from, to);
	}

	@Override
	public void createNewEmptyFile(String fileName) throws IOException {
		Path newEmptyFile = getCurrentPath().resolve(fileName);
		Files.createFile(newEmptyFile);
		logger.debug("Created new empty file:'{}'.", newEmptyFile.normalize());
	}

	private String getNormalizedCurrentPathString() {
		return getNormalizedCurrentPath().toString();
	}

	private Path getNormalizedCurrentPath() {
		return getCurrentPath().normalize();
	}
}
