package com.xiaojianya.colorpickerdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojianya.view.ColorPicker;
import com.xiaojianya.view.ColorPicker.OnColorSelectedListener;


public class MainActivity extends Activity implements OnClickListener {
	private ColorPicker colorPicker;
	private TextView colorValueTxt;
	private ImageView colorIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
    	colorPicker = (ColorPicker)findViewById(R.id.color_picker);
    	colorPicker.setOnColorSelectedListener(new OnColorSelectedListener() {
			
			@Override
			public void onColorSelecting(int red, int green, int blue) {
				// TODO Auto-generated method stub
				String value = "红色：" +  red + "\t绿色：" + green + "\t蓝色:" + blue;
				int color = Color.rgb(red, green, blue);
				value += "\n颜色值：0x" + Integer.toHexString(color);
				colorValueTxt.setText(value);
				colorIndicator.setBackgroundColor(color);
			}
			
			@Override
			public void onColorSelected(int red, int green, int blue) {
				// TODO Auto-generated method stub
				String value = "红色：" +  red + "\t绿色：" + green + "\t蓝色:" + blue;
				int color = Color.rgb(red, green, blue);
				value += "\n颜色值：0x" + Integer.toHexString(color);
				colorValueTxt.setText(value);
				colorIndicator.setBackgroundColor(color);
			}
		});
    	
    	colorValueTxt = (TextView)findViewById(R.id.color_value_txt);
    	colorIndicator = (ImageView)findViewById(R.id.color_indicator);
    	findViewById(R.id.change_color_btn).setOnClickListener(this);
    }
    
    @Override
    public void onClick(View arg0) {
    	// TODO Auto-generated method stub
    	
    	String[][] colorArray = {
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
    					"#9EB801", "#BCC200", "#DBBC01" }};
    	colorPicker.setColor(colorArray);
    	
    }

}
