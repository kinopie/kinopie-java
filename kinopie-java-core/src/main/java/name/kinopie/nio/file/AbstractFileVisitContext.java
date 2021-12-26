package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private Logger logger = LoggerFactory.getLogger(getClass());

	@NonNull
	private final Path startingPath;

	@NonNull
	private final Path currentPath;

	@Override
	public boolean currentPathMatches(String... antPathPatterns) {
		if (PathUtils.anyMatch(getCurrentPath(), antPathPatterns)) {
			String antPathPatternStrings = Arrays.toString(antPathPatterns);
			logger.info("Path:'{}' matches {}.", getCurrentPath().normalize(), antPathPatternStrings);
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
		logger.debug("Read all lines from the path:'{}'.", getCurrentPath().normalize());
		allLines.stream().forEach(logger::debug);
		allLines = editor.apply(allLines);
		logger.debug("Write all lines to the path:'{}'.", getCurrentPath().normalize());
		allLines.stream().forEach(logger::debug);
		Files.write(getCurrentPath(), allLines, cs, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	@Override
	public void changeCharset(Charset from, Charset to) throws IOException {
		List<String> allLines = Files.readAllLines(getCurrentPath(), from);
		Files.write(getCurrentPath(), allLines, to, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		logger.debug("Changed charset of the path:'{}' from '{}' to '{}'.", getCurrentPath().normalize(), from, to);
	}

	@Override
	public void createNewEmptyFile(String fileName) throws IOException {
		Path newEmptyFile = getCurrentPath().resolve(fileName);
		Files.createFile(newEmptyFile);
		logger.debug("Created new empty file:'{}'.", newEmptyFile.normalize());
	}
}
