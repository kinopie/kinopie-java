package name.kinopie.util.function;

import java.util.function.BiFunction;

/**
 * {@link Exception} を送出可能な {@link BiFunction} です。
 *
 * @param <T> 関数の第1引数の型
 * @param <U> 関数の第2引数の型
 * @param <R> 関数の結果の型
 */
@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> {

	/**
	 * 指定された引数にこの関数を適用します。
	 *
	 * @param t 関数の第1引数
	 * @param u 関数の第2引数
	 * @return 関数の結果
	 * @throws Exception 関数の適用に失敗した場合
	 */
	R apply(T t, U u) throws Exception; // NOSONAR
}
