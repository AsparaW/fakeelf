package com.mantouland.atool;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mantouland.atool.callback.impl.JsonCallBack;
import com.mantouland.atool.callback.impl.NetCallBack;
import com.mantouland.atool.callback.impl.PicCallBack;
import com.mantouland.atool.callback.impl.StringCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;



/**
 * Created by asparaw on 2019/3/27.
 */

public class HTTPHelper {
    private static final String TAG ="DEBUG HTTPHELPER:";
    private static final String ERROR_MESSAGE = "http error response:";
    private static final int DEFAULT=0;
    private static final int DEFAULT_WITH_LINES=1;
    private static final int BIG=1;
    private static final int BIGGER=2;
    private static final String _POST="POST";
    private static final String _GET="GET";
    private static String defaultContentEncoding ;

    private HTTPHelper(){
        defaultContentEncoding = Charset.defaultCharset().name();
        //do nothing
    }

    private static class instanceHolder{
        private static final HTTPHelper instance=new HTTPHelper();
    }


    public static HTTPHelper getInstance(){
        return instanceHolder.instance;
    }


    public void doPic(final String urlString, final PicCallBack picCallBack) {
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                    Log.d(TAG, "run: trypic"+urlString);
                    picCallBack.onSuccess(sendPPic(urlString));
            }
        });

    }
    public Bitmap doPic(final String urlString, int type) {
        try {
            return sendPic(urlString,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void doJsonString(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine, final JsonCallBack callBack, final Class clazz){
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: "+"STARTJSON");
                    HTTPContent httpContent =send(urlString,_POST,parameters,properties,withLine);
                    Log.d(TAG, "run: "+"OKSEND");
                    String string=httpContent.getContent();
                    string=string.replaceAll("\"rt\":null,","");
                    string=string.replaceAll("\"h\":null,","");
                    Log.d(TAG, "run: length"+string.length());
                    Object beanObject=Ason.fromJson(string,clazz);
                    callBack.onSuccess(beanObject);
                } catch (IOException e) {
                    callBack.onFail(e);
                }
            }
        });
    }

    public void doString(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine,final StringCallBack callBack){
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HTTPContent httpContent =send(urlString,_POST,parameters,properties,withLine);
                    String string=httpContent.getContent();
                    callBack.onSuccess(string);
                } catch (IOException e) {
                    callBack.onFail(e);
                }
            }
        });
    }
    public void doGet(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine,final NetCallBack callBack){
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HTTPContent httpContent =send(urlString,_GET,parameters,properties,withLine);
                    callBack.onSuccess(httpContent);
                } catch (IOException e) {
                    callBack.onFail(e);
                }
            }
        });
    }

    public void doPost(final String urlString, final Map<String,String> parameters, final Map<String,String> properties, final boolean withLine,final NetCallBack callBack){
            AExecutorPool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        HTTPContent httpContent =send(urlString,_POST,parameters,properties,withLine);
                        callBack.onSuccess(httpContent);
                    } catch (IOException e) {
                        callBack.onFail(e);
                    }
                }
            });
    }

    private Bitmap sendPPic(String url){
            try {

                Log.d("PPIC", "picRunner: "+url);
                URL u=new URL(url);
                InputStream inputStream=u.openStream();
                Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

    }
    private Bitmap sendPic(String urlString, int type) throws IOException {
                URL url;
                HttpURLConnection httpURLConnection;
                    url = new URL(urlString);
                    if(urlString.contains("https"))
                        httpURLConnection = (HttpsURLConnection) url.openConnection();
                    else
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setDoInput(true);
                    return makePicContent(urlString,httpURLConnection,type);
    }
    private HTTPContent send(String urlString, String type, Map<String,String> parameters, Map<String,String> properties,boolean withLine)throws IOException{
        HttpURLConnection httpURLConnection;
        /***
         * inbox get paras
         */
        if (type.equalsIgnoreCase(_GET)&&parameters!=null){
            if (!parameters.isEmpty()){
                StringBuilder tempUrlString=new StringBuilder(urlString);
                urlString= tempUrlString.append("?").append(paramBuilder(parameters)).toString();
            }
        }
        /***
         * set http request body
         */
        URL url=new URL(urlString);
        if(urlString.contains("https"))
            httpURLConnection = (HttpsURLConnection) url.openConnection();
        else
            httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);

        httpURLConnection.setRequestMethod(type);
        if (properties!=null){
            for (String key:properties.keySet()){
                httpURLConnection.addRequestProperty(key,properties.get(key));
            }
        }
        /***
         * inbox post paras
         */
        if (type.equalsIgnoreCase(_POST) && parameters != null) {
            httpURLConnection.setDoInput(true);//make post-able
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.getOutputStream().write(paramBuilder(parameters).getBytes());
            httpURLConnection.getOutputStream().flush();
            httpURLConnection.getOutputStream().close();
        }
        return makeContent(urlString,httpURLConnection,withLine);
    }

    private String paramBuilder(Map<String,String> parameters){
        StringBuilder param = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            param.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        param.deleteCharAt(param.length()-1);
        return param.toString();
    }

    private Bitmap makePicContent(String urlString,HttpURLConnection httpURLConnection,int type){
        try {
            InputStream inputStream= httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode()==200){
                httpURLConnection.disconnect();
                return BitmapFactory.decodeStream(inputStream);
            }else {
                httpURLConnection.disconnect();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
        return null;
    }
    private HTTPContent makeContent(String urlString, HttpURLConnection httpURLConnection, boolean withLine){
        HTTPContent httpContent =new HTTPContent();
        BufferedReader bf;
        String readLine;
        boolean fail=false;
        try {
            StringBuffer temp = new StringBuffer();
            int response = httpURLConnection.getResponseCode();
            if (response == 200) {
                bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                if (withLine){
                    while ((readLine = bf.readLine()) != null) {
                        temp = temp .append(readLine).append("\r\n");
                    }
                }else{
                    while ((readLine = bf.readLine()) != null) {
                        temp = temp .append(readLine);
                    }
                }
            } else {
                fail=true;
                temp.append(ERROR_MESSAGE).append(response);
            }
            //set content

            String pageEncod = httpURLConnection.getContentEncoding();
            if (pageEncod == null)
                pageEncod = defaultContentEncoding;
            httpContent.urlString = urlString;
            httpContent.defaultPort = httpURLConnection.getURL().getDefaultPort();
            httpContent.file = httpURLConnection.getURL().getFile();
            httpContent.host = httpURLConnection.getURL().getHost();
            httpContent.path = httpURLConnection.getURL().getPath();
            httpContent.port = httpURLConnection.getURL().getPort();
            httpContent.protocol = httpURLConnection.getURL().getProtocol();
            httpContent.query = httpURLConnection.getURL().getQuery();
            httpContent.ref = httpURLConnection.getURL().getRef();
            httpContent.userInfo = httpURLConnection.getURL().getUserInfo();
            httpContent.content = new String(temp.toString().getBytes(), pageEncod);
            httpContent.contentEncoding = pageEncod;
            httpContent.code = httpURLConnection.getResponseCode();
            httpContent.message = httpURLConnection.getResponseMessage();
            httpContent.contentType = httpURLConnection.getContentType();
            httpContent.method = httpURLConnection.getRequestMethod();
            httpContent.connectTimeout = httpURLConnection.getConnectTimeout();
            httpContent.readTimeout = httpURLConnection.getReadTimeout();
            return httpContent;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
        return null;
    }


    /***
     *
     */
    public class HTTPContent {
        String urlString;
        int defaultPort;
        String file;
        String host;
        String path;
        int port;
        String protocol;
        String query;
        String ref;
        String userInfo;
        String contentEncoding;
        String content;
        String contentType;
        int code;
        String message;
        String method;
        int connectTimeout;
        int readTimeout;
        Vector<String> contentCollection;


        public String getContent() {
            return content;
        }

        public String getContentType() {
            return contentType;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Vector<String> getContentCollection() {
            return contentCollection;
        }

        public String getContentEncoding() {
            return contentEncoding;
        }

        public String getMethod() {
            return method;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public String getUrlString() {
            return urlString;
        }

        public int getDefaultPort() {
            return defaultPort;
        }

        public String getFile() {
            return file;
        }

        public String getHost() {
            return host;
        }

        public String getPath() {
            return path;
        }

        public int getPort() {
            return port;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getQuery() {
            return query;
        }

        public String getRef() {
            return ref;
        }

        public String getUserInfo() {
            return userInfo;
        }
    }

}

