package google;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

public class DriveServiceUtil {
	private static final String APPLICATION__NAME = "taocalc";

	public static Drive getSheetsService() throws IOException, GeneralSecurityException {
		Credential credential = GoogleAuthorizeUtil.authorize();
		return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), credential)
						.setApplicationName(APPLICATION__NAME)
						.build();
	}

}