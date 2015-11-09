package app.wiplay.connection;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlayHotSpot {

    private String hotspot_name;
    private String hotspot_psk;
    private WifiManager wifiManager;
    private WifiConfiguration netconfig;

    WiPlayHotSpot(Context context)
    {
        hotspot_name = Constants.GetRandomString();
        hotspot_psk = Constants.GetRandomString();
        wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        netconfig = new WifiConfiguration();
    }

    public String getHotspot_name() {
        return  hotspot_name;
    }

    public void setHotspot_name(String name) {
        hotspot_name = name;
    }

    public String getHotspot_psk() {
        return  hotspot_psk;
    }

    public void setHotspot_psk(String psk) {
        hotspot_psk = psk;
    }

    /* start HotSpot, to be used by server */
    public void StartHotSpot()
    {
        if(wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);

        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        boolean method_found = false;

        for(Method method: methods)
        {
            if(method.getName().equals("setWifiApEnabled")) {
                method_found = true;
                //netconfig.BSSID = hotspot_name;
                netconfig.SSID = String.format("\"%s\"", hotspot_name);
                netconfig.preSharedKey = String.format("\"%s\"", hotspot_psk);
                netconfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                netconfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netconfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                netconfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                netconfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                netconfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                netconfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                try{
                    boolean apstatus = (Boolean)method.invoke(wifiManager, netconfig, true);
                    for(Method isWifiApEnabledMethod : methods)
                    {
                        if(isWifiApEnabledMethod.getName().equals("isWifiApEnabled")){
                            while(!(Boolean)isWifiApEnabledMethod.invoke(wifiManager)) {};
                            for(Method method1: methods) {
                                if(method1.getName().equals("getWifiApState")) {
                                    int apState;
                                    apState = (Integer)method1.invoke(wifiManager);
                                }
                            }
                        }
                    }
                    if(apstatus)
                        Log.i(Constants.Tag,"HotSpot Creation Success");
                    else
                        Log.i(Constants.Tag, "HotSpot Creation Error");
                }
                catch (IllegalArgumentException e)
                {

                }
                catch (IllegalAccessException e)
                {

                }
                catch(InvocationTargetException e)
                {

                }
            }
        }
        if(!method_found)
            Log.i(Constants.Tag, "HotSpot is Not supported on this device");
    }

    /* Connect to HotSpot, to be used by client
    * To be called after setHotspot_name and setHotspot_psk
    * TODO: ADD MANIFEST PERMISSION FOR WIFI ACCESS
    */

    public void ConnectToHotSpot()
    {
        netconfig.SSID = String.format("\"%s\"", hotspot_name);
        netconfig.preSharedKey = String.format("\"%s\"", hotspot_psk);
        int netID = wifiManager.addNetwork(netconfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netID, true);
        wifiManager.reconnect();
    }
}
