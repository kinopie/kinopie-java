package name.kinopie.nio.file;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 訪問する {@link Path} に関する情報を保持するコンテキストです。
 */
public interface PreVisitContext extends FileVisitContext {

	/**
	 * 訪問する {@link Path} の {@link BasicFileAttributes} を取得します。
	 * 
	 * @return 訪問する {@link Path} の {@link BasicFileAttributes}
	 */
	BasicFileAttributes getAttrs();
}
