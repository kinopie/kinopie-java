package name.kinopie.nio.file;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractFileTreeWalkContext<R extends PreVisitContext, O extends PostVisitContext>
		implements FileTreeWalkContext<R, O> {

	private Path start;
}
