package name.kinopie.util.function;

@FunctionalInterface
public interface ThrowablePredicate<T> {

    boolean test(T t) throws Exception;
}
