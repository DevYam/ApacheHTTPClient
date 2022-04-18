package ApacheHttpClinetSSLImpl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){

        try{

            SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy(){
                @Override
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }, new AllowAllHostnameVerifier());

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https",8444, sf));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(registry);

            HttpClient httpclient = new DefaultHttpClient(ccm);
            HttpPost httppost = new HttpPost("Api Endpoint URL");

            //creating request entity from json payload as string
            StringEntity requestEntity = new StringEntity(
                    "{\n" +
                            "    \"fields\": {\n" +
                            "       \"project\":\n" +
                            "       {\n" +
                            "          \"key\": \"JIVAPROJECTFOUR\"\n" +
                            "       },\n" +
                            "       \"summary\": \"This java bug is created using Postmjave an\",\n" +
                            "       \"description\": \"Creating of an issue using javaaa keys and issue type names using the REST API\",\n" +
                            "       \"issuetype\": {\n" +
                            "          \"name\": \"Bug\"\n" +
                            "       }\n" +
                            "   }\n" +
                            "}",
                    ContentType.APPLICATION_JSON);


            httppost.setEntity(requestEntity);




            String encoding= Base64.getEncoder().encodeToString("Uname:Pass".getBytes());

            httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);

            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();

            BufferedReader br=new BufferedReader(new InputStreamReader(entity.getContent()));
            String line;

            while((line=br.readLine())!=null)
            {
                System.out.println(line);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
