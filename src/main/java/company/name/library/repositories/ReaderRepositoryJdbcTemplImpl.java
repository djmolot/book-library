package company.name.library.repositories;

import company.name.library.entities.Reader;
import company.name.library.exceptions.DaoLayerException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ReaderRepositoryJdbcTemplImpl implements ReaderRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Reader add(Reader reader) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO readers (name) VALUES (?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reader.getName());
            return ps;
        }, keyHolder);
        try {
            reader.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        } catch (InvalidDataAccessApiUsageException e) {
            throw new DaoLayerException("keyHolder contains no Key" + e.getMessage());
        }
        return reader;    }

    @Override
    public Optional<Reader> getById(Long id) {
        List<Reader> results = jdbcTemplate.query(
                "SELECT * FROM readers WHERE id = ?;",
                this::mapRowToReader,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public List<Reader> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name FROM readers;",
                this::mapRowToReader);
    }

    @Override
    public void update(Reader reader) {

    }

    @Override
    public Optional<Reader> getReaderByBookId(Long bookId) {
        List<Reader> results = jdbcTemplate.query(
                "SELECT b.reader_id as id, r.name as name "
                        + "FROM books b JOIN readers r ON b.reader_id = r.id WHERE b.id = ?;",
                this::mapRowToReader,
                bookId);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    private Reader mapRowToReader(ResultSet resultSet, int rowNum)
            throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setName(resultSet.getString("name"));
        reader.setBooks(new ArrayList<>());
        return reader;
    }

}
