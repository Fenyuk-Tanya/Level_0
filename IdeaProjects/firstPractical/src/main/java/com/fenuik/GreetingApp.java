package com.fenuik;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class GreetingApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingApp.class);
    private static final String DEFAULT_FORMAT = "json";
    private static final String XML_FORMAT = "xml";
    private static final String PROPERTIES_FILE_NAME = "application.properties";


    public static void main(String[] args) {
        LOGGER.info("The program is running");

        String outputFormat = System.getProperty("outputFormat", DEFAULT_FORMAT);
        LOGGER.debug("Output format is set to: {}", outputFormat);

        String userName = getUserName();

        if (userName == null) {
            LOGGER.error("The user name is null");
            return;
        }

        Message message = new Message("Привіт " + userName + "!");

        try {
            ObjectMapper mapper = new ObjectMapper();
            XmlMapper xmlMapper = new XmlMapper();

            String result;

            if (XML_FORMAT.equalsIgnoreCase(outputFormat)) {
                result = xmlMapper.writeValueAsString(message);
                LOGGER.info("Output in XML format: {}", result);
            } else {
                result = mapper.writeValueAsString(message);
                LOGGER.info("Output in JSON format: {}", result);
            }
            LOGGER.info("The program has ended");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static String getUserName() {
        Properties properties = new Properties();

        try (InputStreamReader reader = new InputStreamReader((Objects.requireNonNull(GreetingApp.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE_NAME))), StandardCharsets.UTF_8)) {

            LOGGER.debug("Attempting to load properties from file '{}' with UTF-8 encoding", PROPERTIES_FILE_NAME);

            properties.load(reader);
            LOGGER.info("Properties loaded successfully from file");

            String username = properties.getProperty("username");

            LOGGER.debug("Username retrieved from properties file: {}", username);

            return username;
        } catch (Exception e) {
            LOGGER.error("An error occurred while reading the properties file: ", e);
            return null;
        }
    }

}
