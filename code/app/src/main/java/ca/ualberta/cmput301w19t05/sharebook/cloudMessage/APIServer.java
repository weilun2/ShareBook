package ca.ualberta.cmput301w19t05.sharebook.cloudMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
/**
 * Configure API server
 */
public interface APIServer {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAwZNKFzc:APA91bFKlU8U1En4uSJvoXdvRVV2X3hx0hG_3PjnvLlKNcQSuI1qLkIbdJcWDQxPtMCo9zK4Ug7YXshimavdCXk5wLN8W_gzSPzYDfJ15RNYOe_EYQox7_rWhp7LnPHw6Ho_yo0HX6Xa"
    })
    @POST("fcm/send")

    Call<MyResponse> sendNotification(@Body Sender body);

}
