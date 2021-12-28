package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.util.PathMatcher;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
public abstract class AbstractPostVisitContext extends AbstractFileVisitContext implements PostVisitContext {

	private IOException exc;

	protected AbstractPostVisitContext(PathMatcher pathMatcher, Path startingPath, Path currentPath, IOException exc) {
		super(pathMatcher, startingPath, currentPath);
		this.exc = exc;
	}
}
