package com.example.clientproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.serverproject.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected IMyAidlInterface addService;
    private String serverAppUri = "com.example.serverproject";
    EditText num1,num2;
    TextView total;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num1 = (EditText) findViewById(R.id.editText1);
        num2 = (EditText) findViewById(R.id.editText2);
        total = (TextView)findViewById(R.id.textViewResult);
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(this);
        
        initConnection();
    }

    private void initConnection() {
        if(addService==null)
        {
            Intent intent = new Intent(IMyAidlInterface.class.getName());
            intent.setAction("service.calc");
            intent.setPackage("com.example.serverproject");
            bindService(intent, serverConnection, Service.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection serverConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.d("tag","Service connected");
            addService = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("tag","Service Disconnected");
            addService = null;

        }
    };

    @Override
    public void onClick(View v) {
      switch (v.getId())
      {
          case R.id.buttonAdd:
              if(num1.length()>0 && num2.length()>0 && addService != null){
                  try{
                      total.setText("");
                      total.setText("Result: "+addService.addNumbers(Integer.parseInt(num1.getText().toString()),Integer.parseInt(num2.getText().toString())));
                  }catch (RemoteException e){
                      e.printStackTrace();
                      Log.d("tag","Connection cannot be established");
                  }
              }
              break;
      }
        
    }
}
