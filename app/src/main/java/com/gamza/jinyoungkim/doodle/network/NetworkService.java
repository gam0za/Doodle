package com.gamza.jinyoungkim.doodle.network;

import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmCheckPost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmCheckResponse;
import com.gamza.jinyoungkim.doodle.adapter.doodle_alarm.AlarmResponse;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikeModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.LikePost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapModel;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.ScrapPost;
import com.gamza.jinyoungkim.doodle.adapter.doodle_feel.feel_comment.CommentResponse;
import com.gamza.jinyoungkim.doodle.adapter.doodle_myfeed.myfeed_single.DeleteResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_detail.DetailResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelPost;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.FeelResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentPost;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_comment.CommentPostResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_myfeed.MyfeedProfileResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_myfeed.ProfileResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_other.OtherResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search.SearchDoodleResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_feel.feel_search.SearchNameResponse;
import com.gamza.jinyoungkim.doodle.view.doodle_write.WriteResponse;
import com.gamza.jinyoungkim.doodle.view.main.alarm.PushAlarmPost;
import com.gamza.jinyoungkim.doodle.view.main.alarm.PushAlarmResponse;
import com.gamza.jinyoungkim.doodle.view.signin.SigninModel;
import com.gamza.jinyoungkim.doodle.view.signin.SigninResult;
import com.gamza.jinyoungkim.doodle.view.signup.DuplicatePost;
import com.gamza.jinyoungkim.doodle.view.signup.DuplicateResponse;
import com.gamza.jinyoungkim.doodle.view.signup.SignupResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetworkService {

    // UserApi
    @POST("users/login")
    Observable<retrofit2.Response<SigninResult>> login(@Body SigninModel signinModel);

    @Multipart
    @POST("users/register")
    Observable<retrofit2.Response<SignupResponse>> register(@Part("email")RequestBody email,
                                                            @Part("pw1")RequestBody pw1,
                                                            @Part("pw2")RequestBody pw2,
                                                            @Part("nickname")RequestBody nickname,
                                                            @Part MultipartBody.Part image);

    @POST("users/duplicates")
    Observable<retrofit2.Response<DuplicateResponse>>duplicates(@Body DuplicatePost duplicatePost);

    @Multipart
    @PUT("users/modify")
    Observable<retrofit2.Response<ProfileResponse>>modify(@Header("token")String token,
                                                    @Part MultipartBody.Part image,
                                                    @Part("flag") int flag,
                                                    @Part("description") String description);

    @GET("users")
    Observable<retrofit2.Response<MyfeedProfileResponse>>myprofile(@Header("token")String token);


    // DoodleApi
    @Multipart
    @POST("doodle/post")
    Observable<retrofit2.Response<WriteResponse>> doodlepost(@Header("token")String token,
                                                             @Part("text")RequestBody text,
                                                             @Part MultipartBody.Part image);


    @POST("doodle/all")
    Observable<retrofit2.Response<FeelResponse>> doodlelist(@Header("token")String token,
                                                            @Body FeelPost feelPost);

    @POST("like/{idx}")
    Observable<retrofit2.Response<LikeModel>> like(@Header("token")String token,
                                                   @Body LikePost likePost,
                                                   @Path("idx")int idx);

    @POST("scrap/{idx}")
    Observable<retrofit2.Response<ScrapModel>> scrap(@Header("token")String token,
                                                     @Body ScrapPost scrapPost,
                                                     @Path("idx")int idx);

    @GET("comments/{idx}")
    Observable<retrofit2.Response<CommentResponse>> getcomment(@Header("token")String token,
                                                               @Path("idx")int idx);

    @POST("comments/{idx}")
    Observable<retrofit2.Response<CommentPostResponse>>postcomment(@Header("token")String token,
                                                                   @Path("idx")int idx,
                                                                   @Body CommentPost commentPost);

    @GET("doodle/get/{idx}")
    Observable<retrofit2.Response<DetailResponse>>getdetail(@Header("token")String token,
                                                            @Path("idx")int idx);

    @DELETE("doodle/delete/{idx}")
    Observable<retrofit2.Response<DeleteResponse>>delete(@Header("token")String token,
                                                         @Path("idx")int idx);

    @GET("users/other/{idx}")
    Observable<retrofit2.Response<OtherResponse>>getother(@Header("token")String token,
                                           @Path("idx")int idx);

    // SearchApi
    @GET("search/users/{keyword}")
    Observable<retrofit2.Response<SearchNameResponse>>searchname(@Header("token")String token,
                                                                 @Path("keyword")String keyword);

    @GET("search/doodle/{keyword}")
    Observable<retrofit2.Response<SearchDoodleResponse>>searchdoodle(@Header("token")String token,
                                                                     @Path("keyword")String keyword);

    //AlarmApi
    @GET("alarm/list")
    Observable<retrofit2.Response<AlarmResponse>>getalarm(@Header("token")String token);

    @POST("alarm/item")
    Observable<retrofit2.Response<AlarmCheckResponse>>checkalarm(@Header("token")String token,
                                                                 @Body AlarmCheckPost alarmCheckPost);

    @POST("alarm/token")
    Observable<retrofit2.Response<PushAlarmResponse>>pushalarm(@Header("token")String token,
                                                               @Body PushAlarmPost pushAlarmPost);

}
