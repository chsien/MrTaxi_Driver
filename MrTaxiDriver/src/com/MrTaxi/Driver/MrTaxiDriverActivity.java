package com.MrTaxi.Driver;

import com.MrTaxi.core.localStorage;
import com.MrTaxi.net.core.asycTask_todo;
import com.MrTaxi.Driver.registerActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MrTaxiDriverActivity extends Activity  {
	
    /** Called when the activity is first created. */
	

	private View	  view_standaloneTip;
	private TextView  textView_standaloneTip;
	
	private localStorage localStorageHandler;
	
	private ToggleButton start;

	
	private TextView nameinf;
	private TextView phoneinf;
	private TextView licenseinf;
	private TextView driverlicenseinf;


	Dialog dialog_clearRegData;
	Dialog dialog_inf;
	android.content.DialogInterface.OnClickListener dialogOnClickListener_clearRegData = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int response) {
			switch(response)
			{
				case Dialog.BUTTON_POSITIVE:
				{
					localStorageHandler.clearRegData();

			    	Intent intent = new Intent();
			        intent.setClass(MrTaxiDriverActivity.this, registerActivity.class);
			        startActivity(intent);				    
					break;
				}
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        

        view_standaloneTip = (View)findViewById(R.id.view_standaloneTip);
        textView_standaloneTip = (TextView)findViewById(R.id.textView_standaloneTip);
        
        
        localStorageHandler = new localStorage( this );
        
        
        dialog_clearRegData = new AlertDialog.Builder(this)
        .setTitle("是否清除資料")
        .setPositiveButton("確定", dialogOnClickListener_clearRegData)
        .setNegativeButton("取消", dialogOnClickListener_clearRegData).create();
        


        dialog_inf=new Dialog(MrTaxiDriverActivity.this);
        dialog_inf.setTitle("資訊");
        dialog_inf.setContentView(R.layout.information);
        

        nameinf = (TextView)dialog_inf.findViewById(R.id.textView_nameinf);
        phoneinf=(TextView)dialog_inf.findViewById(R.id.textView_phone);
        licenseinf=(TextView)dialog_inf.findViewById(R.id.textView_license);
        driverlicenseinf=(TextView)dialog_inf.findViewById(R.id.textView_driverlicense);

        
        
        
        
        start = (ToggleButton)findViewById(R.id.start);

        start.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				
                if ( arg1 )
                {
                	start.setChecked(true);
                	Intent it=new Intent();
                	it.setClass(MrTaxiDriverActivity.this, OnStateActivity.class);
                	startActivity(it);
                	
                }
                else
                {
                	start.setChecked(false);
                }
            }
        });


        
        
        
        
        
        
        
        
        
        
        
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo!=null && netInfo.isConnected())
		{
			textView_standaloneTip.setVisibility(View.INVISIBLE);
			view_standaloneTip.setVisibility(View.INVISIBLE);

	        if(!localStorageHandler.isExistRegData())
	        {
	        	Toast.makeText(this, "使用前請先註冊", Toast.LENGTH_LONG).show(); 
		    	Intent intent = new Intent();
		        intent.setClass(this, registerActivity.class);
		        startActivity(intent);
	        }
		}
		else
		{
			textView_standaloneTip.setVisibility(View.VISIBLE);
			view_standaloneTip.setVisibility(View.VISIBLE);
			
		}
		
		
		nameinf.setText("司機姓名: "+localStorageHandler.search(localStorage.NAME));
		phoneinf.setText("電話號碼: "+localStorageHandler.search(localStorage.PHONE));
		licenseinf.setText("車牌號碼: "+localStorageHandler.search(localStorage.LICENSE));
		driverlicenseinf.setText("駕照號碼: "+localStorageHandler.search(localStorage.DRIVERLICENSE));
		

    	start.setChecked(false);

    }
    
    public void linkMenu(Menu menu)
    {
    	if(view_standaloneTip.getVisibility() == View.INVISIBLE)
    	{
    		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "修改資料").setIcon( android.R.drawable.ic_menu_edit);
    		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "清除資料").setIcon( android.R.drawable.ic_menu_delete );
    	}
    	menu.add(Menu.NONE, Menu.FIRST + 3, 3, "資訊").setIcon( android.R.drawable.ic_menu_view );
    	menu.add(Menu.NONE, Menu.FIRST + 4, 4, "關於").setIcon( android.R.drawable.ic_menu_info_details );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	linkMenu(menu);
        return super.onCreateOptionsMenu(menu); 
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case Menu.FIRST + 1:
			{
		    	Intent intent = new Intent();
		        intent.setClass(MrTaxiDriverActivity.this, registerActivity.class);
		        startActivity(intent);
				break;
			}
    		case Menu.FIRST + 2:
    		{
    			dialog_clearRegData.show();
    			break;
    		}
    		case Menu.FIRST + 3:
    		{
    			dialog_inf.show();
    			break;
    		}
    		case Menu.FIRST + 4:
    		{
    			break;
    		}
    		default:
    			break;
    	}
    	return true;
    }
    
    
}

