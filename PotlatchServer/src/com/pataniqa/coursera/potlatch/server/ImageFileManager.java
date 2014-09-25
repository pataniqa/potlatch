/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.pataniqa.coursera.potlatch.server;

import com.pataniqa.coursera.potlatch.server.repository.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageFileManager {

	private Path targetDir_ = Paths.get("images");

	public ImageFileManager() throws IOException {
		if (!Files.exists(targetDir_)) {
			Files.createDirectories(targetDir_);
		}
	}

	private Path getImagePath(ServerGift g) {
		assert (g != null);
		return targetDir_.resolve("image" + g.getId() + ".jpg");
	}

	/**
	 * This method returns true if the specified Video has binary data stored on
	 * the file system.
	 * 
	 * @param v
	 * @return
	 */
	public boolean hasImageData(ServerGift g) {
		return Files.exists(getImagePath(g));
	}

	/**
	 * This method copies the binary data for the given video to the provided
	 * output stream. The caller is responsible for ensuring that the specified
	 * Video has binary data associated with it. If not, this method will throw
	 * a FileNotFoundException.
	 * 
	 * @param v
	 * @param out
	 * @throws IOException
	 */
	public void copyImageData(ServerGift g, OutputStream out) throws IOException {
		Path source = getImagePath(g);
		if (!Files.exists(source)) {
			throw new FileNotFoundException(
					"Unable to find the referenced video file for videoId:"
							+ g.getId());
		}
		Files.copy(source, out);
	}

	/**
	 * This method reads all of the data in the provided InputStream and stores
	 * it on the file system. The data is associated with the Video object that
	 * is provided by the caller.
	 * 
	 * @param v
	 * @param imageData
	 * @throws IOException
	 */
	public void saveImageData(ServerGift g, InputStream imageData)
			throws IOException {
		assert (imageData != null);
		Files.copy(imageData, getImagePath(g), StandardCopyOption.REPLACE_EXISTING);
	}
}
