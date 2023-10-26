package bots;

import com.gats.manager.Bot;
import com.gats.manager.Controller;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;
import com.badlogic.gdx.math.Vector2;

public class UBot extends Bot {

    @Override
    public String getStudentName() {
        return "First name Last name";
    }

    @Override
    public int getMatrikel() {
        return 123456;
    }

    @Override
    public String getName() {
        return "UBot";
    }

    @Override
    protected void init(GameState state) {
        System.out.println("Bot is loading...");
    }

    @Override
    protected void executeTurn(GameState st, Controller con) {
        GameCharacter myCharacter = con.getGameCharacter();
        int myTeam = myCharacter.getTeam();

        GameCharacter MainTarget = findNearestEnemyAroundYou(st, myCharacter, myTeam);
        if (MainTarget != null) {
            Action(con, myCharacter, MainTarget);
        }
    }

    private GameCharacter findNearestEnemyAroundYou(GameState st, GameCharacter myChar, int myTeam) {
        GameCharacter nearestEnemy = null;
        float minDistance = Float.MAX_VALUE;

        for (int team = 0; team < st.getTeamCount(); team++) {
            if (team != myTeam) {
                for (int i = 0; i < st.getCharactersPerTeam(); i++) {
                    GameCharacter enemy = st.getCharacterFromTeams(team, i);
                    if (enemy.isAlive()) {
                        float distance = enemy.getPlayerPos().dst2(myChar.getPlayerPos());
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestEnemy = enemy;
                        }
                    }
                }
            }
        }

        return nearestEnemy;
    }

    private void Action(Controller con, GameCharacter myChar, GameCharacter enemy) {
        Vector2 startPoint = myChar.getPlayerPos();
        Vector2 endPoint = enemy.getPlayerPos();

        float angle = calculateAngleForAction(startPoint, endPoint);
        float strength = calculateStrengthForAction(startPoint, endPoint);

        con.selectWeapon(WeaponType.WATER_PISTOL);
        con.aim(angle, strength);
        con.shoot();
    }

    private float calculateAngleForAction(Vector2 startPoint, Vector2 endPoint) {
        Vector2 dir = endPoint.cpy().sub(startPoint).nor();
        return dir.angleDeg();
    }

    private float calculateStrengthForAction(Vector2 startPoint, Vector2 endPoint) {
        float distance = startPoint.dst(endPoint);
        return Math.max(0.1f, distance / 120.9f);
    }
}
