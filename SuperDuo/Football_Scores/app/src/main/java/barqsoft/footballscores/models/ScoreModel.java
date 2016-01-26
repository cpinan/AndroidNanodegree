package barqsoft.footballscores.models;

import android.database.Cursor;

import barqsoft.footballscores.ScoresAdapter;
import barqsoft.footballscores.Utilies;

/**
 * @author Carlos Piñan
 */
public class ScoreModel {

    private String homeName;
    private String awayName;
    private String date;
    private String score;
    private double matchId;
    private int homeCrestImageResource;
    private int awayCrestImageResource;

    public ScoreModel(Cursor cursor) {
        homeName = cursor.getString(ScoresAdapter.COL_HOME);
        awayName = cursor.getString(ScoresAdapter.COL_MATCHTIME);
        date = cursor.getString(ScoresAdapter.COL_DATE);
        score = Utilies.getScores(cursor.getInt(ScoresAdapter.COL_HOME_GOALS), cursor.getInt(ScoresAdapter.COL_AWAY_GOALS));
        matchId = cursor.getDouble(ScoresAdapter.COL_ID);
        homeCrestImageResource = Utilies.getTeamCrestByTeamName(cursor.getString(ScoresAdapter.COL_HOME));
        awayCrestImageResource = Utilies.getTeamCrestByTeamName(cursor.getString(ScoresAdapter.COL_AWAY));
    }

    public String getHomeName() {
        return homeName == null ? "" : homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName == null ? "" : awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public String getDate() {
        return date == null ? "" : date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public double getMatchId() {
        return matchId;
    }

    public void setMatchId(double matchId) {
        this.matchId = matchId;
    }

    public int getHomeCrestImageResource() {
        return homeCrestImageResource;
    }

    public void setHomeCrestImageResource(int homeCrestImageResource) {
        this.homeCrestImageResource = homeCrestImageResource;
    }

    public int getAwayCrestImageResource() {
        return awayCrestImageResource;
    }

    public void setAwayCrestImageResource(int awayCrestImageResource) {
        this.awayCrestImageResource = awayCrestImageResource;
    }


}
