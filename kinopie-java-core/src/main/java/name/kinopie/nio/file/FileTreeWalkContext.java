package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface FileTreeWalkContext {

	default PreVisitContext createPreVisitContext(Path path, BasicFileAttributes attrs) {
		return new PreVisitContext(this, path, attrs);
	}

	default PostVisitContext createPostVisitContext(Path path, IOException exc) {
		return new PostVisitContext(this, path, exc);
	}
}
