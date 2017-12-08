package com.example.tcpclient;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by zhangyuanyuan on 2017/12/8.
 */

public class MainActivity extends AppCompatActivity {

    private TextView tvServerMessage;
    private Button send;
    final private String SERVER_PORT = "8080";
    private int id = 0;
    private EditText textS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WifiManager myWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        tvServerMessage = (TextView) findViewById(R.id.textViewServerMessage);
        textS = (EditText) findViewById(R.id.editText1);
        send = (Button) findViewById(R.id.button1);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientAsyncTask clientAST = new ClientAsyncTask();
                String ip = intToIP(myWifiManager.getDhcpInfo().gateway);
                Log.d("MainActivity", ip);
                clientAST.execute(new String[]{"192.168.0.243", SERVER_PORT, textS.getText().toString()});
            }
        });
    }

    public String intToIP(int i) {
        return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF));
    }

    /**
     * AsyncTask which handles the communication with the server
     */
    class ClientAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {

                Socket socket = new Socket(params[0], Integer.parseInt(params[1]));

                InputStream is = socket.getInputStream();

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println(params[2]);

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                result = br.readLine();

                socket.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            tvServerMessage.setText(s);
        }
    }


}
