package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class DefaultFileTreeWalkContext extends AbstractFileTreeWalkContext<PreVisitContext, PostVisitContext> {

	public DefaultFileTreeWalkContext(Path start) {
		super(start);
	}

	public PreVisitContext createPreVisitContext(Path path, BasicFileAttributes attrs) {
		return new PreVisitContext(path, attrs);
	}

	public PostVisitContext createPostVisitContext(Path path, IOException exc) {
		return new PostVisitContext(path, exc);
	}
}
