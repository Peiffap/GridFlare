package epl.students.programmers.gridflare.tools;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WifiScanner {

    private float averagePing;
    private int strength;//Between 0 and 100
    private float proportionOfLost;
    private float dl;

    private WifiManager wifiManager;

    public WifiScanner(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        averagePing = 0;
        strength = 0;
        proportionOfLost = 0f;
    }

    public boolean isWifiEnabled(){
        return wifiManager.isWifiEnabled();
    }

    public void enableWifi(){
        wifiManager.setWifiEnabled(true);
    }

    public float getPing() {
        return averagePing;
    }

    public int getStrength() {//La ou je suis j'ai une bonne connection donc a tester voir ce que ca donne
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        strength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 100);
        return strength;
    }

    public float getProportionOfLost() {
        return proportionOfLost;
    }

    public float getDl() {
        return dl;
    }

    public void update(){
        pingRequest("8.8.8.8", 5);//We could ping on google but the DNS is more stable
        dl = uploadTime("http://ptsv2.com", 10000);//Send 10.000 on a server and test the speed 0.01 Mb
    }

    private void pingRequest(String url, int n){
        averagePing = 0;
        String command = "ping -c " + n + " " + url;
        String result = executeCmd(command, false);

        //Case of an error: we got an empty String
        if (result.length()==0) {
            averagePing = -1;
            proportionOfLost = -1;
            return;
        }

        //Analyse in case of all packets are lost
        String[] check1 = result.split(", ");
        if(check1.length == 4) {
            if (check1[2].substring(0,3).equals("100")) {
                averagePing = -1;
                proportionOfLost = -1;
                return;
            }
        }

        //Analyse the result
        String[] splited1 = result.split("/");
        averagePing = Float.parseFloat(splited1[splited1.length - 3]);

        String[] splited2 = result.split("%")[0].split(" ");
        proportionOfLost = Float.parseFloat(splited2[splited2.length - 1]);
    }

    private static String executeCmd(String cmd, boolean sudo){
        try {
            Process p;
            if(!sudo)
                p = Runtime.getRuntime().exec(cmd);
            else
                p = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                sb.append(s).append("\n");
            }
            p.destroy();
            String res = sb.toString();
            Log.w("Wifi Scanner", res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private long uploadTime(String urlname, int nbrOfBytes){
        HttpURLConnection co = null;
        long end = 0; long start = 1;
        try {
            URL url = new URL(urlname);
            //Client setup
            co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            co.setRequestProperty("Key", "Value");//Pas sur
            co.setFixedLengthStreamingMode(nbrOfBytes);//Pas sur non plus :D

            //Output stream of the server
            OutputStream outPost = new BufferedOutputStream(co.getOutputStream());
            //Write the bytes
            start = System.currentTimeMillis();
            byte[] data = new byte[nbrOfBytes];
            for(int i = 0; i < nbrOfBytes; i++){
                data[i] = 'h';//Just to create a heavy packet
            }
            outPost.write(data);
            //Flush
            outPost.flush();//Je sais pas encore ou est le bloquant donc a voir
            end = System.currentTimeMillis();
            outPost.close();
            co.disconnect();

        } catch (MalformedURLException eu){
            Log.w("Upload file", "Bad url : " + eu.getMessage());
        } catch (IOException ioe){
            Log.w("Upload file", "IOException with the connection : " + ioe.getMessage());
        } finally {
            if(co != null)
                co.disconnect();
        }
        return end - start;
    }

    //region Live Scanning
    private int rssi;
    private int v;

    public int update_live_scan(){
        WifiInfo wi = wifiManager.getConnectionInfo();
        rssi = wi.getRssi();
        v = rssi + 94;//RSSI ~ [-94, -60] => v = [0, 34];
        if(v > 34)//Set the margins
            v = 34;
        else if(v < 0)
            v = 0;
        return rssi;
    }

    public int getLiveColor(){
        int colorator = v*255/34;
        return Color.argb(255, 255 - colorator, colorator, 0);
    }

    public int get_live_numeric_scale(){
        return v*100/34;
    }
    //endregion
}
