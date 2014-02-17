package com.android.jamesgrafton.sinEgen.audio;

import java.util.ArrayList;
import java.util.Hashtable;

import android.os.Message;
import android.util.Log;
import android.widget.Button;

import com.android.jamesgrafton.sinEgen.R;
import com.android.jamesgrafton.sinEgen.ChangeButtonBackgroundWrapper;
import com.android.jamesgrafton.sinEgen.SinEgen;
import com.android.jamesgrafton.sinEgen.SinEgen.MyHandler;

public class AudioGenThread implements Runnable {
	private final static int DELAY = 500;
	private int currentColumnLoc = 0;

	private volatile Hashtable<Integer, Boolean> buttonStates;
	private Button[][] allButtons;

	// UI handler ref
	private MyHandler mHandler;

	public AudioGenThread(Hashtable<Integer, Boolean> buttonStates,
			Button[][] allButtons, MyHandler mHandler) {
		this.buttonStates = buttonStates;
		this.allButtons = allButtons;
		this.mHandler = mHandler;
	}

	public void run() {
		ArrayList<Button> needResetting = new ArrayList<Button>(6);
		while (true) {
			Log.i(SinEgen.TAG, "Column " + currentColumnLoc);
			for (Button button : needResetting) {
				ChangeButtonBackgroundWrapper messageObject = new ChangeButtonBackgroundWrapper(
						button, R.drawable.button_on);
				Message message = Message.obtain();
				message.obj = messageObject;
				this.mHandler.sendMessage(message);
				// Log.i(SinEgen.TAG,"removing message");
			}
			needResetting.clear();
			Button[] currentColumnArray = allButtons[currentColumnLoc];
			for (int i = 0; i < currentColumnArray.length; i++) {
				if (buttonStates.get(currentColumnArray[i].getId())) {
					// Make a sound
					// Do animation
					ChangeButtonBackgroundWrapper messageObject = new ChangeButtonBackgroundWrapper(
							currentColumnArray[i], R.drawable.button_glow);
					Message message = Message.obtain();
					message.obj = messageObject;
					// Log.i(SinEgen.TAG,"Sending message");
					this.mHandler.sendMessage(message);
					needResetting.add(currentColumnArray[i]);
				}
			}
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentColumnLoc = ((++currentColumnLoc) % (SinEgen.MAX_COLUMN));
		}
	}
}
