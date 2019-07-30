package com.xiaojianya.view;

import java.io.FileDescriptor;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * 颜色选择器，用于实现色相环颜色选择效果
 * 
 * @author sk8snow
 * 
 */
public class ColorPicker extends View {
	/**
	 * 自定义画笔，用于绘制控件
	 */
	private Paint mPaint;
	/**
	 * 控件高
	 */
	private int mHeight;
	/**
	 * 控件宽
	 */
	private int mWidth;
	/**
	 * 控件中心点X坐标
	 */
	private int centerX;
	/**
	 * 控件中心点Y坐标
	 */
	private int centerY;
	/**
	 * 色块大小
	 */
	private int colorBlockSize = 0;
	/**
	 * 触摸点X坐标
	 */
	private float touchX = 0;
	/**
	 * 触摸点Y坐标
	 */
	private float touchY = 0;

	/**
	 * 默认大小，使用dp作为单位
	 */
	private static final int DEFAULT_WIDTH_DP = 300;
	/**
	 * 默认大小，使用dp作为单位
	 */
	private static final int DEFAULT_HEIGHT_DP = 300;
	/**
	 * 选中色相环ID
	 */
	private int currentColorArrId = -1;
	/**
	 * 选中环中颜色ID
	 */
	private int currentColorId = -1;
	/**
	 * 是否触点释放
	 */
	private boolean isRelease = false;
	/**
	 * 是否选择的色块改变
	 */
	private boolean isBlockChanged = false;

	/**
	 * 色相环颜色值
	 */
	private String[][] colorArray = {
			{ "#fef5ce", "#fff3cd", "#feeeca", "#fdeac9", "#fee7c7", "#fce3c4",
					"#fbddc1", "#fad7c3", "#fad0c2", "#f2ced0", "#e6cad9",
					"#d9c7e1", "#d2c3e0", "#cfc6e3", "#cac7e4", "#c9cde8",
					"#c7d6ed", "#c7dced", "#c7e3e6", "#d2e9d9", "#deedce",
					"#e7f1cf", "#eef4d0", "#f5f7d0" },
			{ "#ffeb95", "#fee591", "#fcdf8f", "#fcd68d", "#facd89", "#f9c385",
					"#f7b882", "#f5ab86", "#f29a82", "#e599a3", "#ce93b3",
					"#b48cbe", "#a588be", "#9d8cc2", "#9491c6", "#919dcf",
					"#89abd9", "#85bada", "#86c5ca", "#9fd2b1", "#bada99",
					"#cbe198", "#dde899", "#edf099" },
			{ "#fee250", "#fed84f", "#fbce4d", "#f9c04c", "#f7b24a", "#f6a347",
					"#f39444", "#f07c4d", "#ec614e", "#d95f78", "#b95b90",
					"#96549e", "#7c509d", "#6e59a4", "#5c60aa", "#5572b6",
					"#3886c8", "#1c99c7", "#0daab1", "#57ba8b", "#90c761",
					"#b0d35f", "#ccdd5b", "#e5e756" },
			{ "#FDD900", "#FCCC00", "#fabd00", "#f6ab00", "#f39801", "#f18101",
					"#ed6d00", "#e94520", "#e60027", "#cf0456", "#a60b73",
					"#670775", "#541b86", "#3f2b8e", "#173993", "#0c50a3",
					"#0168b7", "#0081ba", "#00959b", "#03a569", "#58b530",
					"#90c320", "#b8d201", "#dadf00" },
			{ "#DBBC01", "#DAB101", "#D9A501", "#D69400", "#D28300", "#CF7100",
					"#CD5F00", "#CA3C18", "#C7001F", "#B4004A", "#900264",
					"#670775", "#4A1277", "#142E82", "#0A448E", "#005AA0",
					"#0070A2", "#018287", "#02915B", "#4A9D27", "#7DAB17",
					"#9EB801", "#BCC200", "#DBBC01" },
			{ "#B49900", "#B39000", "#B18701", "#AD7901", "#AB6B01", "#AA5B00",
					"#A84A00", "#A62D10", "#A50011", "#94003C", "#770050",
					"#540060", "#3B0263", "#2B1568", "#10226C", "#053577",
					"#004A87", "#005D88", "#006C6F", "#00784A", "#38831E",
					"#648B0A", "#829601", "#999F01" },
			{ "#9F8700", "#9E7F00", "#9D7601", "#9A6900", "#995E00", "#975000",
					"#954000", "#932406", "#92000B", "#840032", "#6A0048",
					"#4A0055", "#320057", "#240D5D", "#0C1860", "#032C6A",
					"#014076", "#005278", "#016064", "#006B41", "#2E7316",
					"#567C03", "#718500", "#888D00" } };

	/**
	 * 颜色选择监听器，设置此监听器用于监听控件中被选择颜色的改变
	 */
	private OnColorSelectedListener mListener;
	/**
	 * 颜色选中音效文件
	 */
	private FileDescriptor beepFile;

