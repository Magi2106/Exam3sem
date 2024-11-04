package dat.daos;

import java.util.List;

public interface IDAO<T, I> {
    T create(T dto);
    List<T> getAll();
    T getById(I id);
    T update(I id, T dto);
    void delete(I id);
}