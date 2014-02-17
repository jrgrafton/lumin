package com.android.jamesgrafton.sinEgen;

import android.widget.Button;

/**
 * Wrapper class used to pass back request to UI to change
 * button background image
 * @author James Grafton
 *
 */
public class ChangeButtonBackgroundWrapper{
	private Button button;
	private int newBackgroundID;
	
	public ChangeButtonBackgroundWrapper(Button button,int newBackgroundID){
		this.button=button;
		this.newBackgroundID=newBackgroundID;
	}

	public Button getButton() {
		return button;
	}
	
	public int getNewBackgroundID() {
		return newBackgroundID;
	}
}
