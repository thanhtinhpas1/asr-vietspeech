public class Configuration {
    private static final AudioEncoding DEFAULT_ENCODING = AudioEncoding.ENCODING_UNSPECIFIED;
    private static final int DEFAULT_TIMEOUT = 10000;
    private static final int DEFAULT_MAX_SIZE = 51200;
    private static final int DEFAULT_SAMPLE_RATE = 16000;

    public Configuration(String token) {
        this.token = token;
        this.encoding = DEFAULT_ENCODING;
        this.timeout = DEFAULT_TIMEOUT;
        this.sampleRateHertz = DEFAULT_SAMPLE_RATE;
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    public Configuration(String token, AudioEncoding encoding, int timeout, int sampleRateHertz, int maxSize) {
        this.token = token;
        this.encoding = encoding;
        this.timeout = timeout;
        this.sampleRateHertz = sampleRateHertz;
        this.maxSize = maxSize;
    }

    private String token;
    private AudioEncoding encoding;
    private int timeout;
    private int sampleRateHertz;
    private int maxSize;;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AudioEncoding getEncoding() {
        return encoding;
    }

    public void setEncoding(AudioEncoding encoding) {
        this.encoding = encoding;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getSampleRateHertz() {
        return sampleRateHertz;
    }

    public void setSampleRateHertz(int sampleRateHertz) {
        this.sampleRateHertz = sampleRateHertz;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
