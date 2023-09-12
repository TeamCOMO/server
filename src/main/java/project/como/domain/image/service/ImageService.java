package project.como.domain.image.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.como.domain.image.exception.FileDeleteException;
import project.como.domain.image.exception.FileUploadException;
import project.como.domain.image.exception.UnsupportedFileExtensionException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
	private final AmazonS3 amazonS3;

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String uploadImage(String username, MultipartFile file) {
		Optional<String> contentType = Optional.ofNullable(file.getContentType());
		if (file.isEmpty() || !contentType.get().contains("image")) throw new UnsupportedFileExtensionException();

		String fileName = generateFileName(username, file.getOriginalFilename());

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType.get());
		objectMetadata.setContentLength(file.getSize());

		try (InputStream inputStream = file.getInputStream()) {
			amazonS3.putObject(
					new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(
							CannedAccessControlList.PublicRead));
		} catch (SdkClientException | IOException e) {
			log.error("파일을 업로드하는 과정에서 오류가 발생하였습니다.", e);
			throw new FileUploadException();
		}
		return amazonS3.getUrl(bucket, fileName).toString();
	}

	public List<String> uploadImages(String username, List<MultipartFile> files) {
		ArrayList<String> uploadedImages = new ArrayList<>();
		try {
			for (MultipartFile file : files) {
				uploadedImages.add(uploadImage(username, file));
			}
		} catch (FileUploadException e) {
			for (String uploadedFile : uploadedImages) {
				deleteImage(uploadedFile);
			}
			throw e;
		}
		log.info("uploadedIMG: {}", uploadedImages);
		return uploadedImages;
	}

	public String deleteImage(String url) {
		String key = url.substring(url.lastIndexOf("/") + 1);
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
		} catch (SdkClientException e) {
			log.error("파일을 삭제하는 과정에서 오류가 발생하였습니다.", e);
			throw new FileDeleteException();
		}
		return url;
	}

	public List<String> deleteImages(List<String> urls) {
		ArrayList<String> deletedImages = new ArrayList<>();
		try {
			for (String url : urls) {
				deletedImages.add(deleteImage(url));
			}
		} catch (FileDeleteException ignored) {}
		return deletedImages;
	}

	private String generateFileName(String username, String originalFileName) {
		return username + "-" + simpleDateFormat.format(new Date()) + (originalFileName != null ? "-" + originalFileName : "");
	}
}
