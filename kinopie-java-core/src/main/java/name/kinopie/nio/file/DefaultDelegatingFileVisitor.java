package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class DefaultDelegatingFileVisitor extends
		AbstractDelegatingFileVisitor<PreVisitContext, PostVisitContext, FileTreeWalkContext<PreVisitContext, PostVisitContext>> {

	public DefaultDelegatingFileVisitor(Path start) {
		this(new AntPathMatcher(), start);
	}

	public DefaultDelegatingFileVisitor(PathMatcher pathMatcher, Path start) {
		this(new DefaultFileTreeWalkContext(pathMatcher, start));
	}

	public DefaultDelegatingFileVisitor(FileTreeWalkContext<PreVisitContext, PostVisitContext> fileTreeWalkContext) {
		super(fileTreeWalkContext);
	}
}

@RequiredArgsConstructor
class DefaultFileTreeWalkContext implements FileTreeWalkContext<PreVisitContext, PostVisitContext> {

	@NonNull
	private final PathMatcher pathMatcher;

	@NonNull
	private final Path start;

	public PreVisitContext createPreVisitContext(Path current, BasicFileAttributes attrs) {
		return new DefaultPreVisitContext(pathMatcher, start, current, attrs);
	}

	public PostVisitContext createPostVisitContext(Path current, IOException exc) {
		return new DefaultPostVisitContext(pathMatcher, start, current, exc);
	}
}

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class DefaultPreVisitContext extends AbstractPreVisitContext {

	public DefaultPreVisitContext(PathMatcher pathMatcher, Path startingPath, Path currentPath,
			BasicFileAttributes attrs) {
		super(pathMatcher, startingPath, currentPath, attrs);
	}
}

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class DefaultPostVisitContext extends AbstractPostVisitContext {

	public DefaultPostVisitContext(PathMatcher pathMatcher, Path startingPath, Path currentPath, IOException exc) {
		super(pathMatcher, startingPath, currentPath, exc);
	}
}
