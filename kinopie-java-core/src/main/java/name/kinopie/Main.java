package name.kinopie;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import name.kinopie.nio.file.DelegatingFileVisitor;
import name.kinopie.nio.file.FileTreeWalkContext;

public class Main {

	public static void main(String[] args) throws IOException {
		Path start = Paths.get(".");
		FileTreeWalkContext fileTreeWalkContext = new FileTreeWalkContext(start);
		DelegatingFileVisitor visitor = new DelegatingFileVisitor(fileTreeWalkContext);
		visitor.onPreVisitDirectory(context -> context.pathMatches("**/test/**"), context -> {
			System.err.println(context.getPath());
			return FileVisitResult.CONTINUE;
		});
		visitor.onVisitFile(context -> context.pathMatches("**/*.xml"), context -> {
			System.err.println(context.getPath());
			System.err.println("--------------------------------------------------");
			context.readAllLines().stream().forEach(System.out::println);
			return FileVisitResult.CONTINUE;
		});
		Files.walkFileTree(start, visitor);
	}
}