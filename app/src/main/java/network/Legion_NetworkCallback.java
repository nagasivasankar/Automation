package network;

/**
 * <p/>
 * This interface is responsible to handle the network callbacks such as whether response success or failure.
 * Implement this interface in your code to handle the response
 */
public interface Legion_NetworkCallback {

    /**
     * This method will get fired when the request is being start
     * @param requestCode
     */
    public void onStartRequest(int requestCode);

    /*
     * This method will get callback when the server responds with some response
     * @param requestCode
     * @param response
     * @param headers
    * */
    public void onSuccess(int requestCode, Object response, Object headers);


    public void onFailure(int requestCode, String reasonPhrase);
}
