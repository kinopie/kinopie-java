package name.kinopie;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import name.kinopie.nio.file.DelegatingFileVisitor;

public class Main {

	public static void main(String[] args) throws IOException {
		Path start = Paths.get(".");
		DelegatingFileVisitor visitor = new DelegatingFileVisitor(start);

		visitor.onPreVisitDirectory(context -> context.pathMatchesAny("**/test/**"), context -> {
			System.err.println("pre:" + context.getPath());
			return FileVisitResult.CONTINUE;
		});

		visitor.onVisitFile(context -> context.pathMatchesAny("**/*.java"), context -> {
			System.err.println("file:" + context.getPath());
			System.err.println("--------------------------------------------------");
			Files.readAllLines(context.getPath()).stream().forEach(System.out::println);
			return FileVisitResult.CONTINUE;
		});

		visitor.onPostVisitDirectory(context -> context.pathMatchesAny("**/main/**"), context -> {
			System.err.println("post:" + context.getPath());
			return FileVisitResult.CONTINUE;
		});

		Files.walkFileTree(start, visitor);
	}
}