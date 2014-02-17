package com.android.jamesgrafton.sinEgen.controller;

import java.util.Hashtable;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.android.jamesgrafton.sinEgen.R;

/**
 * Class to handle button clicks, will change the image of
 * the button and update the buttonStates table
 * @author James Grafton
 *
 */
public class ButtonTouchListener implements OnTouchListener{
	private volatile Hashtable<Integer,Boolean> buttonStates;
	private Button[][] allButtons;
	private Button lastButtonSwitched;
	
	public ButtonTouchListener(Hashtable<Integer,Boolean> buttonStates,Button[][] allButtons){
		this.buttonStates=buttonStates;
		this.allButtons=allButtons;
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		int loc[]= new int[2];
		v.getLocationOnScreen(loc);
		Button button = getButtonAt((int)event.getX()+loc[0],(int)event.getY()+loc[1]);
		if(button==null){return true;}
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			buttonStates.put(button.getId(), !buttonStates.get(button.getId()));
			if( buttonStates.get(button.getId())==true){
				button.setBackgroundResource(R.drawable.button_on);
			}
			else{
				button.setBackgroundResource(R.drawable.button_off);
			}
			this.lastButtonSwitched=button;
			return true;
		}
		else if(event.getAction()==MotionEvent.ACTION_MOVE){
			if(lastButtonSwitched!=null&&lastButtonSwitched.equals(button)){return true;}
			else{
				buttonStates.put(button.getId(), !buttonStates.get(button.getId()));
				if( buttonStates.get(button.getId())==true){
					button.setBackgroundResource(R.drawable.button_on);
				}
				else{
					button.setBackgroundResource(R.drawable.button_off);
				}
				this.lastButtonSwitched=button;
				return true;
			}
		}
		else if(event.getAction()==MotionEvent.ACTION_UP){
			lastButtonSwitched=null;
		}
		return false;
	}
	
	private Button getButtonAt(int x,int y){
		for(Button buttonColumn[]:allButtons){
			for(Button button:buttonColumn){
				int loc[]= new int[2];
				int width = button.getWidth();
				int height = button.getHeight();
				button.getLocationOnScreen(loc);
				if(x>loc[0]&&x<loc[0]+width){
					if(y>loc[1]&&y<loc[1]+height){
						return button;
					}
				}
			}
		}
		return null;
	}
}