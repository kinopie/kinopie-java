package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.kinopie.util.function.ThrowableBiFunction;
import name.kinopie.util.function.ThrowableFunction;
import name.kinopie.util.function.ThrowablePredicate;

@RequiredArgsConstructor
public class DelegatingFileVisitor<F extends FileTreeWalkContext> extends SimpleFileVisitor<Path> {

	@NonNull
	private final F fileTreeWalkContext;

	private Map<ThrowablePredicate<PreVisitContext>, ThrowableFunction<PreVisitContext, FileVisitResult>> onPreVisitDirectory = new LinkedHashMap<>();
	private Map<ThrowablePredicate<PreVisitContext>, ThrowableFunction<PreVisitContext, FileVisitResult>> onVisitFile = new LinkedHashMap<>();
	private Map<ThrowablePredicate<PostVisitContext>, ThrowableFunction<PostVisitContext, FileVisitResult>> onVisitFileFailed = new LinkedHashMap<>();
	private Map<ThrowablePredicate<PostVisitContext>, ThrowableFunction<PostVisitContext, FileVisitResult>> onPostVisitDirectory = new LinkedHashMap<>();

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

	public DelegatingFileVisitor<F> onPreVisitDirectory(ThrowablePredicate<PreVisitContext> predicate,
			ThrowableFunction<PreVisitContext, FileVisitResult> function) {
		onPreVisitDirectory.put(predicate, function);
		return this;
	}

	public DelegatingFileVisitor<F> onVisitFile(ThrowablePredicate<PreVisitContext> predicate,
			ThrowableFunction<PreVisitContext, FileVisitResult> function) {
		onVisitFile.put(predicate, function);
		return this;
	}

	public DelegatingFileVisitor<F> onVisitFileFailed(ThrowablePredicate<PostVisitContext> predicate,
			ThrowableFunction<PostVisitContext, FileVisitResult> function) {
		onVisitFileFailed.put(predicate, function);
		return this;
	}

	public DelegatingFileVisitor<F> onPostVisitDirectory(ThrowablePredicate<PostVisitContext> predicate,
			ThrowableFunction<PostVisitContext, FileVisitResult> function) {
		onPostVisitDirectory.put(predicate, function);
		return this;
	}

	private <A, C extends AbstractFileVisitContext> FileVisitResult visit(Path path, A arg,
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

	private <A, C extends AbstractFileVisitContext> FileVisitResult doVisit(Path path, A arg,
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

	private <A, C extends AbstractFileVisitContext> FileVisitResult doVisitWithPredicatedFunctions(Path path, A arg,
			ThrowableBiFunction<Path, A, C> visitContextFactory,
			Map<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> predicatedFunctions,
			FileVisitResult... continueSubsequent) throws Exception {
		C visitContext = visitContextFactory.apply(path, arg);
		for (Map.Entry<ThrowablePredicate<C>, ThrowableFunction<C, FileVisitResult>> entry : predicatedFunctions
				.entrySet()) {
			ThrowablePredicate<C> predicate = entry.getKey();
			if (predicate.test(visitContext)) {
				ThrowableFunction<C, FileVisitResult> function = entry.getValue();
				FileVisitResult result = function.apply(visitContext);
				if (!ArrayUtils.contains(continueSubsequent, result)) {
					return result;
				}
			}
		}
		return null;
	}
}
