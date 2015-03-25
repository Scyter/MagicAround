package com.magicaround.moviesurveysactivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	public static final int axesSize = 6;
	public static final int lineSize = 4;

	private DrawThread drawThread;
	Context cnt;
	AttributeSet attribute;
	int diff;
	public ArrayList<Integer> data;
	// public int data[];
	public int answersSize;
	Canvas canvas;
	int drawen;
	int width;
	int height;

	public DrawSurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
		data = new ArrayList<Integer>();
	}

	public DrawSurfaceView(Context context, AttributeSet attr) {
		super(context, attr, 0);
		cnt = context;
		attribute = attr;
		getHolder().addCallback(this);
		data = new ArrayList<Integer>();

	}

	public DrawSurfaceView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		cnt = context;
		attribute = attr;
		getHolder().addCallback(this);
		data = new ArrayList<Integer>();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawThread = new DrawThread(getHolder());
		drawThread.setRunning(true);
		drawThread.start();
	}

	public void setDiff(int d) {
		diff = d;
	}

	public void setData(ArrayList<Integer> d) {
		data = d;
		drawen = 0;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		drawThread.setRunning(false);
		while (retry) {
			try {
				drawThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	class DrawThread extends Thread {
		Paint textPaint;
		Paint axesPaint;
		Paint cellsPaint;
		Paint linePaint;
		Paint positionTextPaint;

		int gStartX;
		int gStartY;
		int pointerDiff;
		// int gXSize;
		// int gYSize;

		int xSize;
		int ySize;
		private boolean running = false;
		private SurfaceHolder surfaceHolder;

		int littlePosition;
		int bigPosition;
		int positionSize;

		int stepSize;

		public DrawThread(SurfaceHolder surfaceHolder) {
			this.surfaceHolder = surfaceHolder;
			textPaint = new Paint();
			textPaint.setColor(Color.BLACK);
			textPaint.setTextSize(20);
			textPaint.setAntiAlias(true);

			positionTextPaint = new Paint();
			positionTextPaint.setColor(Color.RED);
			positionTextPaint.setTextSize(20);
			positionTextPaint.setAntiAlias(true);

			axesPaint = new Paint();
			axesPaint.setColor(Color.GRAY);
			axesPaint.setStrokeWidth(axesSize);

			cellsPaint = new Paint();
			cellsPaint.setColor(Color.GRAY);
			cellsPaint.setAlpha(100);
			cellsPaint.setStrokeWidth(3);

			linePaint = new Paint();
			linePaint.setStrokeWidth(lineSize);
			linePaint.setColor(Color.RED);
			drawen = 0;
		}

		public void setRunning(boolean running) {
			this.running = running;
		}

		@Override
		public void run() {
			// Log.d("aa", "Run Forrest");
			while (running) {
				canvas = null;
				try {
					int dataSize = data.size();
					if (dataSize == 0) {
						TimeUnit.MILLISECONDS.sleep(100);
						continue;
					}
					TimeUnit.MILLISECONDS.sleep(1000 / data.size());
					canvas = surfaceHolder.lockCanvas(null);
					if (canvas == null)
						continue;
					canvas.drawColor(Color.WHITE);
					// canvas.drawText("Hello Kitty" + String.valueOf(drawen),
					// 300, 20 + diff, textPaint);
					diff--;
					if (diff <= 0)
						diff = 500;
					if (drawen == 0)
						calculate();
					draw();
					drawen++;

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}

		private void calculate() {
			// Log.d("aa", "Calc Forrest");
			width = canvas.getWidth();
			height = canvas.getHeight();
			xSize = (int) (width * 0.9);
			ySize = (int) (height * 0.9);
			gStartX = (int) (width * 0.05);
			gStartY = (int) (height * 0.95);

			littlePosition = data.get(0);
			bigPosition = data.get(0);
			positionSize = 1;
			for (int d : data) {
				if (d < littlePosition)
					littlePosition = d;
				if (d > bigPosition)
					bigPosition = d;
			}
			littlePosition--;
			bigPosition++;
			positionSize = bigPosition - littlePosition;

		}

		private void draw() {
			drawAxes();
			drawScale();
			drawGraph();

		}

		private void drawGraph() {

			ArrayList<Integer> d = data;
			int lineXS;
			int lineYS;
			int lineXE;
			int lineYE;

			int xWay = xSize / (d.size() - 1);
			int yWay = ySize / positionSize;
			lineXS = gStartX;
			lineYS = gStartY - (bigPosition - d.get(0)) * yWay;
			lineXE = gStartX + xWay;
			lineYE = gStartY - (bigPosition - d.get(1)) * yWay;

			canvas.drawLine(lineXS, lineYS, lineXE, lineYE, linePaint);

			for (int i = 1; (i < d.size() - 1) && (i <= drawen); i++) {
				lineXS = gStartX + i * xWay;
				lineYS = gStartY - (bigPosition - d.get(i)) * yWay;
				lineXE = gStartX + i * xWay + xWay;
				lineYE = gStartY - (bigPosition - d.get(i + 1)) * yWay;
				canvas.drawLine(lineXS, lineYS, lineXE, lineYE, linePaint);
				// if (d.length <= 20)
				// i++;
				// else
				// i += 1 + (int) d.length / 20;
			}

		}

		private void drawAxes() {
			pointerDiff = (int) ((xSize > ySize) ? (ySize * 0.02)
					: (xSize * 0.02));
			canvas.drawLine(gStartX - 1, gStartY, gStartX + xSize, gStartY,
					axesPaint);
			canvas.drawLine(gStartX, gStartY + 1, gStartX, gStartY - ySize,
					axesPaint);
			canvas.drawCircle(gStartX, gStartY, axesSize / 2, axesPaint);

			canvas.drawLine(gStartX + xSize + 2, gStartY + 2, gStartX + xSize
					- pointerDiff, gStartY - pointerDiff, axesPaint);
			canvas.drawLine(gStartX + xSize + 2, gStartY - 2, gStartX + xSize
					- pointerDiff, gStartY + pointerDiff, axesPaint);
			canvas.drawLine(gStartX + 2, gStartY - ySize - 2, gStartX
					- pointerDiff, gStartY - ySize + pointerDiff, axesPaint);
			canvas.drawLine(gStartX - 2, gStartY - ySize - 2, gStartX
					+ pointerDiff, gStartY - ySize + pointerDiff, axesPaint);
		}

		private void drawScale() {
			ArrayList<Integer> d = data;
			int xWay = xSize / (d.size() - 1);// NB при числе точек
												// большей, чем
												// число пискелей
												// на экране, не
												// рисует график.
												// Надо сделать
												// масштабирование
			int yWay = ySize / positionSize;
			for (int i = 1; i < positionSize;) {
				canvas.drawText(String.valueOf(bigPosition - i), gStartX
						- (int) (0.04 * width), gStartY - i * yWay, textPaint);
				if (positionSize <= 20)
					i++;
				else
					i += (int) positionSize / 20;
			}
			for (int i = 1; i <= positionSize;) {
				canvas.drawLine(gStartX, gStartY - i * yWay, gStartX + xSize,
						gStartY - i * yWay, cellsPaint);
				if (positionSize <= 20)
					i++;
				else
					i += (int) positionSize / 20;
			}
			for (int i = 1; (i <= d.size());) {
				canvas.drawLine(gStartX + i * xWay, gStartY,
						gStartX + i * xWay, gStartY - ySize, cellsPaint);
				if (d.size() <= 20)
					i++;
				else
					i += (int) d.size() / 20;
			}

		}
	}

}