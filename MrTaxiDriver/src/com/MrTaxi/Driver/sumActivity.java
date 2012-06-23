package com.MrTaxi.Driver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sumActivity extends Activity {
	
	private Button ok;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sum);
        
        ok=(Button)findViewById(R.id.ok);
        ok.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
            	Intent it=new Intent();
            	it.setClass(sumActivity.this, MrTaxiDriverActivity.class);
            	startActivity(it);        		
            	sumActivity.this.finish();
        	}
        });
        
    
    }
}
