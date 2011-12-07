package com.bomber.world;

public class ImageTile {
	private String mImageFile;
	private boolean mFlipVertically;
	private boolean mFlipHorizontally;

	public ImageTile() {
		mImageFile = null;
		mFlipHorizontally = false;
		mFlipVertically = false;
	}
	
	public ImageTile(String _imageFile, boolean _flipVertically,
			boolean _flipHorizontally) {
		this.mImageFile = _imageFile;
		this.mFlipVertically = _flipVertically;
		this.mFlipHorizontally = _flipHorizontally;
	}

	public String getImageFile() {
		return mImageFile;
	}

	public void setImageFile(String _imageFile) {
		mImageFile = _imageFile;
	}

	public boolean isFlipVertical() {
		return mFlipVertically;
	}

	public void setFlipVertical(boolean _flipVertically) {
		mFlipVertically = _flipVertically;
	}

	public boolean isFlipHorizontal() {
		return mFlipHorizontally;
	}

	public void setFlipHorizontal(boolean _flipHorizontally) {
		mFlipHorizontally = _flipHorizontally;
	}
	
	
	
	
}
