package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 訪問した {@link Path} に関する情報を保持するコンテキストです。
 */
public interface PostVisitContext extends FileVisitContext {

	/**
	 * {@link Path} の訪問中に発生した {@link IOException} を取得します。
	 * 
	 * @return {@link Path} の訪問中に発生した {@link IOException}
	 */
	IOException getExc();
}
