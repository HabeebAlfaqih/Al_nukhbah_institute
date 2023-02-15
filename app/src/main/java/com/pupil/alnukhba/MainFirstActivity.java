package com.pupil.alnukhba;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.animation.*;
import android.view.animation.*;
public class MainFirstActivity extends Activity
{
	Button ok;
boolean firstopen=false;
SharedPreferences sh;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_first);
		ok=(Button)findViewById(R.id.ok);
		sh=getSharedPreferences("pre",0);
		firstopen=sh.getBoolean("first",false);
	
		if(firstopen){
			startActivity(new Intent(this,MainSignActivity.class));
			finish();
		}
		ok.setOnClickListener(new View.OnClickListener(){
			@Override 
			public void onClick(View v){
				sh.edit().putBoolean("first",true).commit();
				startActivity(new Intent(getApplicationContext(),MainSignActivity.class));
				finish();
			}
		});
	}
	
}
