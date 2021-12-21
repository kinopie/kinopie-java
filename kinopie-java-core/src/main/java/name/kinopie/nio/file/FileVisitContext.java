package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.function.UnaryOperator;

public interface FileVisitContext {

	Path getPath();

	boolean pathMatchesAny(String... antPathPatterns);

	FileVisitContext editFile(Charset cs, UnaryOperator<List<String>> editor) throws IOException;

	FileVisitContext changeCharset(Charset from, Charset to) throws IOException;
}
