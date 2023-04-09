package DeclanAI;

import com.briansea.gamecabinet.game.Game;
import com.briansea.gamecabinet.game.Move;
import com.briansea.gamecabinet.game.Player;

import java.util.List;

public class DeclanMAI extends Player {

    public DeclanMAI(){this.nameProperty().setValue("Coeus's AI");} //Greek Titan God of Intelligence and Foresight

    public void makeMove(Game gamestate, List<Move> move) {
        AlphaBeta alphaBeta = new AlphaBeta(gamestate, move);
    }
}