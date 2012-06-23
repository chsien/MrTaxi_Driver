package com.MrTaxi.core;

import java.io.IOException;

import android.hardware.Camera;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.view.SurfaceHolder;

public class cameraHandler{
	private Camera camera;
	private SurfaceHolder surfaceHolder;
	
	
	public cameraHandler(SurfaceHolder surfaceHolderExt)
	{
		surfaceHolder = surfaceHolderExt;
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	public void setUnlock()
	{
		camera.unlock();
	}
	public void setLock()
	{
		camera.lock();
	}
	
	public void startPreview()
	{
		camera.startPreview();
	}
	public void stopPreview()
	{
		camera.stopPreview();
	}

	public void initial()
	{
		camera = Camera.open();
        
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode("auto");
        camera.setParameters(parameters);        
       
        try {
        	camera.setPreviewDisplay(surfaceHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        //camera.startPreview();
    }
	public void terminate()
	{
        camera.release();
        camera = null;
	}
}