package io.github.yuokada.simulations;

import com.google.common.annotations.Beta;
import io.gatling.javaapi.core.FeederBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

@Beta
public class NeoTpchQueryFeeder implements FeederBuilder<String> {

    private static final List<String> SCHEMAS = Arrays.asList("tiny", "sf1");
    private static final List<String> TABLES = Arrays.asList(
        "customer", "lineitem", "nation", "orders", "part", "partsupp", "region", "supplier"
    );

    private final Random random = new Random();

    public String generate() {
        String schema = SCHEMAS.get(random.nextInt(SCHEMAS.size()));
        String table = TABLES.get(random.nextInt(TABLES.size()));

        int operation = random.nextInt(10);
        switch (operation) {
            case 0:
            case 1:
                return generateShowTables(table, schema);
            case 2:
            case 3:
            case 4:
                return generateCountQuery(table, schema);
            default:
                return generateSimpleSelectQuery(table, schema);
        }
    }

    private String generateShowTables(String table, String schema) {
        return String.format("DESCRIBE tpch.%s.%s", schema, table);
    }

    private String generateCountQuery(String table, String schema) {
        return String.format("SELECT COUNT(*) FROM tpch.%s.%s", schema, table);
    }

    private String generateSimpleSelectQuery(String table, String schema) {
        int limit = random.nextInt(31) + 1;
        return String.format("SELECT * FROM tpch.%s.%s LIMIT %d", schema, table, limit);
    }

    @Override
    public FeederBuilder<String> queue() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public FeederBuilder<String> random() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public FeederBuilder<String> shuffle() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public FeederBuilder<String> circular() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @NotNull
    @Override
    public FeederBuilder<Object> transform(@NotNull BiFunction<String, String, Object> f) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Map<String, Object>> readRecords() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int recordsCount() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public FeederBuilder<String> shard() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public scala.Function0<scala.collection.Iterator<scala.collection.immutable.Map<String, Object>>> asScala() {
        throw new UnsupportedOperationException("Not implemented");
    }
}