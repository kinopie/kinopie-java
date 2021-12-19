package name.kinopie.nio.file;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.Getter;

@Getter
public class PreVisitContext extends AbstractFileVisitContext {

	private BasicFileAttributes attrs;

	public PreVisitContext(Path path, BasicFileAttributes attrs) {
		super(path);
		this.attrs = attrs;
	}
}
