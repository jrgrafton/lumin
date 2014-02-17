package com.android.jamesgrafton.sinEgen;

import java.lang.reflect.Field;
import java.util.Hashtable;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Button;

import com.android.jamesgrafton.sinEgen.audio.AudioGenThread;
import com.android.jamesgrafton.sinEgen.controller.ButtonTouchListener;

public class SinEgen extends Activity {
	public static final int MAX_ROW=6;
	public static final int MAX_COLUMN=10;
	
	private volatile Hashtable<Integer,Boolean> buttonStates= new Hashtable<Integer,Boolean>();
	private Button allButtons[][] = new Button[MAX_COLUMN][MAX_ROW];
	public static final String TAG = "sinEgen";
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Need handler for callbacks to the UI thread
        final MyHandler mHandler = new MyHandler();
        
     	//Hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        //Bind all buttons to listener
        Field allFields[] = R.id.class.getFields();
        for(Field field:allFields){
        	final String name = field.getName().toLowerCase();
        	if(name.contains("button")){
        		try {
        			int id = field.getInt(R.id.class);
        			//Make sure its state is set to off
        			buttonStates.put(id,false);
					Button button = (Button)findViewById(id);
					int row = Integer.parseInt(name.split("_")[1]);
					int column = Integer.parseInt(name.split("_")[2]);
					allButtons[column][row]=button;
					button.setOnTouchListener(new ButtonTouchListener(buttonStates,allButtons));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
        	}
        }
        
       new Thread(new AudioGenThread(buttonStates, allButtons, mHandler)).start();
    }
    
    
    
    /**
     * Callback function used by audiogen thread since API only allows
     * UI modifications from Activity thread
     * @param button Button to be modified
     * @param id refid of background
     */
    public void changeButtonBackground(Button button,int id){
    	button.setBackgroundResource(id);
    }
    
    /**
     * Custom UI handler, atm just used to decode message to change
     * button background
     * @author James Grafton
     *
     */
    public class MyHandler extends Handler{
    	@Override
    	public void handleMessage(Message msg) {
    		ChangeButtonBackgroundWrapper message = (ChangeButtonBackgroundWrapper)msg.obj;
    		changeButtonBackground(message.getButton(),message.getNewBackgroundID());
    	}
    }
}