package com.yoctopuce.testnexusplayer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yoctopuce.YoctoAPI.YAPI;
import com.yoctopuce.YoctoAPI.YAPI_Exception;
import com.yoctopuce.YoctoAPI.YRelay;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class YoctoIntentService extends IntentService
{
    private static final String TAG = "YSRV";
    private static final String ACTION_GET_RELAYS = "com.yoctopuce.testnexusplayer.action.GET_RELAYS";
    private static final String ACTION_SET_RELAY_STATE = "com.yoctopuce.testnexusplayer.action.SET_RELAY_STATE";
    private static final String EXTRA_HWID = "com.yoctopuce.testnexusplayer.extra.HWID";
    private static final String EXTRA_ISON = "com.yoctopuce.testnexusplayer.extra.ISON";

    // TODO: Customize helper method
    public static void requestRelays(Context context)
    {
        Intent intent = new Intent(context, YoctoIntentService.class);
        intent.setAction(ACTION_GET_RELAYS);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void setRelayState(Context context, String hwid, boolean isOn)
    {
        Intent intent = new Intent(context, YoctoIntentService.class);
        intent.setAction(ACTION_SET_RELAY_STATE);
        intent.putExtra(EXTRA_HWID, hwid);
        intent.putExtra(EXTRA_ISON, isOn);
        context.startService(intent);
    }

    public YoctoIntentService()
    {
        super("YoctoIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null) {
            final String action = intent.getAction();

            try {
                YAPI.EnableUSBHost(getApplicationContext());
                YAPI.RegisterHub("usb");
                if (ACTION_GET_RELAYS.equals(action)) {
                    YRelay relay = YRelay.FirstRelay();
                    while (relay != null) {
                        Log.d(TAG, relay.describe());
                        RelayUpdateReceiver.broadcast(getApplicationContext(), relay);
                        relay = relay.nextRelay();
                    }
                } else if (ACTION_SET_RELAY_STATE.equals(action)) {
                    final String hwid = intent.getStringExtra(EXTRA_HWID);
                    final boolean on = intent.getBooleanExtra(EXTRA_ISON, false);
                    YRelay relay = YRelay.FindRelay(hwid);
                    if (relay.isOnline()) {
                        relay.set_output(on ? YRelay.OUTPUT_ON : YRelay.OUTPUT_OFF);
                    }
                }
            } catch (YAPI_Exception e) {
                e.printStackTrace();
            }
            YAPI.FreeAPI();
        }
    }


}
