package name.kinopie.nio.file;

import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;

import name.kinopie.util.function.ThrowableFunction;
import name.kinopie.util.function.ThrowablePredicate;

/**
 * {@link Files#walkFileTree(Path, FileVisitor)} から {@link FileVisitor}
 * の各メソッドが呼び出された際に、指定した {@link ThrowablePredicate} を満たす任意の
 * {@link ThrowableFunction} を実行できるようにした {@link FileVisitor} の拡張インターフェイスです。
 *
 * @param <R> この {@link DelegatingFileVisitor} が扱う {@link PreVisitContext}
 *            のサブタイプ
 * @param <O> この {@link DelegatingFileVisitor} が扱う {@link PostVisitContext}
 *            のサブタイプ
 * @param <F> この {@link DelegatingFileVisitor} が扱う {@link FileTreeWalkContext}
 *            のサブタイプ
 */
public interface DelegatingFileVisitor<R extends PreVisitContext, O extends PostVisitContext, F extends FileTreeWalkContext<R, O>>
		extends FileVisitor<Path> {

	/**
	 * {@link Files#walkFileTree(Path, FileVisitor)} から
	 * {@link FileVisitor#preVisitDirectory(Object, java.nio.file.attribute.BasicFileAttributes)}
	 * が呼びだされた際に、第1引数の {@link ThrowablePredicate} を評価し、 <code>true</code>
	 * が返却された場合に第2引数の {@link ThrowableFunction#apply(Object)} を実行します。
	 * 
	 * @param predicate 第2引数の {@link ThrowableFunction} 実行の事前条件
	 * 
	 * @param function  {@link Files#walkFileTree(Path, FileVisitor)} から
	 *                  {@link FileVisitor#preVisitDirectory(Object, java.nio.file.attribute.BasicFileAttributes)}
	 *                  が呼び出された際に実行する、任意の {@link ThrowableFunction}
	 * @return 実行中の {@link DelegatingFileVisitor} のインスタンス
	 */
	DelegatingFileVisitor<R, O, F> onPreVisitDirectory(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function);

	/**
	 * {@link Files#walkFileTree(Path, FileVisitor)} から
	 * {@link FileVisitor#visitFile(Object, java.nio.file.attribute.BasicFileAttributes)}
	 * が呼びだされた際に、第1引数の {@link ThrowablePredicate} を評価し、 <code>true</code>
	 * が返却された場合に第2引数の {@link ThrowableFunction#apply(Object)} を実行します。
	 * 
	 * @param predicate 第2引数の {@link ThrowableFunction} 実行の事前条件
	 * 
	 * @param function  {@link Files#walkFileTree(Path, FileVisitor)} から
	 *                  {@link FileVisitor#visitFile(Object, java.nio.file.attribute.BasicFileAttributes)}
	 *                  が呼び出された際に実行する、任意の {@link ThrowableFunction}
	 * @return 実行中の {@link DelegatingFileVisitor} のインスタンス
	 */
	DelegatingFileVisitor<R, O, F> onVisitFile(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function);

	/**
	 * {@link Files#walkFileTree(Path, FileVisitor)} から
	 * {@link FileVisitor#visitFileFailed(Object, java.io.IOException)}
	 * が呼びだされた際に、第1引数の {@link ThrowablePredicate} を評価し、 <code>true</code>
	 * が返却された場合に第2引数の {@link ThrowableFunction#apply(Object)} を実行します。
	 * 
	 * @param predicate 第2引数の {@link ThrowableFunction} 実行の事前条件
	 * 
	 * @param function  {@link Files#walkFileTree(Path, FileVisitor)} から
	 *                  {@link FileVisitor#visitFileFailed(Object, java.io.IOException)}
	 *                  が呼び出された際に実行する、任意の {@link ThrowableFunction}
	 * @return 実行中の {@link DelegatingFileVisitor} のインスタンス
	 */
	DelegatingFileVisitor<R, O, F> onVisitFileFailed(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function);

	/**
	 * {@link Files#walkFileTree(Path, FileVisitor)} から
	 * {@link FileVisitor#postVisitDirectory(Object, java.io.IOException)}
	 * が呼びだされた際に、第1引数の {@link ThrowablePredicate} を評価し、 <code>true</code>
	 * が返却された場合に第2引数の {@link ThrowableFunction#apply(Object)} を実行します。
	 * 
	 * @param predicate 第2引数の {@link ThrowableFunction} 実行の事前条件
	 * 
	 * @param function  {@link Files#walkFileTree(Path, FileVisitor)} から
	 *                  {@link FileVisitor#postVisitDirectory(Object, java.io.IOException)}
	 *                  が呼び出された際に実行する、任意の {@link ThrowableFunction}
	 * @return 実行中の {@link DelegatingFileVisitor} のインスタンス
	 */
	DelegatingFileVisitor<R, O, F> onPostVisitDirectory(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function);
}
