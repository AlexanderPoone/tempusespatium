/*
 * The MIT License
 *
 * Copyright 2018 Karima Rafes.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hk.edu.cuhk.cse.tempusespatium;

import android.content.Context;
import android.util.Log;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Karima Rafes.
 */

public class SparqlClient {

    private String _tag = "com.bordercloud.sparqlandroid";

    /**
     * URL of Endpoint to read
     *
     * @access private
     * @var String
     */
    private String _endpointRead;

    /**
     * URL  sparql to write
     *
     * @access private
     * @var String
     */
    private String _endpointWrite;

    /**
     * in the constructor set the proxy_host if necessary
     *
     * @access private
     * @var String
     */
    private String _proxy_host;

    /**
     * in the constructor set the proxy_port if necessary
     *
     * @access private
     * @var int
     */
    private int _proxy_port;

    /**
     * Parser of XML result
     *
     * @access private
     * @var ParserSparqlResult
     */
    private String _parserSparqlResult;

    /**
     * Name of parameter HTTP to send a query SPARQL to read data.
     *
     * @access private
     * @var String
     */
    private String _nameParameterQueryRead;

    /**
     * Name of parameter HTTP to send a query SPARQL to write data.
     *
     * @access private
     * @var String
     */
    private String _nameParameterQueryWrite;

    /**
     * Method HTTP to send a query SPARQL to read data.
     *
     * @access private
     * @var String
     */
    private String _methodHTTPRead;
    private String _methodHTTPWrite;

    private String _login;
    private String _password;

    private SAXParser _parser;
    private DefaultHandler _handler;
    private String _response;

    public SparqlClient() {

        _proxy_host = null;
        _proxy_port = 0;

        _methodHTTPRead = "POST";
        _methodHTTPWrite = "POST";
        _nameParameterQueryRead = "query";
        _nameParameterQueryWrite = "update";

        // Init Sax class
        SAXParserFactory parserSPARQL = SAXParserFactory.newInstance();
        _parser = null;

        try {
            _parser = parserSPARQL.newSAXParser();
        } catch (ParserConfigurationException e) {
            log(_tag, e.getMessage(), e);
        } catch (SAXException e) {
            log(_tag, e.getMessage(), e);
        }

        if (_proxy_host != null && _proxy_port != 0) {
            //todo
        } else {
            //todo
        }
    }

    /**
     * Get the server login
     *
     * @return String password : server password
     * @access public
     */
    public String getPassword() {
        return _password;
    }

    /**
     * Set the server password
     *
     * @param password : server password
     * @access public
     */
    public void setPassword(String password) {
        _password = password;
    }

    /**
     * Get the server login
     *
     * @return String login : server login
     * @access public
     */
    public String getLogin() {
        return _login;
    }

    /**
     * Set the server login
     *
     * @param login : server login
     * @access public
     */
    public void setLogin(String login) {
        _login = login;
    }

    public String getResponse() {
        return _response;
    }

    /**
     * Get the method HTTP to read
     */
    public String getMethodHTTPRead() {
        return this._methodHTTPRead;
    }

    /**
     * Set the method HTTP to read
     *
     * @param method : HTTP method (GET or POST) for reading data (by default is POST)
     * @access public
     */
    public void setMethodHTTPRead(String method) {
        this._methodHTTPRead = method;
    }

    /**
     * Get the method HTTP to write
     */
    public String getMethodHTTPWrite() {
        return this._methodHTTPWrite;
    }

    /**
     * Set the method HTTP to write
     *
     * @param method : HTTP method (GET or POST) for writing data (by default is POST)
     * @access public
     */
    public void setMethodHTTPWrite(String method) {
        this._methodHTTPWrite = method;
    }

    /**
     * Get the url to read
     *
     * @return String url : endpoint's url to read
     * @access public
     */
    public String getEndpointRead() {
        return this._endpointRead;
    }

    /**
     * Set the url to read
     *
     * @param url : endpoint's url to read
     * @access public
     */
    public void setEndpointRead(String url) {
        // FIX for Wikidata
        if (url == "https://query.wikidata.org/sparql") {
            this._methodHTTPRead = "GET";
        }
        this._endpointRead = url;
    }

    /**
     * Get the url to write
     *
     * @return String url : endpoint's url to write
     * @access public
     */
    public String getEndpointWrite() {
        return this._endpointWrite;
    }

    /**
     * Set the url to write
     *
     * @param url : endpoint's url to write
     * @access public
     */
    public void setEndpointWrite(String url) {
        this._endpointWrite = url;
    }

    /**
     * Get the parameter in the query to write
     *
     * @return string name : name of parameter
     * @access public
     */
    public String getNameParameterQueryWrite() {
        return this._nameParameterQueryWrite;
    }

    /**
     * Set the parameter in the query to write
     *
     * @param name : name of parameter
     * @access public
     */
    public void setNameParameterQueryWrite(String name) {
        this._nameParameterQueryWrite = name;
    }

    /**
     * Get the parameter in the query to read
     *
     * @return string name : name of parameter
     * @access public
     */
    public String getNameParameterQueryRead() {
        return this._nameParameterQueryRead;
    }

    /**
     * Set the parameter in the query to read
     *
     * @param name : name of parameter
     * @access public
     */
    public void setNameParameterQueryRead(String name) {
        this._nameParameterQueryRead = name;
    }

