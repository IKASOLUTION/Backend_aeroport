package aeroport.bf.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AbstractRepository<T, ID> extends JpaRepository<T, ID> {

    List<T> findAllByDeletedFalse();

    Optional<T> findTop1ByDeletedFalseAndId(ID id);

    T findOneByDeletedFalseAndId(ID id);

    boolean existsByDeletedFalseAndId(ID id);
}
