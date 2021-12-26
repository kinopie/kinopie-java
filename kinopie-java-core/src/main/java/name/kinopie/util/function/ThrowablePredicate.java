package name.kinopie.util.function;

import java.util.function.Predicate;

/**
 * {@link Exception} を送出可能な {@link Predicate} です。
 *
 * @param <T> 述語の入力の型
 */
@FunctionalInterface
public interface ThrowablePredicate<T> {

	/**
	 * 指定された引数でこの述語を評価します。
	 * 
	 * @param t 入力引数
	 * @return 入力引数が述語に一致する場合はtrue、それ以外の場合はfalse
	 * @throws Exception 述語の評価に失敗した場合
	 */
	boolean test(T t) throws Exception; // NOSONAR
}