    public HashMap<String, HashMap> query(
            String query,
            Context context,
            String filenameCache)
            throws SparqlClientException {
        HashMap<String, HashMap> result = null;
        _handler = null;
        _response = null;
        String param = "";
        String endpoint = "";
        String queryLowerCase = query.toLowerCase();
        if (query != null) {
            if (queryLowerCase.indexOf("insert") > -1 ||
                    queryLowerCase.indexOf("delete") > -1 ||
                    queryLowerCase.indexOf("clear") > -1) {
                param = _nameParameterQueryWrite;
                endpoint = _endpointWrite;
            } else {
                param = _nameParameterQueryRead;
                endpoint = _endpointRead;
            }

//            if (_methodHTTPRead.equalsIgnoreCase("POST")) {
//                if (_login != null && _password != null) {
//                    // TODO
//                    //return sendQueryPOSTwithAuth(_endpoint, param, query, _login, _password);
//                } else {
//                    // TODO
//                    //return sendQueryPOST(_endpoint, param, query);
//                }
//            } else {
            result = sendQueryGET(endpoint, param, query, context, filenameCache);
//            }
        }
        return result;
    }

    private HashMap<String, HashMap> sendQueryGET(
            String urlStr,
            String parameter,
            String query,
            Context context,
            String filenameCache)
            throws SparqlClientException {
        try {
            String urlString = urlStr + "?" + parameter + "=" + URLEncoder.encode(query, "UTF-8");

            Log.d("SPARQL", urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/sparql-results+xml; charset=UTF-8");
            conn.connect();
            Log.d("asdfasdf", Integer.toString(conn.getResponseCode()));
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                //_context.deleteFile(_filenameCache);
                FileOutputStream fos = context.openFileOutput(filenameCache, Context.MODE_PRIVATE);

                int read = 0;
                byte[] buffer = new byte[1024];

                while ((read = in.read(buffer, 0, 1024)) != -1) {
                    fos.write(buffer, 0, read);
                }

                fos.close();
            }
            //log(_tag, "CACHE " + _filenameCache);
            FileInputStream file = null;
            try {
                file = context.openFileInput(filenameCache);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //in = new BufferedReader(input);
            _handler = new ParserSPARQLResultHandler();
            _parser.parse(file, _handler);
            file.close();

        } catch (SAXException e) {
            log(_tag, e.getMessage(), e);
            //e.printStackTrace();
        } catch (IOException e) {
            log(_tag, e.getMessage(), e);
            //e.printStackTrace();
            Log.e("asdfasdf", e.toString());

        } catch (Exception e) {
            log(_tag, e.getMessage(), e);
            Log.e("asdfasdf", e.toString());
        }
        if (_handler != null) {
            return ((ParserSPARQLResultHandler) _handler).getResult();//new HashMap<String, HashMap>();
        } else {
            return null;
        }
    }

    private void log(String tag, String message, Exception e) {
        if (message != null) {
            Log.e(_tag, message);
        }
    }

/*
        todo
      URL myURL = new URL(serviceURL);
      HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
      String userCredentials = "username:password";
      String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
      myURLConnection.setRequestProperty ("Authorization", basicAuth);
      myURLConnection.setRequestMethod("POST");
      myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      myURLConnection.setRequestProperty("Content-Length", "" + postData.getBytes().length);
      myURLConnection.setRequestProperty("Content-Language", "en-US");
      myURLConnection.setUseCaches(false);
      myURLConnection.setDoInput(true);
      myURLConnection.setDoOutput(true);
*/
/*

  private HashMap<String, HashMap> sendQueryPOSTwithAuth(
    String urlStr, String parameter, String query,
    String login, String password)
    throws EndpointException
    {

    int statusCode=0;
    try {
      CredentialsProvider credsProvider = new BasicCredentialsProvider();
      credsProvider.setCredentials(
        new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
        new UsernamePasswordCredentials(login, password));
      CloseableHttpClient httpclient = HttpClients.custom()
        .setDefaultCredentialsProvider(credsProvider)
        .build();
      try {
        HttpPost httpPost = new HttpPost(urlStr);
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair(parameter, query));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
          //System.out.println(response2.getStatusLine());
          statusCode = response2.getStatusLine().getStatusCode() ;
          if ( statusCode < 200 || statusCode >= 300) {
            throw new EndpointException(this, response2.getStatusLine().toString());
          }
          HttpEntity entity2 = response2.getEntity();
          // do something useful with the response body
          // and ensure it is fully consumed
          ////System.out.println(EntityUtils.toString(entity2));

          _response = EntityUtils.toString(entity2);
          //EntityUtils.consume(entity2);
        }
        finally {
          response2.close();
        }
      }
      finally {
        httpclient.close();
      }
    }
    catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    return getResult();

  }

  private HashMap<String, HashMap> sendQueryPOST(String urlStr, String parameter, String query)
    throws EndpointException
  {
    //URL url = null;
    //int port = 0;
    int statusCode=0;
    try {
      // url = new URL(urlStr);

      //_endpointHost = url.getHost();
      //port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort() ;
      // println(_endpointPort);
      // _clientHTTP = new Client(parent, _endpointHost, _endpointPort);

      CloseableHttpClient httpclient = HttpClients.custom()
        .build();
      try {
        HttpPost httpPost = new HttpPost(urlStr);
        httpPost.setHeader("Content-Type", "application/sparql-results+xml; charset=UTF-8");
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair(parameter, query));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);


        try {
          //System.out.println(response2.getStatusLine());
          statusCode = response2.getStatusLine().getStatusCode() ;
          if ( statusCode < 200 || statusCode >= 300) {
            throw new EndpointException(this, response2.getStatusLine().toString());
          }

          HttpEntity entity2 = response2.getEntity();
          // do something useful with the response body
          // and ensure it is fully consumed
          //System.out.println(EntityUtils.toString(entity2));
          _response = EntityUtils.toString(entity2);
          //EntityUtils.consume(entity2);
        }
        finally {
          response2.close();
        }
      }
      finally {
        httpclient.close();
      }
    }
    catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    return getResult();
  }
  */
}

