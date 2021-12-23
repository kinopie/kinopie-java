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

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractFileVisitContext implements FileVisitContext {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@NonNull
	private final Path start;

	@NonNull
	private final Path current;

	@Override
	public boolean pathMatchesAny(String... antPathPatterns) {
		if (PathUtils.anyMatch(getCurrent(), antPathPatterns)) {
			String antPathPatternStrings = Arrays.toString(antPathPatterns);
			logger.info("Path:'{}' matches {}.", getCurrent().normalize(), antPathPatternStrings);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onStart() {
		return start.equals(current);
	}

	@Override
	public boolean isEmptyDir() throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(getCurrent())) {
			return !stream.iterator().hasNext();
		}
	}

	@Override
	public void editFile(Charset cs, UnaryOperator<List<String>> editor) throws IOException {
		List<String> allLines = Files.readAllLines(getCurrent(), cs);
		logger.debug("Read all lines from the path:'{}'.", getCurrent().normalize());
		allLines.stream().forEach(logger::debug);
		allLines = editor.apply(allLines);
		logger.debug("Write all lines to the path:'{}'.", getCurrent().normalize());
		allLines.stream().forEach(logger::debug);
		Files.write(getCurrent(), allLines, cs, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	@Override
	public void changeCharset(Charset from, Charset to) throws IOException {
		List<String> allLines = Files.readAllLines(getCurrent(), from);
		Files.write(getCurrent(), allLines, to, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		logger.debug("Changed charset of the path:'{}' from '{}' to '{}'.", getCurrent().normalize(), from, to);
	}

	@Override
	public void createNewEmptyFile(String fileName) throws IOException {
		Path newEmptyFile = getCurrent().resolve(fileName);
		Files.createFile(newEmptyFile);
		logger.debug("Created new empty file:'{}'.", newEmptyFile.normalize());
	}
}