	public ColorPicker(Context context) {
		this(context, null);
	}

	public ColorPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 设置色相环颜色
	 * 
	 * @param colorArray
	 *            各色相环颜色数组，由内向外依次绘制，索引0绘制于最内侧色环
	 */
	public void setColor(String[][] colorArray) {
		this.colorArray = colorArray;
		int radius = getBigCircleRadius();
		/*
		 * 计算色块大小
		 */
		colorBlockSize = radius / (colorArray.length + 1);
		postInvalidate();
	}

	/**
	 * 设置控件颜色选择监听器，用于监听控件选择颜色的改变
	 * 
	 * @param listener
	 *            颜色选择监听器
	 */
	public void setOnColorSelectedListener(OnColorSelectedListener listener) {
		mListener = listener;
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		/*
		 * 初始化选中音频文件，若需实现选中音效，去掉此处注释
		 */
//		beepFile = getAssetsFile(getContext(), "beep.mp3");
	}

	/**
	 * 获取屏幕密度，用于屏幕适配
	 * 
	 * @return 屏幕密度
	 */
	private float getScreenDensity() {
		DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		return dm.density;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawColors(canvas);
		drawTouchBlock(canvas);

		/*
		 * 播放选中音效，，若需实现选中音效，去掉此处注释
		 */
//		if (isBlockChanged) {
//			playBeep();
//		}

	}

	/**
	 * 从Asset文件夹中读入资源文件
	 * 
	 * @param context
	 * @param filename
	 *            资源文件名称
	 * @return
	 */
	private FileDescriptor getAssetsFile(Context context, String filename) {
		AssetFileDescriptor assetFileDescriptor = null;
		try {
			assetFileDescriptor = context.getResources().getAssets()
					.openFd(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (assetFileDescriptor != null) {
			return assetFileDescriptor.getFileDescriptor();
		} else {
			return null;
		}
	}

	// 播放选中音效
	private void playBeep() {

		MediaPlayer mp = new MediaPlayer();

		try {
			mp.setDataSource(beepFile);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.release();
			}
		});

		mp.start();

	}

	/**
	 * 绘制色相环
	 * 
	 * @param canvas
	 */
	private void drawColors(Canvas canvas) {
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		canvas.drawCircle(centerX, centerY, colorBlockSize, mPaint);
		for (int colorArrId = 0; colorArrId < colorArray.length; colorArrId++) {
			String[] colors = colorArray[colorArrId];
			/* 根据当前环颜色数计算角度步长 */
			int angleStep = 360 / colors.length;
			int radius = (colorArrId + 1) * colorBlockSize + colorBlockSize / 2;
			for (int colorId = 0; colorId < colors.length; colorId++) {
				int startAngle = -angleStep / 2 + colorId * angleStep;
				drawColorBlock(canvas, radius, colors[colorId], startAngle,
						angleStep);
			}

		}
	}

	/**
	 * 绘制选中效果
	 * 
	 * @param canvas
	 */
	private void drawTouchBlock(Canvas canvas) {
		if (touchX == 0 && touchY == 0) {
			return;
		}

		if (currentColorArrId == 0) {
			mPaint.setStyle(Style.FILL);
			mPaint.setColor(Color.parseColor("#33ffffff"));
			canvas.drawCircle(centerX, centerY, colorBlockSize, mPaint);
		} else {
			String[] colors = colorArray[currentColorArrId - 1];
			/* 根据当前环颜色数计算角度步长 */
			int angleStep = 360 / colors.length;
			int radius = (currentColorArrId + 1) * colorBlockSize
					+ colorBlockSize / 2;
			int startAngle = -angleStep / 2 + currentColorId * angleStep;
			drawColorBlock(canvas, radius, "#33ffffff", startAngle, angleStep);
		}
	}

