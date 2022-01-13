package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.kinopie.util.function.ThrowableBiFunction;
import name.kinopie.util.function.ThrowableFunction;
import name.kinopie.util.function.ThrowablePredicate;

/**
 * {@link DelegatingFileVisitor} の抽象実装クラスです。
 *
 * @param <R> この {@link AbstractDelegatingFileVisitor} が扱う
 *            {@link PreVisitContext} のサブタイプ
 * @param <O> この {@link AbstractDelegatingFileVisitor} が扱う
 *            {@link PostVisitContext} のサブタイプ
 * @param <F> この {@link AbstractDelegatingFileVisitor} が扱う
 *            {@link FileTreeWalkContext} のサブタイプ
 */
@RequiredArgsConstructor
public abstract class AbstractDelegatingFileVisitor<R extends PreVisitContext, O extends PostVisitContext, F extends FileTreeWalkContext<R, O>>
		extends SimpleFileVisitor<Path> implements DelegatingFileVisitor<R, O, F> {

	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * この {@link AbstractDelegatingFileVisitor} が扱う {@link FileTreeWalkContext}
	 * のサブタイプのインスタンス
	 */
	@NonNull
	private final F fileTreeWalkContext;

	/**
	 * {@link FileVisitor#preVisitDirectory(Path, BasicFileAttributes)}
	 * が呼び出された際に評価される {@link ThrowablePredicate} と、条件を満たす場合に実行される
	 * {@link ThrowableFunction} の組み合わせ
	 */
	private Map<ThrowablePredicate<R>, ThrowableFunction<R, FileVisitResult>> onPreVisitDirectory = new LinkedHashMap<>();

	/**
	 * {@link FileVisitor#visitFile(Path, BasicFileAttributes)} が呼び出された際に評価される
	 * {@link ThrowablePredicate} と、条件を満たす場合に実行される {@link ThrowableFunction} の組み合わせ
	 */
	private Map<ThrowablePredicate<R>, ThrowableFunction<R, FileVisitResult>> onVisitFile = new LinkedHashMap<>();

	/**
	 * {@link FileVisitor#visitFileFailed(Path, IOException)} が呼び出された際に評価される
	 * {@link ThrowablePredicate} と、条件を満たす場合に実行される {@link ThrowableFunction} の組み合わせ
	 */
	private Map<ThrowablePredicate<O>, ThrowableFunction<O, FileVisitResult>> onVisitFileFailed = new LinkedHashMap<>();

	/**
	 * {@link FileVisitor#postVisitDirectory(Path, IOException)} が呼び出された際に評価される
	 * {@link ThrowablePredicate} と、条件を満たす場合に実行される {@link ThrowableFunction} の組み合わせ
	 */
	private Map<ThrowablePredicate<O>, ThrowableFunction<O, FileVisitResult>> onPostVisitDirectory = new LinkedHashMap<>();

	/**
	 * {@link DelegatingFileVisitor#onPreVisitDirectory(ThrowablePredicate, ThrowableFunction)}
	 * で登録された {@link ThrowablePredicate} と {@link ThrowableFunction}
	 * のペアを登録された順序で評価・実行し、最後の {@link ThrowableFunction} の実行結果の
	 * {@link FileVisitResult} を返却します。
	 * 
	 * {@link DelegatingFileVisitor#onPreVisitDirectory(ThrowablePredicate, ThrowableFunction)}
	 * で {@link ThrowablePredicate} と {@link ThrowableFunction} のペアが登録されていない場合は、
	 * {@link SimpleFileVisitor#preVisitDirectory(Path, BasicFileAttributes)}
	 * の実行結果を返却します。
	 * 
	 * @param dir   訪問するディレクトリの {@link Path}
	 * @param attrs 訪問するディレクトリの基本属性
	 * 
	 * @throws IOException 入出力エラーが発生した場合
	 * 
	 * @see FileVisitor#preVisitDirectory(Path, BasicFileAttributes)
	 */
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return visit(dir, attrs, fileTreeWalkContext::createPreVisitContext, onPreVisitDirectory,
				super::preVisitDirectory, "PreVisitDirectory", FileVisitResult.CONTINUE);
	}

	/**
	 * {@link DelegatingFileVisitor#onVisitFile(ThrowablePredicate, ThrowableFunction)}
	 * で登録された {@link ThrowablePredicate} と {@link ThrowableFunction}
	 * のペアを登録された順序で評価・実行し、最後の {@link ThrowableFunction} の実行結果の
	 * {@link FileVisitResult} を返却します。
	 * 
	 * {@link DelegatingFileVisitor#onVisitFile(ThrowablePredicate, ThrowableFunction)}
	 * で {@link ThrowablePredicate} と {@link ThrowableFunction} のペアが登録されていない場合は、
	 * {@link SimpleFileVisitor#visitFile(Path, BasicFileAttributes)} の実行結果を返却します。
	 * 
	 * @param file  訪問するファイルの {@link Path}
	 * @param attrs 訪問するファイルの基本属性
	 * 
	 * @throws IOException 入出力エラーが発生した場合
	 * 
	 * @see FileVisitor#visitFile(Path, BasicFileAttributes)
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		return visit(file, attrs, fileTreeWalkContext::createPreVisitContext, onVisitFile, super::visitFile,
				"VisitFile", FileVisitResult.CONTINUE, FileVisitResult.SKIP_SUBTREE);
	}

	/**
	 * {@link DelegatingFileVisitor#onVisitFileFailed(ThrowablePredicate, ThrowableFunction)}
	 * で登録された {@link ThrowablePredicate} と {@link ThrowableFunction}
	 * のペアを登録された順序で評価・実行し、最後の {@link ThrowableFunction} の実行結果の
	 * {@link FileVisitResult} を返却します。
	 * 
	 * {@link DelegatingFileVisitor#onVisitFileFailed(ThrowablePredicate, ThrowableFunction)}
	 * で {@link ThrowablePredicate} と {@link ThrowableFunction} のペアが登録されていない場合は、
	 * {@link SimpleFileVisitor#visitFileFailed(Path, IOException)} の実行結果を返却します。
	 * 
	 * @param file 訪問したファイルの {@link Path}
	 * @param exc  ファイルを訪問した際に発生した {@link IOException}
	 * 
	 * @throws IOException 入出力エラーが発生した場合
	 * 
	 * @see FileVisitor#visitFileFailed(Path, IOException)
	 */
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return visit(file, exc, fileTreeWalkContext::createPostVisitContext, onVisitFileFailed, super::visitFileFailed,
				"VisitFileFailed", FileVisitResult.CONTINUE, FileVisitResult.SKIP_SUBTREE);
	}

	/**
	 * {@link DelegatingFileVisitor#onPostVisitDirectory(ThrowablePredicate, ThrowableFunction)}
	 * で登録された {@link ThrowablePredicate} と {@link ThrowableFunction}
	 * のペアを登録された順序で評価・実行し、最後の {@link ThrowableFunction} の実行結果の
	 * {@link FileVisitResult} を返却します。
	 * 
	 * {@link DelegatingFileVisitor#onPostVisitDirectory(ThrowablePredicate, ThrowableFunction)}
	 * で {@link ThrowablePredicate} と {@link ThrowableFunction} のペアが登録されていない場合は、
	 * {@link SimpleFileVisitor#postVisitDirectory(Path, IOException)} の実行結果を返却します。
	 * 
	 * @param dir 訪問したディレクトリの {@link Path}
	 * @param exc ディレクトリを訪問した際に発生した {@link IOException}、入出力エラーが発生しなかった場合は
	 *            <code>null</code>
	 * 
	 * @throws IOException 入出力エラーが発生した場合
	 * 
	 * @see FileVisitor#postVisitDirectory(Path, IOException)
	 */
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return visit(dir, exc, fileTreeWalkContext::createPostVisitContext, onPostVisitDirectory,
				super::postVisitDirectory, "PostVisitDirectory", FileVisitResult.CONTINUE,
				FileVisitResult.SKIP_SUBTREE);
	}

	/**
	 * @see DelegatingFileVisitor#onPreVisitDirectory(ThrowablePredicate,
	 *      ThrowableFunction)
	 */
	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onPreVisitDirectory(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function) {
		onPreVisitDirectory.put(predicate, function);
		return this;
	}

	/**
	 * @see DelegatingFileVisitor#onVisitFile(ThrowablePredicate, ThrowableFunction)
	 */
	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onVisitFile(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function) {
		onVisitFile.put(predicate, function);
		return this;
	}

	/**
	 * @see DelegatingFileVisitor#onVisitFileFailed(ThrowablePredicate,
	 *      ThrowableFunction)
	 */
	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onVisitFileFailed(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function) {
		onVisitFileFailed.put(predicate, function);
		return this;
	}

	/**
	 * @see DelegatingFileVisitor#onPostVisitDirectory(ThrowablePredicate,
	 *      ThrowableFunction)
	 */
	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onPostVisitDirectory(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function) {
		onPostVisitDirectory.put(predicate, function);
		return this;
	}

	private <A, C extends FileVisitContext> FileVisitResult visit(Path path, A arg,
			ThrowableBiFunction<Path, A, C> visitContextFactory,
			Map<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> predicatedFunctions,
			ThrowableBiFunction<Path, A, FileVisitResult> fallbackFunction, String eventType,
			FileVisitResult... continueSubsequent) throws IOException {
		try {
			return doVisit(path, arg, visitContextFactory, predicatedFunctions, fallbackFunction, eventType,
					continueSubsequent);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private <A, C extends FileVisitContext> FileVisitResult doVisit(Path path, A arg,
			ThrowableBiFunction<Path, A, C> visitContextFactory,
			Map<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> predicatedFunctions,
			ThrowableBiFunction<Path, A, FileVisitResult> fallbackFunction, String eventType,
			FileVisitResult... continueSubsequent) throws Exception {
		FileVisitResult result = doVisitWithPredicatedFunctions(path, arg, visitContextFactory, predicatedFunctions,
				eventType, continueSubsequent);
		if (result == null) {
			return fallbackFunction.apply(path, arg);
		}
		return result;
	}

	private <A, C extends FileVisitContext> FileVisitResult doVisitWithPredicatedFunctions(Path path, A arg,
			ThrowableBiFunction<Path, A, C> visitContextFactory,
			Map<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> predicatedFunctions, String eventType,
			FileVisitResult... continueSubsequent) throws Exception {
		FileVisitResult result = null;
		C visitContext = visitContextFactory.apply(path, arg);
		for (Map.Entry<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> entry : predicatedFunctions
				.entrySet()) {
			ThrowablePredicate<C> predicate = entry.getKey();
			if (predicate.test(visitContext)) {
				logger.info("[{}] Start to visit the path:'{}' with context '{}'.", eventType,
						visitContext.getNormalizedCurrentPath(), visitContext);
				ThrowableFunction<C, FileVisitResult> function = entry.getValue();
				result = function.apply(visitContext);
				logger.info("[{}] Finished visiting the path:'{}' with result '{}'.", eventType,
						visitContext.getNormalizedCurrentPath(), result);
				if (!ArrayUtils.contains(continueSubsequent, result)) {
					return result;
				}
			}
		}
		return result;
	}
}
