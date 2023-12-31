package br.dev.brunoxkk0.essenciais.core.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static br.dev.brunoxkk0.syrxmccore.helper.ReflectionUtils.createInstance;
import static java.nio.file.StandardOpenOption.*;

public abstract class DataStore<T> {

    private static final Logger logger = Logger.getLogger("DataStore");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

    @Getter
    private final Path dataStorePath;

    private final Class<T> target;

    private final HashMap<T, Path> DATA_PATH_REFERENCE = new HashMap<>();
    private final ArrayList<T> DATA_ARRAY;

    public DataStore(Class<T> target, Path path, ArrayList<T> dataArray) {

        this.target = target;
        this.dataStorePath = path;
        this.DATA_ARRAY = dataArray;

        File file = path.toFile();

        if (!file.exists()) {
            if (file.mkdirs())
                logger.info("Created folder at " + path);
        }

    }

    public void load() throws IOException {

        if (Files.isDirectory(dataStorePath)) {

            File[] files = dataStorePath.toFile().listFiles();

            if (files == null)
                return;

            for (File file : files) {

                ObjectNode rawData = (ObjectNode) OBJECT_MAPPER.readTree(
                        Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)
                );

                loadDataEntry(file, rawData);

            }
        }
    }

    private void loadDataEntry(File file, JsonNode rawData) throws JsonProcessingException {

        T obj = OBJECT_MAPPER.treeToValue(rawData, target);

        if (obj instanceof Extensible extensible)
            extensible.rootNode = (ObjectNode) rawData;

        DATA_ARRAY.add(obj);
        DATA_PATH_REFERENCE.put(obj, file.toPath());
    }

    public synchronized void save() throws IOException {

        updatedObjectsPath();

        for (Map.Entry<T, Path> item : DATA_PATH_REFERENCE.entrySet()) {

            JsonNode itemNode = OBJECT_MAPPER.valueToTree(item.getKey());

            ObjectNode objectNode = ((ObjectNode) itemNode);

            if (item.getKey() instanceof Extensible extensible) {

                objectNode = getJsonNodes(objectNode, extensible);

            }

            writeItemData(item.getValue(), objectNode);

        }
    }

    private static void writeItemData(Path item, ObjectNode objectNode) throws IOException {
        byte[] data = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode).getBytes(StandardCharsets.UTF_8);
        Files.write(item, data, WRITE, CREATE, TRUNCATE_EXISTING);
    }

    public synchronized void save(T item) throws IOException {

        if (!DATA_PATH_REFERENCE.containsKey(item)) {
            DATA_PATH_REFERENCE.put(item, calculateFilePath(item));
        }

        Path path = DATA_PATH_REFERENCE.get(item);

        ObjectNode objectNode = OBJECT_MAPPER.valueToTree(item);

        if (item instanceof Extensible extensible) {

            objectNode = getJsonNodes(objectNode, extensible);

        }

        writeItemData(path, objectNode);
    }

    private ObjectNode getJsonNodes(ObjectNode objectNode, Extensible extensible) {

        if (extensible.rootNode != null) {
            objectNode = (extensible.rootNode.setAll(objectNode));
        }

        for (Map.Entry<String, Object> data : extensible.getInternalExtensiveReferences().entrySet()) {
            objectNode.set(data.getKey(), OBJECT_MAPPER.valueToTree(data.getValue()));
        }

        return objectNode;
    }

    private void updatedObjectsPath() {
        for (T item : DATA_ARRAY) {
            if (!DATA_PATH_REFERENCE.containsKey(item)) {
                DATA_PATH_REFERENCE.put(item, calculateFilePath(item));
            }
        }
    }

    public abstract Path calculateFilePath(T item);

    public static abstract class Extensible {

        @JsonIgnore
        private transient ObjectNode rootNode;

        @JsonIgnore
        private transient final HashMap<String, Object> internalExtensiveReferences = new HashMap<>();


        public final <T> Optional<T> getExtension(String key, Class<? extends T> target) {

            if (rootNode != null) {

                try {

                    T obj = OBJECT_MAPPER.treeToValue(rootNode.at("/" + key), target);

                    if (obj != null) {
                        setExtension(key, obj);
                        return Optional.of(obj);
                    }

                    return Optional.empty();

                } catch (JsonProcessingException ignored) {
                }

            }

            return Optional.empty();

        }

        @SneakyThrows
        public final <T> T getOrCreateExtension(String key, Class<? extends T> target) {

            Optional<T> extension = getExtension(key, target);

            if (!extension.isPresent()) {

                T obj = createInstance(target);

                setExtension(key, obj);

                return obj;

            }

            return extension.get();
        }

        public void setExtension(String key, Object object) {
            internalExtensiveReferences.put(key, object);
        }

        private HashMap<String, Object> getInternalExtensiveReferences() {
            return internalExtensiveReferences;
        }

    }


}
