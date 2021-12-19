package name.kinopie.nio.file;

import java.io.IOException;

public interface PostVisitContext extends FileVisitContext {

	IOException getExc();
}
