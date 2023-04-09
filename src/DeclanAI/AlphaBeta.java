package DeclanAI;

import com.briansea.gamecabinet.game.Game;
import com.briansea.gamecabinet.game.Move;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlphaBeta {

    private String player;
    private List<Move> FinalMove;
    private List<Move> BestMove;
    private Game GameState;
    private boolean running;
    private int TimerSeconds = 5;

    public AlphaBeta(Game gs, List<Move> move){
        GameState = gs;
        Game Tester = GameState.deepCopy(false);
        for(List<Move> M: GameState.getValidMoves()){
            Tester.makeMove(M);
        }
        running = true;
        FinalMove = move;
        setTimer();
        player = gs.whoseTurn();

        InitiateRoot(2,GameState);
        InitiateRoot(4,GameState);
        InitiateRoot(10,GameState);

        if(running){
            //System.out.print("Move Made by |"+GameState.whoseTurn()+"|");

            for( Move m : BestMove) {
                FinalMove.add(m);
                running = false;
                //System.out.print(", "+m.toString().substring(25,27));
            }
            //System.out.println("");
        }
    }

    private void setTimer(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(running){
                    //System.out.println("- - - - - - - - - - - - - - -");
                    //System.out.println("Time is Up, Your Turn is Over");
                    //System.out.println("- - - - - - - - - - - - - - -");
                    running = false;
                    //System.out.print("Move Made by |"+GameState.whoseTurn()+"|");
                    for( Move m : BestMove) {
                        FinalMove.add(m);
                        //System.out.print(", "+m.toString().substring(25,27));
                    }
                    //System.out.println("");
                }
            }
        };
        Timer timer = new Timer();
        long run = Integer.toUnsignedLong(TimerSeconds * 1000);
        timer.schedule(timerTask, run);
    }

    private void InitiateRoot(int depth, Game gs){
        MaxNode root = new MaxNode(Integer.MIN_VALUE, Integer.MAX_VALUE, gs, depth,player);
        List<List<Move>> ValidMoves = gs.getValidMoves();
        BestMove = ValidMoves.get(0);
        for( List<Move> move : ValidMoves){
            if(!running){ break; }
            Game gsCopy = gs.deepCopy(false);
            gsCopy.makeMove(move);
            MinNode node = new MinNode(root.alpha,root.beta,gsCopy, depth-1,player);
            int gameVal = node.getMinChild();
            if(gameVal > root.alpha){
                root.alpha = gameVal;
                BestMove = move;
            }
        }
    }
}

class MinNode{

    public String player;
    public int alpha;
    public int beta;
    public int minChild;
    public int depth;
    private Game GameState;
    private Game gameStateTest;

    public MinNode(int a, int B, Game gs, int currentDepth, String turn){
        alpha = a;
        beta = B;
        depth = currentDepth;
        GameState = gs;
        player = turn;
        minChild = Integer.MAX_VALUE-1;
    }

    //create children, and get their
    public int getMinChild(){
        List<List<Move>> ValidMoves = GameState.getValidMoves();
        if(depth > 1){
            for( List<Move> move : ValidMoves){
                gameStateTest = GameState.deepCopy(false);
                gameStateTest.makeMove(move);
                MaxNode node = new MaxNode(alpha,beta,gameStateTest,depth-1,player);
                int gameVal = node.getMaxChild();
                if(gameVal < beta){
                    beta = gameVal;
                }
                if(gameVal < minChild){
                    minChild = gameVal;
                }
                if(alpha >= beta){
                    break;
                }
            }
        }
        if(depth == 1){
            for( List<Move> move : ValidMoves){
                gameStateTest = GameState.deepCopy(false);
                gameStateTest.makeMove(move);
                CheckersEvaluator eval = new CheckersEvaluator(player);
                int gameVal = eval.Evaluate(gameStateTest);
                if(gameVal < beta){
                    beta = gameVal;
                }
                if(gameVal < minChild){
                    minChild = gameVal;
                }
                if(alpha >= beta){
                    break;
                }
            }
        }
        if(depth < 1){
            System.out.println("---!!!BigErrorAlert!!!---");
        }
        return minChild;
    }
}

class MaxNode{

    public String player;
    public int alpha;
    public int beta;
    public int maxChild;
    public int depth;
    private Game GameState;
    private Game gameStateTest;

    public MaxNode(int a, int B, Game gs, int currentDepth, String turn){
        alpha = a;
        beta = B;
        depth = currentDepth;
        GameState = gs;
        player = turn;
        maxChild = Integer.MIN_VALUE+1;
    }

    public int getMaxChild(){
        List<List<Move>> ValidMoves = GameState.getValidMoves();
        if(depth > 1){
            for( List<Move> move: ValidMoves){
                gameStateTest = GameState.deepCopy(false);
                gameStateTest.makeMove(move);
                MinNode node = new MinNode(alpha,beta,gameStateTest,depth-1,player);
                int gameVal = node.getMinChild();
                if(gameVal > alpha){
                    alpha = gameVal;
                }
                if(gameVal > maxChild){
                    maxChild = gameVal;
                }
                if(alpha >= beta){
                    break;
                }
            }
        }
        if(depth == 1){
            for( List<Move> move : ValidMoves){
                gameStateTest = GameState.deepCopy(false);
                gameStateTest.makeMove(move);
                CheckersEvaluator eval = new CheckersEvaluator(player);
                int gameVal = eval.Evaluate(gameStateTest);
                if(gameVal > alpha){
                    alpha = gameVal;
                }
                if(gameVal > maxChild){
                    maxChild = gameVal;
                }
                if(alpha >= beta){
                    break;
                }
            }
        }
        if(depth < 1){
            System.out.println("---!!!BigErrorAlert!!!---");
        }
        return maxChild;
    }
}