package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class DelegatingFileVisitor extends
		AbstractDelegatingFileVisitor<PreVisitContext, PostVisitContext, FileTreeWalkContext<PreVisitContext, PostVisitContext>> {

	public DelegatingFileVisitor() {
		this(new DefaultFileTreeWalkContext());
	}

	public DelegatingFileVisitor(FileTreeWalkContext<PreVisitContext, PostVisitContext> fileTreeWalkContext) {
		super(fileTreeWalkContext);
	}
}

class DefaultFileTreeWalkContext implements FileTreeWalkContext<PreVisitContext, PostVisitContext> {

	public DefaultPreVisitContext createPreVisitContext(Path path, BasicFileAttributes attrs) {
		return new DefaultPreVisitContext(path, attrs);
	}

	public DefaultPostVisitContext createPostVisitContext(Path path, IOException exc) {
		return new DefaultPostVisitContext(path, exc);
	}
}

class DefaultPreVisitContext extends AbstractPreVisitContext {

	public DefaultPreVisitContext(Path path, BasicFileAttributes attrs) {
		super(path, attrs);
	}
}

class DefaultPostVisitContext extends AbstractPostVisitContext {

	public DefaultPostVisitContext(Path path, IOException exc) {
		super(path, exc);
	}
}
