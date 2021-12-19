package name.kinopie.nio.file;

import lombok.NonNull;

public class DelegatingFileVisitor
		extends AbstractDelegatingFileVisitor<PreVisitContext, PostVisitContext, DefaultFileTreeWalkContext> {

	public DelegatingFileVisitor(@NonNull DefaultFileTreeWalkContext fileTreeWalkContext) {
		super(fileTreeWalkContext);
	}
}
