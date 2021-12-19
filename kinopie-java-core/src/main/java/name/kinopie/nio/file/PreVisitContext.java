package name.kinopie.nio.file;

import java.nio.file.attribute.BasicFileAttributes;

public interface PreVisitContext extends FileVisitContext {

	BasicFileAttributes getAttrs();
}
