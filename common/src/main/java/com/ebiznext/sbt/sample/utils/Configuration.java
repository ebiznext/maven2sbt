package com.ebiznext.sbt.sample.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Configuration {
    private static final Logger LOGGER = Logger.getLogger(Configuration.class
            .getName());
    protected static final Properties PROPERTIES = new Properties();

    private Configuration() {
    }

    protected static String getProperty(String key) {
        FileInputStream fis = null;
        try {
            if (PROPERTIES.isEmpty()) {
                fis = new FileInputStream(new File(System
                        .getProperty("sample.properties")));
                PROPERTIES.load(fis);
                @SuppressWarnings("rawtypes")
                Enumeration enu = PROPERTIES.keys();
                while (enu.hasMoreElements()) {
                    String k = (String) enu.nextElement();
                    LOGGER.info(k + "=" + PROPERTIES.getProperty(key));
                }
            }
            return PROPERTIES.getProperty(key);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        return "";
    }

    public static String getDRSA() {
        return getProperty("rsa.d");
    }

    public static String getNRSA() {
        return getProperty("rsa.n");
    }

    public static String getERSA() {
        return getProperty("rsa.e");
    }

    public static int getMaxDescLen() {
        return Integer.parseInt(getProperty("masdesclen"));
    }

    public static int getMaxListLen() {
        return Integer.parseInt(getProperty("maxListLen"));
    }

    public static URL getWSDL(String name) throws MalformedURLException {
        String value = getProperty(name);
        if (value.startsWith("http")) {
            return new URL(value);
        }
        return new File(value).toURI().toURL();
    }
}
