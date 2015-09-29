package com.example.rayc.wifi_mode_shortlion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button scan,connection,disconnection;
    TextView t1;
    private WifiManager wifi;
    private WifiReceiver wifi_rec;    //自訂類別
    private List<WifiConfiguration>    mWifiConfiguration;
    List<ScanResult> result_list;
    ArrayAdapter<String> adapter;
    ListView list;
    int connectionNo;
    String tmp="";
    Context context;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan=(Button)findViewById(R.id.button);
        connection=(Button)findViewById(R.id.button2);
        disconnection=(Button)findViewById(R.id.button3);
        t1=(TextView)findViewById(R.id.textView);
        list=(ListView)findViewById(R.id.listView);
        context=this;
        adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(MainActivity.this, "onAdClosed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Toast.makeText(MainActivity.this, "onAdFailedToLoad", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Toast.makeText(MainActivity.this, "onAdLeftApplication", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Toast.makeText(MainActivity.this, "onAdOpened", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_LONG).show();
            }
        });

        wifi=(WifiManager) getSystemService(context.WIFI_SERVICE);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText("開始搜尋\n");
                wifi_rec = new WifiReceiver();
                registerReceiver(wifi_rec, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifi.startScan();
                mWifiConfiguration=wifi.getConfiguredNetworks();
                String tmp="";
                String[]  wifiItem=new String[mWifiConfiguration.size()];
                for(int i=0;i<mWifiConfiguration.size();i++)
                {
                    tmp+= mWifiConfiguration.get(i).SSID+" : "+mWifiConfiguration.get(i).networkId+"\n";
                    wifiItem[i]=mWifiConfiguration.get(i).SSID;
                }
                t1.setText(tmp);
                adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout
                        .simple_expandable_list_item_1,wifiItem);
                list.setAdapter(adapter);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectionNo = position;
                Toast.makeText(MainActivity.this, connectionNo + "", Toast.LENGTH_LONG).show();
            }
        });
        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.enableNetwork(connectionNo, true);
            }
        });
        disconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectWifi(0);
            }
        });

    }
    public void disconnectWifi(int netId) {
        // mWifiManager.disableNetwork(netId);
        wifi.disconnect();
    }
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }
    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuffer sb = new StringBuffer();
            result_list = wifi.getScanResults();
            for (int i = 0; i < result_list.size(); i++) {
                sb.append(new Integer(i + 1).toString() + ".");
                // sb.append((result_list.get(i)).toString());
                sb.append((result_list.get(i).SSID));
                sb.append("\n");
            }
            // t1.setText(sb);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
