package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface FileTreeWalkContext<R extends FileVisitContext, O extends FileVisitContext> {

	Path getStart();

	R createPreVisitContext(Path path, BasicFileAttributes attrs);

	O createPostVisitContext(Path path, IOException exc);
}
