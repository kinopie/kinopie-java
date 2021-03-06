package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * {@link DelegatingFileVisitor} のデフォルト実装クラスです。
 */
public class DefaultDelegatingFileVisitor extends
		AbstractDelegatingFileVisitor<PreVisitContext, PostVisitContext, FileTreeWalkContext<PreVisitContext, PostVisitContext>> {

	/**
	 * 開始地点の {@link Path} を指定して、 {@link DefaultDelegatingFileVisitor} のインスタンスを構築します。
	 * 
	 * @param start 開始地点の {@link Path}
	 */
	public DefaultDelegatingFileVisitor(Path start) {
		this(new DefaultFileTreeWalkContext(start));
	}

	/**
	 * {@link FileTreeWalkContext} のインスタンスを指定して、
	 * {@link DefaultDelegatingFileVisitor} のインスタンスを構築します。
	 * 
	 * @param fileTreeWalkContext {@link FileTreeWalkContext} のインスタンス
	 */
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

	public DefaultPreVisitContext(Path startingPath, Path currentPath, BasicFileAttributes attrs) {
		super(startingPath, currentPath, attrs);
	}
}

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class DefaultPostVisitContext extends AbstractPostVisitContext {

	public DefaultPostVisitContext(Path startingPath, Path currentPath, IOException exc) {
		super(startingPath, currentPath, exc);
	}
}
