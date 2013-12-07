package dev.ds.emlbsm.activity;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import dev.ds.emlbsm.R;

public class LegendActivity extends Activity {


	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ArrayList<String> oTypes = getIntent().getStringArrayListExtra("oTypes");
    ArrayList<String> colors = getIntent().getStringArrayListExtra("colors");
	// setContentView(R.layout.rainbow);
    LinearLayout ll = new LinearLayout(this);
	ll.setOrientation(LinearLayout.VERTICAL);
	ll.setGravity(Gravity.CENTER);
	Vector<TextView> textViews = new Vector<TextView>();
	for(int i = 0; i < oTypes.size(); i++)
    {                       
    	System.out.println("opoooooooo:"+i);
        textViews.add(new TextView(this));
        System.out.println("obt : "+oTypes.get(i));
        textViews.get(i).setTextSize(20);
        textViews.get(i).setTextColor(Color.parseColor(colors.get(i))); 
        textViews.get(i).setText(oTypes.get(i));     
        textViews.get(i).setGravity(Gravity.CENTER);
        ll.addView(textViews.get(i));
    }
	/*TextView tv1 = new TextView(this);
	tv1.setText("FIRST");
	tv1.setTextSize(10);
	tv1.setGravity(Gravity.CENTER);

	TextView tv2 = new TextView(this);
	tv2.setTextSize(10);
	tv2.setGravity(Gravity.CENTER);
	tv2.setText("MIDDLE");
	*/
	 
	setContentView(ll);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_legend, menu);
		return true;
	}

}
