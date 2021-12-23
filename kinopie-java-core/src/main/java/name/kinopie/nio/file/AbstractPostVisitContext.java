package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;

import lombok.Getter;

@Getter
public abstract class AbstractPostVisitContext extends AbstractFileVisitContext implements PostVisitContext {

	private IOException exc;

	protected AbstractPostVisitContext(Path start, Path current, IOException exc) {
		super(start, current);
		this.exc = exc;
	}
}
