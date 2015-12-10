package com.lxyg.app.business.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxyg.app.business.R;
import com.lxyg.app.business.app.AppContext;
import com.lxyg.app.business.bean.Order;
import com.lxyg.app.business.utils.ImmersionModeUtils;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.util.Set;

import dev.mirror.library.utils.JsonUtils;
import dev.mirror.library.utils.PriceUtil;

/**
 * Created by 王沛栋 on 2015/11/30.
 */
public class PrintTicketActivity extends BaseActivity {
    private BluetoothService mServiceBluetooth;


    private Button mBtnSearch,mBtnSend,mBtnClose;
    private EditText mEdtContext;

    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;  //获取设备消息
    private static final int REQUEST_ENABLE_BT = 2;
    private int conn_flag = 0;
    private ConnectPaireDev mConnPaireDev = null;

    private String mIntent;
    private Order mOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ticket);



        mIntent = getIntent().getExtras().getString(INTENT_ID);
        if(TextUtils.isEmpty(mIntent)){
            finish();
        }

        mOrder = JsonUtils.parse(mIntent,Order.class);

        if(mServiceBluetooth == null){
            mServiceBluetooth = new BluetoothService(this,mHandler);
        }

        //蓝牙不可用退出程序
        if(mServiceBluetooth.isAvailable() == false){
            Toast.makeText(this, "您的蓝牙不可用!", Toast.LENGTH_LONG).show();
            finish();
        }
        //蓝牙未开启，打开蓝牙
        if(mServiceBluetooth.isBTopen() == false){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mBtnSearch = (Button) this.findViewById(R.id.btnSearch);
            mBtnSearch.setOnClickListener(this);
            mBtnSend = (Button) this.findViewById(R.id.btnSend);
            mBtnSend.setOnClickListener(this);
            mBtnClose = (Button) this.findViewById(R.id.btnClose);
            mBtnClose.setOnClickListener(this);
            mBtnClose.setEnabled(false);
            mBtnSend.setEnabled(false);

            mEdtContext = (EditText) findViewById(R.id.txt_content);
        } catch (Exception ex) {
            Log.e("出错信息", ex.getMessage());
        }
        mConnPaireDev = new  ConnectPaireDev();
        mConnPaireDev.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mServiceBluetooth != null){
            mServiceBluetooth.stop();
        }
        mServiceBluetooth = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearch://搜索设备
                Intent serverIntent = new Intent(PrintTicketActivity.this,DeviceListActivity.class);
                startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
                break;
            case R.id.btnSend:
                int price = 0;
                StringBuilder sb = new StringBuilder();
//                byte[] cmd = new byte[3];
//                cmd[0] = 0x1b;
//                cmd[1] = 0x21;
//
//                cmd[2] |= 0x10;
//                mServiceBluetooth.write(cmd);           //倍宽、倍高模式
//                mServiceBluetooth.sendMessage("  蜂域·智能社区 购物小票\n", "GBK");
//                cmd[2] &= 0xEF;
//                mServiceBluetooth.write(cmd);           //取消倍高、倍宽模式
                sb.append("  蜂域·智能社区 购物小票\n");
                sb.append("-----------------------------\n");
                for (Order.orderItems item : mOrder.getOrderItems()) {

                    int p = item.getProduct_price();
                    int n = Integer.valueOf(item.getProduct_number());

                    price = price + p*n;
                    String name = item.getName();//.substring(0,item.getName().indexOf(" "));
                    name = name.substring(0, name.indexOf(" "));
                    if(name.length()<12){
                        StringBuilder sb2 = new StringBuilder();
                        for (int i =name.length();i<12;i++){
                            sb2.append(" ");
                        }

                        name = name +sb2.toString();
                    }else{
                        name = name.substring(0,11);
                    }

                    sb.append(name + " ￥ " + PriceUtil.floatToString(p)+" x "+n + "\n");

                }
                sb.append("-----------------------------\n");
                sb.append("              合计:￥"+PriceUtil.floatToString(price)+"\n");
                String msg2 = sb.toString();
                mEdtContext.setText(msg2);
//                String msg2 = mEdtContext.getText().toString();
                if( msg2.length() > 0 ){
                    mServiceBluetooth.sendMessage(msg2, "GBK");
                }
                break;
            case R.id.btnClose:
                mServiceBluetooth.stop();
                break;

        }
    }


    //打印图形
    @SuppressLint("SdCardPath")
    private void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(576);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        //
        sendData = pg.printDraw();
        mServiceBluetooth.write(sendData);   //打印byte流数据
        Log.d("蓝牙调试",""+sendData.length);
    }

    public class ConnectPaireDev extends Thread {
        public void run(){
            while(true)
                //确保蓝牙已经完全打开
                if( mServiceBluetooth.isBTopen() == true)
                    break;
            //应用程序启动，遍历连接已匹配设备
            Set<BluetoothDevice> pairedDevices = mServiceBluetooth.getPairedDev();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if(conn_flag == 1){
                        conn_flag = 0;
                        break;
                    }
                    while(true)
                        if(conn_flag==-1 || conn_flag==0)  //等待上次循环的连接操作执行完成
                            break;
                    mServiceBluetooth.connect(device);
                    conn_flag = 2;
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                //请求打开蓝牙
                if (resultCode == Activity.RESULT_OK) {   //蓝牙已经打开
                    Toast.makeText(this, "蓝牙已经打开!", Toast.LENGTH_LONG).show();
                } else {                 //用户不允许打开蓝牙
                    finish();
                }
                break;
            case  REQUEST_CONNECT_DEVICE:     //请求连接某一蓝牙设备
                if (resultCode == Activity.RESULT_OK) {   //已点击搜索列表中的某个设备项
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //获取列表项中设备的mac地址
                    con_dev = mServiceBluetooth.getDevByMac(address);

                    mServiceBluetooth.connect(con_dev);
                }
                break;
        }
    }

    /**
     * 创建一个Handler实例，用于接收BluetoothService类返回回来的消息
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //已连接
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            mBtnClose.setEnabled(true);
                            mBtnSend.setEnabled(true);
                            conn_flag = 1;
                            break;
                        case BluetoothService.STATE_CONNECTING:  //正在连接
                            Log.d("蓝牙调试", "正在连接.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //监听连接的到来
                        case BluetoothService.STATE_NONE:
                            Log.d("蓝牙调试","等待连接.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    mBtnClose.setEnabled(false);
                    mBtnSend.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    conn_flag = -1;
                    break;
            }
        }

    };
}

