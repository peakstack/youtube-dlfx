package data;

public class Audio extends Playable {

    @Override
    public String toString() {
        return title.getName() + " by " + author.getName();
    }
}
