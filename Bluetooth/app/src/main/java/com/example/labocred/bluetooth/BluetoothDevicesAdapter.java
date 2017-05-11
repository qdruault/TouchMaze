package com.example.labocred.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

/**
 * Created by LaboCred on 30/03/2017.
 */

public class BluetoothDevicesAdapter extends ArrayAdapter<BluetoothDevice>{
    List<BluetoothDevice> devices;
    BluetoothSettingsActivity context;

    public BluetoothDevicesAdapter(BluetoothSettingsActivity context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public BluetoothDevicesAdapter(BluetoothSettingsActivity context, int resource, List<BluetoothDevice> devices) {
        super(context, resource, devices);
        this.devices = devices;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_bluetooth_device, parent, false);
        rowView.setTag(position);

        BluetoothDevice device = devices.get(position);

        TextView deviceNameView = (TextView)rowView.findViewById(R.id.bluetooth_device_name);
        deviceNameView.setText(device.getName());

        TextView deviceMACView = (TextView)rowView.findViewById(R.id.bluetooth_device_mac);
        deviceMACView.setText(device.getAddress());

        ImageButton connectToBoxView = (ImageButton)rowView.findViewById(R.id.bluetooth_device_connect_button);
        if (context.app.socket != null && context.app.socket.getRemoteDevice().getAddress().equals(device.getAddress())) {
            connectToBoxView.setImageResource(R.drawable.ic_highlight_off_black_24dp);
        }

        return rowView;
    }
}
