package epl.students.programmers.gridflare;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WifiScanner {

    private float averagePing;
    private int strength;//Between 0 and 100
    private float proportionOfLost;

    private WifiManager wifiManager;

    public WifiScanner(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        averagePing = 0;
        strength = 0;
        proportionOfLost = 0f;
    }

    public float getPing() {
        pingRequest("www.google.com", 5);
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

    private void pingRequest(String url, int n){
        averagePing = 0;
        String command = "ping -c " + n + " " + url;
        String result = executeCmd(command, false);
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
                p= Runtime.getRuntime().exec(cmd);
            else
                p= Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }
            p.destroy();
            Log.w("Wifi Scanner", res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }
}
