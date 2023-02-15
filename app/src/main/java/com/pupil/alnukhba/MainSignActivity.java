package com.pupil.alnukhba;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.pupil.alnukhba.volley.*;
import java.util.*;

public class MainSignActivity extends Activity
{
	int typeaccount=-1;
Button sginin,sginup,nonsgin;
EditText txtemail,txtpassword;
SharedPreferences sh;
PupilsDBUtility dbutility;
String email,password;
int account=-1,idme=-1,id=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sginmain);
		sginin=(Button)findViewById(R.id.sginin);
		sginup=(Button)findViewById(R.id.sginuptemp);
		nonsgin=(Button)findViewById(R.id.nonsgin);
		txtemail=(EditText)findViewById(R.id.email_sginin);
		txtpassword=(EditText)findViewById(R.id.password_sginin);
		sendBroadcast(new Intent("startservice"));
		dbutility=new PupilsDBUtility(this,MySqliteHelper.DATABASE_NAME,MySqliteHelper.DATABASE_VERSION);
		dbutility.open();
		
		sh=getSharedPreferences("pre",0);
		idme=sh.getInt("idme",-1);
		
		if(!sh.getBoolean("first",false)){
			startActivity(new Intent(getApplicationContext(),MainFirstActivity.class));
			dbutility.insertall();
			finish();
			}
		
		if(idme!=-1){
			typeaccount=dbutility.getTypeAccount(idme);
			}
		if(typeaccount!=-1&& idme!=-1){
			sh.edit().putInt("type_account",typeaccount).commit();
			Pupils p=dbutility.getAllDataPupil(idme);
			email=p.getEmail();
			password=p.getPassword();
			loginPost();
			
		}
		
		
		
		sginin.setOnClickListener(new View.OnClickListener(){
				@Override 
				public void onClick(View v){
					email=txtemail.getText().toString().toLowerCase();
					password=txtpassword.getText().toString().toLowerCase();
					dbutility.open();
				     idme=dbutility.getId(email,password);
					sh.edit().putInt("idme",idme).commit();
					if(idme>-1)
					loginPost();
					else{
						
					}
			}});
			sginup.setOnClickListener(new View.OnClickListener(){
				@Override 
				public void onClick(View v){
					startActivity(new Intent(getApplicationContext(),InsertActivity.class));
				
				}
			});
			nonsgin.setOnClickListener(new View.OnClickListener(){
			@Override 
			public void onClick(View v){
				sh.edit().putInt("type_account",0).commit();
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
				finish();
			}
			});
	}
	HashMap map=new HashMap<String,String>();
	public void loginPost(){
		final String link =new String( "http://localhost:8080/ser/loginp.php");

		
		map.put("email",email);
		map.put("password",password);
		try{
			new SignAsyncTask(this,1,link,
							  (Response.Listener)new Response.Listener(){
								  public void onResponse(String s){
									  try{
									  String[] arrData=s.toString().split(",");
									  if(Integer.valueOf(arrData[0])==1)
									  {
										  String t=arrData[2].toString();
										  account=Integer.valueOf(t);
										 sh.edit().putInt("type_account",account).commit();
										dbutility.open();
										dbutility.upDateType(idme,account);
										  startActivity(new Intent(getApplicationContext(),MainActivity.class));
										  Toast.makeText(getApplicationContext(),"connect to server successfully ",Toast.LENGTH_SHORT).show();
										  finish();
									  }
									  else if(Integer.valueOf(arrData[0])==0)
									  {
										  if(Integer.valueOf(arrData[2])==0){
									DBUtilityServer dd=new DBUtilityServer(getApplicationContext());
									dd.sendDataAccountToServer(idme);
										
										  account=0;
										  sh.edit().putInt("type_account",account).commit();
										  dbutility.open();
										  dbutility.upDateType(idme,account);
										  startActivity(new Intent(getApplicationContext(),MainActivity.class));
											  Toast.makeText(getApplicationContext()," not found account in server ",Toast.LENGTH_SHORT).show();
											  finish();
											  
									  }
									  else if(Integer.valueOf(arrData[2])==1){
										  DBUtilityServer dd=new DBUtilityServer(getApplicationContext());
										  dd.deleteAccountFormServer(idme);
										  dbutility.deletePupil(dbutility.getId(email,password));
										  sh.edit().putInt("idme",-1).putInt("type_account",-1).commit();
										  startActivity(new Intent(getApplicationContext(),MainActivity.class));
										  MainSignActivity.this.finish();
										  
									  }}
									  } catch (Exception e){  }
								 }
							  },(Response.ErrorListener)new Response.ErrorListener(){
								  public void onErrorResponse(String error){
									  try{
								  if(email.length()>1&&password.length()>1){
										  try{
											  account= dbutility.validAccount(email,password);
											  sh.edit().putInt("type_account",account).commit();
										  } catch (Exception e){  }
										  if(account!=-1){
										  Toast.makeText(getApplicationContext()," not connect internet ",Toast.LENGTH_SHORT).show();
										  
												  startActivity(new Intent(getApplicationContext(),MainActivity.class));
												  finish();
											}
										  }} catch (Exception e){  }
									  
								  }
							  },map,null
							  ).execute();} catch (Exception e){  }
	}
	
   }
