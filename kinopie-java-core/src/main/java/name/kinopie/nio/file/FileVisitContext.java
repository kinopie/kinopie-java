package name.kinopie.nio.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 訪問中の {@link Path} に関する情報を保持するコンテキストです。
 */
public interface FileVisitContext {

	/**
	 * 訪問開始地点の {@link Path} を取得します。
	 * 
	 * @return 訪問開始地点の {@link Path}
	 */
	Path getStartingPath();

	/**
	 * 現在訪問中の {@link Path} を取得します。
	 * 
	 * @return 現在訪問中の {@link Path}
	 */
	Path getCurrentPath();

	/**
	 * 現在訪問中の {@link Path} が、指定された Antパスパターンのいずれかにマッチするかどうかを判定します。
	 * 
	 * @param antPathPatterns Antパスパターン（複数指定可）
	 * 
	 * @return 現在訪問中の {@link Path} が、指定された Antパスパターンのいずれかにマッチする場合は
	 *         <code>true</code>、いずれにもマッチしない場合は <code>false</code>
	 */
	boolean currentPathMatches(String... antPathPatterns);

	/**
	 * 現在訪問中の {@link Path} が開始地点の {@link Path} かどうかを判定します。
	 * 
	 * @return 訪問中の {@link Path} が開始地点の {@link Path} である場合は
	 *         <code>true</code>、そうでない場合は <code>false</code>
	 */
	boolean onStartingPath();

	/**
	 * 現在訪問中の {@link Path} が空ディレクトリかどうかを判定します。
	 * 
	 * @return 訪問中の {@link Path} が空ディレクトリである場合は <code>true</code>、そうでない場合は
	 *         <code>false</code>
	 * @throws IOException 空ディレクトリかどうかの判定に失敗した場合
	 */
	boolean isEmptyDir() throws IOException;

	/**
	 * 現在訪問中の {@link Path} が指し示すファイルの全ての行を第1引数で指定した {@link Charset} で読み込み、第2引数で指定した
	 * {@link UnaryOperator} で編集して保存します。
	 * 
	 * @param cs     現在訪問中の {@link Path} が指し示すファイルの {@link Charset}）
	 * @param editor 現在訪問中のファイルの全行を文字列の {@link List} で受け取り、編集・加工する
	 *               {@link UnaryOperator}
	 * 
	 * @throws IOException ファイルの読み込みや保存に失敗した場合
	 */
	void editFile(Charset cs, UnaryOperator<List<String>> editor) throws IOException;

	/**
	 * 現在訪問中の {@link Path} が指し示すファイルの {@link Charset} を変更します。
	 * 
	 * @param from 変更前の {@link Charset} （現在訪問中の {@link Path} が指し示すファイルの
	 *             {@link Charset}）
	 * @param to   変更後の {@link Charset}
	 * 
	 * @throws IOException ファイルの読み込みや保存に失敗した場合
	 */
	void changeCharset(Charset from, Charset to) throws IOException;

	/**
	 * 現在訪問中の {@link Path} が指し示すディレクトリに、指定された名前の空ファイルを新規作成します。
	 * 
	 * @param fileName 新規作成するフィル名
	 * @throws IOException ファイルの作成に失敗した場合
	 */
	void createNewEmptyFile(String fileName) throws IOException;
}
