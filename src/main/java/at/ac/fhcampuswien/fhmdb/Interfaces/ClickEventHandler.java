package at.ac.fhcampuswien.fhmdb.Interfaces;


@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T item);
}
