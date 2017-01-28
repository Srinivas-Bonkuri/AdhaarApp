package com.fcm.net.adhaarapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.PB510.Component.FP.BiomSDK.FPCapture;
import com.PB510.Component.FP.BiomSDK.HelperInterface;
import com.PB510.Component.FP.BiomSDK.Log;
import com.PB510.Component.FP.BiomSDK.PreErrorcodes;
import com.PB510.Component.FP.BiomSDK.Precision510;

public class MainActivity extends Activity implements HelperInterface {

    private Button Capture;
    private Button ForceCapture;
    private TextView TvNfiq;
    public static TextView TvImageQuality;
    private ImageView Fpimage;

    private FPCapture captureobj = null;
    private int iResult = -1;
    private String ACTION_USB_PERMISSION = "com.Precision.Component.FP.BiomSDK.USB_PERMISSION";
    private boolean bDevicePermission = false;
    private Precision510 precision510 = null;
    private int result = -1;
    private int thresholdValue;

    String path = Environment.getExternalStorageDirectory().toString();
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_WIFI_STATE = 113;
    private static final int REQUEST_INTERNET =114;
    private static final int REQUEST_NETWORK_STATE =115;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Capture = (Button) findViewById(R.id.btnStartCapture);
        ForceCapture = (Button)findViewById(R.id.btnForceCapture);
        TvNfiq = (TextView) findViewById(R.id.NFIQTextView);
        TvImageQuality = (TextView)findViewById(R.id.ImageQualityTextView);
        Fpimage = (ImageView) findViewById(R.id.imageview);

        //For Marshmallow permission

		/* boolean hasWritePermission = (ContextCompat.checkSelfPermission(MainActivity.this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
if (!hasWritePermission) {
ActivityCompat.requestPermissions(MainActivity.this,
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
            REQUEST_WRITE_STORAGE);
}
boolean hasWifiPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
    Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED);
if (!hasWifiPermission) {
ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.ACCESS_WIFI_STATE},
        REQUEST_WIFI_STATE);
}
boolean hasInternetPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED);
if (!hasInternetPermission) {
ActivityCompat.requestPermissions(MainActivity.this,
    new String[]{Manifest.permission.INTERNET},
    REQUEST_INTERNET);
}
boolean hasNetworkPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED);
if (!hasNetworkPermission) {
ActivityCompat.requestPermissions(MainActivity.this,
new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
REQUEST_NETWORK_STATE);
}*/


        Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                precision510 = new Precision510(MainActivity.this , MainActivity.this);
                precision510.EnableLog(1);
                precision510.SetCaptureTimeout(20);
                precision510.setcapturethreshold(80);

