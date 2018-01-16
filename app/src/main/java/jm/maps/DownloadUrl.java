package jm.maps;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.id.input;

/**
 * Created by hp on 2018-01-12.
 */

public class DownloadUrl {

    public String readUrl(String myUrl) throws IOException {
        String data="";
        InputStream inputStream=null;
        HttpURLConnection urlConnection=null;

        try{
            URL url= new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line ="";
            while((line=br.readLine())!=null)
            {
                sb.append(line);
            }

            data=sb.toString();
            br.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        Log.d("DownloadURL","Returning data= "+data);

        return data;

    }
}
