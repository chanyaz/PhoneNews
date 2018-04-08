package test.service;



import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import test.bean.Movie;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public interface MovieServiceDao {

    @GET("top250")
    Observable<Movie> getTopMovie(@Query("start") int start, @Query("count") int count);

}

