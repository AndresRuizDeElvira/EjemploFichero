package com.example.ejemplofichero;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import android. graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.OutputStream;
import android.os.Environment;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
public class SingleListItem extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);
        AssetManager assetManager = getAssets();
        TextView txtProduct = (TextView) findViewById(R.id.product_label);
        String a;
        Intent i = getIntent();
        // getting attached intent data
        String product = i.getStringExtra("product");
        String cachepath=i.getStringExtra("path");
        System.out.println("Nuevo");
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        File root = new File(Environment.getExternalStorageDirectory(), "/data/ochoquilates.pub");
        a=Environment.getExternalStorageDirectory().getAbsolutePath()+"/airdroid/upload/ochoquilates.epub";
        System.out.println(a);
        try
        {
        	//InputStream epubInputStream = assetManager.open(a);
           System.out.println("Hola111111111");
            // Load Book from inputStream
         //   Book book = (new EpubReader()).readEpub(epubInputStream);
          //  System.out.println(book.getTitle());	
            File myFile = new File("/sdcard/mysdfile.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = 
									new OutputStreamWriter(fOut);
			myOutWriter.append("AJJJJJJJJJ");
			myOutWriter.close();
        	System.out.println(myFile.getAbsolutePath());
        	
        	
        	
        }catch(Exception ex){
        	getStackTrace(ex);
        	System.out.println(ex);
        }
        // displaying selected product name
        txtProduct.setText(product);
         
         
    }
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
   }  
}