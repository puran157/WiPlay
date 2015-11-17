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
    private Method setWifiApEnabled;
    private Method getWifiApState;
    private boolean wasItOn;
    int netID;

    public WiPlayHotSpot(Context context)
    {
        hotspot_name = Constants.GetRandomString(Constants.HOTSPOT_CHAR_LEN);
        hotspot_psk = Constants.GetRandomString(Constants.HOTSPOT_CHAR_LEN);
        wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        netconfig = new WifiConfiguration();
        netID = -1;
        setWifiApEnabled = null;
        wasItOn = false;
    }

    public String getHotspot_name() {
        return  hotspot_name;
    }

    public String getHotspot_psk() {
        return  hotspot_psk;
    }

    /* start HotSpot, to be used by server */
    public void StartHotSpot()
    {
        if(wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            wasItOn = true;
        }

        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        boolean method_found = false;
//TODO: hotspot is created without PSK, fix this
        for(Method method: methods)
        {
            if(method.getName().equals("setWifiApEnabled")) {
                method_found = true;
                setWifiApEnabled = method;
                //netconfig.BSSID = hotspot_name;
                netconfig.SSID = String.format("%s", hotspot_name);
                netconfig.preSharedKey = String.format("%s", hotspot_psk);
                netconfig.hiddenSSID = true;
                netconfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                //netconfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                netconfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                netconfig.allowedProtocols.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                netconfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                netconfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                netconfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                netconfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                try{
                    boolean apstatus = (Boolean)method.invoke(wifiManager, netconfig, true);
                    int apState = 0;
                    for(Method isWifiApEnabledMethod : methods)
                    {
                        if(isWifiApEnabledMethod.getName().equals("isWifiApEnabled")){
                            while(!(Boolean)isWifiApEnabledMethod.invoke(wifiManager)) {};
                            for(Method method1: methods) {
                                if(method1.getName().equals("getWifiApState")) {
                                    getWifiApState = method1;
                                    apState = (Integer)method1.invoke(wifiManager);
                                }
                            }
                        }
                    }
                    if(apstatus && apState == WifiManager.WIFI_STATE_ENABLED )
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
    */

    public void ConnectToHotSpot()
    {
        netconfig.SSID = String.format("\"%s\"", hotspot_name);
        netconfig.preSharedKey = String.format("\"%s\"", hotspot_psk);
        netID = wifiManager.addNetwork(netconfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netID, true);
        wifiManager.reconnect();
    }

    public void setHotspot(String hotspot_name, String hotspot_psk)
    {
        this.hotspot_name = hotspot_name;
        this.hotspot_psk = hotspot_psk;
    }

    public void cleanUp() {
        if(netID != -1) {
            wifiManager.disableNetwork(netID);
            wifiManager.removeNetwork(netID);
        }
        else
        {
            boolean apstatus = false;
            int apState = 0;
            try {
                apstatus = (Boolean)setWifiApEnabled.invoke(wifiManager, netconfig, false);
                apState = (Integer)getWifiApState.invoke(wifiManager);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            if(apstatus && apState == WifiManager.WIFI_STATE_DISABLED )
                Log.i(Constants.Tag,"HotSpot CleanUp Success");
            else
                Log.i(Constants.Tag, "HotSpot CleanUp Error");
        }

        if(wasItOn)
            wifiManager.setWifiEnabled(true);
    }
}
