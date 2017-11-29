package com.king.annotation.poet;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MyFile extends File {

	private static final long serialVersionUID = 1L;
	private volatile transient Path filePath;
	
	public MyFile(String path) {
		super(path);
	}

	public Path toPath() {
        Path result = filePath;
        if (result == null) {
            synchronized (this) {
                result = filePath;
                if (result == null) {
                    result = FileSystems.getDefault().getPath(getPath());
                    filePath = result;
                }
            }
        }
        return result;
    }
}
