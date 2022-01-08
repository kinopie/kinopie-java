package name.kinopie.nio.file;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * {@link PreVisitContext} の抽象実装クラスです。
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true, exclude = "attrs")
public abstract class AbstractPreVisitContext extends AbstractFileVisitContext implements PreVisitContext {

	/**
	 * 訪問する {@link Path} の {@link BasicFileAttributes}
	 */
	private BasicFileAttributes attrs;

	/**
	 * 訪問開始地点の {@link Path}, 訪問する {@link Path} とその {@link BasicFileAttributes}
	 * を指定してインスタンスを生成します。
	 * 
	 * @param startingPath 訪問開始地点の {@link Path}
	 * @param currentPath  訪問する {@link Path}
	 * @param attrs        訪問する {@link Path} の {@link BasicFileAttributes}
	 */
	protected AbstractPreVisitContext(Path startingPath, Path currentPath, BasicFileAttributes attrs) {
		super(startingPath, currentPath);
		this.attrs = attrs;
	}
}