                if ((result = precision510.InitDevice(precision510
                        .GetAttachedDeviceID())) == PreErrorcodes.PB_SUCCESS){

                    String serialNum = precision510.getserialnumber();
                    Toast.makeText(MainActivity.this,
                            "Serial Number: " +serialNum, Toast.LENGTH_SHORT)
                            .show();
                    precision510.BeginCapture();



                } else if (result == PreErrorcodes.PB_SDK_NOT_COMATIBLE_WITH_DEVICE) {
                    Toast.makeText(MainActivity.this,
                            "Device not compatible ", Toast.LENGTH_SHORT)
                            .show();
                } else if (result == PreErrorcodes.PB_TELECO_DEVICE_NOT_COMPATIBLE) {
                    Toast.makeText(MainActivity.this,
                            "TELECO device not compatible", Toast.LENGTH_SHORT)
                            .show();
                }else if (result == PreErrorcodes.PB_LICENSE_FAILED) {
                    Toast.makeText(MainActivity.this, "License failed",
                            Toast.LENGTH_SHORT).show();
                } else if (result == PreErrorcodes.PB_LICENSE_NOT_FOUND) {
                    Toast.makeText(MainActivity.this, "License failed",
                            Toast.LENGTH_SHORT).show();
                } else if (result == PreErrorcodes.PB_DEVICE_NOT_CONNECTED
                        || result == PreErrorcodes.PB_CANNOT_OPEN_DEVICE) {
                    Toast.makeText(MainActivity.this,
                            "Please connect the device", Toast.LENGTH_SHORT)
                            .show();
                } else if (result == PreErrorcodes.PB_DEVIVCE_INIT_FAILED) {
                    Toast.makeText(MainActivity.this, "Device init failed",
                            Toast.LENGTH_SHORT).show();
                } else if (result == PreErrorcodes.PB_USB_DEVICE_HAS_NO_PERMISSION) {
                    Toast.makeText(MainActivity.this,
                            "Device has no permission to access",
                            Toast.LENGTH_SHORT).show();
                } else if (result == PreErrorcodes.PB_DEVICE_ALREADY_INITIALIZED) {
                    Toast.makeText(MainActivity.this,
                            "Device already initialized",
                            Toast.LENGTH_SHORT).show();
                } else if (result == PreErrorcodes.PB_INVALID_SERIAL_NUMBER) {
                    Toast.makeText(MainActivity.this,
                            "Invalid Serial Number", Toast.LENGTH_SHORT)
                            .show();
                }  else if (result == PreErrorcodes.PB_LICENSE_FAILED) {
                    Toast.makeText(MainActivity.this,
                            "License failed", Toast.LENGTH_SHORT)
                            .show();
                }else {
                    Toast.makeText(MainActivity.this, "Device Init Failed",
                            Toast.LENGTH_SHORT).show();
                }
				/*} else{
					Toast.makeText(MainActivity.this, "Invalid serial ",
							Toast.LENGTH_SHORT).show();
			 }*/
            }
        });

		/*
		 * getimage.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { precision510.GetImage(); }
		 * });
		 */
        ForceCapture.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(precision510!=null)
                {
                    precision510.ForceCapture();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "First click capture button and then click ForceCapture",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void handlerFunction(final byte[] rawImage, final int imageHeight,
                                final int imageWidth, final int status, final String errorMessage,
                                final boolean complete, final byte[] isoData, final Bitmap bitmap, final int imageQuality, final int NFIQ) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (status == 0)
                {
                    System.out.println("Height : "+imageHeight );
                    System.out.println("Width : "+imageWidth );
                    System.out.println("Status : "+status );
                    System.out.println("complete : "+complete );
                    System.out.println("NFIQ : "+NFIQ );

                    if (complete)
                    {

                        Log.WriteLog("MainActivity=>handlerFunction=> Capture completed");
                        Fpimage.setImageBitmap(bitmap);
                        String template = Base64.encodeToString(isoData,
                                Base64.NO_WRAP);
                        System.out.println("Template:" + template);
                        Log.WriteLog("MainActivity=>handlerFunction=> Template:"
                                + template);
                        Toast.makeText(MainActivity.this,
                                "Image Capture Success", Toast.LENGTH_SHORT)
                                .show();

                        runOnUiThread(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                TvNfiq.setText(NFIQ+"");
                                TvImageQuality.setText(imageQuality+"");
                            }
                        });



                        Log.WriteLog("MainActivity=>handlerFunction=> Capture competed, Before Unint");
                        precision510.UnInitDevice();
                        Log.WriteLog("MainActivity=>handlerFunction=> Capture competed, After Unint");
                    }
                    else
                    {
                        Log.WriteLog("MainActivity=>handlerFunction=> Capture still not completed");
                        Fpimage.setImageBitmap(bitmap);
                        TvNfiq.setText(NFIQ+"");
                        TvImageQuality.setText(imageQuality+"");

                    }
                } else if (status == PreErrorcodes.PB_IMAGE_CAPTURE_FAILED) {
                    Toast.makeText(MainActivity.this, "Image capture failed",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Image capture failed, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Image capture failed, After UnInitDevice");
                }else if (status == PreErrorcodes.PB_EXCEPTION_OCCURED) {
                    Toast.makeText(MainActivity.this, "exception occured",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> exception, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> exception, After UnInitDevice");
                }else if (status == PreErrorcodes.PB_LICENSE_FAILED) {
                    Toast.makeText(MainActivity.this, "License failed",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> License failed, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> License failed, After UnInitDevice");
                } else if (status == PreErrorcodes.PB_SDK_NOT_COMATIBLE_WITH_DEVICE) {
                    Toast.makeText(MainActivity.this, "Invalid device key",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Invalid device key, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Invalid device key, After UnInitDevice");
                }else if (status == PreErrorcodes.PB_TELECO_DEVICE_NOT_COMPATIBLE) {
                    Toast.makeText(MainActivity.this, "SDK not compatible",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> SDK not compatible, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> SDK not compatible, After UnInitDevice");
                }else if (status == PreErrorcodes.PB_DEVIVCE_INIT_FAILED) {
                    Toast.makeText(MainActivity.this, "device initialization failed",
                            Toast.LENGTH_SHORT).show();

                } else if (status == PreErrorcodes.PB_CAPTURE_TIME_OUT) {
                    Toast.makeText(MainActivity.this, "Capture Timeout",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Capture Timeout, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Capture Timeout, After UnInitDevice");
                } else if (status == PreErrorcodes.PB_DEVICE_NOT_CONNECTED) {
                    Toast.makeText(MainActivity.this, "Device not connected",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Device not connected, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Device not connected, After UnInitDevice");
                } else if (status == PreErrorcodes.PB_EXTRACTION_FAILED) {
                    Toast.makeText(MainActivity.this, "Extraction failed",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Extraction failed, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Extraction failed, After UnInitDevice");
                }else if (status == PreErrorcodes.PB_INVALID_CAPTURE_TIMEOUT) {
                    Toast.makeText(MainActivity.this, "Invalid capture timeout",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Invalid capture timeout, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Invalid capture timeout, After UnInitDevice");
                } else if (status == PreErrorcodes.PB_INVALID_THRESHOLD_VALUE) {
                    Toast.makeText(MainActivity.this, "Invalid Threshold value",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=>Invalid Threshold value, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Invalid Threshold value, After UnInitDevice");
                } else if (status == PreErrorcodes.PB_THRESHOLD_VALUE_OBTAINED) {
                    Toast.makeText(MainActivity.this, "Threshold value obtained",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Threshold value obtained, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Threshold value obtained, After UnInitDevice");
                } else if (status == PreErrorcodes.PB_THRESHOLD_VALUE_NOT_OBTAINED) {
                    Toast.makeText(MainActivity.this, "Threshold quality not obtained, please wait  !!!",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Threshold value not obtained, Before UnInitDevice");
                    // precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Threshold value not obtained, After UnInitDevice");
                }else if (status == PreErrorcodes.PB_INVALID_SERIAL_NUMBER) {
                    Toast.makeText(MainActivity.this, "Invalid serial number",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Invalid serial number, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Invalid serial number, After UnInitDevice");
                }	/*else if(status == PreErrorcodes.PB_STOP_CAPTURE_INVOKED){
                    Toast.makeText(MainActivity.this, "Force Capture Invoked",
                            Toast.LENGTH_SHORT).show();
                    Log.WriteLog("MainActivity=>handlerFunction=> Force capture invoked, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Force capture invoked, After UnInitDevice");

                    TvNfiq.setText("");
                    TvImageQuality.setText("");
                    Fpimage.setImageBitmap(bitmap);

					*//*File dir = new File("/sdcard/BMP");
					if (!dir.exists())
					{
						dir.mkdir();
					}

					try
					{
						FileOutputStream fos = new FileOutputStream("/sdcard/BMP/rawimg.bmp");
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				      	fos.close();

					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*//*
                }*/

                else {
                    Log.WriteLog("MainActivity=>handlerFunction=> Image Capture Failed, Before UnInitDevice");
                    precision510.UnInitDevice();
                    Log.WriteLog("MainActivity=>handlerFunction=> Image Capture Failed, After UnInitDevice");
                    Toast.makeText(MainActivity.this, "Image Capture Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @SuppressLint("NewApi")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                try {
                    Log.WriteLog("MainActivity=>BroadcastReceiver=> Device Attached");
                    UsbManager mUsbManager = (UsbManager) getSystemService(USB_SERVICE);
                    for (UsbDevice device : mUsbManager.getDeviceList()
                            .values()) {
                        if (0x2109 == device.getVendorId()
                                && 0x7638 == device.getProductId() || 0x2D38== device.getVendorId()
                                && 0x07D0 == device.getProductId()
                                ) {
                            if (!mUsbManager.hasPermission(device)) {
                                PendingIntent mPermissionIntent = PendingIntent
                                        .getBroadcast(MainActivity.this, 0,
                                                new Intent(
                                                        ACTION_USB_PERMISSION),
                                                0);
                                mUsbManager.requestPermission(device,
                                        mPermissionIntent);
                                bDevicePermission = false;
                            } else {
                                bDevicePermission = true;

                            }
                            break;
                        }
                    }

                } catch (Exception Ex) {
                    Log.WriteLog("MainActivity=>BroadcastReceiver=>Exception occred in Attached event:"
                            + Ex.getMessage().toString());
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.WriteLog("MainActivity=>BroadcastReceiver=> Device Detached");
            }
        }
    };

    //For Marshmallow permission

	/*	@SuppressLint("NewApi") @Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	    switch (requestCode)
	    {
	        case REQUEST_WRITE_STORAGE: {
	            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
	            {
	                Toast.makeText(getApplicationContext(), "Permission granted to read", Toast.LENGTH_LONG).show();

	                //reload my activity with permission granted or use the features what required the permission
	            } else
	            {
	                Toast.makeText(getApplicationContext(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
	            }
	        }
	        case REQUEST_WIFI_STATE: {
	            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
	            {
	                Toast.makeText(getApplicationContext(), "Permission granted for wifi state", Toast.LENGTH_LONG).show();

	                //reload my activity with permission granted or use the features what required the permission
	            } else
	            {
	                Toast.makeText(getApplicationContext(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
	            }
	        }
	        case REQUEST_INTERNET: {
	            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
	            {
	                Toast.makeText(getApplicationContext(), "Permission granted for wifi state", Toast.LENGTH_LONG).show();

	                //reload my activity with permission granted or use the features what required the permission
	            } else
	            {
	                Toast.makeText(getApplicationContext(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
	            }
	        }
	        case REQUEST_NETWORK_STATE: {
	            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
	            {
	                Toast.makeText(getApplicationContext(), "Permission granted for wifi state", Toast.LENGTH_LONG).show();

	                //reload my activity with permission granted or use the features what required the permission
	            } else
	            {
	                Toast.makeText(getApplicationContext(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
	            }
	        }
	    }

	}
*/

}
