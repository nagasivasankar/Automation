package interfaces;

public interface ResponseParserListener {
    public void onResponseParsingStart(int parserId);
    public void onResponseParsingComplete(int parserId, Object response);
}