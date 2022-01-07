package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
public abstract class AbstractPostVisitContext extends AbstractFileVisitContext implements PostVisitContext {

	private IOException exc;

	protected AbstractPostVisitContext(Path startingPath, Path currentPath, IOException exc) {
		super(startingPath, currentPath);
		this.exc = exc;
	}
}
