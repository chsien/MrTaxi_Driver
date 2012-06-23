package com.MrTaxi.Driver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.MrTaxi.core.bitmapBase64CVTR;
import com.MrTaxi.core.localStorage;

public class registerActivity extends Activity {
    /** Called when the activity is first created. */

	/*Intent code*/
	private static int SELECT_PHOTO = 	0xA;
	private static int TAKE_PHOTO = 	0xB;
	
	/*For sharedpreference*/
	private localStorage localStorageHandler;
		
	/*Layout component*/
	private Button    button_request;
	private ImageView imageView_photo;
	private View      view_shadow;
	private EditText  editText_name;
	private EditText  editText_phone;
	private EditText  editText_license;
	private EditText  editText_driverlicense;

	/*Interest Data*/
	private Bitmap bitmap_photo;
	private final int bitmap_photo_width = 320;
	private final int bitmap_photo_height = 320;
	private String string_name;
	private String string_phone;
	private String string_license;
	private String string_driverlicense;
	
	private File tempFile;
	Dialog dialog_choosePhoto;
	android.content.DialogInterface.OnClickListener dialog_operation = new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int response) {
				switch(response)
				{
					case Dialog.BUTTON_POSITIVE:
					{
		                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		                intent.setType("image/*"); 
			    		intent.putExtra("crop", "true");
			    		intent.putExtra("aspectX", bitmap_photo_width);
			    		intent.putExtra("aspectY", bitmap_photo_height);
			    		intent.putExtra("outputX", bitmap_photo_width);
			    		intent.putExtra("outputY", bitmap_photo_height);
			    		intent.putExtra("scale", true);
			    		intent.putExtra("noFaceDetection", false);
			    		intent.putExtra("output", Uri.fromFile(tempFile));
			            intent.putExtra("outputFormat", "JPEG");            
		                startActivityForResult(Intent.createChooser(intent, "����ӤH�ۤ�"), SELECT_PHOTO); 
						break;
					}
					case Dialog.BUTTON_NEUTRAL:
					{
			    		Intent intent =  new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			    		
			    		/*Use front camera*/
			    		intent.putExtra("camerasensortype", 2);
			    		
			    		intent.putExtra("crop", "true");
			    		intent.putExtra("aspectX", bitmap_photo_width);
			    		intent.putExtra("aspectY", bitmap_photo_height);
			    		intent.putExtra("outputX", bitmap_photo_width);
			    		intent.putExtra("outputY", bitmap_photo_height);
			    		intent.putExtra("scale", true);
			    		intent.putExtra("noFaceDetection", false);
			    		intent.putExtra("output", Uri.fromFile(tempFile));
			            intent.putExtra("outputFormat", "JPEG");
			            startActivityForResult(intent, TAKE_PHOTO);
						break;
					}
					case Dialog.BUTTON_NEGATIVE:
					{
						break;
					}
				}
			}			
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        localStorageHandler = new localStorage( this );

        /*Layout linking*/
        imageView_photo = (ImageView) findViewById(R.id.imageView_photo);
        view_shadow 	= (View)      findViewById(R.id.view_shadow);
        editText_name 	= (EditText)  findViewById(R.id.editText_name);
        editText_phone 	= (EditText)  findViewById(R.id.editText_phone);
        editText_license= (EditText)  findViewById(R.id.editText_license);
        editText_driverlicense	= (EditText)  findViewById(R.id.editText_driverlicense);
        button_request 	= (Button)    findViewById(R.id.button_request);
        
        
        /*set temp pic locate*/
        tempFile=new File("/sdcard/temp.jpg");
        
        dialog_choosePhoto = new AlertDialog.Builder(this)
        .setTitle("��ܬۤ��ӷ�")
        .setPositiveButton("����ۤ�", dialog_operation)
        .setNeutralButton ("���", dialog_operation)
        .setNegativeButton("����", dialog_operation).create();
                
        imageView_photo.setClickable(true);        
        imageView_photo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
            	dialog_choosePhoto.show();
            }
        });
        
        
        
        string_name  		= localStorageHandler.search(localStorage.NAME);
        string_phone 		= localStorageHandler.search(localStorage.PHONE);        
        string_license		= localStorageHandler.search(localStorage.LICENSE);
        string_driverlicense= localStorageHandler.search(localStorage.DRIVERLICENSE);
        
        if( string_name!=null )
        	editText_name.setText(string_name);
        if( string_phone!=null )
        	editText_phone.setText(string_phone);
        if( string_license!=null )
        	editText_license.setText(string_license);
        if( string_driverlicense!=null )
        	editText_driverlicense.setText(string_driverlicense);
        
        if( localStorageHandler.search(localStorage.PHOTO) !=null )
        {
        	bitmap_photo = bitmapBase64CVTR.Base642Bitmap( localStorageHandler.search(localStorage.PHOTO) );
            imageView_photo.setImageBitmap(bitmap_photo);
            view_shadow.setVisibility(View.VISIBLE);
        }
        
        
        button_request.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	string_name = editText_name.getText().toString();
            	string_phone = editText_phone.getText().toString();
            	string_license = editText_license.getText().toString();
            	string_driverlicense = editText_driverlicense.getText().toString();
            	
            	//check value isValid
            	/*******************/
   			    
			    if(string_phone!="" && string_name!= "" && string_license!= "" && string_driverlicense!= "" && bitmap_photo!= null)
			    {
			    	localStorageHandler.create(localStorage.NAME, string_name);
			    	localStorageHandler.create(localStorage.PHONE, string_phone);
			    	localStorageHandler.create(localStorage.LICENSE, string_license);
			    	localStorageHandler.create(localStorage.DRIVERLICENSE, string_driverlicense);
			    	localStorageHandler.create(localStorage.PHOTO,  bitmapBase64CVTR.Bitmap2Base64(bitmap_photo));
			    	
	            	//upload data & get id

			    	//store id
			    	
			    }
			    

			    
			    finish();
            }
        });
    }
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        if (resultCode == RESULT_OK) { 
            if (requestCode == SELECT_PHOTO || requestCode == TAKE_PHOTO) {
            	
            	/*avoid OOM*/
            	if(bitmap_photo!=null)
                	bitmap_photo.recycle();
  	
            	Uri uri = Uri.fromFile(tempFile);            	
            	ContentResolver cr = this.getContentResolver();
            	
            	try {
					bitmap_photo = BitmapFactory.decodeStream(cr.openInputStream(uri));
				} catch (FileNotFoundException e) {
					Log.v("error", e.toString());
				}

                /*Delete temp pic*/
                tempFile.delete();
                
                imageView_photo.setImageBitmap(bitmap_photo);
            	view_shadow.setVisibility(View.VISIBLE);
            }
        } 
    }
}