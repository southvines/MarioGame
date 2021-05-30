package com.itheima.mario;

import javax.swing.*;
import java.util.*;

public class Task extends TimerTask{
	Mario mario=Mario.getInstance(null);
	public void run() {
		mario.setcanHurt(true);
	}
}
