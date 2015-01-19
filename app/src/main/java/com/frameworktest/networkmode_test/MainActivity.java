package com.frameworktest.networkmode_test;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
//import com.android.internal.telephony.PhoneConstants;
//import com.android.internal.telephony.TelephonyProperties;

import java.lang.reflect.Method;


public class MainActivity extends Activity {

    private static final String LOG_TAG = "NetworkMode_Test";
    private Phone mPhone;
    private MyHandler2 mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mPhone = PhoneFactory.getDefaultPhone();
        /*
        mPhone = PhoneGlobals.getInstance().getPhone();
        mPhone = PhoneGlobals.getInstance().getPhone(MobileNetworkSettings.mSimId);
        mHandler = new MyHandler2();

        mPhone.getPreferredNetworkType(mHandler.obtainMessage(MyHandler2.MESSAGE_GET_PREFERRED_NETWORK_TYPE));
        */


        final Button button = (Button) findViewById(R.id.update_networkMode);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = getApplicationContext();
                int settingsNetworkMode = android.provider.Settings.Global.getInt(context.getContentResolver(),
                        "preferred_network_mode", Phone.NT_MODE_LTE_GSM_WCDMA);

                Log.d(LOG_TAG, "Network Mode is:" + settingsNetworkMode);
                final String modeTxt = "Network Mode is " + settingsNetworkMode;
                TextView networkMode_text = (TextView) findViewById(R.id.networkMode);
                networkMode_text.setText(modeTxt);
            }
        });
    }

    private class MyHandler2 extends Handler {
        private static final int MESSAGE_GET_PREFERRED_NETWORK_TYPE = 0;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_GET_PREFERRED_NETWORK_TYPE:
                    handleGetPreferredNetworkTypeResponse(msg);
                    break;
            }
        }

        private void handleGetPreferredNetworkTypeResponse(Message msg) {
            AsyncResult ar = (AsyncResult) msg.obj;

            if (ar.exception == null) {
                int type = ((int[])ar.result)[0];
                Log.i(LOG_TAG, "[Other SIM] get preferred network type=" + type);
            } else {
                // Weird state,    disable    the    setting
                Log.i(LOG_TAG, "[Other SIM] get preferred network type, exception="+ar.exception);
            }
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
