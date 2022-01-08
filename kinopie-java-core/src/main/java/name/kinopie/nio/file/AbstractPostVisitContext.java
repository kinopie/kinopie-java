package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.file.Path;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * {@link PostVisitContext} の抽象実装クラスです。
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
public abstract class AbstractPostVisitContext extends AbstractFileVisitContext implements PostVisitContext {

	/**
	 * コンストラクタの第2引数の {@link Path} の 訪問中に発生した {@link IOException}
	 */
	private IOException exc;

	/**
	 * 訪問開始地点の {@link Path}, 訪問した {@link Path} と訪問中に発生した {@link IOException}
	 * を指定してインスタンスを生成します。
	 * 
	 * @param startingPath 訪問開始地点の {@link Path}
	 * @param currentPath  訪問した {@link Path}
	 * @param exc          第2引数の {@link Path} の 訪問中に発生した {@link IOException}
	 */
	protected AbstractPostVisitContext(Path startingPath, Path currentPath, IOException exc) {
		super(startingPath, currentPath);
		this.exc = exc;
	}
}
