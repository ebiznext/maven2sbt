/**
 * 
 */
package com.ebiznext.http.client

import groovy.json.JsonSlurper
import groovy.util.slurpersupport.GPathResult

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.logging.Logger

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

import org.cyberneko.html.parsers.SAXParser
import org.xml.sax.SAXParseException

/**
 * @author stephane.manciot@ebiznext.com
 *
 */
final class HTTPClient 
{

	static final String DEFAULT_CHARSET = 'utf-8'

	private static final Logger logger = Logger.getLogger(HTTPClient.class.getName())

	private static HTTPClient client

	static
	{
		installOpenSSLTrustManager();
	}

	private HTTPClient()
	{
		// singleton
	}

	static HTTPClient getInstance()
	{
		if(client == null)
		{
			client = new HTTPClient()
		}		
		return client
	}

	HttpURLConnection doGet(Map config = [:], String url, Map<String, String> params = null, boolean follow = true){
        long before = System.currentTimeMillis()
        try{
    		String charset = config['charset'] ? config['charset'] : DEFAULT_CHARSET
    		if(params){
    			if(url.indexOf('?') < 0){
    				url += '?'
    			}
    			boolean first = true
    			params.keySet().each { key ->
    				String value = params.get(key)
    				// if(!value) value=''
    				if(value){
    					url += (first ? '' : '&') + key + '=' + URLEncoder.encode(value, charset)
    					if(first){
    						first = false
    					}
    				}
    			}
    		}
    		HttpURLConnection conn = openConnection(config, url)
    		String method = 'GET'
    		conn.requestMethod = method
    		conn.setDoOutput(false)
    		conn.setRequestProperty("Accept-Charset", charset)
    
            outputHeaders(config, conn)
    
    		if(conn.responseCode == 302 || conn.responseCode == 301) {
    			if(follow){
    				String location = conn.headerFields['Location']?.iterator().next()
    				logger.info('Perform redirection to -> '  + location)
    				return doGet(config, location, null)
    			}
    			else{
    				logger.info('Redirection not handled')
    			}
    		}
    		return conn
        }
        finally{
			boolean debug = config['debug'] ? true : false
            if(debug){
                logger.info('Perform GET request to ' + url + ' within ' + (System.currentTimeMillis() - before) + ' ms')
            }
        }
	}

	HttpURLConnection doPost(Map config = [:], String url, Map<String, String> params = null, String body = '', boolean follow = true){
        long before = System.currentTimeMillis()
        try{
    		String charset = config['charset'] ? config['charset'] : DEFAULT_CHARSET
    		boolean first = true
    		if(params){
    			params?.keySet().each { key ->
    				def value = params.get(key)
    				// if(!value) value=''
    				if(value){
    					body += (first ? '' : '&') + key + '=' + URLEncoder.encode(value, charset)
    					if(first){
    						first = false
    					}
    				}
    			}
    		}
    		HttpURLConnection conn = openConnection(config, url)
    		conn.setDoOutput(true); // Triggers POST.
    		String method = 'POST'
    		conn.requestMethod = method
    		conn.setRequestProperty('Accept-Charset', charset)
    		if(params && !params.isEmpty()){
    			conn.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')
    		}
    		int len = body.getBytes().length
    		conn.setRequestProperty('Content-Length', '' + len)
    		conn.setRequestProperty('Length', '' + len)
    
            outputHeaders(config, conn)
    
    		def o = conn.outputStream
    		o.write(body.getBytes(charset))
    		o.flush()
    		o.close()
    		if(conn.responseCode == 302 || conn.responseCode == 301) {
    			if(follow){
    				String location = conn.headerFields['Location']?.iterator().next()
    				logger.info('Perform redirection to -> '  + location)
    				return doPost(config, location, null)
    			}
    			else{
    				logger.info('Redirection not handled')
    			}
    		}
    		return conn
        }
        finally{
			boolean debug = config.isDebug() ? true : false
            if(debug){
                logger.info('POST request to ' + url + ' within ' + (System.currentTimeMillis() - before) + ' ms')
            }
        }
	}

