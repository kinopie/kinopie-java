package name.kinopie.nio.file;

import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;

import name.kinopie.util.function.ThrowableFunction;
import name.kinopie.util.function.ThrowablePredicate;

public interface DelegatingFileVisitor<R extends PreVisitContext, O extends PostVisitContext, F extends FileTreeWalkContext<R, O>>
		extends FileVisitor<Path> {

	DelegatingFileVisitor<R, O, F> onPreVisitDirectory(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function);

	DelegatingFileVisitor<R, O, F> onVisitFile(ThrowablePredicate<R> predicate,
			ThrowableFunction<R, FileVisitResult> function);

	DelegatingFileVisitor<R, O, F> onVisitFileFailed(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function);

	DelegatingFileVisitor<R, O, F> onPostVisitDirectory(ThrowablePredicate<O> predicate,
			ThrowableFunction<O, FileVisitResult> function);
}
