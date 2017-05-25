package at.lmy.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class CameraActivity extends CordovaPlugin {

	public static final String TEST = "test";
	public static final String OPEN_CAMERA = "openCamera";
	public Activity activity;

	private File imageFile = null;
	private File imageFileOfMark = null;
	private File dir = null;
	private boolean isSuccess;

	public static final String CENTER = "CENTER";
	public static final String LEFT_TOP = "LEFT_TOP";
	public static final String LEFT_BOTTOM = "LEFT_BOTTOM";
	public static final String RIGHT_TOP = "RIGHT_TOP";
	public static final String RIGHT_BOTTOM = "RIGHT_BOTTOM";

	private String text = "none";
	private int size = 70;
	private String position = CENTER;
	private int color = Color.WHITE;
	private boolean saveToPictureAlbum = false;
	private CallbackContext callbackContext;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		// TODO 自动生成的方法存根
		super.initialize(cordova, webView);
		this.activity = this.cordova.getActivity();
		this.dir = new File(this.cordova.getActivity().getExternalCacheDir(), "");
		this.isSuccess = true;
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		// TODO 自动生成的方法存根
		boolean validAction = true;
		if (TEST.equals(action)) {
			Toast.makeText(activity, "可以运行了", 1000).show();
		} else if (OPEN_CAMERA.equals(action)) {
			this.text = args.getString(0);
			this.size = args.getInt(1);
			this.position = args.getString(2);
			this.color = args.getInt(3);
			this.saveToPictureAlbum = args.getBoolean(4);
			if (saveToPictureAlbum) {
				this.dir = new File(Environment.getExternalStorageDirectory(), "pictures");
			}
			this.callbackContext = callbackContext;
			openCamera();
			PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
			result.setKeepCallback(true);
			this.callbackContext.sendPluginResult(result);
		} else {
			validAction = false;
		}
		return validAction;
	}

	/**
	 * 打开系统摄像机
	 * 
	 * @throws IOException
	 */
	public void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 将图片保存在此目录下

		if (!dir.exists()) {// 如果此目录不存在，则创建此目录
			dir.mkdirs();
		}
		// 图片名依据当前时间取值，格式为jpg
		imageFile = new File(dir, System.currentTimeMillis() + ".jpg");
		if (!imageFile.exists()) {// 如果不存在则创建
			try {
				imageFile.createNewFile();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
				this.cordova.startActivityForResult((CordovaPlugin)this, intent, Activity.DEFAULT_KEYS_DIALER);
				//this.cordova.getActivity().startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

	}

	// 加上文字水印
	public Bitmap addTextMark(Bitmap bitmap, String text, int size, String position, int color) {
		if (!text.equals("none")) {
			if (CENTER.equals(position)) {
				return ImageUtil.drawTextToCenter(activity, bitmap, text, size, color);
			} else if (LEFT_TOP.equals(position)) {
				return ImageUtil.drawTextToLeftTop(activity, bitmap, text, size, color, 15, 30);
			} else if (LEFT_BOTTOM.equals(position)) {
				return ImageUtil.drawTextToLeftBottom(activity, bitmap, text, size, color, 15, 30);
			} else if (RIGHT_TOP.equals(position)) {
				return ImageUtil.drawTextToRightTop(activity, bitmap, text, size, color, 15, 30);
			} else if (RIGHT_BOTTOM.equals(position)) {
				return ImageUtil.drawTextToRightBottom(activity, bitmap, text, size, color, 15, 30);
			}
		}
		return bitmap;
	}

	/**
	 * 将bitmap保存在file路径中
	 * 
	 * @param bitmap
	 * @param file
	 * @throws IOException
	 */
	public void saveBitMap(Bitmap bitmap, File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
		fos.flush();
		fos.close();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == Activity.DEFAULT_KEYS_DIALER && resultCode==Activity.RESULT_OK) {
			Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
			Bitmap bitmapOfMark = addTextMark(bitmap, text, size, position, color);
			if (imageFile.exists()) {
				imageFile.delete();
			}
			this.imageFileOfMark = new File(dir, System.currentTimeMillis() + ".jpg");
			try {
				if (!imageFileOfMark.exists()) {
					imageFileOfMark.createNewFile();
				}
				saveBitMap(bitmapOfMark, imageFileOfMark);
				PluginResult result = new PluginResult(PluginResult.Status.OK, imageFileOfMark.getAbsolutePath());
				result.setKeepCallback(true);
				this.callbackContext.sendPluginResult(result);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				isSuccess = false;
				this.callbackContext.error(e.getMessage());
			}

		}
	}
}
