package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class DefaultDelegatingFileVisitor extends
		AbstractDelegatingFileVisitor<PreVisitContext, PostVisitContext, FileTreeWalkContext<PreVisitContext, PostVisitContext>> {

	public DefaultDelegatingFileVisitor(Path start) {
		this(new DefaultFileTreeWalkContext(start));
	}

	public DefaultDelegatingFileVisitor(FileTreeWalkContext<PreVisitContext, PostVisitContext> fileTreeWalkContext) {
		super(fileTreeWalkContext);
	}
}

@RequiredArgsConstructor
class DefaultFileTreeWalkContext implements FileTreeWalkContext<PreVisitContext, PostVisitContext> {

	@NonNull
	private final Path start;

	public PreVisitContext createPreVisitContext(Path current, BasicFileAttributes attrs) {
		return new DefaultPreVisitContext(start, current, attrs);
	}

	public PostVisitContext createPostVisitContext(Path current, IOException exc) {
		return new DefaultPostVisitContext(start, current, exc);
	}
}

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class DefaultPreVisitContext extends AbstractPreVisitContext {

	public DefaultPreVisitContext(Path start, Path current, BasicFileAttributes attrs) {
		super(start, current, attrs);
	}
}

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class DefaultPostVisitContext extends AbstractPostVisitContext {

	public DefaultPostVisitContext(Path start, Path current, IOException exc) {
		super(start, current, exc);
	}
}