	/**
	 * 获取触点与圆心的距离
	 * 
	 * @return 触点与圆心的距离
	 */
	private double getDistanceFromCenter() {
		float factor = (touchX - centerX) * (touchX - centerX);
		factor += (touchY - centerY) * (touchY - centerY);
		return Math.sqrt(factor);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		touchX = event.getX();
		touchY = event.getY();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			isRelease = true;
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_DOWN:
			isRelease = false;
			break;
		}
		isBlockChanged = false;
		computeTouchBlock();
		postInvalidate();
		return true;
	}

	/**
	 * 计算当前选中的色块位置
	 */
	private void computeTouchBlock() {
		if (touchX == 0 && touchY == 0) {
			return;
		}

		int tmpColorArrId = currentColorArrId;
		int tmpColorId = currentColorId;

		double distanceFromCenter = getDistanceFromCenter();
		if (distanceFromCenter >= getBigCircleRadius()) {
			return;
		}

		/*
		 * 计算色环ID
		 */
		currentColorArrId = (int) (distanceFromCenter / colorBlockSize);
		if (currentColorArrId == (colorArray.length + 1)) {
			currentColorArrId -= 1;
		}

		/*
		 * 计算当前选中的色块在色环中的位置
		 */
		if (currentColorArrId != 0) {
			String[] colors = colorArray[currentColorArrId - 1];
			/* 根据当前环颜色数计算角度步长 */
			int angleStep = 360 / colors.length;
			int angle = (int) (Math.atan2(touchY - centerY, touchX - centerX) * 180 / Math.PI);
			angle %= 360;
			if (angle < -angleStep / 2) {
				angle += 360;
			}

			if (angle > 360 - angleStep / 2) {
				angle -= 360;
			}
			currentColorId = angle / angleStep;
		}

		/*
		 * 判断选择的色块是否已经改变
		 */
		if (tmpColorArrId != currentColorArrId || tmpColorId != currentColorId) {
			isBlockChanged = true;
		}

		if (mListener == null) {
			return;
		}

		/**
		 * 处理颜色监听
		 */
		if (currentColorArrId != 0) {
			String color = colorArray[currentColorArrId - 1][currentColorId];
			int colorInInt = Color.parseColor(color);
			if (isRelease) {
				isRelease = false;
				mListener.onColorSelected(Color.red(colorInInt),
						Color.green(colorInInt), Color.blue(colorInInt));
			} else {
				mListener.onColorSelecting(Color.red(colorInInt),
						Color.green(colorInInt), Color.blue(colorInInt));
			}
		} else {
			if (isRelease) {
				isRelease = false;
				mListener.onColorSelected(0xff, 0xff, 0xff);
			} else {
				mListener.onColorSelecting(0xff, 0xff, 0xff);
			}
		}
	}

	/**
	 * 绘制色块
	 * 
	 * @param canvas
	 * @param radius
	 *            半径
	 * @param color
	 *            色块颜色
	 * @param startAngle
	 *            开始角度
	 * @param sweepAngle
	 *            覆盖角度
	 */
	private void drawColorBlock(Canvas canvas, int radius, String color,
			int startAngle, int sweepAngle) {
		mPaint.setStrokeWidth(colorBlockSize);
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.parseColor(color));
		RectF oval = new RectF(centerX - radius, centerY - radius, centerX
				+ radius, centerY + radius);
		canvas.drawArc(oval, startAngle, sweepAngle, false, mPaint);
	}

	/**
	 * 获取整个色环的半径，取宽和高中最小值的二分之一减去8像素
	 * 
	 * @return 色环半径
	 */
	private int getBigCircleRadius() {
		int radius = mWidth > mHeight ? mHeight / 2 : mWidth / 2;
		return radius - 8;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		float density = getScreenDensity();
		int defaultWidth = (int) (DEFAULT_WIDTH_DP * density);
		int defaultHeight = (int) (DEFAULT_HEIGHT_DP * density);
		if (widthMode == MeasureSpec.UNSPECIFIED
				|| widthMode == MeasureSpec.AT_MOST) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(defaultWidth,
					MeasureSpec.EXACTLY);
			mWidth = defaultWidth;
		} else {
			mWidth = widthSize;
		}

		if (heightMode == MeasureSpec.UNSPECIFIED
				|| heightMode == MeasureSpec.AT_MOST) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(defaultHeight,
					MeasureSpec.EXACTLY);
			mHeight = defaultHeight;
		} else {
			mHeight = heightSize;
		}

		centerX = mWidth / 2;
		centerY = mHeight / 2;
		int radius = getBigCircleRadius();
		/*
		 * 计算色块大小
		 */
		colorBlockSize = radius / (colorArray.length + 1);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 颜色选择监听器，用于监听颜色的改变，在选择颜色过程中，当颜色改变时且未释放触点时调用onColorSelecting方法，
	 * 在选择过程中所有被临时选中的颜色
	 * 都将通过onColorSelecting方法回传；当最终选择的颜色确定时，调用onColorSelected，此方法回传最终选择的颜色
	 * 
	 * @author sk8snow
	 * 
	 */
	public interface OnColorSelectedListener {
		/**
		 * 颜色选中改变，当未释放触点时调用此方法，此方法在选择颜色过程中可能颜色改变时就会被调用，整个过程中会被多次调用，
		 * 但所传值为选择的不同颜色值
		 * 
		 * @param red
		 * @param green
		 * @param blue
		 */
		public void onColorSelecting(int red, int green, int blue);

		/**
		 * 颜色选中改变，选中最终颜色，此方法在触点被释放时调用，在选择颜色过程中只会调用一次，所传值为最终选择的颜色值
		 * 
		 * @param red
		 * @param green
		 * @param blue
		 */
		public void onColorSelected(int red, int green, int blue);
	}
}
