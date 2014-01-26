package com.example.ejemplofichero;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.DropboxAPI.ThumbFormat;
import com.dropbox.client2.DropboxAPI.ThumbSize;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
public class DownloadEpubFile extends AsyncTask<Void, Long, Boolean>  {
	     private Context mContext;
	    private ProgressDialog mDialog1;
	    private DropboxAPI<?> mApi;
	    private String mPath;
	    private ImageView mView;
	    private Drawable mDrawable;
        private String pathImagen;
        private String nombre;
	    private FileOutputStream mFos;
        private long mFileLen;
	    private boolean mCanceled;
	    private String mErrorMsg;
	    private String NombreFicheroImagen;
	    public String cachePath="";
	    @SuppressWarnings("deprecation")
	      DownloadEpubFile(Context context, DropboxAPI<?> api,
	            String dropboxPath, ImageView view,String pathImagen, String nombreFichero,long longitudFichero,int ix)
	    {
	    	 mContext = context.getApplicationContext();
	         mApi = api;
	         mPath = dropboxPath;
	         mView = view;
             pathImagen=pathImagen;
             nombre=nombreFichero;
             NombreFicheroImagen=nombreFichero;
             mFileLen=longitudFichero;
             System.out.println("HOLLLLLLA");
	         mDialog1 = new ProgressDialog(context);
	         mDialog1.setMessage("Downloading Image");
	         mDialog1.setButton("Cancel", new OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) {
	                 mCanceled = true;
	                 mErrorMsg = "Canceled";

	                 // This will cancel the getThumbnail operation by closing
	                 // its stream
	                 if (mFos != null) {
	                     try {
	                         mFos.close();
	                     } catch (IOException e) {
	                     }
	                 }
	             }
	         });

	         mDialog1.show();
	    }
	    	
	    @Override
	    protected Boolean doInBackground(Void... params) {
	        try {
	            if (mCanceled) {
	                return false;
	            }
	            System.out.println("HOLLLLLLA");
	           cachePath = mContext.getFilesDir().getAbsolutePath() + "/" + NombreFicheroImagen;
	           //File newFile = new File(cachepath);
	           System.out.println(cachePath);
	            try {
	                mFos = new FileOutputStream(cachePath);
	            } catch (FileNotFoundException e) {
	                mErrorMsg = "No se pudo crear directorio para almacener la imagen";
	                return false;
	            }
	            System.out.println("HOLLLLLLA");
	            System.out.println(mPath);
	            
	            
	            
	            
	            
	            
	            mApi.getFile(mPath, null, mFos, null);
	            System.out.println("NUEVO1aaaa");
	            System.out.println(cachePath);
	            if (mCanceled) {
	                return false;
	            }
	            return true;
	        } catch (DropboxUnlinkedException e) {
	            // The AuthSession wasn't properly authenticated or user unlinked.
	        } catch (DropboxPartialFileException e) {
	            // We canceled the operation
	            mErrorMsg = "Download canceled";
	        } catch (DropboxServerException e) {
	            // Server-side exception.  These are examples of what could happen,
	            // but we don't do anything special with them here.
	            if (e.error == DropboxServerException._304_NOT_MODIFIED) {
	                // won't happen since we don't pass in revision with metadata
	            } else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
	                // Unauthorized, so we should unlink them.  You may want to
	                // automatically log the user out in this case.
	            } else if (e.error == DropboxServerException._403_FORBIDDEN) {
	                // Not allowed to access this
	            } else if (e.error == DropboxServerException._404_NOT_FOUND) {
	                // path not found (or if it was the thumbnail, can't be
	                // thumbnailed)
	            } else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
	                // too many entries to return
	            } else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
	                // can't be thumbnailed
	            } else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
	                // user is over quota
	            } else {
	                // Something else
	            }
	            // This gets the Dropbox error, translated into the user's language
	            mErrorMsg = e.body.userError;
	            if (mErrorMsg == null) {
	                mErrorMsg = e.body.error;
	            }
	        } catch (DropboxIOException e) {
	            // Happens all the time, probably want to retry automatically.
	            mErrorMsg = "Network error.  Try again.";
	        } catch (DropboxParseException e) {
	            // Probably due to Dropbox server restarting, should retry
	            mErrorMsg = "Dropbox error.  Try again.";
	        } catch (DropboxException e) {
	            // Unknown error
	            mErrorMsg = "Unknown error.  Try again.";
	        }
	        return false;
	          	
	    }
	
	    @Override
	    protected void onProgressUpdate(Long... progress) {
	        int percent = (int)(100.0*(double)progress[0]/mFileLen + 0.5);
	        mDialog1.setProgress(percent);
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        mDialog1.dismiss();
	        if (result) {
	        	showToast("Hola");
	            // Set the image now that we have it
	           // mView.setImageDrawable(mDrawable);
	        } else {
	            // Couldn't download it, so show an error
	            showToast(mErrorMsg);
	        }
	    }

	    private void showToast(String msg) {
	        Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
	        error.show();
	    }
	    
	    public String DameDireccion(){
	    	return cachePath;
	    }
	
	
	
	

}
