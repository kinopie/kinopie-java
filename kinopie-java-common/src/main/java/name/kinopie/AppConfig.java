package name.kinopie;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import name.kinopie.nio.file.DefaultDelegatingFileVisitor;
import name.kinopie.nio.file.DelegatingFileVisitor;
import name.kinopie.nio.file.FileTreeWalkContext;
import name.kinopie.nio.file.PostVisitContext;
import name.kinopie.nio.file.PreVisitContext;

@Configuration
public class AppConfig {

	@Bean
	public FileVisitor<Path> fileVisitor() {
		DelegatingFileVisitor<PreVisitContext, PostVisitContext, FileTreeWalkContext<PreVisitContext, PostVisitContext>> visitor = new DefaultDelegatingFileVisitor();
		visitor.onPreVisitDirectory(context -> context.pathMatchesAny("**/test/**"),
				context -> FileVisitResult.CONTINUE);

		visitor.onVisitFile(context -> context.pathMatchesAny("**/*.java"), context -> {
			Files.readAllLines(context.getPath()).stream().forEach(System.out::println); // NOSONAR
			return FileVisitResult.CONTINUE;
		});

		visitor.onVisitFile(context -> context.pathMatchesAny("src/test/resources/test*.txt"), context -> {
			context.editFile(StandardCharsets.UTF_8,
					allLines -> allLines.stream().map(s -> s.replace('a', 'z')).collect(Collectors.toList()));
			return FileVisitResult.CONTINUE;
		});

		visitor.onPostVisitDirectory(context -> context.pathMatchesAny("**/main/**"),
				context -> FileVisitResult.CONTINUE);
		return visitor;
	}
}
