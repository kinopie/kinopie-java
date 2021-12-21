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
	private final Path path;

	@Override
	public boolean pathMatchesAny(String... antPathPatterns) {
		if (PathUtils.anyMatch(getPath(), antPathPatterns)) {
			String antPathPatternStrings = Arrays.toString(antPathPatterns);
			logger.info("Path:'{}' matches {}.", path.normalize(), antPathPatternStrings);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isEmptyDir() throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(getPath())) {
			return !stream.iterator().hasNext();
		}
	}

	@Override
	public void editFile(Charset cs, UnaryOperator<List<String>> editor) throws IOException {
		List<String> allLines = Files.readAllLines(getPath(), cs);
		logger.debug("Read all lines from the path:'{}'.", getPath().normalize());
		allLines.stream().forEach(logger::debug);
		allLines = editor.apply(allLines);
		logger.debug("Write all lines to the path:'{}'.", getPath().normalize());
		allLines.stream().forEach(logger::debug);
		Files.write(getPath(), allLines, cs, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	@Override
	public void changeCharset(Charset from, Charset to) throws IOException {
		List<String> allLines = Files.readAllLines(getPath(), from);
		Files.write(getPath(), allLines, to, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		logger.debug("Changed charset of the path:'{}' from '{}' to '{}'.", getPath().normalize(), from, to);
	}

	@Override
	public void createNewEmptyFile(String fileName) throws IOException {
		Path newEmptyFile = getPath().resolve(fileName);
		Files.createFile(newEmptyFile);
		logger.debug("Created new empty file:'{}'.", newEmptyFile.normalize());
	}
}
