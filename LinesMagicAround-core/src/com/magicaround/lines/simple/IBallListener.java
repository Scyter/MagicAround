package com.magicaround.lines.simple;

import com.magicaround.lines.Ball;

public interface IBallListener {
	public void movingStarted();

	public void movingFinished(int sign);

	public void rainFinished(Ball b);

}
