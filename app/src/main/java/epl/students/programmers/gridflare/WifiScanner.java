package epl.students.programmers.gridflare;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WifiScanner {

    private float averagePing;
    private int strength;//Between 0 and 100
    private float proportionOfLost;

    private WifiManager wifiManager;

    public WifiScanner(Context context){
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        averagePing = 0;
        strength = 0;
        proportionOfLost = 0f;
    }

    public float getPing() {
        return averagePing;
    }

    public int getStrength() {//La ou je suis j'ai une bonne connection donc a tester voir ce que ca donne
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        strength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);
        return strength;
    }

    public float getProportionOfLost() {
        return proportionOfLost;
    }

    public void update(){
        pingRequest("8.8.8.8", 5);//We could ping on google but the DNS is more stable
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
}
