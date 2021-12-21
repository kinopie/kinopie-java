package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
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

@RequiredArgsConstructor
public abstract class AbstractDelegatingFileVisitor<R extends PreVisitContext, O extends PostVisitContext, F extends FileTreeWalkContext<R, O>>
		extends SimpleFileVisitor<Path> implements DelegatingFileVisitor<R, O, F> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@NonNull
	private final F fileTreeWalkContext;

	private Map<ThrowablePredicate<R>, ThrowableFunction<R, FileVisitResult>> onPreVisitDirectory = new LinkedHashMap<>();
	private Map<ThrowablePredicate<R>, ThrowableFunction<R, FileVisitResult>> onVisitFile = new LinkedHashMap<>();
	private Map<ThrowablePredicate<O>, ThrowableFunction<O, FileVisitResult>> onVisitFileFailed = new LinkedHashMap<>();
	private Map<ThrowablePredicate<O>, ThrowableFunction<O, FileVisitResult>> onPostVisitDirectory = new LinkedHashMap<>();

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return visit(dir, attrs, fileTreeWalkContext::createPreVisitContext, onPreVisitDirectory,
				super::preVisitDirectory, FileVisitResult.CONTINUE);
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		return visit(file, attrs, fileTreeWalkContext::createPreVisitContext, onVisitFile, super::visitFile,
				FileVisitResult.CONTINUE, FileVisitResult.SKIP_SUBTREE);
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return visit(file, exc, fileTreeWalkContext::createPostVisitContext, onVisitFileFailed, super::visitFileFailed,
				FileVisitResult.CONTINUE, FileVisitResult.SKIP_SUBTREE);
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		return visit(dir, exc, fileTreeWalkContext::createPostVisitContext, onPostVisitDirectory,
				super::postVisitDirectory, FileVisitResult.CONTINUE, FileVisitResult.SKIP_SUBTREE);
	}

	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onPreVisitDirectory(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function) {
		onPreVisitDirectory.put(predicate, function);
		return this;
	}

	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onVisitFile(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function) {
		onVisitFile.put(predicate, function);
		return this;
	}

	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onVisitFileFailed(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function) {
		onVisitFileFailed.put(predicate, function);
		return this;
	}

	@Override
	public AbstractDelegatingFileVisitor<R, O, F> onPostVisitDirectory(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function) {
		onPostVisitDirectory.put(predicate, function);
		return this;
	}

	private <A, C extends FileVisitContext> FileVisitResult visit(Path path, A arg,
			ThrowableBiFunction<Path, A, C> visitContextFactory,
			Map<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> predicatedFunctions,
			ThrowableBiFunction<Path, A, FileVisitResult> fallbackFunction, FileVisitResult... continueSubsequent)
			throws IOException {
		try {
			return doVisit(path, arg, visitContextFactory, predicatedFunctions, fallbackFunction, continueSubsequent);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private <A, C extends FileVisitContext> FileVisitResult doVisit(Path path, A arg,
			ThrowableBiFunction<Path, A, C> visitContextFactory,
			Map<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> predicatedFunctions,
			ThrowableBiFunction<Path, A, FileVisitResult> fallbackFunction, FileVisitResult... continueSubsequent)
			throws Exception {
		FileVisitResult result = doVisitWithPredicatedFunctions(path, arg, visitContextFactory, predicatedFunctions,
				continueSubsequent);
		if (result == null) {
			return fallbackFunction.apply(path, arg);
		}
		return result;
	}

	private <A, C extends FileVisitContext> FileVisitResult doVisitWithPredicatedFunctions(Path path, A arg,
			ThrowableBiFunction<Path, A, C> visitContextFactory,
			Map<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> predicatedFunctions,
			FileVisitResult... continueSubsequent) throws Exception {
		FileVisitResult result = null;
		C visitContext = visitContextFactory.apply(path, arg);
		for (Map.Entry<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> entry : predicatedFunctions
				.entrySet()) {
			ThrowablePredicate<C> predicate = entry.getKey();
			if (predicate.test(visitContext)) {
				logger.info("Start to visit the path:'{}'.", path.normalize());
				ThrowableFunction<C, FileVisitResult> function = entry.getValue();
				result = function.apply(visitContext);
				logger.info("Finished visiting the path:'{}' successfully.", path.normalize());
				if (!ArrayUtils.contains(continueSubsequent, result)) {
					return result;
				}
			}
		}
		return result;
	}
}
