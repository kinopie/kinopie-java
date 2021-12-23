package name.kinopie.nio.file;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.Getter;

@Getter
public abstract class AbstractPreVisitContext extends AbstractFileVisitContext implements PreVisitContext {

	private BasicFileAttributes attrs;

	protected AbstractPreVisitContext(Path start, Path current, BasicFileAttributes attrs) {
		super(start, current);
		this.attrs = attrs;
	}
}