	HttpURLConnection doDelete(Map config = [:], String url, Map<String, String> params = null, boolean follow = true){
        long before = System.currentTimeMillis()
        try{
    		String charset = config.charset ? config.charset : DEFAULT_CHARSET
    		if(params){
    			if(url.indexOf('?') < 0){
    				url += '?'
    			}
    			boolean first = true
    			params.keySet().each { key ->
    				String value = params.get(key)
    				// if(!value) value=''
    				if(value){
    					url += (first ? '' : '&') + key + '=' + URLEncoder.encode(value, charset)
    					if(first){
    						first = false
    					}
    				}
    			}
    		}
    		HttpURLConnection conn = openConnection(config, url)
    		conn.setDoOutput(false)
    		String method = 'DELETE'
    		conn.requestMethod = method
    		conn.setRequestProperty('Accept-Charset', charset)
    
            outputHeaders(config, conn)
    
    		if(conn.responseCode == 302 || conn.responseCode == 301) {
    			if(follow){
    				def location = conn.headerFields['Location']?.iterator().next()
    				logger.info('Perform redirection to -> '  + location)
    				return doPost(config, location, null)
    			}
    			else{
    				logger.info('Redirection not handled')
    			}
    		}
    		return conn
        }
        finally{
			boolean debug = config['debug'] ? true : false
            if(debug){
                logger.info('DELETE request to ' + url + ' within ' + (System.currentTimeMillis() - before) + ' ms')
            }
        }
	}

	HttpURLConnection doPut(Map config = [:], String url, Map<String, String> params = null, String content = '', boolean follow = true){
        long before = System.currentTimeMillis()
        try{
    		String charset = config.charset ? config.charset : DEFAULT_CHARSET
    		boolean first = true
    		if(params){
    			params?.keySet().each { key ->
    				String value = params.get(key)
    				// if(!value) value=''
    				if(value){
    					content += (first ? '' : '&') + key + '=' + URLEncoder.encode(value, charset)
    					if(first){
    						first = false
    					}
    				}
    			}
    		}
    		HttpURLConnection conn = openConnection(config, url)
    		conn.setDoOutput(true)
    		String method = 'PUT'
    		conn.requestMethod = method
    		conn.setRequestProperty('Accept-Charset', charset)
    		byte[] bytes = content.getBytes(charset)
    		int len = bytes.length
    		conn.setRequestProperty('Content-Length', '' + len)
    		conn.setRequestProperty('Length', '' + len)
    
            outputHeaders(config, conn)
    
    		OutputStream o = conn.outputStream
    		o.write(bytes)
    		o.flush()
    		o.close()
    		if(conn.responseCode == 302 || conn.responseCode == 301) {
    			if(follow){
    				String location = conn.headerFields['Location']?.iterator().next()
    				logger.info('Perform redirection to -> '  + location)
    				return doPost(config, location, null)
    			}
    			else{
    				logger.info('Redirection not handled')
    			}
    		}
    		return conn
        }
        finally{
			boolean debug = config['debug'] ? true : false
            if(debug){
                logger.info('PUT request to ' + url + ' within ' + (System.currentTimeMillis() - before) + ' ms')
            }
        }
	}

	def String getText(Map config = [:], HttpURLConnection conn){
		String charset = config['charset']
		String text = conn.responseCode >= 400 ? conn.errorStream.getText(charset ? charset : DEFAULT_CHARSET) :
		conn.content.getText(charset ? charset : DEFAULT_CHARSET)
		boolean debug = config['debug'] ? true : false
        if(debug){
			logger.info(text)
		}
		return text
	}

	def GPathResult parseTextAsXML(Map config = [:], HttpURLConnection conn, SAXParser parser = null)
	{
		def text = getText(config, conn)
		def slurper = parser ? new XmlSlurper(parser) : new XmlSlurper()
		try{
			return slurper.parseText( text )
		}
		catch(SAXParseException e){
			logger.info(text)
			return null
		}
	}

	def Object parseTextAsJSON(Map config = [:], HttpURLConnection conn)
	{
		def text = getText(config, conn)
		def slurper = new JsonSlurper()
		try{
			return slurper.parseText( text )
		}
		catch(SAXParseException e){
			logger.info(text)
			return null
		}
	}

