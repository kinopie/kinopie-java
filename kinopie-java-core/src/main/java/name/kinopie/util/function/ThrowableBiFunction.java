package name.kinopie.util.function;

@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> {

	R apply(T t, U u) throws Exception; // NOSONAR
}
