package it.giuggi.iotremote.net;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Config;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class WebRequestTask extends AsyncTask<WebRequestTask.WebRequest, Integer, WebRequestTask.WebRequest[]> {

    //public static final String WEBSITE = "http://mbp-di-federico:5000";
    public static final String WEBSITE = "http://192.168.43.132:5000";
    public static final String POST = "POST";
    public static final String GET = "GET";

    public static final String DATA = "post_data";
    private static final String OK = "ok";
    private static final String ERROR = "error";

    public enum Tipo{ JSON, STRING, ESITO, JSON_ARRAY, LONG }
    public enum Azione{ SEND_GCM_ID, LOGOUT_GCM, GET_NODE_LIST }

    private String scripts[] = new String[]
            {
                    "/gcm/registration",
                    "/gcm/logout",
                    "/node/list"
            };

    /**
     * Default FailureHandler for WebRequests
     * only retries the request
     */
    private static FailureHandler defaultFailureHandler = new FailureHandler()
    {
        /**
         * onFailure(WebRequest request) default handler which retries the request
         * @param request request which has failed
         * @return true
         */
        @Override
        public boolean onFailure(WebRequest request)
        {
            request.retry();
            return true;
        }
    };

    /**
     * FailureHandler for WebRequests
     *
     */
    public interface FailureHandler
    {

        /**
         * onFailure(WebRequest request) default handler which retries the request
         * @param request request which has failed
         * @return true if the error has been handled, else false
         */
        public boolean onFailure(WebRequest request);
    }

    public enum EsitoConnessione{
        OK, UTENTE_MANCANTE, VALORI_MANCANTI, ERRORE, DATI_ERRATI, UTENTE_ESISTE
    };

    public class RisultatoConnessione {

        public Object esito;
        public Tipo tipo;
        public Object[] datiIniziali;
        public OnResponseListener listener;
        public int http_status;

        public RisultatoConnessione(OnResponseListener listener, Object esito, Tipo t, Object... datiIniziali) {
            this.listener = listener;
            this.esito = esito;
            this.tipo = t;
            this.datiIniziali = datiIniziali;

        }

    }

    public static WebRequest perform(Azione action)
    {
        WebRequest newrequest = new WebRequest();
        newrequest.azione = action;
        return newrequest;
    }

    /**
     * Handles all kind of WebRequests
     *
     */
    public static class WebRequest
    {
        /**
         * Specifies maximum number of retries on errors
         * with default FailureHandler
         */
        public static final int MAX_RETRIES = 4;

        protected Azione azione;
        protected OnResponseListener listener;
        protected Object[] datiIniziali;
        protected Bundle dati;
        protected RisultatoConnessione result;
        protected FailureHandler handler = defaultFailureHandler;
        protected int retries;
        protected long original_id;

        protected Context context = null;

        public WebRequest()
        {
            retries = 0;
        }

        public WebRequest(Context context)
        {
            retries = 0;
            this.context = context;
        }

        public boolean retry()
        {
            retries++;
            if(retries < MAX_RETRIES)
            {
                send();
                return true;
            }
            else
            {
                return false;
            }
        }

        public RisultatoConnessione getResult()
        {
            return result;
        }

        /**
         * Sends the request
         */
        public WebRequestTask send()
        {
            return eseguiRichiesta(this);
        }

        public WebRequest with(@Nullable Bundle bundle)
        {
            this.dati = bundle;
            return this;
        }

        public WebRequest listen(OnResponseListener listener)
        {
            this.listener = listener;
            return this;
        }

        public WebRequest addDataForResponse(Object[] initialData)
        {
            this.datiIniziali = initialData;
            return this;
        }

        public WebRequest error(FailureHandler handler)
        {
            this.handler = handler;
            return this;
        }

        public void setResult(RisultatoConnessione result)
        {
            this.result = result;
        }

        public boolean checkResult()
        {
            listener.onResponseReceived(result.esito, result.tipo, result.datiIniziali);
            return true;
        }

        /**
         * Checks HTTP response code
         * @return true if it is 200 OK, else returns false and calls the failure handler
         */
        public boolean checkResult(boolean handleIt)
        {
            switch(result.http_status)
            {
                case 200:
                    if(handleIt)
                    {
                        listener.onResponseReceived(result.esito, result.tipo, result.datiIniziali);
                    }
                    return true;
                default:
                    if(handleIt)
                    {
                        handler.onFailure(this);
                    }
                    return false;
            }
        }

    }

    public interface OnResponseListener {

        public void onResponseReceived(Object ris, Tipo t, Object... datiIniziali);

    }

    public static void initContext()
    {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    public static WebRequestTask eseguiRichiestaWithContext(Azione azione,
                                                            Bundle dati, OnResponseListener onResponseReceived,
                                                            long original_id, Context context,
                                                            Object...datiIniziali)
    {
        WebRequest request = perform(azione).
                with(dati).
                listen(onResponseReceived).
                addDataForResponse(datiIniziali);
        request.context = context;
        request.original_id = original_id;
        return eseguiRichiesta(request);
    }

    public static WebRequestTask eseguiRichiesta(Azione azione, Bundle dati, OnResponseListener onResponseReceived, Object...datiIniziali)
    {
        return eseguiRichiesta(
                perform(azione)
                        .with(dati)
                        .listen(onResponseReceived)
                        .addDataForResponse(datiIniziali));
    }

    public static WebRequestTask eseguiRichiesta(WebRequest... request)
    {
        WebRequestTask task = new WebRequestTask();
        task.execute(request);
        return task;
    }

    /**
     * params e' una lista di valori separati da virgola o array di WebRequest,
     * ognuna delle quali delinea una specifica richiesta Web
     * In caso di errore, si ritenta un numero MAX_RETRIES di volte
     */
    @Override
    protected WebRequest[] doInBackground(WebRequest... params) {

        int current_request;

        for(current_request = 0; current_request < params.length; current_request++)
        {
            WebRequest current = params[current_request];

            Azione azione = current.azione;
            Bundle dati = current.dati;
            OnResponseListener onResponseReceived = current.listener;
            Object[] datiIniziali = current.datiIniziali;

            String[] post = null;
            if (dati != null)
            {
                post = dati.getStringArray(DATA);
            }

            String response_string = null;
            Object result = null;

            Tipo tipo = Tipo.ESITO;

            ContentValues valori;

            valori = createValuePairs(azione, post);

            switch (azione)
            {
                case SEND_GCM_ID:
                    response_string = request(valori, scripts[azione.ordinal()], POST);
                    tipo = Tipo.STRING;
                    break;
                case LOGOUT_GCM:
                    response_string = request(valori, scripts[azione.ordinal()], POST);
                    tipo = Tipo.STRING;
                    break;
                case GET_NODE_LIST:
                    response_string = request(valori, scripts[azione.ordinal()], GET);
                    tipo = Tipo.JSON_ARRAY;
                    break;
                default:
                    break;
            }

            switch(tipo)
            {
                case STRING:
                    result = response_string;
                    break;
                case ESITO:
                    result = checkEsito(response_string);
                    break;
                case JSON:
                    result = readJSONObject(response_string);
                    break;
                case JSON_ARRAY:
                    result = readJSONArray(response_string);
                    break;
                case LONG:
                    result = response_string;
                    break;
            }

            RisultatoConnessione risultato = new RisultatoConnessione(onResponseReceived, result, tipo, datiIniziali);

            params[current_request].setResult(risultato);
        }

        return params;
    }

    @Override
    protected void onPostExecute(WebRequest result[])
    {
        int i;

        for(i = 0; i < result.length; i++)
        {
            result[i].checkResult();
        }
    }

    /**
     * Mette i dati di cui fare il POST nella richiesta:
     * REGISTRAZIONE: email, password, registration_ids
     * @param azione
     * @param params
     * @return
     */
    private ContentValues createValuePairs(Azione azione, String... params)
    {
        ContentValues nameValuePairs = new ContentValues();

        switch(azione)
        {
            case SEND_GCM_ID:
                nameValuePairs.put("registration_id", params[0]);
                break;
            case LOGOUT_GCM:
                nameValuePairs.put("registration_id", params[0]);
                break;
            case GET_NODE_LIST:
                break;
            default:
                break;
        }

        return nameValuePairs;
    }

    private JSONArray readJSONArray(String response)
    {
        JSONArray json = null;
        if(response != null)
        {
            try
            {
                json = new JSONArray(response);
            }
            catch (JSONException ex)
            {

            }
        }

        return json;
    }

    private JSONObject readJSONObject(String response)
    {
        JSONObject json = null;
        if(response != null)
        {
            try
            {
                json = new JSONObject(response);
            }
            catch (JSONException e)
            {

            }
        }

        return json;
    }

    private String request(ContentValues nameValuePairs, String script, String method)
    {
        script = WEBSITE + script;

        StringBuilder chaine = new StringBuilder("");
        try{
            URL url = new URL(script);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);

            if(nameValuePairs.size() > 0)
            {
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(nameValuePairs));
                writer.flush();
                writer.close();
                os.close();
            }

            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return chaine.toString();
    }

    private String getPostDataString(ContentValues params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(String key : params.keySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");

            String param = params.getAsString(key);
            if(param != null)
            {
                result.append(URLEncoder.encode(param, "UTF-8"));
            }
        }

        return result.toString();
    }

    private EsitoConnessione checkEsito(String response)
    {
        if (response != null)
        {
            String[] split = response.split("_");

            String risultato = split[split.length - 1];

            if (risultato.equalsIgnoreCase(OK))
            {
                return EsitoConnessione.OK;
            } else if (risultato.contains(ERROR))
            {
                return EsitoConnessione.DATI_ERRATI;
            } else if (risultato.equalsIgnoreCase("exist"))
            {
                return EsitoConnessione.UTENTE_ESISTE;
            }
        }
        return EsitoConnessione.ERRORE;
    }

    //private static final String boundary = "gc0p4Jq0M2Yt08jU534c0p";
    private static final String boundary = "q0M2Yt08jU534c0pgc0p4J";
    private static final String lineEnd = "\r\n";
    private static final String twoHyphens = "--";

    private String uploadFile(String picturePath, String uploadURL, String[] names, String[] params) {

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, bitmapOptions);
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        int scaleFactor = Math.min(photoW/1024, photoH/1024);

        opts.inSampleSize = scaleFactor;
        Bitmap bmp = BitmapFactory.decodeFile(picturePath, opts);

        try {
            File file = new File(picturePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            fileInputStream.close();

            String fileName = file.getName();

            URL url = new URL(uploadURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");

            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            //send multipart form data (required) for file
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bmp.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);

            outputStream.writeBytes(lineEnd);


            for(int i = 0; i < params.length; i++)
            {
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + names[i] + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(params[i] + lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            outputStream.flush();
            outputStream.close();

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();

            responseStream.close();
            connection.disconnect();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NOTHING";
    }
}