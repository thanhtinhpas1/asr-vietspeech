import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class SpeechToText {
    public SpeechToText(Configuration configuration) {
        this.configuration = configuration;
    }

    private Configuration configuration;
    public static final String ASR_URL = "http://asr.vietspeech.com:7070/v1/speech";
    private static final String FIELD_NAME = "voice";
    private static final String FIELD_ENCODING = "encoding";
    private static final String FIELD_SAMPLE_RATE = "sampleRate";
    private static final String FIELD_MAX_SIZE = "maxSize";
    private static final String AUTHORIZATION = "Authorization";
    // it can be Basic or another authorization way
    private static final String AUTHORIZE_PREFIX = "Bearer ";

    /**
     * Call ASR Service to convert audio file to text with model language VietNamese
     * Method temp just support mime type Wave in this time
     *
     * @param file File audio need to convert. It should be a .wav file
     * @return content of audio file by text
     * @throws IllegalArgumentException when file invalid with can not read or empty
     * @throws IOException              in case a problem or server connection was abort
     */
    @SuppressWarnings("resource")
	public String call(File file) throws IllegalArgumentException, IOException {
        if (!file.canRead()) throw new IllegalArgumentException("File can not read.");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody(FIELD_NAME, file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);
        builder.addTextBody(FIELD_ENCODING, configuration.getEncoding().toString());
        builder.addTextBody(FIELD_MAX_SIZE, String.valueOf(configuration.getMaxSize()));
        builder.addTextBody(FIELD_SAMPLE_RATE, String.valueOf(configuration.getMaxSize()));
        HttpEntity multipart = builder.build();
        HttpPost httpPost = new HttpPost(ASR_URL);
        httpPost.addHeader(AUTHORIZATION, AUTHORIZE_PREFIX + configuration.getToken());
        httpPost.setEntity(multipart);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
        return getMessage(response.getEntity());
    }

    private String getMessage(HttpEntity entity) throws IOException {
        String jsonString = EntityUtils.toString(entity);
        System.out.println("ASR Response: " + jsonString);
        if ("".equals(jsonString) || jsonString.length() == 0) return jsonString;
        Gson gson = new Gson();
        try {
            SuccessResponse response = gson.fromJson(jsonString, SuccessResponse.class);
            return response.getText();
        } catch (JsonParseException ex) { // parse failed
            FailedResponse failedResponse = gson.fromJson(jsonString, FailedResponse.class);
            throw new IllegalArgumentException(failedResponse.getMessage());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Something went wrong when get message.", ex);
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}

class FailedResponse {
    private int statusCode;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class SuccessResponse {
    private int returncode;
    private String returnmessage;
    private String text;

    public int getReturncode() {
        return returncode;
    }

    public void setReturncode(int returncode) {
        this.returncode = returncode;
    }

    public String getReturnmessage() {
        return returnmessage;
    }

    public void setReturnmessage(String returnmessage) {
        this.returnmessage = returnmessage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
