package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileTreeWalkContext {

	private Path start;

	public PreVisitContext createPreVisitContext(Path path, BasicFileAttributes attrs) {
		return new PreVisitContext(this, path, attrs);
	}

	public PostVisitContext createPostVisitContext(Path path, IOException exc) {
		return new PostVisitContext(this, path, exc);
	}
}
