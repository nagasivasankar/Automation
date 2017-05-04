package interfaces;

import android.content.DialogInterface;
import android.graphics.Bitmap;

public interface ImagePickerListener {
	public void onImagePick(String imagePath, Bitmap bmp, DialogInterface dialog, int position);
	public void onImageRemove(DialogInterface dialog);
}