package com.yoctopuce.testnexusplayer;

/**
 * Created by seb on 22.07.2015.
 */
public class Relay
{
    final private String _functionId;
    final private String _serial;
    private String _name;
    private boolean _ison;

    public Relay(String serial, String functionId, String name, boolean ison)
    {
        _name = name;
        _serial = serial;
        _functionId = functionId;
        _ison = ison;
    }

    public String getName()
    {
        if (_name.length()==0) {
            return _functionId;
        }
        return _name;
    }

    public String getHwid()
    {
        return _serial + "." + _functionId;
    }

    public boolean ison()
    {
        return _ison;
    }


    public void setIson(boolean ison)
    {
        _ison = ison;
    }
}