	def SAXParser getHtmlParser(Map config = [:])
	{
		def parser = new SAXParser()
		String charset = config['charset']
		parser.setProperty('http://cyberneko.org/html/properties/default-encoding', charset ? charset : DEFAULT_CHARSET)
		return parser
	}

	private HttpURLConnection openConnection(Map config = [:], String url)
	{
		def conn = null
		logger.info ('Open connection to -> ' + url)
		String proxyHost = config['proxyHost']
		String proxyPort = config['proxyPort']
		String proxyUser = config['proxyUser']
		String proxyPass = config['proxyPass']
		if (proxyHost != null
		&& proxyHost.trim().length() > 0
		&& proxyPort != null
		&& proxyPort.trim().length() > 0)
		{
			InetSocketAddress socket = InetSocketAddress.createUnresolved(proxyHost, Integer.parseInt(proxyPort))
			java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, socket)
			conn = url.toURL().openConnection(proxy)
			if (proxyUser != null && proxyUser.trim().length() > 0)
			{
				AuthenticatorSelector.setProxyAuth(proxyHost, proxyUser, proxyPass)
			}
		}
		else{
			conn = url.toURL().openConnection()
		}
		return conn
	}

    private closeConnection(HttpURLConnection conn)
	{
        try{
            conn?.inputStream?.close()
            conn?.outputStream?.close()
            conn?.disconnect()
        }
        catch(IOException io){
            // rien à faire
        }
    }

	private outputHeaders(Map config = [:], HttpURLConnection conn) {
		def debug = config['debug'] ? true : false
        if(debug)
		{
			logger.info('Request Headers -> {')
			Map<String, List<String>> requestProperties = conn.requestProperties
			requestProperties?.keySet().each {String key ->
				logger.info('\t' + key + ': ' + requestProperties.get(key).get(0))
			}
			logger.info('}')
		}
	}

	/**
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static void installOpenSSLTrustManager() throws NoSuchAlgorithmException,
	KeyManagementException
	{
		def trustManager = new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers()
					{
						return new X509Certificate[0]
					}

					public void checkClientTrusted(X509Certificate[] certs, String authType)
					{
					}

					public void checkServerTrusted(X509Certificate[] certs, String authType)
					{
					}
				}

		TrustManager[] trustAllCerts = new TrustManager[1]
		trustAllCerts[0] = trustManager

		HostnameVerifier hv = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session)
					{
						return true
					}
				}

		SSLContext sc = SSLContext.getInstance('SSL')
		sc.init(null, trustAllCerts, new SecureRandom())
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
		HttpsURLConnection.setDefaultHostnameVerifier(hv)
	}

}

/**
 * @version $Id $
 *
 * @author stephane.manciot@ebiznext.com
 *
 */
class UserPassword
{
	final String user;
	final String pass;
	
	UserPassword(String user, String pass)
	{
		this.user = user;
		this.pass = pass;
	}
}
	
/**
 * @version $Id $
 *
 * @author stephane.manciot@ebiznext.com
 *
 */
class AuthenticatorSelector extends Authenticator
{

	private static Map < String, UserPassword > proxies =
															new HashMap < String, UserPassword >();

	synchronized public static void setProxyAuth(String proxyHost, String proxyUser,
		String proxyPass)
	{
		if (proxyUser == null)
		{
			proxies.remove(proxyHost);
		}
		else
		{
			if (proxies.size() == 0)
			{
				Authenticator.setDefault(new AuthenticatorSelector());
			}
			proxies.put(proxyHost, new UserPassword(proxyUser, proxyPass));
		}
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		String proxy = this.getRequestingHost();
		UserPassword up = proxies.get(proxy);
		if (up != null)
		{
			logger.info("**************" + proxy + "**********************");
			logger.info(up.user + ":" + up.pass);
			logger.info("**************" + proxy + "**********************");
			return new PasswordAuthentication(up.user, up.pass.toCharArray());
		}
		else
		{
			return null;
		}
	}
}

