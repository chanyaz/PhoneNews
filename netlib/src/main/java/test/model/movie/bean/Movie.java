package test.model.movie.bean;

import java.util.List;

/**
 * @author donghailong
 */
public class Movie {
    private String title;
    private List<Subjects> subjects;


    public String getTitle() {
        return title;
    }

    public List<Subjects> getSubjects() {
        return subjects;
    }

    public static class Subjects {
        private String title, year, id;

        public Subjects(String title, String year, String id) {
            this.title = title;
            this.year = year;
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public String getYear() {
            return year;
        }

        public String getId() {
            return id;
        }
    }
}

