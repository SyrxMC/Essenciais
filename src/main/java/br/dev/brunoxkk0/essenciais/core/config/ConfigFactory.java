package br.dev.brunoxkk0.essenciais.core.config;

import br.dev.brunoxkk0.essenciais.core.data.DataStore;
import br.dev.brunoxkk0.syrxmccore.libs.com.electronwill.nightconfig.core.conversion.ObjectConverter;
import br.dev.brunoxkk0.syrxmccore.libs.com.electronwill.nightconfig.core.conversion.Path;
import br.dev.brunoxkk0.syrxmccore.libs.com.electronwill.nightconfig.core.file.CommentedFileConfig;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Logger;

public class ConfigFactory {

    private static final Logger logger = Logger.getLogger("ConfigFactory");

    @SneakyThrows
    public static <T extends PathConfig> T loadConfig(java.nio.file.Path path, Class<T> config) {

        File file = path.toFile();

        if (!file.getParentFile().exists()) {
            if (file.getParentFile().mkdirs())
                logger.info("Created folder at " + file.getParentFile().getPath());
        }

        if (!file.exists()) {
            if (file.createNewFile())
                logger.info("Created config file at " + file.getPath());
        }

        ObjectConverter objectConverter = new ObjectConverter();

        try (CommentedFileConfig fileConfig = CommentedFileConfig.ofConcurrent(file)) {

            fileConfig.load();

            T obj = DataStore.Extensible.createInstance(config);

            if (fileConfig.isEmpty()) {

                ((PathConfig) obj).configPath = path;
                ((PathConfig) obj).config = fileConfig;

                objectConverter.toConfig(obj, fileConfig);
                syncComments(fileConfig, config);
                fileConfig.save();

                return obj;
            }

            objectConverter.toObject(fileConfig, obj);

            return obj;

        }

    }

    private static void syncComments(CommentedFileConfig config, Class<?> source) {

        for (Field field : source.getDeclaredFields()) {
            if (field.isAnnotationPresent(Path.class) && field.isAnnotationPresent(Comment.class)) {

                Path path = field.getAnnotation(Path.class);
                Comment comment = field.getAnnotation(Comment.class);

                if (!comment.value().isEmpty()) {
                    config.setComment(path.value(), comment.value());
                }

            }
        }

    }


    public static abstract class PathConfig {

        private transient java.nio.file.Path configPath;
        private transient CommentedFileConfig config;

        public java.nio.file.Path getConfigPath() {
            return configPath;
        }

        public CommentedFileConfig getConfig() {
            return config;
        }
    }


}
