package br.dev.brunoxkk0.essenciais.core.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public abstract class DataStore<T> {

    private static final Logger logger = Logger.getLogger("DataStore");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

    private final Path dataStorePath;

    private final ArrayList<T> data;
    private final Class<T> target;

    private final HashMap<T, Path> DATA_PATH_REFERENCE = new HashMap<>();

    public DataStore(Class<T> target, Path path, ArrayList<T> data) {
        this.target = target;
        this.dataStorePath = path;
        this.data = data;
    }

    public void load() throws IOException {

        if (Files.isDirectory(dataStorePath)) {

            File[] files = dataStorePath.toFile().listFiles();

            if (files == null)
                return;

            for (File file : files) {

                JsonNode rawData = OBJECT_MAPPER.readTree(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8));

                if (rawData.isArray()) {

                    for (Iterator<JsonNode> it = rawData.elements(); it.hasNext(); ) {

                        JsonNode node = it.next();

                        T obj = OBJECT_MAPPER.treeToValue(node, target);

                        if (obj instanceof Extensible extensible)
                            extensible.rootNode = node;

                        data.add(obj);
                        DATA_PATH_REFERENCE.put(obj, file.toPath());
                    }

                    return;
                }

                if (rawData.isObject()) {

                    T obj = OBJECT_MAPPER.treeToValue(rawData, target);

                    if (obj instanceof Extensible extensible)
                        extensible.rootNode = rawData;

                    data.add(obj);
                    DATA_PATH_REFERENCE.put(obj, file.toPath());
                }

            }
        }
    }

    public abstract Path calculateFilePath(T item);

    public Path getDataStorePath() {
        return dataStorePath;
    }

    public synchronized void save() throws IOException {

        for(T item : data){
            if(!DATA_PATH_REFERENCE.containsKey(item)){
                DATA_PATH_REFERENCE.put(item, calculateFilePath(item));
            }
        }

        for (Map.Entry<T, Path> item : DATA_PATH_REFERENCE.entrySet()) {

            JsonNode itemNode = OBJECT_MAPPER.valueToTree(item.getKey());
            ObjectNode objectNode = ((ObjectNode) itemNode);

            if (item.getKey() instanceof Extensible extensible) {

                if (extensible.rootNode != null) {

                    JsonNode originalNode = extensible.rootNode;

                    for (Iterator<Map.Entry<String, JsonNode>> iter = originalNode.fields(); iter.hasNext(); ) {
                        Map.Entry<String, JsonNode> nodeItem = iter.next();

                        if (!itemNode.has(nodeItem.getKey())) {
                            objectNode.set(nodeItem.getKey(), nodeItem.getValue());
                        }

                    }
                } else {
                    for (Map.Entry<String, Object> data : extensible.internalExtensiveReferences.entrySet()) {
                        objectNode.set(data.getKey(), OBJECT_MAPPER.valueToTree(data.getValue()));
                    }
                }

            }

            Files.write(item.getValue(), OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode).getBytes(StandardCharsets.UTF_8));

        }
    }

    public static abstract class Extensible {

        @JsonIgnore
        private JsonNode rootNode;

        @JsonIgnore
        private final HashMap<String, Object> internalExtensiveReferences = new HashMap<>();


        public final <T> Optional<T> getExtension(String key, Class<? extends T> target) {

            if (rootNode != null) {
                try {
                    T obj = OBJECT_MAPPER.treeToValue(rootNode.at(key), target);

                    if (obj != null) {
                        internalExtensiveReferences.put(key, obj);
                        return Optional.of(obj);
                    }

                    return Optional.empty();
                } catch (JsonProcessingException ignored) {
                }

            }

            return Optional.empty();

        }

        public final <T> T getOrCreateExtension(String key, Class<? extends T> target, Path path) {

            Optional<T> extension = getExtension(key, target);

            if (!extension.isPresent()) {
                try {
                    T obj = target.getDeclaredConstructor().newInstance();
                    internalExtensiveReferences.put(key, obj);
                    return obj;
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         NoSuchMethodException e) {
                    throw new IllegalArgumentException("The class " + target.getName() + " don't have a default constructor declared.");
                }
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
