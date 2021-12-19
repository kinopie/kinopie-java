package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;

import lombok.Getter;

@Getter
public abstract class AbstractPostVisitContext extends AbstractFileVisitContext implements PostVisitContext {

	private IOException exc;

	protected AbstractPostVisitContext(Path path, IOException exc) {
		super(path);
		this.exc = exc;
	}
}
