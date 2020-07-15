package data;

import java.io.File;

public abstract class Playable {

    protected Title title;
    protected Author author;
    protected File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public Title getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title.getName() + " by " + author.getName();
    }
}
