package name.kinopie.nio.file;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractFileTreeWalkContext implements FileTreeWalkContext {

	private Path start;
}
