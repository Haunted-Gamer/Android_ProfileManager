package profile.manager;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServiceBase extends Service implements SensorEventListener {
	    AudioManager audioManager;
	    SensorManager sensorManager;
	    NotificationManager notificationManager;
	    Sensor lightSensor,accelerometerSensor,proximitySensor,magnetometerSensor;
	    boolean servicechecker=false;
		float lightValue;
		private int priority=0;
		  
		  boolean upsideDown=false,unknownState=false;
		    
	    @Override
		public IBinder onBind(Intent intent) {
			return null;
		}
		
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(SensorEvent event) {
	        /*LIGHT SENSOR*/
	        if(event.sensor.getType() == Sensor.TYPE_LIGHT)
	        {
	            String time=new SimpleDateFormat("hh:mm:ss a").format(new Date());
	            if(this.checkTime(time))
	            {
	                lightValue = event.values[0];

	                if(lightValue < 8.0)
	                {
	                    setSilent();

	                }else if(lightValue >= 10.0)
	                {
	                   
	                    setRinging();

	                }
	            }
	        }

	        /*ACCELEROMETER SENSOR*/
	        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
	        {
	            String time=new SimpleDateFormat("hh:mm:ss a").format(new Date());
	            //Log.d("TIME",time);
	            if(!this.checkTime(time) && this.priority==0)
	            {
	                float valueX = event.values[0];
	                float valueY=event.values[1];
	                float valueZ=event.values[2];
	                if((valueX <= 3.0 && valueX>=-5.0) && (valueY>7.00) && (valueZ <= 5.0 && valueZ>=-2.0))
	                {

	                    this.setVibrate();

	                }

	                else if(valueZ<=-8.0 )
	                {
	                    this.setSilent();
	                }
	                else this.setRinging();
	                	
	                
	            }
		}
	        
	        if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
	        	//Toast.makeText(this, "GG", Toast.LENGTH_SHORT).show();
	        	
	        	String time=new SimpleDateFormat("hh:mm:ss a").format(new Date());
	        	float azimuth = event.values[0];
	        	if(this.checkPrayerTime(time)){
	        		if(azimuth>220 && azimuth <310){
	        			this.setSilent();
	        			this.priority=1;
	        		}
	        		else this.priority=0;
	        		
	        	}
	        	
	        	
	        }
		}
		
		@Override
	    public void onCreate() {
	        super.onCreate();
	        
	        this.audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
	        this.sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
	        this.notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

	        this.magnetometerSensor=this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	        this.proximitySensor=this.sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	        this.accelerometerSensor=this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        this.lightSensor=this.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	          
	        boolean prox=this.sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
	        boolean acc=this.sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
	        boolean light=this.sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);   
	        boolean grav=this.sensorManager.registerListener(this,magnetometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
	        if (!prox){Toast.makeText(this, "Proximity Sensor Missing",
	        		   Toast.LENGTH_LONG).show();}
	        else if (!acc){Toast.makeText(this, "Accelerometer Sensor Missing",
	        		   Toast.LENGTH_LONG).show();}
	        else if (!light){Toast.makeText(this, "Light Sensor Missing",
	        		   Toast.LENGTH_LONG).show();}
	        else if (!grav){Toast.makeText(this, "Magnetometer Sensor Missing",
	        		   Toast.LENGTH_LONG).show();}
	    }
		
		@Override
	    public int onStartCommand(Intent intent, int flags, int startId)
	    {

	        if(!this.servicechecker) 
	        {
	            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
	            this.servicechecker=true;
	        }
	        else
	        {
	            Toast.makeText(this, "Service is already running", Toast.LENGTH_SHORT).show();
	        }
	        return START_STICKY;
	    }
		
		  @Override
		    public void onDestroy() {
		        super.onDestroy();
		            if(servicechecker)
		            {
		                Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
		                sensorManager.unregisterListener(this);
		                servicechecker=false;
		            }
		            else {
		                Toast.makeText(this, "No Service Running", Toast.LENGTH_SHORT).show();
		        }
		    }

		  public void setVibrate(){
		        try {
		            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		            SystemClock.sleep(30);
		            Toast.makeText(this, "Vibrate", Toast.LENGTH_SHORT).show();
		        }catch (Exception ex)
		        {
		            Log.d("Vibrate Ex",ex.getMessage());
		        }
		    }
		  
		  public void setRinging()
		    {
		        try {
		            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		            //Log.d("Profile","Ringer");
		            SystemClock.sleep(30);
		            //Toast.makeText(this, "Ringing", Toast.LENGTH_SHORT).show();
		        }catch (Exception ex)
		        {
		            Log.d("Ringing Ex",ex.getMessage());
		        }
		    }
		  
		  public void setSilent(){
		        try {
		            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		            //Log.d("Profile","Silent");
		            SystemClock.sleep(30);
		            Toast.makeText(this, "Silent", Toast.LENGTH_SHORT).show();
		        }
		        catch (Exception ex)
		        {
		            Log.d("Silent Ex",ex.getMessage());
		        }
		    }
		  
		  
		  private boolean checkTime(String time)
		    {
		        String[] timeFormats=time.split("[: ]");
		        int hour=Integer.parseInt(timeFormats[0]);
		        String format=timeFormats[3];
		        if(format.equals("AM"))
		        {
		            if(hour>=1 && hour<8)
		            {
		                return true;
		            }
		            else
		            {
		                return false;
		            }
		        }
		        else
		        {
		            return  false;
		        }
		    }
		  
		  private boolean checkPrayerTime(String time){
			  String[] timeFormats=time.split("[: ]");
			  int hour=Integer.parseInt(timeFormats[0]);
			  String format=timeFormats[3];
			  if(format.equals("PM"))
			  {
				  if((hour>=1 && hour<2) || (hour>=4 && hour <5) || (hour>=6 && hour<7) || (hour>=8 && hour<9)){return true;}
				  
				  
			  }
			  return false;//false
		  }
	    
}
