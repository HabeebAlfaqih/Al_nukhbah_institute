package com.pupil.alnukhba;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import com.pupil.alnukhba.volley.*;

import android.graphics.Interpolator;

public class MainActivity extends Activity
 {
	Button fees,account,degrees,browser;
	int idme=-1;
	int typeaccount=-1;
	boolean firstopen=false;
	SharedPreferences sha;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		getMenuInflater().inflate(R.menu.menu,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId())
		{
			case R.id.share:
				break;
			case R.id.settings:
			  break;
			case R.id.signout:
				typeaccount= -1;
				idme=-1;
			SharedPreferences.Editor shed=	getSharedPreferences("pre",0).edit();
				shed.putInt("idme",idme).commit();
				shed.putInt("type_account",typeaccount).commit();
				startActivity(new Intent(this,MainSignActivity.class));
				finish();
				break;
			case R.id.exit:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
 @Override 
 protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);
 PupilsDBUtility pdb=new PupilsDBUtility(this,MySqliteHelper.DATABASE_NAME,MySqliteHelper.DATABASE_VERSION);
	 pdb.open();
	sha =getSharedPreferences("pre",0);
	 firstopen=sha.getBoolean("first",false);
     account=(Button)findViewById(R.id.account);
	 degrees = (Button)findViewById(R.id.degrees);
	 browser = (Button)findViewById(R.id.browser);
	 fees = (Button)findViewById(R.id.fees);
     
     
     Animation shake = AnimationUtils.loadAnimation(this, R.anim.shakeanim);
     shake.setDuration(100);
     ImageView imgBell1= (ImageButton) findViewById(R.id.ro1);
     imgBell1.startAnimation(shake);

	 idme=sha.getInt("idme",-1);
	 typeaccount=sha.getInt("type_account",-1);
	 if(idme!=-1){
		 DBUtilityServer dbs=	 new DBUtilityServer(getApplicationContext());
		 dbs.getUpdateAccountMeFromServer(idme,new Response.UpdateListener(){public void onUpdate(int i){
			 if(i!=typeaccount){
				 startActivity(new Intent(getApplicationContext(),MainActivity.class));
				 finish();}
			 
		 }});
		 dbs.getDegreesPupil(idme);
	 }
	 if(sha.getBoolean("initial",true))
	 {
		 pdb.insertall();
		 sha.edit().putBoolean("initial",false).commit();
	 }
	 if(!firstopen)
	 {
		 startActivity(new Intent(this,MainFirstActivity.class));
		 finish();
	 }else if(firstopen&&typeaccount==-1)
	 {
		 startActivity(new Intent(this,MainSignActivity.class));
		 finish();
	 }
	else  if(typeaccount==0&&firstopen){
		 degrees.setEnabled(false);
		 fees.setEnabled(false);
		 degrees.setBackgroundResource(R.drawable.radio_rounded_corner_invisible);
		 fees.setBackgroundResource(R.drawable.radio_rounded_corner_invisible);
	 }else if(typeaccount>0&&idme!=-1&&firstopen){
		 fees.setEnabled(true);
		 degrees.setEnabled(true);
		 }
	
	 account.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v) {
				 if(idme!=-1){
				 Intent intent = new Intent(MainActivity.this,discripactivity.class);
				intent.putExtra("id",idme);
				 startActivity(intent);}
			 }
		 });
		
	 degrees.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v) {
				 startActivity(new Intent(getApplicationContext(),DegreesActivity.class));
				 
				 }});
				 
	 fees.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v) {
				 
				 startActivity(new Intent(getApplicationContext(),ShowFees.class));
				 
			 }});
	 
	 browser.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v) {
				 
			 }});
 
 
 }

 @Override
 protected void onResume()
 {
	 // TODO: Implement this method
	 super.onResume();
 }

}
