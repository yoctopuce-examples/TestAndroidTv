package com.yoctopuce.testnexusplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yoctopuce.YoctoAPI.YAPI_Exception;
import com.yoctopuce.YoctoAPI.YRelay;

public abstract class RelayUpdateReceiver extends BroadcastReceiver
{
    public static final String ACTION_RELAY_STATE = "com.yoctopuce.testnexusplayer.action.RELAY_STATE";
    private static final String EXTRA_SERIAL = "com.yoctopuce.testnexusplayer.extra.SERIAL";
    private static final String EXTRA_FUNCTIONID = "com.yoctopuce.testnexusplayer.extra.FUNCTIONID";
    private static final String EXTRA_ISON = "com.yoctopuce.testnexusplayer.extra.ISON";
    private static final String EXTRA_LOGICALNAME = "com.yoctopuce.testnexusplayer.extra.LOGICALNAME";

    public RelayUpdateReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if (ACTION_RELAY_STATE.equals(action)) {
            String serial = intent.getStringExtra(EXTRA_SERIAL);
            String functionId = intent.getStringExtra(EXTRA_FUNCTIONID);
            String name = intent.getStringExtra(EXTRA_LOGICALNAME);
            boolean ison = intent.getBooleanExtra(EXTRA_ISON, false);
            onRelayUpdate(serial,functionId, name, ison);
        }
    }

    public abstract void onRelayUpdate(String serial, String functionId, String name, boolean ison);

    public static void broadcast(Context context, YRelay relay) throws YAPI_Exception
    {
        Intent intent = new Intent(ACTION_RELAY_STATE);
        intent.putExtra(EXTRA_SERIAL, relay.get_module().get_serialNumber());
        intent.putExtra(EXTRA_FUNCTIONID, relay.get_functionId());
        intent.putExtra(EXTRA_ISON,relay.get_output()== YRelay.OUTPUT_ON);
        intent.putExtra(EXTRA_LOGICALNAME,relay.get_logicalName());
        context.sendBroadcast(intent);

    }
}
