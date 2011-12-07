package com.bomber.world;

public class ImageTile {
	public String mImageFile;
	public boolean mFlipVertically;
	public boolean mFlipHorizontally;

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

	
}
