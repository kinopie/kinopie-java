package name.kinopie.util.function;

import java.util.function.Function;

/**
 * {@link Exception} を送出可能な {@link Function} です。
 *
 * @param <T> 関数の入力の型
 * @param <R> 関数の結果の型
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {

	/**
	 * 指定された引数にこの関数を適用します。
	 * 
	 * @param t 関数の引数
	 * @return 関数の結果
	 * @throws Exception 関数の適用に失敗した場合
	 */
	R apply(T t) throws Exception; // NOSONAR
}
