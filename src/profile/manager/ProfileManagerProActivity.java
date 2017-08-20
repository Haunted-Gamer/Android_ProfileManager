package profile.manager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProfileManagerProActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        newFunction();
    }
    
    
    public void newFunction(){
    	Button Start =(Button)findViewById(R.id.button1);
    	Button Stop =(Button)findViewById(R.id.button2);
    	
    	Start.setOnClickListener(new OnClickListener(){
    		public void onClick(View view) {
    		startService(new Intent(getBaseContext(),ServiceBase.class));
			}
    		});
    
    
    Stop.setOnClickListener(new OnClickListener(){
		public void onClick(View view) {
		stopService(new Intent(getBaseContext(),ServiceBase.class));
		}
		});
    }
    
}