package name.kinopie.nio.file;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true, exclude = "attrs")
public abstract class AbstractPreVisitContext extends AbstractFileVisitContext implements PreVisitContext {

	private BasicFileAttributes attrs;

	protected AbstractPreVisitContext(Path startingPath, Path currentPath, BasicFileAttributes attrs) {
		super(startingPath, currentPath);
		this.attrs = attrs;
	}
}
