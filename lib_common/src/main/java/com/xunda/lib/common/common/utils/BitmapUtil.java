package com.xunda.lib.common.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@SuppressLint("NewApi")
public class BitmapUtil {

	public static final int TAKE_PICTURE = 99;
	public static final int CHOOSE_PICTURE = 98;
	public static final int INTENT_TAKE = 97;

	/**
	 * 图片按比例大小压缩 根据sd卡获得
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimage(String srcPath, int size) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bitMap = BitmapFactory.decodeFile(srcPath, opts);
		opts.inJustDecodeBounds = false;
		int w = opts.outWidth;
		int h = opts.outHeight;
		int be = 1;
		if (w > h && w > 800) {
			be = (int) (w / 800);
		} else if (w < h && h > 600) {
			be = (int) (h / 600);
		}
		if (be < 0) {
			be = 1;
		}
		opts.inSampleSize = be;
		bitMap = BitmapFactory.decodeFile(srcPath, opts);
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		if (width <= 800 && height <= 600) {
			return compressImage(bitMap, size);
		}
		int newWidth = 800;
		int newHeight = 600;
		float scale = 0;
		if (width > height && width > newWidth) {
			scale = ((float) newWidth) / width;
		} else if (width < height && height > newHeight) {
			scale = ((float) newHeight) / height;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

		return compressImage(bitMap, size);
	}

	public static Bitmap getimage(String srcPath) {
		return getimage(srcPath, 200);
	}

	/**
	 * 图片质量压缩
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, int size) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		while (baos.toByteArray().length / 1024 > size) {
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 5;
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	public static float yswidth = 800f;
	public static float ysheight = 480f;

	public static Bitmap getimage(Bitmap bitMap, int size) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inJustDecodeBounds = false;
		int w = opts.outWidth;
		int h = opts.outHeight;
		int be = 1;
		if (w > h && w > yswidth) {
			be = (int) (w / yswidth);
		} else if (w < h && h > ysheight) {
			be = (int) (h / ysheight);
		}
		if (be < 0) {
			be = 1;
		}
		opts.inSampleSize = be;
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		if (width <= width && height <= ysheight) {
			return compressImage(bitMap, size);
		}
		float newWidth = yswidth;
		float newHeight = ysheight;
		float scale = 0;
		if (width > height && width > newWidth) {
			scale = ((float) newWidth) / width;
		} else if (width < height && height > newHeight) {
			scale = ((float) newHeight) / height;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

		return compressImage(bitMap, size);
	}

	public static Bitmap getimage(Bitmap bitMap) {
		return getimage(bitMap, 200);
	}

	/**
	 * 图片加上圆角效果
	 * 
	 * @param bitmap
	 *            要处理的位图
	 * @param roundPx
	 *            圆角大小
	 * @return 返回处理后的位图
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float percent) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, bitmap.getWidth() * percent,
				bitmap.getHeight() * percent, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static File SaveBitmap(Context context, Bitmap bitmap, int size) {
		String path = FileUtil.getSdPath() + "/" + context.getPackageName()
				+ "/" + System.currentTimeMillis() + ".jpg";
		Bitmap bitMap = getimage(bitmap, size);
		File file = new File(path);
		File pfile = file.getParentFile();
		if (!pfile.exists()) {
			pfile.mkdirs();
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static File SaveBitmap(Context context, Bitmap bitmap) {
		return SaveBitmap(context, bitmap, 200);
	}

	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	
	
	/**
	 * 把图片url转化成bitmap
	 * @param context
	 * @param uri
	 * @return
	 */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
