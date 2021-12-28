package name.kinopie;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import name.kinopie.nio.file.DefaultDelegatingFileVisitor;
import name.kinopie.nio.file.DelegatingFileVisitor;
import name.kinopie.nio.file.FileTreeWalkContext;
import name.kinopie.nio.file.FileVisitContext;
import name.kinopie.nio.file.PostVisitContext;
import name.kinopie.nio.file.PreVisitContext;

public class Main {

	public static void main(String[] args) throws IOException {
		Path start = Paths.get(".");

		DelegatingFileVisitor<PreVisitContext, PostVisitContext, FileTreeWalkContext<PreVisitContext, PostVisitContext>> visitor = new DefaultDelegatingFileVisitor(
				start);
		visitor.onPreVisitDirectory(context -> context.currentPathMatches("**/main/**"),
				context -> FileVisitResult.CONTINUE);

		visitor.onVisitFile(context -> context.currentPathMatches("**/*.java"), context -> {
			Files.readAllLines(context.getCurrentPath()).stream().forEach(System.out::println); // NOSONAR
			return FileVisitResult.CONTINUE;
		});

		visitor.onVisitFile(context -> context.currentPathMatches("src/test/resources/editFileTest*.txt"), context -> {
			context.editFile(StandardCharsets.UTF_8,
					allLines -> allLines.stream().map(s -> s.replace('z', 'a')).collect(Collectors.toList()));
			return FileVisitResult.CONTINUE;
		});

		visitor.onVisitFile(context -> context.currentPathMatches("src/test/resources/changeEncodingTest.txt"),
				context -> {
					context.changeCharset(Charset.forName("shift_jis"), StandardCharsets.UTF_8);
					return FileVisitResult.CONTINUE;
				});

		visitor.onPostVisitDirectory(
				context -> context.currentPathMatches("**/test/resources/**") && context.isEmptyDir(), context -> {
					context.createNewEmptyFile(".gitkeep");
					return FileVisitResult.CONTINUE;
				});

		visitor.onPostVisitDirectory(FileVisitContext::onStartingPath, context -> FileVisitResult.CONTINUE);

		Files.walkFileTree(start, visitor);
	}
}
