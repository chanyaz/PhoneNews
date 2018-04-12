package test.model.movie;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import test.model.movie.bean.Movie;
import test.model.movie.bean.MovieDetail;

/**
 * @author donghailong
 * @date 2018/4/4
 */

public interface MovieServiceDao {

    @GET("top250")
    Observable<Movie> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<MovieDetail> getMovieDetail();

}

