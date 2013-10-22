package com.ebiznext.sbt.sample.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ebiznext.sbt.sample.reception.vo.UserToken;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Utils {
    private static final int MAX_DESC_LEN = 24;
    private static final Logger LOGGER = Logger
            .getLogger(Utils.class.getName());

    private Utils() {
    }

    public static String soapCall(URL url, String soapData, String soapAction,
            String xPathResult) throws IOException,
            ParserConfigurationException, SAXException,
            XPathExpressionException {
        try {
            final HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "text/xml; charset=UTF-8");
            connection.setRequestProperty("SOAPAction", soapAction);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            final Writer writer = new OutputStreamWriter(
                    connection.getOutputStream());
            writer.write(soapData);
            writer.flush();

            final StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            LOGGER.info(answer.toString());
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(
                    answer.toString())));
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(xPathResult);
            Object result = expr.evaluate(doc, XPathConstants.STRING);
            return result.toString();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (SAXException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (XPathExpressionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }

    }

    public static UserToken decrypt(String message) {
        StringTokenizer st = new StringTokenizer(message, "-");
        ArrayList<BigInteger> idPartToDecrypt = new ArrayList<BigInteger>();
        while (st.hasMoreTokens()) {
            String idpart = st.nextToken();
            idPartToDecrypt.add(new BigInteger(idpart));
        }

        List<BigInteger> idPartDecrypted = decryptRSA(idPartToDecrypt);
        StringBuilder sb = new StringBuilder();
        for (BigInteger bigInteger : idPartDecrypted) {
            String aChar = Character.valueOf((char) bigInteger.intValue())
                    .toString();
            sb.append(aChar);
        }
        final String clear = sb.toString();
        final int index1 = clear.indexOf('|');
        final int index2 = clear.indexOf('|', index1 + 1);
        final int index3 = clear.indexOf('|', index2 + 1);
        final String deviceuuid = clear.substring(0, index1);
        final String service = clear.substring(index1 + 1, index2);
        final String user = clear.substring(index2 + 1, index3);
        final String password = clear.substring(index3 + 1);
        return new UserToken(deviceuuid, service, user, password);
    }

    private static List<BigInteger> decryptRSA(List<BigInteger> c) {
        List<BigInteger> res = new ArrayList<BigInteger>();
        for (int i = 0; i < c.size(); i++) {
            res.add(i,
                    c.get(i).modPow(
                            BigInteger.valueOf(Long.parseLong(Configuration
                                    .getDRSA())),
                            BigInteger.valueOf(Long.parseLong(Configuration
                                    .getNRSA()))));
        }
        return res;
    }

    public static String jsonOutput(Object data)
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mapper.writeValue(bos, data);
        return bos.toString();
    }

    public static String formatMontant(String montant) {
        try {
            double dres = Double.parseDouble(montant);
            // renvoyer au format US quelque soit la langue du serveur
            return new DecimalFormat("#0.00").format(dres).replace(",", ".");
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatDescription(String desc) {
        if (desc.length() > MAX_DESC_LEN) {
            return desc.substring(0, MAX_DESC_LEN);
        } else {
            return desc;
        }
    }

    public static String readFile(File inputFile) throws IOException {
        if (inputFile.length() <= 0L) {
            return "";
        }
        int i = 0;
        byte buffer[] = new byte[(int) inputFile.length()];
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(
                    new FileInputStream(inputFile));
            int j;
            while ((j = inputStream.read()) != -1) {
                buffer[i++] = (byte) j;
            }
            return new String(buffer);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }
}
