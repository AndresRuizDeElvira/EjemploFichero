 
package com.example.ejemplofichero;

import android.widget.AdapterView.OnItemSelectedListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.StrictMode;
import java.io.File;
import android.widget.AdapterView.OnItemLongClickListener;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import android. graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Collections;
import java.util.Arrays;  
import java.util.List;  
import java.util.ArrayList;  
import java.util.Locale;
import java.io.StringWriter;
import java.io.PrintWriter;
import android.view.Menu;
import android.app.ListActivity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Object;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import   java.util.ArrayList;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.content.res. AssetManager; 
import java.io.InputStream;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DeltaEntry;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import java.util.Comparator;

public class AndroidListViewActivity extends Activity {
    private static final String TAG = "AndroidListViewActivity";

    ///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////

    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
    final static private String APP_KEY = "3osybic4fqg3m3s";
    final static private String APP_SECRET = "w1k5mp0yrvz0k8i";

    // If you'd like to change the access type to the full Dropbox instead of
    // an app folder, change this value.
    final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    // You don't need to change these, leave them alone.
    // You don't need to change these, leave them alone.
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    DropboxAPI<AndroidAuthSession> mApi;

    private boolean mLoggedIn;
    private ListView m_listview;
    // Android widgets
    private Button mSubmit;
    private LinearLayout mDisplay;
    private Button mPhoto;
    private Button mRoulette;

    private ImageView mImage;
    private ArrayList<Entry> thumbs1 = new ArrayList<Entry>();
    private List<String> nombres2= new ArrayList<String>();	
    private List<String> nombres3= new ArrayList<String>();
    private final String PHOTO_DIR = "/Photos/";

    final static private int NEW_PICTURE = 1;
    private String mCameraFileName;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Date> dates=new ArrayList<Date>();
        String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
        SimpleDateFormat format = new SimpleDateFormat(pattern,Locale.ENGLISH);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        if (savedInstanceState != null) {
            mCameraFileName = savedInstanceState.getString("mCameraFileName");
        }
        Comparator<Date> comparator = new Comparator<Date>() {
        	 
            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        };

        // We create a new AuthSession so that we can use the Dropbox API.
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        // Basic Android widgets
        setContentView(R.layout.main);
        System.out.println("Hola..............");
        checkAppKeySetup();

        mSubmit = (Button)findViewById(R.id.auth_button);

        mSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // This logs you out if you're logged in, or vice versa
                if (mLoggedIn) {
                    logOut();
                } else {
                    // Start the remote authentication
                    mApi.getSession().startAuthentication(AndroidListViewActivity.this);
                }
            }
        });

        mDisplay = (LinearLayout)findViewById(R.id.logged_in_display);

        // This is where a photo is displayed
       // mImage = (ImageView)findViewById(R.id.image_view);

        // This is the button to take a photo
        mPhoto = (Button)findViewById(R.id.photo_button);

        mPhoto.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                
                // Picture from camera
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                // This is not the right way to do this, but for some reason, having
                // it store it in
                // MediaStore.Images.Media.EXTERNAL_CONTENT_URI isn't working right.

                Date date = new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss");

                String newPicFile = df.format(date) + ".jpg";
                String outPath = "/sdcard/" + newPicFile;
                File outFile = new File(outPath);

                mCameraFileName = outFile.toString();
                Uri outuri = Uri.fromFile(outFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
                Log.i(TAG, "Importing New Picture: " + mCameraFileName);
                try {
                    startActivityForResult(intent, NEW_PICTURE);
                } catch (ActivityNotFoundException e) {
                    showToast("There doesn't seem to be a camera.");
                }
            }
        });


        // This is the button to take a photo
        mRoulette = (Button)findViewById(R.id.roulette_button);
   
        mRoulette.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	
                //DownloadRandomPicture download = new DownloadRandomPicture(DbRoulette.this, mApi, PHOTO_DIR, mImage);
                System.out.println("Hola");
                try{ 
                	 Entry dirent1=mApi.metadata("/", 1000, null, true, null);
        	    System.out.println("HOLA2");
                } catch (Exception ex) {
                	
                	String message = getStackTrace(ex);
                	System.out.println(message);
                	
                }
               // download.execute();
            }
        });
        
      
        // Display the proper UI state if logged in or not
       
      
        try{ 
        	System.out.println("HOLA");
        	//String a1="\";
        	ArrayList<String> folderName=new ArrayList<String>();
        	ArrayList<String> nombres1= new ArrayList<String>();
            ArrayList<String> path2= new ArrayList<String>();
            //Entry dropboxDir2 = mApi.metadata("/", 0, null, true, null);
            
            //System.out.println("\n\n\nHola5\n\n");
            //DropboxAPI.DeltaPage deltapage=mApi.delta();
            //deltapage.
           //ystem.out.println(deltapage.entries.size());
        	  Entry dropboxDir1 = mApi.metadata(PHOTO_DIR, 0, null, true, null);  
        	    System.out.println("HOLA3");
        	     if (dropboxDir1.isDir) { 
        	       
        	    	 System.out.println("HOLA");
        	            List<Entry> contents1 = dropboxDir1.contents;
        	             
        	           if (contents1 != null) {
        	            folderName.clear();
        	             
        	                for (int i = 0; i < contents1.size(); i++) {
        	                    Entry e = contents1.get(i);
        	                    try
        	                    {
        	                    	 dates.add(format.parse(e.modified));
        	                    }
        	                    catch(Exception ex)
        	                    {
        	                    	System.out.println("Hola1233123");
        	                    	getStackTrace(ex);
        	                    	System.out.println(ex);
        	                    }
        	                    
        	                    String a = e.fileName();  
        	                    System.out.println(e.fileName());
        	                    if ((e.mimeType).equals("application/epub+zip"))
        	                    {
        	                    	 thumbs1.add(e);	
        	                         nombres2.add(e.fileName());
        	                        dates.add(format.parse(e.modified));
        	                         System.out.println("HOLA4path");
        	                         System.out.println(e.path);
        	                         //System.out.println(dates.get(1).getDate());
        	                         System.out.println(e.modified);
        	                         System.out.println(thumbs1.get(0).fileName());
        	                         //dates.add(format.parse(e.modified));	
        	                    	
        	                    }	
        	                    	
        	                    if(String.valueOf(e.isDir).equalsIgnoreCase("true")){
        	                     folderName.add(a);
        	                    }
        	                 }
        	           }
        	     }}catch (Exception ex) {
        	             getStackTrace(ex);
        	             System.out.println(ex);
        	                    
        	           }
        
        AssetManager assetManager = getAssets();
        
       // try {c
            // find InputStream for book
        	//String cachePath = getAbsolutePath() + "/" + IMAGE_FILE_NAME;
          //  InputStream epubInputStream = assetManager.open(thumbs1.get(0).path);

            // Load Book from inputStream
            //Book book = (new EpubReader()).readEpub(epubInputStream);
            //Log.i("epublib", "title: " + book.getTitle());

            // Log the book's coverimage property
           // Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage()
                  //  .getInputStream());
           // Log.i("epublib", "Coverimage is " + coverImage.getWidth() + " by "
                    //+ coverImage.getHeight() + " pixels");

            // Log the book's authors
            //Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());
      //  } catch (IOException e) {
        	
        //	String message = getStackTrace(e);
        //	System.out.println(message);
     //   }
        m_listview = (ListView) findViewById(R.id.list);
        String[] items = new String[thumbs1.size()];
      final  String[] ite={"1","2","3"};
      int [] numeros=new int[thumbs1.size()];
        String[] itemsOrdenadosNombre1=new String[thumbs1.size()];
         String[] itemsOrdenadosNombre=new String[thumbs1.size()];
         String[] itemsOrdenadosFecha11=new String[thumbs1.size()];
       final  String[] itemsOrdenadosFecha=new String[thumbs1.size()];
       Collections.sort(dates, comparator);
       // Date[]  itemsFechas=new Date[25];
        int i=0;
        int j=0;
        for(i=0; i<thumbs1.size(); i++){
        items[i]=((thumbs1.get(i)).fileName()); 
        try{
        Date javaDate=format.parse((thumbs1.get(i)).modified);
        System.out.println("En el listador de fecha");
        	for(j=0; j<thumbs1.size(); j++)
        	{
        		if (javaDate.equals(dates.get(j)))
        		{
        			 System.out.println("En el listador de fecha2222222\n");
        			 System.out.println(j);
        			 System.out.println("\n");
        			 System.out.println(i);
        		numeros[i]=j;
        		}
        	}
        }catch(Exception ex){}
        System.out.println(thumbs1.get(i).fileName());
        }
        for(i=0; i<thumbs1.size(); i++)
        {
        itemsOrdenadosFecha11[numeros[i]]=thumbs1.get(i).fileName();
        } 
       
        nombres2=Arrays.asList(items);
        nombres3=Arrays.asList(items);
       // dates=Arrays.asList(itemsFechas);
        
       Collections.reverse(nombres2);
       Collections.sort(nombres3);
         itemsOrdenadosNombre=nombres2.toArray(items);
         itemsOrdenadosNombre1=nombres3.toArray(items);
        final String []itemsOrdenadosFecha1=itemsOrdenadosNombre;
        final String[] itemsOrdenadosFecha2=itemsOrdenadosFecha11;
        final String [] itemsOrdenadosFecha3=itemsOrdenadosNombre1;
        final String [] itemsOrdenadosFecha4=itemsOrdenadosNombre1;
        //System.out.println(itemsOrdenadosFecha11.size());
        System.out.println("HOLA234");
        for(i=0; i<itemsOrdenadosFecha1.length;i++)
        	System.out.println(itemsOrdenadosNombre[i]);
        ArrayAdapter<String> adapter =
          new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        ArrayAdapter<String> adapter2=
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsOrdenadosFecha3);
        m_listview.setAdapter(adapter);
        //Creacion Del Spinner//
        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
     ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
             R.array.planets_array, android.R.layout.simple_spinner_item);
     adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     spinner.setAdapter(adapter1);
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	
    	 public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
    	                 String str =(String)parent.getSelectedItem(); 
    	               //  string nuevo="";
    	                 if(str.equals("Venus"))
    	                		 {
    	                	 System.out.println(itemsOrdenadosFecha1.length);
    	                	// itemsOrdenadosFecha1=itemsOrdenadosNombre;
    	                	 ArrayAdapter<String> lstAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, itemsOrdenadosFecha2);
    	                     m_listview.setAdapter(lstAdapter);
    	             
    	                		 }
    	                 if(str.equals("Mercury"))
                		 {
                	 System.out.println(itemsOrdenadosFecha2.length);
                	// itemsOrdenadosFecha1=itemsOrdenadosNombre;
                     //m_listview.setAdapter(lstAdapter);
             
                		 }
    	                 
    	                 
    	                 
    	               System.out.println(str);
    	          }
    	  public void onNothingSelected(AdapterView<?> parent) {
    	  }
    	 }); 
     
    m_listview.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {
             String path="aaa";
             String  size="123";
             String rev;
             String nombre="nUEVO";
             String cogeFichero="aaa";
             String cachePath;
             long l=0;
            // selected item 
            String product = ((TextView) view).getText().toString();
            int is=0;
            System.out.println(product);
            for(is=0; is<thumbs1.size();is++)
            {
            	if(product.equals(thumbs1.get(is).fileName()))
            	{
            		path=(thumbs1.get(is)).path;
            		l=(thumbs1.get(is)).bytes;
            		rev=(thumbs1.get(is)).rev;
            		nombre=(thumbs1.get(is).fileName());
            		System.out.println(path);
            		System.out.println(size);
            		System.out.println(l);
            		System.out.println(nombre);
            		
            	}
            }
          //DownloadEpubFile download=new DownloadEpubFile(AndroidListViewActivity.this, mApi,
  	         //   path, mImage,"aaa", nombre,l,7);
        // download.execute();
          cachePath = (AndroidListViewActivity.this).getFilesDir().getAbsolutePath() + "/" + nombre;
          System.out.println("1al5");
         // System.out.println(download.cachePath);
          
          
          
            // Launching new Activity on selecting single List Item
            Intent i = new Intent(getApplicationContext(), SingleListItem.class);
            // sending data to new activity
            i.putExtra("path",cachePath);
            i.putExtra("product", product);
           // cogeFichero=download.DameDireccion();
            //System.out.println(cogeFichero);
            startActivity(i);
           
        }
        
      });
     m_listview.setOnItemLongClickListener(new OnItemLongClickListener(){
    	 public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
    	        final String title = (String) ((TextView) v).getText();
                System.out.println("Hola 123");
    
     
        return true;

    	 }
    	 
    	 
     });
    	 
    	 
        setLoggedIn(mApi.getSession().isLinked());
       
       
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mCameraFileName", mCameraFileName);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }
    }

    // This is what gets called on finishing a media piece to import
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_PICTURE) {
            // return from file upload
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                if (uri == null && mCameraFileName != null) {
                    uri = Uri.fromFile(new File(mCameraFileName));
                }
                File file = new File(mCameraFileName);

                if (uri != null) {
                 //   UploadPicture upload = new UploadPicture(this, mApi, PHOTO_DIR, file);
                   // upload.execute();
                }
            } else {
                Log.w(TAG, "Unknown Activity Result from mediaImport: "
                        + resultCode);
            }
        }
    }

    private void logOut() {
        // Remove credentials from the session
        mApi.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) {
            mLoggedIn = loggedIn;
            if (loggedIn) {
                    mSubmit.setText("Unlink from Dropbox");
            mDisplay.setVisibility(View.VISIBLE);
            } else {
                    mSubmit.setText("Link with Dropbox");
            mDisplay.setVisibility(View.GONE);
           // mImage.setImageDrawable(null);
            }
    }

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     *
     * @return Array of [access_key, access_secret], or null if none stored
     */
    private String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
                String[] ret = new String[2];
                ret[0] = key;
                ret[1] = secret;
                return ret;
        } else {
                return null;
        }
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeKeys(String key, String secret) {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.putString(ACCESS_KEY_NAME, key);
        edit.putString(ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        return session;
    }
    
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
   }  
    public void BusquedaRecursivaDropBoxAPI(){
    /*try{ 
    	System.out.println("HOLA");
    	//String a1="\";
    	ArrayList<String> folderName=new ArrayList<String>();
    	ArrayList<String> nombres1= new ArrayList<String>();
        ArrayList<String> path2= new ArrayList<String>();
        Entry dropboxDir2 = mApi.metadata("/", 0, null, true, null);
        List<Entry> CFolder = dropboxDir2.contents;
        for(Entry entry:CFolder)
        {
        	if(entry.isDir)
        	{
        	Entry dropboxDir3=mApi.metadata(entry.path,1000,null,true,null);
        	List<Entry> Cfolder1=dropboxDir3.contents;
        		for(Entry entry1:Cfolder1)
        		{
        			if(entry.isDir)
        			{
        				Entry dropboxDir4=mApi.metadata(entry1.path,1000,null,true,null);
                    	List<Entry> Cfolder2=dropboxDir4.contents;
                    	   
                    		for(Entry entry2:Cfolder2)
                    		{   
                    			 if (contents1 != null) {
                     	            folderName.clear();
                     	             
                     	                for (int i = 0; i < contents1.size(); i++) {
                     	                    Entry e = contents1.get(i);
                    			
                    			
                    			
                    			if(!entry2.isDir)
                    			{	
                    				
                    				
                    			
                    			}	
                    		}
        				
        			}
        				
        		}
        	
        }
    
    
    
        }  
    
    }catch(Exception ex){}
   */ }
    
  
    
}  


