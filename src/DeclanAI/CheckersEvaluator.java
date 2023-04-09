package DeclanAI;

import com.briansea.gamecabinet.game.Game;

import java.util.List;

public class CheckersEvaluator {

    private String player;

    public CheckersEvaluator(String turn){
        player = turn;
    }

    public int Evaluate(Game gs){
        int rtn = 0;
        if(gs.isGameOver()){
            if(gs.getWinners().get(0).equals(player)){
                return Integer.MAX_VALUE-1;
            }
            return Integer.MIN_VALUE+1;
        }

        List<String> locations = gs.getLocations();
        for(String str: locations){
            String owner = gs.getOwner(str);
            int spot = Integer.parseInt(str);
            if(owner.equals("")){
            }else if(!owner.equals(player)){
                int pieceLevel = gs.getLevel(str);
                int depth = (spot-1)/4; //--> {0,1,2,3,4,5,6,7}
                if(pieceLevel == 1){
                    rtn -= 10;
                    if(player.equals("1")){
                        rtn -= (7-depth);
                    }else{
                        rtn -= depth;
                    }
                }else{
                    rtn -= 20;
                }
            }
            if(owner.equals(player)){
                int pieceLevel = gs.getLevel(str);
                int depth = (spot-1)/4; //--> {0,1,2,3,4,5,6,7}
                if(pieceLevel == 1){
                    rtn += 10;
                    if(player.equals("1")){
                        rtn += depth;
                    }else{
                        rtn += (7-depth);
                    }
                }else{
                    rtn += 15;
                }
            }
        }
        return rtn;
    }
}
