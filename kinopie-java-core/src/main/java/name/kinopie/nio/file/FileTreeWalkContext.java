package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * {@link Files#walkFileTree(Path, java.nio.file.FileVisitor)} で訪問中の FileTree
 * に関する情報を保持するコンテキストです。
 * 
 * @param <R> {@link FileTreeWalkContext#createPreVisitContext(Path, BasicFileAttributes)}
 *            の戻り値の {@link PreVisitContext} のサブタイプ
 * @param <O> {@link FileTreeWalkContext#createPostVisitContext(Path, IOException)}
 *            の戻り値の {@link PostVisitContext} のサブタイプ
 */
public interface FileTreeWalkContext<R extends PreVisitContext, O extends PostVisitContext> {

	/**
	 * 指定された {@link Path} と {@link BasicFileAttributes}
	 * を使用して、{@link PreVisitContext} を作成します。
	 * 
	 * @param currentPath 訪問する {@link Path}
	 * @param attrs       訪問する {@link Path} の {@link BasicFileAttributes}
	 * @return {@link PreVisitContext} またはそのサブタイプのインスタンス
	 */
	R createPreVisitContext(Path currentPath, BasicFileAttributes attrs);

	/**
	 * 指定された {@link Path} と {@link IOException} を使用して、{@link PostVisitContext}
	 * を作成します。
	 * 
	 * @param currentPath 訪問した {@link Path}
	 * @param exc         第1引数の {@link Path} の訪問中に発生した {@link IOException}
	 * @return {@link PostVisitContext} またはそのサブタイプのインスタンス
	 */
	O createPostVisitContext(Path currentPath, IOException exc);
}
