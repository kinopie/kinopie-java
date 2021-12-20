package name.kinopie;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import name.kinopie.nio.file.DelegatingFileVisitor;

public class Main {

	public static void main(String[] args) throws IOException {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
			FileVisitor<Path> visitor = context.getBean(DelegatingFileVisitor.class);
			Path start = Paths.get(".");
			Files.walkFileTree(start, visitor);
		}
	}
}