package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.function.UnaryOperator;

public interface FileVisitContext {

	Path getPath();

	boolean pathMatchesAny(String... antPathPatterns);

	boolean isEmptyDir() throws IOException;
	
	void editFile(Charset cs, UnaryOperator<List<String>> editor) throws IOException;

	void changeCharset(Charset from, Charset to) throws IOException;

	void createNewEmptyFile(String fileName) throws IOException;
}
