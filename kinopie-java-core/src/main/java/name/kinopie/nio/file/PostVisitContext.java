package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;

import lombok.Getter;

@Getter
public class PostVisitContext extends AbstractFileVisitContext {

	private IOException exc;

	public PostVisitContext(Path path, IOException exc) {
		super(path);
		this.exc = exc;
	}
}
