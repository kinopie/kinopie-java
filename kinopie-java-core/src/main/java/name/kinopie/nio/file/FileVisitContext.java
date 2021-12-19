package name.kinopie.nio.file;

import java.nio.file.Path;

public interface FileVisitContext {

	Path getPath();

	boolean pathMatchesAny(String... antPathPatterns);
}
